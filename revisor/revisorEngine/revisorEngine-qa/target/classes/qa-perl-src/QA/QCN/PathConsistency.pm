package QCN;

use 5.010;
use strict;
use warnings;

use Array::Utils qw(intersect array_minus);
use List::MoreUtils qw(none uniq);

# This defines path consistency checking functions for SCENARIOS
# only. At this time, it doesn't check that what it gets is a
# scenario, but for sure it won't work if it isn't!
sub is_path_consistent {
    my ($self) = @_;
    my $triples = $self->{'variables'}->strictly_monotonic_cartesian_cube_as_array;
    for my $triple (@$triples) {
        return 0 unless $self->is_locally_path_consistent( $triple->[0], $triple->[1], $triple->[2] );
    }

    return 1;
}

sub is_locally_path_consistent {
    my ( $self, $i, $j, $k ) = @_;

    # artifice pour déclarer consistent tout triangle contenant au moins
    # une relation non-spécifiée
    if (   !$self->constraint_exists( $i, $j )
        || !$self->constraint_exists( $j, $k )
        || !$self->constraint_exists( $i, $k ) )
    {
        return 1;
    }

    my $ij           = ( $self->get_constraint( $i, $j ) )->[0];
    my $jk           = ( $self->get_constraint( $j, $k ) )->[0];
    my $ik_effective = ( $self->get_constraint( $i, $k ) )->[0];
    my @ik_deduced = @{ $self->{'algebra'}->get_composition( [$ij], [$jk] ) };

    return scalar grep { $_ eq $ik_effective } @ik_deduced;
}

sub is_path_consistent_special {
    my ($self) = @_;
    if ( $self->is_path_consistent ) {
        return $self;
    }
    else {
        return undef;
    }
}

sub enforce_path_consistency {
    &enforce_path_consistency_classic;  # super slow
    #&enforce_path_consistency_pc1;      # ok
    #&enforce_path_consistency_pcvk;    # fastest
    #&enforce_path_consistency_pcvb      # slow
}


sub enforce_path_consistency_pc1 {
    my $self      = shift;
    my @variables = $self->{'variables'}->get_keys;
    my $changes   = 0;
    for my $i (@variables) {
        for my $j (@variables) {
            for my $k (@variables) {
                my $r = $self->pc_revise( $i, $j, $k );
                return -1 if $r < 0;
                $changes += $r;
            }
        }
    }
    return $changes;
}


sub enforce_path_consistency_pcvk {
    my ($self, @queue) = @_;
    my @variables = $self->{'variables'}->get_keys;
    @queue = @queue || @{ $self->squares };
    while (@queue) {
        my ( $i, $j ) = @{ pop @queue };
        for my $k (@variables) {

            # from i->j and j->k, what do we decuce on i->k
            my $r = $self->pc_revise( $i, $j, $k );
            if ( $r < 0 ) {
                return -1;
            }
            elsif ( $r > 0 ) {
                @queue = uniq @queue, ( [ $i, $k ] );
            }

            # from k->i and i->j, what do we deduce on k->j
            $r = $self->pc_revise( $k, $i, $j );
            if ( $r < 0 ) {
                return -1;
            }
            elsif ( $r > 0 ) {
                @queue = uniq @queue, ( [ $k, $j ] );
            }
        }
    }
    return 0;    # this algorithm doesn't need to be called more than once
}

sub enforce_path_consistency_pcvb {
    my $self    = shift;
    my @squares = @{ $self->squares };

    my @queue = map { $self->pcvb_related_paths(@$_) } @squares;
    while (@queue) {
        my ( $i, $k, $j ) = @{ pop @queue };
        my $r = $self->pc_revise3_bess( $i, $k, $j );
        if ( $r < 0 ) {
            return -1;
        }
        elsif ( $r > 0 ) {
            @queue = uniq @queue, $self->pcvb_related_paths( $i, $j );
        }
    }
    return 0;    # this algorithm doesn't need to be called more than once
}

sub pcvb_related_paths {
    my ( $self, $i, $j ) = @_;
    return map { $_ ne $i && $_ ne $j ? ( [ $i, $j, $_ ], [ $_, $i, $j ] ) : () } $self->{'variables'}->get_keys;
}

# from i->k and k->j, what do we deduce on i->j
sub pc_revise {
    my ( $self, $i, $k, $j ) = @_;

    # skipping condition 1
    return 0 if $i eq $j || $j eq $k || $k eq $i;

    my $ik = $self->get_constraint( $i, $k );
    my $kj = $self->get_constraint( $k, $j );

    # skipping condition 2
    return 0 if $self->{'algebra'}->get_relation_count == @$ik || $self->{'algebra'}->get_relation_count == @$kj;

    my $ij_effective = $self->get_constraint( $i, $j );
    my $ij_deduced = $self->{'algebra'}->get_composition( $ik, $kj );
    my $ij = [ intersect @$ij_effective, @$ij_deduced ];
    if ( !@$ij ) {
        return -1;
    }
    elsif ( @$ij != @$ij_effective ) {
        $self->change_constraint( $i, $j, $ij );
        return 1;
    }
    else {
        return 0;
    }
}

# Do not call directly: skipping conditions are evaluated in pc_revise3_bess
# ik and kj are optional: because revise3 expends them already, it makes sense to pass them if the order of the argument
# hasn't been swapped
sub pc_revise2_bess {
    my ( $self, $i, $k, $j, $ik, $kj ) = @_;
    my $change = 0;
    $ik //= $self->get_constraint( $i, $k );
    $kj //= $self->get_constraint( $k, $j );
    my $ij = $self->get_constraint( $i, $j );
    my @new_ij = @$ij;

    for my $gamma (@$ij) {
        if (
            none {
                intersect @{
                    $self->algebra->get_singleton_composition( $self->algebra->reverse_singleton_constraint($_),
                        $gamma )
                  },
                  @$kj;
            }
            @$ik
          )
        {

            my @gamma = ($gamma);
            @new_ij = array_minus @new_ij, @gamma;
            $change++;
            return -1 if !@new_ij;

        }

    }

    $self->change_constraint( $i, $j, \@new_ij ) if $change;
    return $change;
}

sub pc_revise3_bess {
    my ( $self, $i, $k, $j ) = @_;

    # skipping condition 1
    return 0 if $i eq $j || $j eq $k || $k eq $i;

    my $ik = $self->get_constraint( $i, $k );
    my $kj = $self->get_constraint( $k, $j );

    # skipping condition 2
    return 0 if $self->{'algebra'}->get_relation_count == @$ik || $self->{'algebra'}->get_relation_count == @$kj;

    if ( @$ik <= @$kj ) {
        return $self->pc_revise2_bess( $i, $k, $j, $ik, $kj );
    }
    else {
        return $self->pc_revise2_bess( $j, $k, $i );
    }
}

sub enforce_path_consistency_classic {
    my ($self)  = @_;
    my $triples = $self->{'variables'}->strictly_monotonic_cartesian_cube_as_array;
    my $changes = 0;
    for my $triple (@$triples) {
        my ( $i, $j, $k ) = @$triple;
        my $ij           = $self->get_constraint( $i, $j );
        my $jk           = $self->get_constraint( $j, $k );
        my $ik_effective = $self->get_constraint( $i, $k );
        my $ik_deduced = $self->{'algebra'}->get_composition( $ij, $jk );
        my $ik = [
            grep {
                my $x = $_;
                scalar grep { $x eq $_ } @$ik_deduced
              } @$ik_effective
        ];    # get the intersection of ki_deduced and ki_effective
         #print "[$i] {", (join ',', @$ij) ,"} [$j] {", (join ',', @$jk) ,"} [$k] : ",  (join ',', @$ik_effective), " / ", (join ',', @$ik_deduced), " --> ", (join ',', @$ik) ,"\n";
        if ( scalar @$ik != scalar @$ik_effective ) {
            $self->change_constraint( $i, $k, $ik );
            $changes++;
        }

        # à partir d'ici, c'est un test débile sur les permutations
        ( $i, $k, $j ) = @$triple;
        $ij           = $self->get_constraint( $i, $j );
        $jk           = $self->get_constraint( $j, $k );
        $ik_effective = $self->get_constraint( $i, $k );
        $ik_deduced = $self->{'algebra'}->get_composition( $ij, $jk );
        $ik = [
            grep {
                my $x = $_;
                scalar grep { $x eq $_ } @$ik_deduced
              } @$ik_effective
        ];    # get the intersection of ki_deduced and ki_effective
        if ( scalar @$ik != scalar @$ik_effective ) {
            $self->change_constraint( $i, $k, $ik );
            $changes++;
        }

        ( $j, $k, $i ) = @$triple;
        $ij           = $self->get_constraint( $i, $j );
        $jk           = $self->get_constraint( $j, $k );
        $ik_effective = $self->get_constraint( $i, $k );
        $ik_deduced = $self->{'algebra'}->get_composition( $ij, $jk );
        $ik = [
            grep {
                my $x = $_;
                scalar grep { $x eq $_ } @$ik_deduced
              } @$ik_effective
        ];    # get the intersection of ki_deduced and ki_effective
        if ( scalar @$ik != scalar @$ik_effective ) {
            $self->change_constraint( $i, $k, $ik );
            $changes++;
        }

        ( $j, $i, $k ) = @$triple;
        $ij           = $self->get_constraint( $i, $j );
        $jk           = $self->get_constraint( $j, $k );
        $ik_effective = $self->get_constraint( $i, $k );
        $ik_deduced = $self->{'algebra'}->get_composition( $ij, $jk );
        $ik = [
            grep {
                my $x = $_;
                scalar grep { $x eq $_ } @$ik_deduced
              } @$ik_effective
        ];    # get the intersection of ki_deduced and ki_effective
        if ( scalar @$ik != scalar @$ik_effective ) {
            $self->change_constraint( $i, $k, $ik );
            $changes++;
        }

        ( $k, $j, $i ) = @$triple;
        $ij           = $self->get_constraint( $i, $j );
        $jk           = $self->get_constraint( $j, $k );
        $ik_effective = $self->get_constraint( $i, $k );
        $ik_deduced = $self->{'algebra'}->get_composition( $ij, $jk );
        $ik = [
            grep {
                my $x = $_;
                scalar grep { $x eq $_ } @$ik_deduced
              } @$ik_effective
        ];    # get the intersection of ki_deduced and ki_effective
        if ( scalar @$ik != scalar @$ik_effective ) {
            $self->change_constraint( $i, $k, $ik );
            $changes++;
        }

        ( $k, $i, $j ) = @$triple;
        $ij           = $self->get_constraint( $i, $j );
        $jk           = $self->get_constraint( $j, $k );
        $ik_effective = $self->get_constraint( $i, $k );
        $ik_deduced = $self->{'algebra'}->get_composition( $ij, $jk );
        $ik = [
            grep {
                my $x = $_;
                scalar grep { $x eq $_ } @$ik_deduced
              } @$ik_effective
        ];    # get the intersection of ki_deduced and ki_effective
        if ( scalar @$ik != scalar @$ik_effective ) {
            $self->change_constraint( $i, $k, $ik );
            $changes++;
        }

    }
    return $changes;
}

sub enforce_total_path_consistency {
    my ($self) = @_;
    while ( my $changes = $self->enforce_path_consistency ) {

        #print "$changes changes\n";
    }

    #print "stabilised\n";
}

# 30.10.2013: do not croak if the result has impossible constraints:
#             it's very likely to occur when working with disjunctions
sub restrict_relations_by_enforcing_path_consistency {
    my ($self) = @_;
    while ( !$self->has_impossible_constraint ) {

        #print "Hello!\n";
        last unless $self->enforce_path_consistency > 0;
    }
    if ( $self->has_impossible_constraint ) {
        return 0;
    }
    else {
        return 1;
    }
}

1;
