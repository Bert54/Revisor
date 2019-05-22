package QCN;

use strict;
use warnings;

use Carp;

use QA::AstarSet;

use constant _INFINITY_ => 0+sprintf('%u', -1);

# le paramètre $opt est une option utilisée par
# _authorised_successor_constraint, qui indique l'heuristique désirée
#
# 31.10.2013: if experimental_h is used, it must be used on the initial state
#             as well, in case it's a final state (a very real possibility when
#             dealing with scheduling problems with disjunctive DK)
sub revise_using_a_star_multiple_with {
    my ($psi, $mu, $opt, $debug, $distance, $max_d) = @_;

    # sanity check
    if ( ! ref $mu eq 'QCN' ) {
        croak 'Attempting to revise using something that is not a QCN';
    }
    if ( $psi->{'algebra'} ne $mu->{'algebra'} ) {
        croak 'Heterogeneous revision not implemented';
    }
    
    #print STDERR "Will now revise:\n";
    #print STDERR 'psi= ', $psi->compact_pretty_print, "\n";
    #print STDERR ' mu= ', $mu->compact_pretty_print, "\n";

    my $queue = new AstarSet;
    my %closed = ( $mu->get_hash_not_unique => 1 );
    
    my $success = 0;
    my @solutions = ();
    
    my $max_f = _INFINITY_;
    my $d = _INFINITY_;
    
    # PCQA optimisation
    $max_f = $max_d if defined $max_d;
    
    # état initial
    #$queue->push( $mu, 0, $mu->h( $psi ) );
    $queue->push( $mu, 0, $mu->experimental_h( $psi ) );
    
    my $i = 0;
    my $ii = 0;

    while ( ! $queue->is_empty ) {
        
        $i++;
        
        my ($current, $g, $h) = @{ $queue->pop };
        
        #print STDERR "Unqueued $current, g=$g h=$h\n";
        
        if ($g + $h > $max_f) {
            last;
        }
        
        if ($current->is_final) {
            $success = 1;
            $max_f = $g + $h;
            $d = $g + $h;
            #print STDERR "Final! d=$d\n";
            #print STDERR $current->pretty_print;
            push @solutions, $current;
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
                    
                    #print "$i/$j: min distance between $r and {", (join ',', @{$psi->get_constraint( $i, $j )}), "} is ",  $successor->algebra->get_min_distance( [$r], $psi->get_constraint( $i, $j ) ), "\n";
                    
                    # experimental: compute f as the minimal distance between psi and mu,
                    # disregarding the difference between g and h
                    $queue->push( $successor, 0, $successor->experimental_h( $psi ) );
                    #my $new_g = $g + $successor->algebra->get_min_distance( [$r], $psi->get_constraint( $i, $j ) );
                    #$queue->push( $successor, $new_g, $successor->h( $psi ) );
                    
                    $ii++;
                    
                    #print "Adding $successor\n";
                }
            }
            
        }
        
    }
    continue {
        #print "C'est un lamentable échec !\n" if $queue->is_empty;
    }
    
    #print STDERR "visited $i states; created $ii states\n";
    
    if (defined $distance && $distance) {
        return ($d, @solutions);
    }
    else {
        return @solutions;
    }

}


sub revise_using_a_star_multiple_distance_with {
    my ($psi, $mu, $opt, $debug) = @_;
    return $psi->revise_using_a_star_multiple_with( $mu, undef, undef, 1 );
}


1;
