package HashSet;

sub new {
    my $class = shift;
    my $self = {};
    bless $self, $class;
    return $self;
}

sub add {
    my ($self, $member) = @_;
    return $self->{ $member } = 1;
}

sub del {
    my ($self, $member) = @_;
    return delete $self->{ $member };
}

sub has {
    my ($self, $member) = @_;
    return defined $self->{ $member };
}

sub get_keys {
    my ($self) = @_;
    return keys %$self;
}

# 29.10.2013: squares are sorted to make sure that same QCN => same hash
sub strictly_monotonic_cartesian_square_as_array {
    my ($self) = @_;
    my @set = sort keys %$self;
    my $cp = [];
    for my $m (0..$#set-1) {
        for my $n ($m+1..$#set) {
            push @$cp, [ $set[$m], $set[$n] ];
        }
    }
    return $cp
}

sub strictly_monotonic_cartesian_cube_as_array {
    my ($self) = @_;
    my @set = keys %$self;
    my $cp = [];
    for my $m (0..$#set-2) {
        for my $n ($m+1..$#set-1) {
            for my $o ($n+1..$#set) {
                push @$cp, [ $set[$m], $set[$n], $set[$o] ];
            }
        }
    }
    return $cp;
}


1;
