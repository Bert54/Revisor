package QCN;

use strict;
use warnings;

#use Devel::Size qw(size total_size);
#use Term::ProgressBar;

# algorithme simpliste :
#    1. initialiser une liste de scénarios partiels l avec un QCN vide
#    2. pour chaque couple de variables i et j :
#    3.     pour chaque scénario partiel s :
#    4.         retirer s de l
#    5.         pour chaque relation de base r entre i et j :
#    6.             ajouter un scénario partiel identique à s avec en
#                   plus la contrainte (i, j, r)

sub get_all_scenarios {
    my ($self) = @_;

    # initialisation de la liste de scnénarios par un QCN vide
    my $scenarios = [ new QCN( algebra => $self->{'algebra'} ) ];


    my $pairs = $self->{'variables'}->strictly_monotonic_cartesian_square_as_array;
    
    #~ my $iter = 0;
    #~ my $total = scalar @$pairs;


    #~ my $progress = Term::ProgressBar->new( { name  => 'Scenario generation',
                                             #~ count => $self->estimate_scenario_count,
                                             #~ ETA   => 'linear', } );
    
    # pour chaque couple de variables...
    for my $p ( 0 .. scalar @$pairs -1 ) {
        #~ print "Itération : $iter sur $total\n";
        #~ $iter++;
        my ($i, $j) = @{ $pairs->[$p] };
        
        # pour chaque scénario partiel...
        #   attention, pas un foreach, parce que le tableau est modifié
        #   à l'intérieur de la boucle
        for my $n ( 1 .. scalar @$scenarios ) {
            my $scenario = shift @$scenarios;

            my @r = @{ $self->get_constraint( $i, $j ) };
            for my $r ( @r ) {
                my $new_scenario = $scenario->pseudo_clone;
                $new_scenario->add_constraint( $i, $j, [$r] );
                #push @$scenarios, $new_scenario;
                if ($new_scenario->is_path_consistent) {
                    push @$scenarios, $new_scenario ;
                    #~ $progress->update( ++$iter );
                }
                #~ else {
                    #~ $iter += $self->estimate_scenario_count_starting_at( $p+1 );
                    #~ $progress->update( $iter );
                #~ }
                #print $iter, ' / ', $self->estimate_scenario_count, "\n";
            }

        }
    }
    return $scenarios;
}


sub _expend_scenario_space {
    my ($self, $scenarios, $pairs) = @_;
    my ($i, $j) = @{ shift @$pairs };


}

sub estimate_scenario_count {
    my ($self) = @_;
    my $pairs = $self->{'variables'}->strictly_monotonic_cartesian_square_as_array;
    my $n = 1;
    for my $pair (@$pairs) {
        #print scalar @{ $self->get_constraint( $pair->[0], $pair->[1] ) }, "\n";
        $n *= scalar @{ $self->get_constraint( $pair->[0], $pair->[1] ) };
    }
    #print "$n\n";
    return $n;
}

sub estimate_scenario_count_starting_at {
    my ($self, $x) = @_;
    my @set = $self->{'variables'}->get_keys;
    my $pairs = [];
    my $n = 1;
    for my $m ($x..$#set-1) {
        for my $n ($m+1..$#set) {
            push @$pairs, [ $set[$m], $set[$n] ];
        }
    }
    for my $pair (@$pairs) {
        $n *= scalar @{ $self->get_constraint( $pair->[0], $pair->[1] ) };
    }
    #print "$x $#set ", scalar @$pairs, " $n\n";
    return $n;
}





1;
