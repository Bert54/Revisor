package QCN;

use strict;
use warnings;

use QCN::Astar;
use QCN::Astar_opt1;
use QCN::Clone;
use QCN::Conjoin;
use QCN::Explosion;
use QCN::MultipleSolutionAstar;
use QCN::Parser;
use QCN::PathConsistency;
use QCN::Repair;
use QCN::Revision;
use QCN::ScenarioGeneration;

use Carp 'confess';
use Data::Dumper;
#use Devel::Size qw(size total_size);

use HashSet;    # trouvé dans @INC grâce au script appelant, à arranger

sub new {
    my $class = shift;
    my %params = @_;
    my $self = {};
    bless $self, $class;
    $self->_initialise_params( \%params );
    $self->_initialise_empty_qcn;
    return $self;
}

sub algebra {
    my ($self) = @_;
    return $self->{'algebra'};
}

sub variables {
    my ($self) = @_;
    return $self->{'variables'};
}

sub virtual_variables {
    my $self = shift;
    return grep { $_ =~ $self->virtual_regex } $self->variables->get_keys;
}

# encapsulate this regex because I'm thinking of changing the format
# to something more distinctive like #value#[unit]
sub virtual_regex {
    qr{^(\d+)min$}o;
}


sub squares {
    my ($self) = @_;
    return $self->{'squares'};
}

sub cubes {
    my ($self) = @_;
    return $self->{'cubes'};
}

sub _initialise_params {
    my ($self, $params) = @_;
    
    # sanity check
    if (!defined $params->{'algebra'}) {
        croak 'Cannot create QCN without algebra';
    }
    if (ref $params->{'algebra'} ne 'Allen' && ref $params->{'algebra'} ne 'RCC8' && ref $params->{'algebra'} ne 'INDU' && ref $params->{'algebra'} ne 'InduReduit') {
        croak 'Algebra passed to QCN constructor is not a known Algebra type';
    }
    
    $self->{'algebra'} = $params->{'algebra'};
}

sub _initialise_empty_qcn {
    my ($self) = @_;
    $self->{'variables'} = new HashSet;
    $self->{'constraints'} = {};
    $self->{'relevant_functs'} = [];
    $self->update_tuples;
}

# note: if this method is called multiple times with the same two variables,
#       the intersection of the specified constraints will be retained
sub add_constraint {
    my ($self, $i, $j, $r) = @_;
    $self->add_variable( $i );
    $self->add_variable( $j );

    # sanity check
    # (changed 29.10.2013 for extended format because some constraints can be
    #  specified twice--because those are mostly caused by applying De Morgan
    #  law, I'm not sure if we need this here, but we sure need it in the
    # _reverse version)
    if ($self->constraint_exists( $i, $j )) {
        my $s = $self->get_constraint( $i, $j );
        my $t = _intersection( $r, $s );
        $self->change_constraint( $i, $j, $t );
    }
    else {
        $self->{'constraints'}{$i}{$j} = _sort_constraint( $r );
    }
}

# Automatically add an order constraint between two virtual variables
sub add_virtual_constraint {
    my ($self, $i, $j) = @_;
    if (ref $self->algebra ne 'INDU') {
        croak 'add_virtual_constraint supports only the INDU algebra';
    }
    my $in = $self->virtual_value($i);
    my $jn = $self->virtual_value($j);
    if ($in > $jn) {
        $self->add_constraint($i, $j, $self->algebra->get_greater_length);
    }
    elsif ($in < $jn) {
        $self->add_constraint($i, $j, $self->algebra->get_smaller_length);
    }
    # it should not actually happen that two different virtuals have the same value...
    else {
        $self->add_constraint($i, $j, $self->algebra->get_equal_length);
    }
}

sub virtual_value {
    my ($self, $i) = @_;
    my ($value) = $i =~ $self->virtual_regex;
    return $value;
}


# new version 29.10.2013 for extended format
sub add_constraint_negate {
    my ($self, $i, $j, $r) = @_;
    $self->add_variable( $i );
    $self->add_variable( $j );

    # sanity check
    if ($self->constraint_exists( $i, $j )) {
        my $s = $self->get_constraint( $i, $j );
        my $t = _intersection( $self->{'algebra'}->negate_constraint( $r ), $s );
        $self->change_constraint( $i, $j, $t );
        #print STDERR (join ',', @{$self->{'algebra'}->negate_constraint( $r )}) .' o '. (join ',', @$s) . ' = ' . (join ',', @$t) . "\n";
    }
    else {
        $self->{'constraints'}{$i}{$j} = _sort_constraint( $self->{'algebra'}->negate_constraint( $r ) );
    }
}

sub change_constraint {
    my ($self, $i, $j, $r) = @_;

    # sanity check
    if (! $self->variable_exists($i) || ! $self->variable_exists($j)) {
        croak 'Trying to change a constraint between variables that do not exist';
    }

    #my $orig = $self->get_constraint($i,$j);
    #print "Changing constraint <$i> <$j> from {@$orig} to {@$r}\n";

    # if ji exists, ij doesn't exist. either we reverse or we delete (and the latter is just faster)
    delete $self->{'constraints'}{$j}{$i} if exists $self->{'constraints'}{$j}{$i};

    $self->{'constraints'}{$i}{$j} = _sort_constraint( $r );

}

sub add_random_constraint {
    my ($self, $i, $j) = @_;

    my @rels = @{ $self->{'algebra'}->get_relations };
    my $n = int(rand( scalar @rels ));
    my $r = [];
    for my $i (0..$n) {
        my $index = int(rand( scalar @rels ));
        push @$r, splice @rels, $index, 1;
    }
    $self->add_constraint( $i, $j, $r );
}

sub constraint_exists {
    my ($self, $i, $j) = @_;
    return defined $self->{'constraints'}{$i}{$j} || defined $self->{'constraints'}{$j}{$i};
}

sub variable_exists {
    my ($self, $i) = @_;
    return $self->{'variables'}->has($i);
}

sub ordered_constraint_exists {
    my ($self, $i, $j) = @_;
    return exists $self->{'constraints'}{$i}{$j};
}

sub add_variable {
    my ($self, $i) = @_;
    $self->{'variables'}->add( $i );
}

# note: added constraint deletion, will need to check whether this breaks explode
sub delete_variable {
    my ($self, $i) = @_;
    $self->{'variables'}->del( $i );
    delete $self->{'constraints'}{$i};
    delete $self->{'constraints'}{$_}{$i} for grep { exists $self->{'constraints'}{$_}{$i} } keys %{$self->{'constraints'}};
}


sub get_variables {
    my ($self, $i) = @_;
    return [$self->{'variables'}->get_keys];
}

sub get_constraint {
    my ($self, $i, $j) = @_;
      
    if ( $self->ordered_constraint_exists( $i, $j ) ) {
        return $self->{'constraints'}{$i}{$j};
    }
    elsif ( $self->ordered_constraint_exists( $j, $i ) ) {
        return $self->{'algebra'}->reverse_constraint( $self->{'constraints'}{$j}{$i} );
    }
    else {
        if ( $self->{'variables'}->has( $i ) && $self->{'variables'}->has( $j ) ) {
            # si les variables existent mais aucune contrainte n'est
            # définie, renvoyer toutes les relations de l'algèbre
            return $self->{'algebra'}->get_relations;
        }
        else {
            #croak 'Trying to get a constraint over variables that do not exist';
            confess 'Trying to get a constraint over variables that do not exist';
        }
    }
}

# This method returns only explicit constraints
sub constraints {
    my $self = shift;
    return map { my $i = $_; map {[$i,$_]} keys %{$self->{'constraints'}{$i}} } keys %{$self->{'constraints'}};
}


sub delete_constraint {
    my ($self, $i, $j) = @_;
      
    if ( $self->ordered_constraint_exists( $i, $j ) ) {
        delete $self->{'constraints'}{$i}{$j};
    }
    elsif ( $self->ordered_constraint_exists( $j, $i ) ) {
        delete $self->{'constraints'}{$j}{$i};
    }
    else {
        croak 'Trying to delete a constraint that does not exist or was not explicitely set';
    }
}


# cette méthode retourne undef si la contrainte n'a pas été
# explicitement spécifiée
sub get_constraint_if_explicit {
    my ($self, $i, $j) = @_;
      
    if ( $self->ordered_constraint_exists( $i, $j ) ) {
        return $self->{'constraints'}{$i}{$j};
    }
    elsif ( $self->ordered_constraint_exists( $j, $i ) ) {
        return $self->{'algebra'}->reverse_constraint( $self->{'constraints'}{$j}{$i} );
    }
    else {
        if ( $self->{'variables'}->has( $i ) && $self->{'variables'}->has( $j ) ) {
            # si les variables existent mais aucune contrainte n'est
            # définie, renvoyer undef !
            return undef;
        }
        else {
            croak 'Trying to get a constraint over variables that do not exist';
        }
    }
    
}

# Returns all the variables which have constraints that were explitely set relative to the given variable
sub get_related_variables {
    my ($self, $i) = @_;
    return keys %{$self->{'constraints'}{$i}}, grep { exists $self->{'constraints'}{$_}{$i} } keys %{$self->{'constraints'}};
}



sub get_nth_constraint {
    my ($self, $i, $j, $n) = @_;
    return $self->get_constraint( $i, $j )->[$n];
}

# fonction ajoutée pour get_constraint_relation_count
sub get_constraint_dont_reverse {
    my ($self, $i, $j) = @_;
      
    if ( $self->ordered_constraint_exists( $i, $j ) ) {
        return $self->{'constraints'}{$i}{$j};
    }
    elsif ( $self->ordered_constraint_exists( $j, $i ) ) {
        return $self->{'constraints'}{$j}{$i};
    }
    else {
        if ( $self->{'variables'}->has( $i ) && $self->{'variables'}->has( $j ) ) {
            # si les variables existent mais aucune contrainte n'est
            # définie, renvoyer toutes les relations de l'algèbre
            return $self->{'algebra'}->get_relations;
        }
        else {
            croak 'Trying to get a constraint over variables that do not exist';
        }
    }
    
}

sub get_constraint_relation_count {
    my ($self, $i, $j) = @_;
    return scalar @{ $self->get_constraint_dont_reverse( $i, $j ) };
}

sub has_impossible_constraint {
    my ($self) = @_;
    for my $i (keys %{$self->{'constraints'}}) {
        for my $j (keys %{$self->{'constraints'}{$i}}) {
            return 1 if scalar @{$self->{'constraints'}{$i}{$j}} == 0;
        }
    }
    return undef;
}

sub has_only_singleton_constraints {
    my ($self) = @_;
    for my $pair (@{$self->squares}) {
        return undef if $self->get_constraint_relation_count( $pair->[0], $pair->[1] ) != 1;
    }
    return 1;
}

sub update_tuples {
    my ($self) = @_;
    $self->{'squares'} = $self->variables->strictly_monotonic_cartesian_square_as_array;
    $self->{'cubes'} = $self->variables->strictly_monotonic_cartesian_cube_as_array;
}


sub rename_variable {
    my ($self, $old, $new) = @_;
    
    if ( $self->{'variables'}->has( $old ) ) {
        $self->rename_simple_variable( $old, $new );
    }
    else {
        $self->rename_parametrised_variable( $old, $new );
    }
          
    $self->update_tuples;
    
}

sub rename_simple_variable {
    my ($self, $old, $new) = @_;
    $self->{'variables'}->del( $old );
    $self->{'variables'}->add( $new );
    for my $i ( keys %{$self->{'constraints'}} ) {
        if ($i eq $old) {
            $self->{'constraints'}{$new} = $self->{'constraints'}{$old};
            delete $self->{'constraints'}{$old};
        }
        else {
            for my $j ( keys %{$self->{'constraints'}{$i}} ) {
                if ($j eq $old) {
                    $self->{'constraints'}{$i}{$new} = $self->{'constraints'}{$i}{$old};
                    delete $self->{'constraints'}{$i}{$old};
                    last;
                }
            }
        }
    }    
}

sub rename_parametrised_variable {
    my ($self, $old, $new) = @_;
    my @vars = $self->{'variables'}->get_keys;
    my @change_vars = grep { m{ \( \s* $old \s* \) }x } @vars;
    for my $old_v (@change_vars) {
        my $new_v = $old_v;
        $new_v =~ s{ \( \s* $old \s* \) }{($new)}x;
        $self->rename_simple_variable( $old_v, $new_v );
        my ($fun) = $new_v =~ m{ (.+) \( }x;
        push @{$self->{'relevant_functs'}}, $fun;
    }
}

sub delete_parameter {
    my ($self, $param) = @_;
    my @vars = $self->{'variables'}->get_keys;
    my @del_vars = grep { m{ \( \s* $param \s* \) }x } @vars;
    $self->delete_variable($_) for @del_vars;
}


# does as above, but in case it finds a(i+j+k) and we are renaming i -> x,
# it will explode the variable as a(x) and a(j+k)
sub abstract_with_explosion {
    my ($self, $old, $new) = @_;
    my @vars = $self->{'variables'}->get_keys;
    my @change_vars = grep { m{ \( .* $old .* \) }x } @vars;
    for my $old_v (@change_vars) {
        $self->explode_variable($old_v, $old);
    }
    $self->rename_parametrised_variable($old, $new);
    $self->update_tuples;
}


sub relevant_functs {
    my ($self) = @_;
    return $self->{'relevant_functs'};
}

sub _sort_constraint {
    my $r = shift // [];
    return [sort @$r];
}

#~ sub DESTROY {
    #~ my ($self) = @_;
    #~ my $size = total_size( $self );
    #~ print "Destruction de $self qui prenait $size octets\n";
#~ }

1;
