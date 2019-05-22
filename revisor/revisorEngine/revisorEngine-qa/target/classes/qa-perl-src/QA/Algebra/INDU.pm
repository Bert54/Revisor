package INDU;
use strict;
use warnings;
use 5.010;
use base ('Allen');

use Carp;

use INDU::DistanceTable;
use INDU::TransitivityTable;


our %r = (
    b     => 0,
    bi    => 1,
    d     => 2,
    di    => 3,
    o     => 4,
    oi    => 5,
    m     => 6,
    mi    => 7,
    s     => 8,
    si    => 9,
    f     => 10,
    fi    => 11,
    dc    => 0,
    ec    => 1,
    po    => 2,
    tpp   => 3,
    ntpp  => 4,
    tppi  => 5,
    ntppi => 6,
);

our %relations = (
    'b<'  => 1,
    'b='  => 1,
    'b>'  => 1,
    'm<'  => 1,
    'm='  => 1,
    'm>'  => 1,
    'o<'  => 1,
    'o='  => 1,
    'o>'  => 1,
    's<'  => 1,
    'd<'  => 1,
    'f<'  => 1,
    'eq=' => 1,
    'bi<' => 1,
    'bi=' => 1,
    'bi>' => 1,
    'mi<' => 1,
    'mi=' => 1,
    'mi>' => 1,
    'oi<' => 1,
    'oi=' => 1,
    'oi>' => 1,
    'si>' => 1,
    'di>' => 1,
    'fi>' => 1
);

our %inverse = (
    'mi<' => 'm>',
    'oi=' => 'o=',
    'o='  => 'oi=',
    'b='  => 'bi=',
    'm='  => 'mi=',
    's<'  => 'si>',
    'oi<' => 'o>',
    'd<'  => 'di>',
    'b>'  => 'bi<',
    'oi>' => 'o<',
    'o<'  => 'oi>',
    'bi<' => 'b>',
    'si>' => 's<',
    'bi>' => 'b<',
    'm<'  => 'mi>',
    'bi=' => 'b=',
    'f<'  => 'fi>',
    'fi>' => 'f<',
    'di>' => 'd<',
    'b<'  => 'bi>',
    'mi>' => 'm<',
    'm>'  => 'mi<',
    'eq=' => 'eq=',
    'o>'  => 'oi<',
    'mi=' => 'm='
);

# TODO: Is this still needed?
sub performance_test {
    my $r = shift;
    return $r{$r};
}

# TODO: Is this still needed?
sub __indu_signs_allowed__ {
    {
        b  => [ '<', '=', '>' ],
        m  => [ '<', '=', '>' ],
        o  => [ '<', '=', '>' ],
        s  => ['<'],
        d  => ['<'],
        f  => ['<'],
        eq => ['='],
        bi => [ '<', '=', '>' ],
        mi => [ '<', '=', '>' ],
        oi => [ '<', '=', '>' ],
        si => ['>'],
        di => ['>'],
        fi => ['>'],
    };
}

# TODO: Is this still needed?
sub __indu_signs__ {
    [ '<', '=', '>' ];
}

# TODO: Is this still needed?
sub __indu_inverse__ {
    {
        '<' => '>',
        '=' => '=',
        '>' => '<',
    };
}

# TODO: Is this still needed?
sub __indu_distance__ {
    {
        '<' => { '<' => 0, '=' => 1, '>' => 2 },
        '=' => { '<' => 1, '=' => 0, '>' => 1 },
        '>' => { '<' => 2, '=' => 1, '>' => 0 },
    };
}

sub get_equivalence_relation { 'eq=' }

# those three are obviously specific to INDU
sub get_greater_length { [qw(b> m> o> bi> mi> oi> si> di> fi>)] }
sub get_smaller_length { [qw(b< m< o< bi< mi< oi< s< d< f<)] }
sub get_equal_length   { [qw(b= m= o= bi= mi= oi= eq=)] }


# The two methods below are overloaded in INDU to improve performances.
# It probably shouldn't be that way though.
sub get_relations {
    return [ keys %relations ];
}
sub get_relation_count {
    return 25;
}

# TODO: Is this still needed?
# retourne vrai si la combinaison de relation d'intervalle et de durée
# est possible
sub _indu_allowed_ {
    my ($r) = @_;
    return exists $relations{$r};
}


# most INDU overloaded methods have been replaced with classic Algebra handling for efficiency
sub _set_transitivity_table {
    my ($self) = @_;
    $self->{'transitivity_table'} = _transitivity_;
}

sub _set_distance_table {
    my ($self) = @_;
    $self->{'distance_table'} = _distance_table_;
}

sub _set_inverse_table {
    my ($self) = @_;
    $self->{'inverse'} = \%inverse;
}


# We need a different composition method because our table is a hash of hashes of arrays,
# whether the other algebras have an array of arrays of arrays
sub get_singleton_composition {
    my ($self, $r, $s) = @_;
    return $self->{'transitivity_table'}{$r}{$s};
}



1;
