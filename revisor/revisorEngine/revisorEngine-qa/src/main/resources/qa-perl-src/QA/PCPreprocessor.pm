# This is a new module created 29.10.2013 to normalise extended QCN files

package QA::PCPreprocessor;

use strict;
use warnings;

use base qw( Exporter );

use File::Slurp;

our @EXPORT_OK = qw( normalise_file );

sub normalise_file {
    my $file = shift;

    my $variable_tokens = qr{ [\w-] }x;
    
    if ( ! -r $file ) {
         die "Unable to open file $file for normalisation\n";
    }
    
    my @stack = read_file( $file );
    
    my $qcns = [[]];
    
    while (scalar @stack) {
        my $line = shift @stack;
        chomp $line;   
        $line =~ s{ % .* }{}x;
        next unless $line =~ m{ $variable_tokens }x;
        
        # disjunction
        if ( my ($disjunction) = $line =~ m{ ^ \s* or \s* \( \s* (.+) \s* \) \s* $ }x ) {
            my @disjuncts = split /\s*[;&]\s*/, $disjunction;
            $qcns = split_and_push( $qcns, @disjuncts );
        }
        
        # negation
        elsif ( my ($negation) = $line =~ m{ ^ \s* not \s* \( \s* (.+) \s* \) \s* $ }x ) {
            if ($negation =~ m{ [;&] }) {   # DM1
                my @conjuncts = split /\s*[&]\s*/, $negation;
                my @dm;
                for (@conjuncts) {
                    my ($v1, $r, $v2) = m{ \s* ($variable_tokens+) \s* \{ \s* (.+?) \s* \} \s* ($variable_tokens+) \s* }x;
                    push @dm, "$v1 { not( $r ) } $v2";
                }
                my $dm = 'or( ' . (join ' ; ', @dm) . ' )';
                unshift @stack, $dm;
            }
            elsif ($negation =~ m{ [|] }) { # DM2
                my @disjuncts = split /\s*[|]\s*/, $negation;
                for (@disjuncts) {
                    my ($v1, $r, $v2) = m{ \s* ($variable_tokens+) \s* \{ \s* (.+?) \s* \} \s* ($variable_tokens+) \s* }x;
                    unshift @stack, "$v1 { not( $r ) } $v2";
                }
            }
            else {                          # atomic
                my ($v1, $r, $v2) = $negation =~ m{ \s* ($variable_tokens+) \s* \{ \s* (.+?) \s* \} \s* ($variable_tokens+) \s* }x;
                unshift @stack, "$v1 { not( $r ) } $v2";
            }
        }
        
        # conjoined atomic statement
        else {
            push_all( $qcns, $line );
        }
    }
    
    return print_all( $qcns );
    
}



sub print_all {
    my ($qcns) = @_;
    return join "*", map { join ';', @$_ } @$qcns;
}

sub push_all {
    my ($qcns, $statement) = @_;
    for my $qcn (@$qcns) {
        push @$qcn, $statement;
    }
}

sub split_and_push {
    my ($qcns, @statements) = @_;
    my $new_qcns = [];
    for my $qcn (@$qcns) {
        for my $statement (@statements) {
            push @$new_qcns, [ @$qcn, $statement ];
        }
    }
    return $new_qcns;
}





1;
