package Algebra;

use strict;
use warnings;

use Carp;
use List::MoreUtils qw(uniq);

# ces constantes sont définies ici en attendant que je trouve une
# manière intelligente de les exporter directement depuis le namespace
# des sous-classes Allen et RCC8.
use constant {
    _b_  => 0,
    _bi_ => 1,
    _d_  => 2,
    _di_ => 3,
    _o_  => 4,
    _oi_ => 5,
    _m_  => 6,
    _mi_ => 7,
    _s_  => 8,
    _si_ => 9,
    _f_  => 10,
    _fi_ => 11,
    _dc_    => 0,
    _ec_    => 1,
    _po_    => 2,
    _tpp_   => 3,
    _ntpp_  => 4,
    _tppi_  => 5,
    _ntppi_ => 6,
};

    our %r = (
        b  => 0,
        bi => 1,
        d  => 2,
        di => 3,
        o  => 4,
        oi => 5,
        m  => 6,
        mi => 7,
        s  => 8,
        si => 9,
        f  => 10,
        fi => 11,
        dc    => 0,
        ec    => 1,
        po    => 2,
        tpp   => 3,
        ntpp  => 4,
        tppi  => 5,
        ntppi => 6,
    );

sub performance_test {
    my $r = shift;
    return $r{$r};
}

sub new {
    my $class = shift;
    my %parms = @_;
    my $self = {};
    bless $self, $class;
    $self->_set_relations;
    $self->_set_equivalence_relation;
    $self->_set_transitivity_table;
    $self->_set_inverse_table;
    $self->_set_distance_table;
    return $self;
}

sub parse_relation {
    my ($self, $string) = @_;
    my @relations = split /\s*,\s*/, $string;
    for my $relation (@relations) {
        $self->validate_relation( $relation ) or croak "Invalid relation: $relation";
    }
    return \@relations;
    # j'aurais envie de retourner plutôt un objet pour améliorer la robustess
    # en aval au niveau de QCN::add_constraint
}

sub validate_relation {
    my ($self, $relation) = @_;
    return scalar grep { $_ eq $relation } @{ $self->get_relations };
}

sub get_relations {
    my ($self) = @_;
    return $self->{'relations'};
}

sub get_relation_count {
    my ($self) = @_;
    return scalar @{$self->{'relations'}};
}

sub get_composition {
    my ($self, $r, $s) = @_;
    my @t;
    for my $rr (@$r) {
        for my $ss (@$s) {
            push @t, @{ $self->get_singleton_composition( $rr, $ss ) };
        }
    }
    #print "Composition of ", (join ',', @$r), " and ", (join ',', @$s), " is ", (join ',', keys %{{ map { $_ => 1 } @$t }}), "\n";
    return [ uniq @t ]; # enlever les doublons !
}

sub get_singleton_composition {
    my ($self, $r, $s) = @_;

    # vérifier si on a une relation d'équivalence car elle ne figure pas
    # dans la table de transitivité
    if ($r eq $self->{'equivalence_relation'}) {
        return [$s];
    }
    elsif ($s eq $self->{'equivalence_relation'}) {
        return [$r];
    }

    else {
        # trouver une manière de se débarasser des eval serait souhaitable
        #return $self->{'transitivity_table'}[ eval( '_'.$r.'_' ) ][ eval( '_'.$s.'_' ) ];
        return $self->{'transitivity_table'}[ performance_test($r) ][ performance_test($s) ];
    }
}

sub reverse_constraint {
    my ($self, $r) = @_;
    my $ri;
    for my $rr (@$r) {
        push @$ri, $self->reverse_singleton_constraint( $rr );
    }
    return $ri
}

sub negate_constraint {
    my ($self, $r) = @_;
    my $ri = {map { $_ => 1 } @{$self->{'relations'}}};
    for my $rr (@$r) {
        delete $ri->{$rr};
    }
    return [keys %$ri];
}

sub reverse_singleton_constraint {
    my ($self, $r) = @_;
    
    # sanity check
    if ( ! defined( $self->{'inverse'}{ $r } ) ) {
        croak "Trying to reverse an undefined relation \{$r\}";
    }
    
    return $self->{'inverse'}{ $r };
}

# pas robuste
sub get_distance {
    my ($self, $r, $s) = @_;
    return $self->{'distance_table'}{$r}{$s};
}

# sur des ensembles de relations -- aucune robustesse non plus
sub get_min_distance {
    my ($self, $r, $s) = @_;
    my $d = 2**8;
    for my $rr (@$r) {
        for my $ss (@$s) {
            my $D = $self->get_distance( $rr, $ss );
            return 0 if $d == 0;
            $d = $D if $D < $d;
        }
    }
    return $d;
}

sub equivalence_relation {
    my $self = shift;
    return $self->{'equivalence_relation'};
}

sub relax_constraint {
    my ($self, $r) = @_;
    return [uniq map { @{$self->relax_singleton($_)} } @$r];
}


sub relax_singleton {
    my ($self, $r) = @_;
    my @res;
    my $i = 1;
    while (!@res) {
        @res = grep { $self->{'distance_table'}{$r}{$_} == $i } keys %{$self->{'distance_table'}{$r}};
        $i++;
    }
    return \@res;
}

# Those three methods only make sense in INDU
sub get_greater_length { my $self = shift; croak 'This method is not defined for algebra ' . ref $self; }
sub get_smaller_length { my $self = shift; croak 'This method is not defined for algebra ' . ref $self; }
sub get_equal_length   { my $self = shift; croak 'This method is not defined for algebra ' . ref $self; }

1;
