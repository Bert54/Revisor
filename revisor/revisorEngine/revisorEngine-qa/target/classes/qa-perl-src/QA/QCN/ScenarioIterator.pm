package ScenarioIterator;

use Carp;

sub new {
    my ($class, %param) = @_;
    
    if (!defined $param{'qcn'} || ref $param{'qcn'} ne 'QCN') {
        croak 'ScenarioIterator needs a QCN';
    }
    
    my $self = {};
    bless $self, $class;
    $self->{'qcn'} = $param{'qcn'};
    $self->_initialise_iterator;
    return $self;
}

sub qcn {
    my ($self) = @_;
    return $self->{'qcn'};
}

sub previous {
    my ($self) = @_;
    return $self->{'previous'};
}

sub constraints {
    my ($self) = @_;
    return $self->{'constraints'};
}

sub iter {
    my ($self) = @_;
    return $self->{'iter'};
}

sub count {
    my ($self) = @_;
    return $self->{'count'};
}


sub _initialise_iterator {
    my ($self) = @_;
    my $pairs = $self->qcn->squares;
    $self->{'constraints'} = [ map { [ @$_ ] } @$pairs ];
    $self->{'count'} = [ map { $self->qcn->get_constraint_relation_count( @$_ ) - 1 } @$pairs ];
    $self->{'iter'} = [ (0) x scalar @$pairs ];
    $self->{'active'} = 0;
    $self->{'previous'} = undef;
}

sub next_scenario {
    my ($self) = @_;
    if (defined $self->previous) {
        return $self->_iterate_scenario;
    }
    else {
        return $self->first_scenario;
    }
}

sub next_consistent_scenario {
    my ($self, $counter, $factor) = @_;
    while (my $scenario = $self->next_scenario) {
        return $scenario if $scenario->is_path_consistent;
        $$counter += $factor;
    }
    return undef;
}

sub first_scenario {
    my ($self) = @_;
    my $qcn = new QCN( algebra => $self->qcn->algebra );
    for my $i (0 .. $self->_last_constraint_index) {
        $qcn->add_constraint( $self->constraints->[$i][0], $self->constraints->[$i][1], [$self->qcn->get_nth_constraint( $self->constraints->[$i][0], $self->constraints->[$i][1], 0 )] );
    }
    $self->{'previous'} = $qcn;
    return $self->previous;
}

sub _last_constraint_index {
    my ($self) = @_;
    return scalar @{ $self->constraints } -1;
}

# si l'itération n'est plus possible, retournera undef
sub _iterate_scenario {
    my ($self) = @_;
    my $found;
    for my $i ( 0.. $self->_last_constraint_index ) {
        if ($self->iter->[$i] + 1 > $self->count->[$i]) {
            $self->iter->[$i] = 0;
            $self->previous->change_constraint( $self->constraints->[$i][0], $self->constraints->[$i][1], [$self->qcn->get_nth_constraint( $self->constraints->[$i][0], $self->constraints->[$i][1], $self->iter->[$i] )] );
        }
        else {
            $self->{'iter'}[ $i ]++;
            $found = 1;
            $self->previous->change_constraint( $self->constraints->[$i][0], $self->constraints->[$i][1], [$self->qcn->get_nth_constraint( $self->constraints->[$i][0], $self->constraints->[$i][1], $self->iter->[$i] )] );
            last;
        }        
    }
    return $found ? $self->previous : undef;
}

1;
