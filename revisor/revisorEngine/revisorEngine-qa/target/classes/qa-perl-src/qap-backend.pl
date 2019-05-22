#!/usr/bin/perl

# This is used as a server of sorts by the Java API to Revisor/QA.

use strict;

# Note that anything caught on STDERR is fatal to the Java client anyway
use warnings FATAL => 'all';

# CHANGE THIS AS NEEDED!
use lib qw( ./qa-perl-src
            ./qa-perl-src/QA
            ./qa-perl-src/QA/Algebra );

use QA::QCN;
use QA::PCPreprocessor qw( normalise_file );
use Algebra::Allen;
use Algebra::INDU;
use Algebra::RCC8;

# flush stdout automatically
$| = 1;

our %QCNs;
our %algebras;


while (<>) {
    chomp;
    my @request = split /\s+/;
    print handle_request( @request ), "\n";
}

sub handle_request {
    my ( $cmd, @arguments ) = @_;
    
    if ($cmd eq 'hello') {
        return request_hello( @arguments );
    }
    elsif ($cmd eq 'new') {
        return request_new( @arguments );
    }
    elsif ($cmd eq 'new_pc') {
        return request_new_pc( @arguments );
    }
    elsif ($cmd eq 'new_from_string') {
        return request_new_from_string( @arguments );
    }
    elsif ($cmd eq 'revise') {
        return request_revise( @arguments );
    }
    elsif ($cmd eq 'revise_exhaustive') {
        return request_revise_exhaustive( @arguments );
    }
    elsif ($cmd eq 'revise_exhaustive_with_distance') {
        return request_revise_exhaustive_with_distance( @arguments );
    }
    elsif ($cmd eq 'conjoin') {
        return request_conjoin( @arguments );
    }
    elsif ($cmd eq 'string') {
        return request_string( @arguments );
    }
    elsif ($cmd eq 'abstract') {
        return request_abstract( @arguments );
    }
    elsif ($cmd eq 'refine') {
        return request_refine( @arguments );
    }
    else {
        die "Received unimplemented request type: $cmd @arguments\n";
    }
    
    
}

sub request_hello {
    return 'hello';
}

sub request_new {
    my ( $file, $algebra_str ) = @_;

    # pick and eventually create the right algebra object
    my $algebra;
    if (defined $algebras{ $algebra_str }) {
        $algebra = $algebras{ $algebra_str };
    }
    else {
        if ($algebra_str =~ m{^allen$}i) {
            $algebra = new Allen;
        }
        elsif ($algebra_str =~ m{^indu$}i) {
            $algebra = new INDU;
        }
        elsif ($algebra_str =~ m{^rcc8$}i) {
            $algebra = new RCC8;
        }
        else {
            die "Received QCN creation request with unimplemented algebra: $algebra_str\n";
        }
    }
    $algebras{ $algebra_str } = $algebra unless exists $algebras{ $algebra_str };
    
    # create the QCN
    my $qcn = QCN->new( algebra => $algebra );
    $qcn->add_constraints_from_file( $file );
    my $qcn_ref = "$qcn";
    $QCNs{ $qcn_ref } = $qcn;
    
    return $qcn_ref;
}

sub request_new_pc {
    my ( $file, $algebra_str ) = @_;

    # pick and eventually create the right algebra object
    my $algebra;
    if (defined $algebras{ $algebra_str }) {
        $algebra = $algebras{ $algebra_str };
    }
    else {
        if ($algebra_str =~ m{^allen$}i) {
            $algebra = new Allen;
        }
        elsif ($algebra_str =~ m{^indu$}i) {
            $algebra = new INDU;
        }
        elsif ($algebra_str =~ m{^rcc8$}i) {
            $algebra = new RCC8;
        }
        else {
            die "Received QCN creation request with unimplemented algebra: $algebra_str\n";
        }
    }
    $algebras{ $algebra_str } = $algebra unless exists $algebras{ $algebra_str };
    
    # create the QCNs
    my @qcns = split /\*/, normalise_file( $file );
    my %qcn_hashes;
    my @qcn_refs = ();

    for my $qcn_str (@qcns) {
        my $qcn = QCN->new( algebra => $algebra );
        $qcn->add_constraints_from_string( $qcn_str );
        next if $qcn->has_impossible_constraint; # possible because constraints can be specified more than once
        next if exists $qcn_hashes{ $qcn->get_hash_not_unique };
        $qcn_hashes{ $qcn->get_hash_not_unique } = 1;
        my $qcn_ref = "$qcn";
        $QCNs{ $qcn_ref } = $qcn;
        push @qcn_refs, $qcn_ref;
    }
    return join ' ', @qcn_refs;
}

sub request_new_from_string {
    my ( $algebra_str, @string ) = @_;
    my $string = join ' ', @string;
    
    # pick and eventually create the right algebra object
    my $algebra;
    if (defined $algebras{ $algebra_str }) {
        $algebra = $algebras{ $algebra_str };
    }
    else {
        if ($algebra_str =~ m{^allen$}i) {
            $algebra = new Allen;
        }
        elsif ($algebra_str =~ m{^indu$}i) {
            $algebra = new INDU;
        }
        elsif ($algebra_str =~ m{^rcc8$}i) {
            $algebra = new RCC8;
        }
        else {
            die "Received QCN creation request with unimplemented algebra: $algebra_str\n";
        }
    }
    $algebras{ $algebra_str } = $algebra unless exists $algebras{ $algebra_str };
    
    # create the QCN
    my $qcn = QCN->new( algebra => $algebra );
    $qcn->add_constraints_from_string( $string );
    my $qcn_ref = "$qcn";
    $QCNs{ $qcn_ref } = $qcn;
    
    return $qcn_ref;
}

sub request_revise {
    my ( $psi_ref, $mu_ref ) = @_;
    my $psi = $QCNs{ $psi_ref };
    my  $mu = $QCNs{  $mu_ref };
    if ( !defined $psi || !defined $mu ) {
        die "I received references to QCN objects I don't know about: psi=$psi_ref, mu=$mu_ref\n";
    }

    $psi->uniformise_variables_with( $mu );
    $mu->uniformise_variables_with( $psi );
    $psi->restrict_relations_by_enforcing_path_consistency;
    $mu->restrict_relations_by_enforcing_path_consistency;
    my @scenarios = $psi->revise_using_a_star_with( $mu );

    $QCNs{ "$_" } = $_ for @scenarios;
    return join ' ', map { "$_" } @scenarios;
}

sub request_revise_exhaustive {
    my ( $psi_ref, $mu_ref ) = @_;
    my $psi = $QCNs{ $psi_ref };
    my  $mu = $QCNs{  $mu_ref };
    if ( !defined $psi || !defined $mu ) {
        die "I received references to QCN objects I don't know about: psi=$psi_ref, mu=$mu_ref\n";
    }

    $psi->uniformise_variables_with( $mu );
    $mu->uniformise_variables_with( $psi );
    $psi->restrict_relations_by_enforcing_path_consistency;
    $mu->restrict_relations_by_enforcing_path_consistency;
    my @scenarios = $psi->revise_using_a_star_multiple_with( $mu );

    $QCNs{ "$_" } = $_ for @scenarios;
    return join ' ', map { "$_" } @scenarios;
}

sub request_revise_exhaustive_with_distance {
    my ( $psi_ref, $mu_ref, $max_d ) = @_;
    my $psi = $QCNs{ $psi_ref };
    my  $mu = $QCNs{  $mu_ref };
    if ( !defined $psi || !defined $mu ) {
        die "I received references to QCN objects I don't know about: psi=$psi_ref, mu=$mu_ref\n";
    }

    $psi->uniformise_variables_with( $mu );
    $mu->uniformise_variables_with( $psi );
    $psi->restrict_relations_by_enforcing_path_consistency;
    $mu->restrict_relations_by_enforcing_path_consistency;
    my ($distance, @scenarios) = $psi->revise_using_a_star_multiple_distance_with( $mu, undef, undef );

    $QCNs{ "$_" } = $_ for @scenarios;
    return join ' ', map { "$_" } ($distance, @scenarios);
}

sub request_conjoin {
    my ( $qcn1_ref, $qcn2_ref ) = @_;
    my $qcn1 = $QCNs{ $qcn1_ref };
    my $qcn2 = $QCNs{ $qcn2_ref };
    $qcn1->uniformise_variables_with( $qcn2 );
    $qcn2->uniformise_variables_with( $qcn1 );
    $qcn1->restrict_relations_by_enforcing_path_consistency;
    $qcn2->restrict_relations_by_enforcing_path_consistency;
    my $qcn = $qcn1->conjoin_with( $qcn2 );
    if ( $qcn->restrict_relations_by_enforcing_path_consistency ) {
        my $qcn_ref = "$qcn";
        $QCNs{ $qcn_ref } = $qcn;       
        return $qcn_ref;
    }
    else {
        return 'INCONSISTENT';
    }
}

sub request_string {
    my ( $qcn_ref ) = @_;
    warn "No QCN given\n" unless defined $qcn_ref;
    my $qcn = $QCNs{ $qcn_ref };
    warn "Not a QCN\n" unless defined ref $qcn && ref $qcn eq 'QCN';
    return $qcn->compact_pretty_print;
}

sub request_abstract {
    my ( $qcn_ref, $varF, $i ) = @_;
    my $varT = 'X'.$i;
    my $qcn = $QCNs{ $qcn_ref };
    my $result = $qcn->pseudo_clone;
    $result->rename_variable( $varF, $varT );
    
    my $result_ref = "$result";
    $QCNs{ $result_ref } = $result;
    
    return $result_ref;
}

sub request_refine {
    my ( $qcn_ref, $varT, $i, $qcn1_ref, $qcn2_ref ) = @_;
    my $qcn = $QCNs{ $qcn_ref };
    my $qcn1 = defined $qcn1_ref ? $QCNs{ $qcn1_ref } : undef;
    my $qcn2 = defined $qcn2_ref ? $QCNs{ $qcn2_ref } : undef;
    my $varF = 'X'.$i;
    my $algebra = $qcn->algebra;
    my $eq = $algebra->equivalence_relation;
    my $refinement = QCN->new( algebra => $algebra );
    
    if ( defined $qcn1 || defined $qcn2 ) {
        my @functs;
        push @functs, @{ $qcn1->relevant_functs } if defined $qcn1;
        push @functs, @{ $qcn2->relevant_functs } if defined $qcn2;
        for my $fun (@functs) {
            $refinement->add_constraints_from_string( "$fun($varF) { $eq } $fun($varT)" );
        }
    }
    else {
        $refinement->add_constraints_from_string( "$varF { $eq } $varT" );
    }
    
    $qcn->uniformise_variables_with( $refinement );
    $refinement->uniformise_variables_with( $qcn );
    $qcn->restrict_relations_by_enforcing_path_consistency;
    $refinement->restrict_relations_by_enforcing_path_consistency;
    my $result = $qcn->conjoin_with( $refinement );
    $result->restrict_relations_by_enforcing_path_consistency;

    my $result_ref = "$result";
    $QCNs{ $result_ref } = $result;
    
    return $result_ref;

}
