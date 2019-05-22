package RCC8;
use base ('Algebra');

use strict;
use warnings;

use Carp;

use constant {
    _dc_    => 0,
    _ec_    => 1,
    _po_    => 2,
    _tpp_   => 3,
    _ntpp_  => 4,
    _tppi_  => 5,
    _ntppi_ => 6,
};

sub _set_relations {
    my ($self) = @_;
    $self->{'relations'} =
      [ 'dc', 'ec', 'tpp', 'ntpp', 'po', 'eq', 'tppi', 'ntppi' ];
}

sub _set_equivalence_relation {
    my ($self) = @_;
    $self->{'equivalence_relation'} = 'eq';
}

sub _set_inverse_table {
    my ($self) = @_;
    $self->{'inverse'} = {
        dc    => 'dc',
        ec    => 'ec',
        tpp   => 'tppi',
        ntpp  => 'ntppi',
        po    => 'po',
        eq    => 'eq',
        tppi  => 'tpp',
        ntppi => 'ntpp',
    };
}

sub _set_distance_table {
    my ($self) = @_;

    # j'utilise le graphe suivant, qui est légèrement différent de la
    # proposition dans RCC (1992)
    # dc--ec--po--eq--tpp--ntpp
    #              \--tppi--ntppi
    $self->{'distance_table'} = {
        dc => {
            dc    => 0,
            ec    => 1,
            tpp   => 3,
            ntpp  => 4,
            po    => 2,
            eq    => 3,
            tppi  => 3,
            ntppi => 4
        },
        ec => {
            dc    => 1,
            ec    => 0,
            tpp   => 2,
            ntpp  => 3,
            po    => 1,
            eq    => 2,
            tppi  => 2,
            ntppi => 3
        },
        tpp => {
            dc    => 3,
            ec    => 2,
            tpp   => 0,
            ntpp  => 1,
            po    => 1,
            eq    => 1,
            tppi  => 2,
            ntppi => 3
        },
        ntpp => {
            dc    => 4,
            ec    => 3,
            tpp   => 1,
            ntpp  => 0,
            po    => 2,
            eq    => 1,
            tppi  => 2,
            ntppi => 2
        },
        po => {
            dc    => 2,
            ec    => 1,
            tpp   => 1,
            ntpp  => 2,
            po    => 0,
            eq    => 1,
            tppi  => 1,
            ntppi => 2
        },
        eq => {
            dc    => 3,
            ec    => 2,
            tpp   => 1,
            ntpp  => 1,
            po    => 1,
            eq    => 0,
            tppi  => 1,
            ntppi => 1
        },
        tppi => {
            dc    => 3,
            ec    => 2,
            tpp   => 2,
            ntpp  => 2,
            po    => 1,
            eq    => 1,
            tppi  => 0,
            ntppi => 1
        },
        ntppi => {
            dc    => 4,
            ec    => 3,
            tpp   => 2,
            ntpp  => 2,
            po    => 2,
            eq    => 1,
            tppi  => 1,
            ntppi => 0
        },
    };
}

sub _set_transitivity_table {
    my ($self) = @_;
    $self->{'transitivity_table'} = [
        [
            #['dc'],
            [ 'dc', 'ec', 'tpp', 'ntpp', 'po', 'eq', 'tppi', 'nttpi' ],
            [ 'dc', 'ec', 'po',  'tpp',  'ntpp' ],
            [ 'dc', 'ec', 'po',  'tpp',  'ntpp' ],
            [ 'dc', 'ec', 'po',  'tpp',  'ntpp' ],
            [ 'dc', 'ec', 'po',  'tpp',  'ntpp' ],
            ['dc'],
            ['dc'],
            ['dc']
        ],[
            #['ec'],
            [ 'dc', 'ec', 'po', 'tppi', 'ntppi' ],
            [ 'dc', 'ec',  'po',  'tpp', 'tppi', 'eq' ],
            [ 'dc', 'ec',  'po',  'tpp', 'ntpp' ],
            [ 'ec', 'po',  'tpp', 'ntpp' ],
            [ 'po', 'tpp', 'ntpp' ],
            [ 'dc', 'ec' ],
            ['dc'],
            ['ec']
        ],[
            #['po'],
            [ 'dc', 'ec', 'po', 'tppi', 'ntppi' ],
            [ 'dc', 'ec', 'po', 'tppi', 'ntppi' ],
            [ 'dc', 'ec',  'tpp', 'ntpp', 'po', 'eq', 'tppi', 'nttpi' ],
            [ 'po', 'tpp', 'ntpp' ],
            [ 'po', 'tpp', 'ntpp' ],
            [ 'dc', 'ec', 'po', 'tppi', 'ntppi' ],
            [ 'dc', 'ec', 'po', 'tppi', 'ntppi' ],
            ['po']
        ],[
            #['tpp'],
            ['dc'],
            [ 'dc', 'ec' ],
            [ 'dc',  'ec', 'po', 'tpp', 'ntpp' ],
            [ 'tpp', 'ntpp' ],
            ['ntpp'],
            [ 'dc', 'ec', 'po', 'tpp',  'tppi', 'eq' ],
            [ 'dc', 'ec', 'po', 'tppi', 'ntppi' ],
            ['tpp']
        ],[
            #['ntpp'],
            ['dc'],
            ['dc'],
            [ 'dc', 'ec', 'po', 'tpp', 'ntpp' ],
            ['ntpp'],
            ['ntpp'],
            [ 'dc', 'ec', 'po', 'tpp', 'ntpp' ],
            [ 'dc', 'ec', 'tpp', 'ntpp', 'po', 'eq', 'tppi', 'nttpi' ],
            ['ntpp']
        ],[
            #['tppi'],
            [ 'dc', 'ec',   'po',   'tppi', 'ntppi' ],
            [ 'ec', 'po',   'tppi', 'ntppi' ],
            [ 'po', 'tppi', 'ntppi' ],
            [ 'po',   'tpp', 'tppi', 'eq' ],
            [ 'po',   'tpp', 'ntpp' ],
            [ 'tppi', 'ntppi' ],
            ['ntppi'],
            ['tppi']
        ],[
            #['ntppi'],
            [ 'dc', 'ec',   'po', 'tppi', 'ntppi' ],
            [ 'po', 'tppi', 'ntppi' ],
            [ 'po', 'tppi', 'ntppi' ],
            [ 'po', 'tppi', 'ntppi' ],
            [ 'po', 'tpp', 'ntpp', 'tppi', 'ntppi', 'eq' ],
            ['ntppi'],
            ['ntppi']
        ]
    ];
}

1;
