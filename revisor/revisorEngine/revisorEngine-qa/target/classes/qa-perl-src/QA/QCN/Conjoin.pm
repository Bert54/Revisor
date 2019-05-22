package QCN;

use strict;
use warnings;

use Carp;

sub conjoin_with {
    my ($a, $b) = @_;
    my $c = $a->pseudo_clone;
    for my $pair (@{ $c->squares }) {
        my ($i, $j) = @$pair;
        my $r = $a->get_constraint( $i, $j );
        my $s = $b->get_constraint( $i, $j );
        my $t = _intersection( $r, $s );
        if (scalar $t == 0) {
            # fail already if there will be an empty contraint
            return undef;
        }
        $c->change_constraint( $i, $j, $t );
    }

    # before returning, we could maybe enforce path consistency and
    # check again for consistency
    return $c;
}

sub _intersection {
    my ($a, $b) = @_;
    my $i = [];
    my %count = ();
    $count{$_}++ for (@$a, @$b);
    for my $e (keys %count) {
        push @$i, $e if $count{$e} == 2;
    }
    return $i
}



1;
