package QCN;

# Méthodes pour copier des objets QCN rapidement et en économisant de
# la mémoire.  /!\ Ce module joue à un bas niveau dans les attributs
# de la classe QCN.

# Clone peut faire économiser environ 10% sur le temps pris par le
# programme, mais n'est pas disponible sur les clusters
#use Storable qw( dclone );
use Clone qw( clone );

use Time::HiRes qw( gettimeofday );

sub pseudo_clone {
    my ($self) = @_;
    my $clone = new QCN( algebra => $self->{'algebra'} );

    # petite optimisation mémoire : les variables n'ont pas de raison
    # de changer, donc on peut simplement copier la référence
    #$clone->{'variables'} = clone( $self->{'variables'} );
    $clone->{'variables'} = $self->{'variables'};
    
    # idem pour les tuples
    $clone->{'squares'} = $self->{'squares'};
    $clone->{'cubes'} = $self->{'cubes'};

    # idem pour les variables fonctionnelles pertinentes identifiées
    $clone->{'relevant_functs'} = $self->{'relevant_functs'};

    $clone->{'constraints'} = clone( $self->{'constraints'} );
    return $clone;
}

sub copy_and_restrict {
    my ($self, $i, $j, $r) = @_;
    my $copy = $self->pseudo_clone;
    $copy->change_constraint( $i, $j, [$r] );

    # pour s'assurer que chaque MD5 dans Parser.pm sera vraiment unique
    # (je suis pas sûr si c'est nécessaire, à vérifier éventuellement)
    $copy->{'creation_time'} = gettimeofday();
    return $copy;
}


1;
