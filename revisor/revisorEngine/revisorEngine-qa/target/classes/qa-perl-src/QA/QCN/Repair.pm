package QCN;

use 5.010;
use strict;
use warnings;

use Array::Utils qw(intersect array_minus);
use List::MoreUtils qw(none uniq);

# Too many return values to keep track of, or longing for C? ;-)
use constant {
    REVR_SKIP       => 0,
    REVR_FAIL       => 1,
    REVR_NOCHANGE   => 2,
    REVR_INTERSECT  => 3,
    REVR_RELAX      => 4,
    REVR_FORCEDKEEP => 5,
};

# This method has the same prototype and function as change_constraint,
# but it will make sure the QCN remains consistent after changing the
# constraint
sub change_constraint_and_repair {
    my ( $self, $i, $j, $r ) = @_;
    $self->change_constraint( $i, $j, $r );
    $self->protect_constraint( $i, $j );
    $self->enforce_path_consistency_relaxedly( $i, $j );
    return;
}

# This method applies the PC-VK algorithm, except that when a revision
# operation fails because of an empty intersection, it relaxes the
# offending constraint instead of reporting failure
# The goal is that it should always succeed in finding a consistent
# closed QCN N^∘, even though it may be that N ⊭ N^∘
sub enforce_path_consistency_relaxedly {
    my ( $self, $I, $J ) = @_;
    my @variables = $self->{'variables'}->get_keys;
    my @queue = ( [ $I, $J ] );
    
    #DEBUG
    my $i = 0;
    while (@queue) {
        
        #DEBUG
        #say ++$i;
        $i++;        
        
        my ( $i, $j ) = @{ pop @queue };
        for my $k (@variables) {
            
            #say "Enforcing $i $j $k";

            # from i->j and j->k, what do we decuce on i->k
            my $r = $self->pc_revise_relaxedly( $i, $j, $k );
            if ( $r == REVR_INTERSECT ) {
                @queue = uniq @queue, ( [ $i, $k ] );
            }
            elsif ( $r == REVR_RELAX ) {
                # Whenever REVR_RELAX is returned, we may enqueue (i,k) as if
                # a tighetning had been done, or just requeue (i,j). Anecdotal
                # evidence seems to suggest none is more efficient than the
                # other and they give the same result.
                @queue = uniq @queue, ( [ $i, $k ] );
                #@queue = uniq @queue, ( [ $i, $j ] );
            }


            # from k->i and i->j, what do we deduce on k->j
            $r = $self->pc_revise_relaxedly( $k, $i, $j );
            if ( $r == REVR_INTERSECT ) {
                @queue = uniq @queue, ( [ $k, $j ] );
            }
            elsif ( $r == REVR_RELAX ) {
                # See note above about (k,j) vs (i,j).
                @queue = uniq @queue, ( [ $k, $j ] );
                #@queue = uniq @queue, ( [ $i, $j ] );
            }
        }
    }
    
    #DEBUG
    say "D> Finished repairing with $i steps";
    
    return;
}

# This method is where the relaxed magic happens: if we cannot constrain,
# we relax. The same skipping conditions apply.
sub pc_revise_relaxedly {
    my ( $self, $i, $k, $j ) = @_;

    # skipping condition 1
    #say "REVR_SKIP:      C$i$j irrelevant" if $i eq $j || $j eq $k || $k eq $i;
    return REVR_SKIP if $i eq $j || $j eq $k || $k eq $i;

    my $ik = $self->get_constraint( $i, $k ) // [];
    my $kj = $self->get_constraint( $k, $j ) // [];

    # skipping condition 2
    #say "REVR_SKIP:      C$i$j irrelevant" if $self->{'algebra'}->get_relation_count == @$ik || $self->{'algebra'}->get_relation_count == @$kj;
    return REVR_SKIP if $self->{'algebra'}->get_relation_count == @$ik || $self->{'algebra'}->get_relation_count == @$kj;

    my $ij_effective = $self->get_constraint( $i, $j );
    my $ij_deduced = $self->{'algebra'}->get_composition( $ik, $kj );
    my $ij = [ intersect @$ij_effective, @$ij_deduced ];
    if ( !@$ij ) {
        if ($self->protected_constraint($i, $j)) {
            #say "REVR_FORCEDKEEP:C$i$j is protected";
            return REVR_FORCEDKEEP;
        }
        else {
            $ij = $self->algebra->relax_constraint( $ij_effective );
            $self->change_constraint( $i, $j, $ij );
            #say "REVR_RELAX:     C$i$j now @$ij";
            return REVR_RELAX;
        }
    }
    elsif ( @$ij != @$ij_effective ) {
        $self->change_constraint( $i, $j, $ij );
        #say "REVR_INTERSECT: C$i$j now @$ij";
        return REVR_INTERSECT;
    }
    else {
        #say "REVR_NOCHANGE:  C$i$j not changed";
        return REVR_NOCHANGE;
    }
}


# A protected constraint can be tightened but not relaxed.
# Think about it as a constraint from μ.
sub protect_constraint {
    my ($self, $i, $j) = @_;
    $self->{'protected'}{$i}{$j} = 1;
    $self->{'protected'}{$j}{$i} = 1;
    return 1;
}

sub unprotect_constraint {
    my ($self, $i, $j) = @_;
    delete $self->{'protected'}{$i}{$j};
    delete $self->{'protected'}{$j}{$i};
    return 0;
}

sub protected_constraint {
    my ($self, $i, $j) = @_;
    return exists $self->{'protected'}{$i}{$j};
}



1;
