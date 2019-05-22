package Allen;
use base ('Algebra');

use Carp;


#~ use constant {
    #~ _b_  => 0,    # pour la table de composition
    #~ _bi_ => 1,    # l'ordre (inhabituel) est celui de Allen (1983)
    #~ _d_  => 2,
    #~ _di_ => 3,
    #~ _o_  => 4,
    #~ _oi_ => 5,
    #~ _m_  => 6,
    #~ _mi_ => 7,
    #~ _s_  => 8,
    #~ _si_ => 9,
    #~ _f_  => 10,
    #~ _fi_ => 11,
#~ };
       

sub _set_relations {
    my ($self) = @_;
    $self->{'relations'} =
      [ 'b', 'm', 'o', 's', 'd', 'f', 'eq', 'bi', 'mi', 'oi', 'si', 'di', 'fi' ];
    return 1;
}

sub _set_equivalence_relation {
    my ($self) = @_;
    $self->{'equivalence_relation'} = 'eq';
}

sub _set_inverse_table {
    my ($self) = @_;
    $self->{'inverse'} = {
        b  => 'bi',
        m  => 'mi',
        o  => 'oi',
        s  => 'si',
        d  => 'di',
        f  => 'fi',
        eq => 'eq',
        bi => 'b',
        mi => 'm',
        oi => 'o',
        si => 's',
        di => 'd',
        fi => 'f',
    }
}

sub _set_distance_table {
    my ($self) = @_;
    $self->{'distance_table'} = {
        b  => { b => 0, m => 1, o => 2, s => 3, d => 4, f => 5, eq => 4, bi => 8, mi => 7, oi => 6, si => 5, di => 4, fi => 3 },
        m  => { b => 1, m => 0, o => 1, s => 2, d => 3, f => 4, eq => 3, bi => 7, mi => 6, oi => 5, si => 4, di => 3, fi => 2 },
        o  => { b => 2, m => 1, o => 0, s => 1, d => 2, f => 3, eq => 2, bi => 6, mi => 5, oi => 4, si => 3, di => 2, fi => 1 },
        s  => { b => 3, m => 2, o => 1, s => 0, d => 1, f => 2, eq => 1, bi => 5, mi => 4, oi => 3, si => 2, di => 3, fi => 2 },
        d  => { b => 4, m => 3, o => 2, s => 1, d => 0, f => 1, eq => 2, bi => 4, mi => 3, oi => 2, si => 3, di => 4, fi => 3 },
        f  => { b => 5, m => 4, o => 3, s => 2, d => 1, f => 0, eq => 1, bi => 3, mi => 2, oi => 1, si => 2, di => 3, fi => 2 },
        eq => { b => 4, m => 3, o => 2, s => 1, d => 2, f => 1, eq => 0, bi => 4, mi => 3, oi => 2, si => 1, di => 2, fi => 1 },
        bi => { b => 8, m => 7, o => 6, s => 5, d => 4, f => 3, eq => 4, bi => 0, mi => 1, oi => 2, si => 3, di => 4, fi => 5 },
        mi => { b => 7, m => 6, o => 5, s => 4, d => 3, f => 2, eq => 3, bi => 1, mi => 0, oi => 1, si => 2, di => 3, fi => 4 },
        oi => { b => 6, m => 5, o => 4, s => 3, d => 2, f => 1, eq => 2, bi => 2, mi => 1, oi => 0, si => 1, di => 2, fi => 3 },
        si => { b => 5, m => 4, o => 3, s => 2, d => 2, f => 2, eq => 1, bi => 3, mi => 2, oi => 1, si => 0, di => 1, fi => 2 },
        di => { b => 4, m => 3, o => 2, s => 3, d => 4, f => 3, eq => 2, bi => 4, mi => 3, oi => 2, si => 1, di => 0, fi => 1 },
        fi => { b => 3, m => 2, o => 1, s => 2, d => 3, f => 2, eq => 1, bi => 5, mi => 4, oi => 3, si => 2, di => 1, fi => 0 },
    };
}

sub _set_transitivity_table {
    my ($self) = @_;
    # = cette horrible structure de données a été sauvagement piquée de
    #   http://groups.engin.umd.umich.edu/CIS/course.des/cis479/tanimoto/TEMPORAL.CL
    # = 'eq' est absent et j'ai la flemme de l'ajouter, donc il sera
    #   traité directement dans get_singleton_composition()
    $self->{'transitivity_table'} = [
        [
            [ 'b', ],
            [
                'b', 'bi', 'd', 'di', 'o', 'oi', 'm', 'mi',
                's', 'si', 'f', 'fi', 'eq'
            ],
            [ 'b', 'o', 'm', 'd', 's', ],
            [ 'b', ],
            [ 'b', ],
            [ 'b', 'o', 'm', 'd', 's', ],
            [ 'b', ],
            [ 'b', 'o', 'm', 'd', 's', ],
            [ 'b', ],
            [ 'b', ],
            [ 'b', 'o', 'm', 'd', 's', ],
            [ 'b', ],
        ],
        [
            [
                'b', 'bi', 'd', 'di', 'o', 'oi', 'm', 'mi',
                's', 'si', 'f', 'fi', 'eq'
            ],
            [ 'bi', ],
            [ 'bi', 'oi', 'mi', 'd', 'f', ],
            [ 'bi', ],
            [ 'bi', 'oi', 'mi', 'd', 'f', ],
            ['bi'],
            [ 'bi', 'oi', 'mi', 'd', 'f', ],
            [ 'bi', ],
            [ 'bi', 'oi', 'mi', 'd', 'f', ],
            [ 'bi', ],
            [ 'bi', ],
            [ 'bi', ],
        ],
        [
            [ 'b', ],
            [ 'bi', ],
            [ 'd', ],
            [
                'b', 'bi', 'd', 'di', 'o', 'oi', 'm', 'mi',
                's', 'si', 'f', 'fi', 'eq'
            ],
            [ 'b',  'o',  'm',  'd', 's', ],
            [ 'bi', 'oi', 'mi', 'd', 'f', ],
            [ 'b', ],
            [ 'bi', ],
            [ 'd', ],
            [ 'bi', 'oi', 'mi', 'd', 'f', ],
            [ 'd', ],
            [ 'b', 'o', 'm', 'd', 's', ],
        ],
        [
            [ 'b',  'o',  'm',  'di', 'fi', ],
            [ 'bi', 'oi', 'di', 'mi', 'si', ],
            [ 'o', 'oi', 'd', 's', 'f', 'di', 'si', 'fi', 'eq', ],
            [ 'di', ],
            [ 'o',  'di', 'fi', ],
            [ 'oi', 'di', 'si', ],
            [ 'o',  'di', 'fi', ],
            [ 'o',  'di', 'si', ],
            [ 'di', 'fi', 'o', ],
            [ 'di', ],
            [ 'di', 'si', 'oi', ],
            [ 'di', ],
        ],
        [
            [ 'b', ],
            [ 'bi', 'oi', 'di', 'mi', 'si', ],
            [ 'o',  'd',  's', ],
            [ 'b',  'o',  'm',  'di', 'fi', ],
            [ 'b',  'o',  'm', ],
            [ 'o', 'oi', 'd', 's', 'f', 'di', 'si', 'fi', 'eq', ],
            [ 'b', ],
            [ 'oi', 'di', 'si', ],
            [ 'o', ],
            [ 'di', 'fi', 'o', ],
            [ 'd',  's',  'o', ],
            [ 'b',  'o',  'm', ],
        ],
        [
            [ 'b', 'o', 'm', 'di', 'fi', ],
            [ 'bi', ],
            [ 'oi', 'd', 'f', ],
            [ 'bi', 'oi', 'mi', 'di', 'si', ],
            [ 'o',  'oi', 'd', 's', 'f', 'di', 'si', 'fi', 'eq', ],
            [ 'bi', 'oi', 'mi', ],
            [ 'o',  'di', 'fi', ],
            [ 'bi', ],
            [ 'oi', 'd',  'f', ],
            [ 'oi', 'bi', 'mi', ],
            [ 'oi', ],
            [ 'oi', 'di', 'si' ],
        ],
        [
            [ 'b', ],
            [ 'bi', 'oi', 'mi', 'di', 'si', ],
            [ 'o',  'd',  's', ],
            [ 'b', ],
            [ 'b', ],
            [ 'o', 'd', 's', ],
            [ 'b', ],
            [ 'f', 'fi', 'eq', ],
            [ 'm', ],
            [ 'm', ],
            [ 'd', 's', 'o', ],
            [ 'b', ],
        ],
        [
            [ 'b', 'o', 'm', 'di', 'fi', ],
            [ 'bi', ],
            [ 'oi', 'd', 'f', ],
            [ 'bi', ],
            [ 'oi', 'd', 'f', ],
            [ 'bi', ],
            [ 's', 'si', 'eq', ],
            [ 'bi', ],
            [ 'd', 'f', 'oi', ],
            [ 'bi', ],
            [ 'mi', ],
            [ 'mi', ],
        ],
        [
            [ 'b', ],
            [ 'bi', ],
            [ 'd', ],
            [ 'b',  'o', 'm', 'di', 'fi', ],
            [ 'b',  'o', 'm', ],
            [ 'oi', 'd', 'f', ],
            [ 'b', ],
            [ 'mi', ],
            [ 's', ],
            [ 's', 'si', 'eq', ],
            [ 'd', ],
            [ 'b', 'm', 'o', ],
        ],
        [
            [ 'b', 'o', 'm', 'di', 'fi', ],
            [ 'bi', ],
            [ 'oi', 'd', 'f', ],
            [ 'di', ],
            [ 'o', 'di', 'fi', ],
            [ 'oi', ],
            [ 'o', 'di', 'fi', ],
            [ 'mi', ],
            [ 's', 'si', 'eq', ],
            [ 'si', ],
            [ 'oi', ],
            [ 'di', ],
        ],
        [
            [ 'b', ],
            [ 'bi', ],
            [ 'd', ],
            [ 'bi', 'oi', 'mi', 'di', 'si', ],
            [ 'o',  'd',  's', ],
            [ 'bi', 'oi', 'mi', ],
            [ 'm', ],
            [ 'bi', ],
            [ 'd', ],
            [ 'bi', 'oi', 'mi', ],
            [ 'f', ],
            [ 'f', 'fi', 'eq', ],
        ],
        [
            [ 'b', ],
            [ 'bi', 'oi', 'mi', 'di', 'si', ],
            [ 'o',  'd',  's', ],
            [ 'di', ],
            [ 'o', ],
            [ 'oi', 'di', 'si', ],
            [ 'm', ],
            [ 'si', 'oi', 'di', ],
            [ 'o', ],
            [ 'di', ],
            [ 'f', 'fi', 'eq', ],
            [ 'fi', ],
        ],
    ];
}

1;
