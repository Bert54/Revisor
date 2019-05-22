package QCN;

use strict;
use warnings;

use Carp;

use QCN::ScenarioIterator;

#use Term::ProgressBar;

sub _timestamp_ {
    my @days = qw( Su Mo Tu We Th Fr Sa );
    my @time = localtime(time);
    return sprintf( '[%s:%02d:%02d:%02d] ', $days[$time[6]], $time[2], $time[1], $time[0] );
}


sub revise_with {
    my ($psi, $mu) = @_;

    # sanity check
    if ( ! ref $mu eq 'QCN' ) {
        croak 'Attempting to revise using something that is not a QCN';
    }
    if ( $psi->{'algebra'} ne $mu->{'algebra'} ) {
        croak 'Heterogeneous revision not implemented';
    }

    #my $rev = new QCN( algebra => $mu->{'algebra'} );
    
    print STDERR _timestamp_, "Making variables uniform\n";

    $psi->uniformise_variables_with( $mu );
    $mu->uniformise_variables_with( $psi );
    
    print STDERR _timestamp_, "Getting scenarios for ψ\n";
    my $psi_scenarios = $psi->get_all_scenarios;
    
    print STDERR _timestamp_, "Getting scenarios for μ\n";
    my $mu_scenarios  =  $mu->get_all_scenarios;

    print STDERR _timestamp_, "Computing distance\n";

    my @new_scenarios;
    my $minimal_distance = 2**32;
    
    for my $mu_scenario (@$mu_scenarios) {
        
        my $distance = 2**32;
        for my $psi_scenario (@$psi_scenarios) {            
            my $local_distance = $mu_scenario->get_scenario_distance_to( $psi_scenario );
            $distance = $local_distance if $distance > $local_distance;
            
            #~ print "==========\n";
            #~ print $mu_scenario->compact_pretty_print, "----------\n";
            #~ print $psi_scenario->compact_pretty_print, "----------\n";
            #~ print $local_distance, "\n";
        }
        
        if ($distance < $minimal_distance) {
            $minimal_distance = $distance;
            @new_scenarios = ( $mu_scenario );
        }
        elsif ($distance == $minimal_distance) {
            push @new_scenarios, $mu_scenario;
        }
        
    }
    
    print STDERR _timestamp_, "All done\n";
    
    print "Minimal distance was $minimal_distance\n\n";
    for my $sc (@new_scenarios) {
        print $sc->pretty_print, "\n";
    }
    
    #return $rev;
}

# testée et fonctionne 03.08.2012
sub uniformise_variables_with {
    my ($a, $b) = @_;
    
    # sanity check
    if ( ! ref $b eq 'QCN' ) {
        croak 'Attempting to revise using something that is not a QCN';
    }
      
    for my $i (@{ $b->get_variables }) {
        $a->add_variable( $i );
    }
    
    $a->update_tuples;
    
}

# pas robuste :
#    1. ne vérifie pas qu'il s'agit bien d'un scénario
#    2. ne vérifie pas que les variables sont uniformisées
#    3. fonctionne dans tous les cas, mais donne des résultats ridicules si les conditions ne sont pas respectées
sub get_scenario_distance_to {
    my ($a, $b) = @_;
    my $pairs = $a->{'variables'}->strictly_monotonic_cartesian_square_as_array;
    my $distance = 0;
    for my $pair (@$pairs) {
        my $r = $a->get_constraint( $pair->[0], $pair->[1] );
        my $s = $b->get_constraint( $pair->[0], $pair->[1] );
        $distance += $a->{'algebra'}->get_distance( $r->[0], $s->[0] );
    }
    return $distance;
}

# petite mais efficace optimisation : arrêter de calculer la distance
# lorsqu'elle est trop grande
sub get_scenario_distance_to_maxed {
    my ($a, $b, $d) = @_;
    my $pairs = $a->{'variables'}->strictly_monotonic_cartesian_square_as_array;
    my $distance = 0;
    for my $pair (@$pairs) {
        my $r = $a->get_constraint( $pair->[0], $pair->[1] );
        my $s = $b->get_constraint( $pair->[0], $pair->[1] );
        $distance += $a->{'algebra'}->get_distance( $r->[0], $s->[0] );
        return $distance if $distance > $d;
    }
    return $distance;
}

# méthode appelée sur un scénario et recenant un QCN en paramètre
sub get_min_bound_on_scenario_network_distance {
    my ($scenario, $qcn) = @_;
    my $distance = 0;
    for my $pair (@{ $qcn->squares }) {
        my $s_relation = $scenario->get_constraint( $pair->[0], $pair->[1] )->[0];
        my $local_min = 2**8;
        for my $q_relation (@{ $qcn->get_constraint( $pair->[0], $pair->[1] ) }) {
            my $local_distance = $qcn->algebra->get_distance( $s_relation, $q_relation );
            #print "$s_relation $q_relation $local_distance\n";
            $local_min = $local_distance if $local_distance < $local_min;
        }
        $distance += $local_min;
    }
    return $distance;
}

# méthode appelée sur un scénario et recenant un QCN en paramètre
sub get_min_bound_on_network_network_distance {
    my ($a, $b) = @_;
    my $distance = 0;
    for my $pair (@{ $a->squares }) {
        my $local_min = 2**8;
        #print "Calculating distance between $pair->[0] $pair->[1] : ", (join ',', @{ $a->get_constraint( $pair->[0], $pair->[1] ) }), " and ", (join ',', @{ $b->get_constraint( $pair->[0], $pair->[1] ) }), "\n" ;
        for my $a_relation ( @{ $a->get_constraint( $pair->[0], $pair->[1] ) } ) {
            for my $b_relation ( @{ $b->get_constraint( $pair->[0], $pair->[1] ) } ) {
                my $local_distance = $a->algebra->get_distance( $a_relation, $b_relation );
                if ($local_distance < $local_min) {
                    $local_min = $local_distance;
                    #print " d($a_relation,$b_relation) = $local_distance, beats local min\n";
                }
            }
        }
        #print " Distance between $pair->[0] $pair->[1] : ", (join ',', @{ $a->get_constraint( $pair->[0], $pair->[1] ) }), " and ", (join ',', @{ $b->get_constraint( $pair->[0], $pair->[1] ) }), " is $local_min\n" ;
        $distance += $local_min;
    }
    return $distance;
}

# ne pas considérer les contraintes singleton
# $a = mu, $b = psi
sub get_min_bound_on_network_network_distance_for_h {
    my ($a, $b) = @_;
    my $distance = 0;
    for my $pair (@{ $a->squares }) {
        # singleton
        next if @{ $a->get_constraint( $pair->[0], $pair->[1] ) } <= 1;
        my $local_min = 2**8;
        #print "Calculating distance between $pair->[0] $pair->[1] : ", (join ',', @{ $a->get_constraint( $pair->[0], $pair->[1] ) }), " and ", (join ',', @{ $b->get_constraint( $pair->[0], $pair->[1] ) }), "\n" ;
        for my $a_relation ( @{ $a->get_constraint( $pair->[0], $pair->[1] ) } ) {
            for my $b_relation ( @{ $b->get_constraint( $pair->[0], $pair->[1] ) } ) {
                my $local_distance = $a->algebra->get_distance( $a_relation, $b_relation );
                if ($local_distance < $local_min) {
                    $local_min = $local_distance;
                    #print " d($a_relation,$b_relation) = $local_distance, beats local min\n";
                }
            }
        }
        #print " Distance between $pair->[0] $pair->[1] : ", (join ',', @{ $a->get_constraint( $pair->[0], $pair->[1] ) }), " and ", (join ',', @{ $b->get_constraint( $pair->[0], $pair->[1] ) }), " is $local_min\n" ;
        $distance += $local_min;
    }
    return $distance;
}




sub revise_iteratively_with {
    my ($psi, $mu, $debuglevel) = @_;
    
    # sanity check
    if ( ! ref $mu eq 'QCN' ) {
        croak 'Attempting to revise using something that is not a QCN';
    }
    if ( $psi->{'algebra'} ne $mu->{'algebra'} ) {
        croak 'Heterogeneous revision not implemented';
    }

    #my $rev = new QCN( algebra => $mu->{'algebra'} );
    
    print STDERR _timestamp_, "Making sure variables are uniform\n" if $debuglevel;

    $psi->uniformise_variables_with( $mu );
    $mu->uniformise_variables_with( $psi );
    
    print $psi->pretty_print, "\n" if $debuglevel;
    print $mu->pretty_print if $debuglevel;
   
    print STDERR _timestamp_, "Computing minimum bound on distance\n" if $debuglevel;
      
    # -1 pour incrémenter en début de boucle while
    my $max_distance =  $mu->get_min_bound_on_network_network_distance( $psi ) - 1;
    my @new_scenarios;

    while (!scalar @new_scenarios) {

        $max_distance++;
        
        print STDERR _timestamp_, "Trying distance $max_distance\n" if $debuglevel;

        my $mu_scenarios  = new ScenarioIterator( qcn => $mu  );
        
        #my $progress = Term::ProgressBar->new( { name  => "Distance $max_distance",
        #                                 count => $mu->estimate_scenario_count * $psi->estimate_scenario_count,
        #                                 ETA   => 'linear', } );
        my $progress_counter = 0;
              
        while ( my $mu_scenario = $mu_scenarios->next_consistent_scenario( \$progress_counter, $psi->estimate_scenario_count ) ) {
            
                    
            # optimisation basée sur la propritété Σ min ≤ min Σ
            if ($mu_scenario->get_min_bound_on_scenario_network_distance( $psi ) > $max_distance) {
                #print STDERR _timestamp_, "Skipped scenario\n";
                $progress_counter += $psi->estimate_scenario_count;
                #$progress->update( $progress_counter );
                #~ print $progress_counter, ' / ', $mu->estimate_scenario_count * $psi->estimate_scenario_count, "\n";
                next;
            }
            
            my $psi_scenarios  = new ScenarioIterator( qcn => $psi  );


            my $local_progress_counter = 0;
            
            while ( my $psi_scenario = $psi_scenarios->next_consistent_scenario( \$progress_counter, 1 ) ) {
                
                #$progress->update( ++$local_progress_counter + $progress_counter );
                #~ print $  progress_counter, ' / ', $mu->estimate_scenario_count * $psi->estimate_scenario_count, "\n";
                              
                if ( $mu_scenario->get_scenario_distance_to_maxed( $psi_scenario, $max_distance ) <= $max_distance ) {
                    
                    if (!scalar @new_scenarios) {
                        print STDERR _timestamp_, "$max_distance is the actual distance\n" if $debuglevel;
                    }
                    
                    push @new_scenarios, $mu_scenario->pseudo_clone;
                    #~ print STDERR _timestamp_, "Found scenario\n", $mu_scenario->pretty_print;
                    last;
                }
                
            }
            
            $progress_counter += $psi->estimate_scenario_count;
            #$progress->update( $progress_counter );
            #~ print $progress_counter, ' / ', $mu->estimate_scenario_count * $psi->estimate_scenario_count, "\n";
            
        }

    }
    
    print _timestamp_, "Completed\n" if $debuglevel;
    
    print "Minimal distance was $max_distance\n\n" if $debuglevel;
    for my $sc (@new_scenarios) {
        print $sc->pretty_print, "\n" if $debuglevel;
    }
    
    return @new_scenarios;
}


1;
