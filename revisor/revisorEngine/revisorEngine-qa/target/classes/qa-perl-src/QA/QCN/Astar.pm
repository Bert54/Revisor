package QCN;

use strict;
use warnings;

use Carp;

use QA::AstarSet;

# le paramètre $opt est une option utilisée par
# _authorised_successor_constraint, qui indique l'heuristique désirée
sub revise_using_a_star_with {
    my ($psi, $mu, $opt, $debug) = @_;

    # sanity check
    if ( ! ref $mu eq 'QCN' ) {
        croak 'Attempting to revise using something that is not a QCN';
    }
    if ( $psi->{'algebra'} ne $mu->{'algebra'} ) {
        croak 'Heterogeneous revision not implemented';
    }

    my $queue = new AstarSet;
    my %closed = ( $mu->get_hash_not_unique => 1 );
    
    my $success = 0;
    
    # état initial
    $queue->push( $mu, 0, $mu->h( $psi ) );
    
    print STDERR $mu->get_hash, "[label='0+", $mu->h( $psi ), "'];\n" if $debug;
    
    my $i = 0;
    my $ii = 0;

    while ( ! $queue->is_empty ) {
        
        $i++;
        
        my ($current, $g, $h) = @{ $queue->pop };
        
        if ($current->is_final) {
            return ($current);
        }
        
        else {
            
            # jusqu'à un successeur possible par relation par contrainte
            
            # boucle sur les contraintes...
            for my $pair (@{$current->squares}) {
                my ($i, $j) = @$pair;
                my @constraint = @{ $current->get_constraint( $i, $j ) };
                next unless @constraint > 1;
                next unless _authorised_successor_constraint( $opt );
                
                # boucle sur les relations... et hop
                for my $r (@constraint) {
                    my $successor = $current->copy_and_restrict( $i, $j, $r );
                    
                    $successor->enforce_total_path_consistency;
                    next if $successor->has_impossible_constraint;

                    # handle closed set stuff
                    next if defined $closed{ $successor->get_hash_not_unique };
                    $closed{ $successor->get_hash_not_unique } = 1;

                    # experimental: compute f as the minimal distance between psi and mu,
                    # disregarding the difference between g and h
                    $queue->push( $successor, 0, $successor->experimental_h( $psi ) );
                    #my $new_g = $g + $successor->algebra->get_min_distance( [$r], $psi->get_constraint( $i, $j ) );
                    #$queue->push( $successor, $new_g, $successor->h( $psi ) );
                    
                    #print STDERR $successor->get_hash, "[label='", $new_g, '+', $successor->h( $psi ), "'];\n" if $debug;
                    #print STDERR $current->get_hash, '->', $successor->get_hash, ";\n" if $debug;
                    
                    $ii++;
                    
                    #print "Adding $successor\n";
                }
            }
            
        }
        
    }
    continue {
        print "C'est un lamentable échec !\n" if $queue->is_empty;
    }
    
    print "visited $i states; created $ii states\n";
    
    return $success;

}

# $qcn1 = mu, $qcn2 = psi
sub h {
    my ($qcn1, $qcn2) = @_;
    return $qcn1->get_min_bound_on_network_network_distance_for_h( $qcn2 );
}
sub experimental_h {
    my ($qcn1, $qcn2) = @_;
    return $qcn1->get_min_bound_on_network_network_distance( $qcn2 );
}

sub is_final {
    my ($qcn) = @_;
    return $qcn->has_only_singleton_constraints;
    #if ($qcn->has_only_singleton_constraints) {
    #    my $test_qcn = $qcn->pseudo_clone;
    #    $test_qcn->enforce_total_path_consistency;
    #    return !$test_qcn->has_impossible_constraint;
    #}
    #else {
    #    return undef;
    #}
}

sub _authorised_successor_constraint {
    my ($opt) = @_;
    return 1 unless defined $opt;
    return 0;
}




1;
