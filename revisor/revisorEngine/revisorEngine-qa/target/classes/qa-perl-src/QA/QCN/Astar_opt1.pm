package QCN;

use strict;
use warnings;

use Carp;
use List::Util qw( max );

use QA::AstarSet;

# Cette optimisation utilise la carte de distances calculée par
# acquire_distance_to_x.
# PROBLÈME : cet algorithme est-il complet ? sinon, on peut incrémenter
# la distance si la file se retrouve vide, mais alors est-il décidable ?
sub revise_using_a_star_opt1_with {
    my ($psi, $mu, $opt, $debug) = @_;

    # sanity check
    if ( ! ref $mu eq 'QCN' ) {
        croak 'Attempting to revise using something that is not a QCN';
    }
    if ( $psi->{'algebra'} ne $mu->{'algebra'} ) {
        croak 'Heterogeneous revision not implemented';
    }

    my $queue = new AstarSet;
    
    print "digraph {\n" if $debug;
    
    # état initial
    $queue->push( $mu, 0, $mu->h( $psi ) );
    print "subgraph cluster_" . $mu->get_hash . " {\ncolor=blue\n". $mu->to_dot . "}\n" if $debug;

    # OPT1: compteur sur la distance à X maximale
    #my $max_d = 1;
    # OPT2: en fait, je me rends compte que le compteur ne va pas devoir
    #       être incrémenté à chaque itération de la boucle while, mais
    #       bien à chaque fois qu'elle aura été vidée complètement.
    #       cf. ci-après

    my $iter = 0;
    
    # OPT2
    my $max_d_maximum = max( values %{$mu->{'distmap'}} );

    while ( ! $queue->is_empty ) {
        
        my ($current, $g) = @{ $queue->pop };
        
        # OPT1: on commence à 2, parce que à un on n'a que X {eq} i
        #$max_d++;
        # OPT2: tentons de prendre le g le plus bas de la queue + 1
        #       comme distance maximale à X (juste pour voir)
        my $max_d_theoretical = $g + 1;
        
        $iter++;
        
        #print "Distance incrémentée à $max_d\n";
        
        if ($current->is_final) {
            print "C'est un succès !\n" unless $debug;
            print $current->pretty_print unless $debug;
            last;
        }
        
        else {
            
            # jusqu'à un successeur possible par relation par contrainte
            # OPT2: boucle additionnelle pour relaxation de $max_d           
            my $pushed = 0;
            for  (my $max_d = $max_d_theoretical ; $max_d <= $max_d_maximum && !$pushed ; $max_d++ ) {
            
                # boucle sur les contraintes...
                for my $pair (@{$current->squares}) {
                    my ($i, $j) = @$pair;
                    
                    #print "On regarde $i et $j\n";
                    #print "  Les distances sont ".$mu->{'distmap'}{$i}." et ".$mu->{'distmap'}{$j}."\n";
                                    
                    # OPT: vérification de la distance à X
                    next if $mu->{'distmap'}{$i} > $max_d || $mu->{'distmap'}{$j} > $max_d;
                    
                    #print "OK\n";
                    
                    my @constraint = @{ $current->get_constraint( $i, $j ) };
                    
                    #print "  Le nb de relations est ".scalar @constraint."\n";
                    
                    next unless @constraint > 1;
                    #next unless _authorised_successor_constraint( $opt );
                    
                    # boucle sur les relations... et hop
                    for my $r (@constraint) {
                        #print "=====$r=====\n";
                        my $successor = $current->copy_and_restrict( $i, $j, $r );
                        $successor->enforce_total_path_consistency;
                        next if $successor->has_impossible_constraint;
                        my $g += $successor->algebra->get_min_distance( [$r], $psi->get_constraint( $i, $j ) );
                        $queue->push( $successor, $g, $successor->h( $psi ) );
                        
                        my $color = $successor->is_final ? 'red' : 'black';
                        #print "Adding $successor\n";
                        print "subgraph cluster_" . $successor->get_hash . " {\ncolor=$color\n". $successor->to_dot . "}\n" if $debug;
                        printf 'v_%s_X -> v_%s_X [label="%s",ltail=cluster_%s,lhead=cluster_%s]%s', $current->get_hash, $successor->get_hash, $iter, $current->get_hash, $successor->get_hash, "\n" if $debug;
                        #print "cluster_" . $current->get_hash . " -> cluster_" . $successor->get_hash . " [label=\"$iter\"]\n" if $debug;
                        
                        $pushed++;
                        
                    }
                }
            }
            
        }
        
    }
    continue {
        print "C'est un lamentable échec !\n" if $queue->is_empty && !$debug;
    }

    print "}\n" if $debug;

}


# Cette méthode prend pour acquis qu'il existe une variable nommée X,
# issue de l'abstraction. Elle calcule la distance des variables à X et
# l'enregistre dans l'objet QCN. Il est indispensable que cette méthode
# soit appelée avant restrict_relations_by_enforcing_path_consistency,
# car le graphe du RCQ devient explicitement complet après cette
# opération. (Avant, il n'est qu'implicitement complet.)
sub acquire_distance_to_x {
    my ($self) = @_;
    my $hashgraph = $self->to_hashgraph;
    my %distmap;
    for my $i (@{$self->get_variables}) {
        $distmap{$i} = _distance( $self->to_hashgraph, 'X', $i );
    }
    $self->{'distmap'} = \%distmap;
}

sub to_hashgraph {
    my ($self) = @_;
    my %hashgraph;
    for my $i (@{$self->get_variables}) {
        for my $j (@{$self->get_variables}) {
            next if $i eq $j;
            next unless $self->get_constraint_if_explicit( $i, $j );
            $hashgraph{$i}{$j} = 1;
            $hashgraph{$j}{$i} = 1;
        }
    }
    return \%hashgraph;
}


# adapté d'un perl golf, d'où l'aspect rebutant !
#   http://www.perlmonks.org/?node_id=79811
sub _distance {
    no strict;
    no warnings;
    (*g,$f,$t)=@_;
    @s=[$f,0];
    push@s,map[@p,$_,$d+$r{$_}],keys%{*r=$g{$n}}
      while(@s=sort{$b->[-1]<=>$a->[-1]}@s),$d=pop@{*p=pop@s},
      ($n=$p[-1])ne$t;
    return scalar @p - 1;
}



1;
