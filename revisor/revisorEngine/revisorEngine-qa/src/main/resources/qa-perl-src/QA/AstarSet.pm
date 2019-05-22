# Cette classe définit une file avec priorité pour l'algorithme A*

package AstarSet;

use strict;
use warnings;

# à vérifier : l'effet réel de ces pragmas sur les performances.
# mergesort devrait être plus rapide pour ajouter un élément à une liste
# déjà triée.
use sort '_mergesort';
no  sort 'stable';

use Carp;

# très chiant : il semble qu'une fois la variable bénie, on ne puisse
# plus changer la valeur de la référence. du coup, impossible d'écrire
# quelque chose du genre $self = [ sort $machin, @$self ]. (en fait
# c'est possible, mais la nouvelle référence est perdue à tout jamais
# à la fin de l'exécution de la méthode. c'est la raison pour laquelle
# $self est un hachage à une seule valeur. vivement Perl 6 !

sub new {
    my ($class) = @_;
    my $self = { q => [] };
    bless $self, $class;
    return $self;
}

sub is_empty {
    my ($self) = @_;
    return ! @{ $self->{'q'} };
}

sub push {
    my ($self, $qcn, $g, $h) = @_;
    my $state = [ $qcn, $g, $h, $g + $h ];
    $self->{'q'} = [ sort _by_f $state, @{ $self->{'q'} } ];
    #print "Pushed $qcn, $g, $h\n";
}

# en réalité, l'ordre de classement en fait un shift. mais on s'en fout.
sub pop {
    my ($self) = @_;
    return shift @{ $self->{'q'} };
}

# fonction pour sort
sub _by_f {
    $a->[3] <=> $b->[3];
}



1;
