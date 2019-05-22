package QCN;

use 5.010;
use strict;
use warnings;

use Carp;

# Does something similar to RecipeConstraint::explode, but directly in the QCN.
# The idea is that it will probably be a lot faster to close the QCN _before_ exploding it.
sub explode {
    my $self = shift;
    my @vars = grep { m{ \( .+ \+ .+ \) }x } $self->{'variables'}->get_keys;
    for my $var (@vars) {
        $self->explode_variable($var);
    }
    $self->update_tuples; # much needed now!
}

sub explode_variable {
    my ($self, $var, $only_parm) = @_;
    my ($funct, $parms) = $var =~ m{ (.+) \( (.+) \) }x;
    my @parms = split /\+/, $parms;
    
    # will explode to only two vars, with respect to specified parameter
    if (defined $only_parm) {
        my $ok_parm = join '+', grep { $_ ne $only_parm } @parms;
        @parms = ($ok_parm, $only_parm);
    }
    
    my @rels = $self->get_related_variables($var);
    for my $rel (@rels) {
        my $r = $self->get_constraint($var, $rel);

        # re-create the old constraints
        for my $parm (@parms) {
            $self->add_constraint( "$funct($parm)", $rel, $r );
        }
    }
    $self->delete_variable($var);
    
    # specify that we are all identical (it's ok, remember that will go to PSI, not MU)
    for my $parm1 (@parms) {
        for my $parm2 (@parms) {
            next if $parm1 eq $parm2;
            $self->add_constraint( "$funct($parm1)", "$funct($parm2)", [$self->algebra->get_equivalence_relation] );
        }
    }
}


# Does pretty much the opposite of explode:
# Once all the required processing has been done, replace all
# a(i) Id. a(j) with a(i+j)
# IMPORTANT NOTE: the result of applying this method without first
#                 enforcing algebraic closure will probably be: _shit_
sub collapse {
    my $self = shift;
    my @vars = grep { m{ \( .+ \) }x } $self->{'variables'}->get_keys;
    for my $i (@vars) {
        my @equals;
        for my $j (@vars) {
            next if $i eq $j;
            my $r = $self->get_constraint($i,$j) // next;
            if (@$r == 1 && $r->[0] eq $self->algebra->get_equivalence_relation) {
                push @equals, $j;
            }
        }
        $self->collapse_variables($i, @equals);
        
        # do this again after calling collapse_variables because some will have been deleted!
        @vars = grep { m{ \( .+ \) }x } $self->{'variables'}->get_keys;
    }
    $self->update_tuples;
}


sub collapse_variables {
    my ($self, @vars) = @_;
    
    my $var = $vars[0];
    
    my ($funct) = $var =~ m{ (.+) \( }x;
    
    my @parms = map {m{ \( (.+) \) }x} @vars;
    my $parm  = join '+', @parms;
    
    my @rels = $self->get_related_variables($var);
    for my $rel (@rels) {
        my $r = $self->get_constraint($var, $rel);
        $self->add_constraint( "$funct($parm)", $rel, $r );
    }
    $self->delete_variable($_) for @vars;
}



1;
