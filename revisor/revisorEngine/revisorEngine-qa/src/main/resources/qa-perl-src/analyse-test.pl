#!/usr/bin/perl

use 5.010;
use strict;
use warnings;

my %perf;

while (<>) {
    chomp;
    my @f = split ':';
    next unless @f == 7;
    if ($f[5]) {
        $perf{$f[0]}{$f[1]}{$f[2]}{'t'} += $f[6];
        $perf{$f[0]}{$f[1]}{$f[2]}{'d'} += $f[4];
        $perf{$f[0]}{$f[1]}{$f[2]}{'n'}++;
    }
}

format STDOUT_TOP =
  @|||||||||||||||||||||||||||||||||||||  Page @<
  'Performance data',                     $%

  PC?  Courses  Breaks  Vars  Avg distance  Avg time (s)
  ---  -------  ------  ----  ------------  ------------
.

for my $p (sort keys %perf) {
    for my $c (sort keys %{$perf{$p}}) {
        for my $b (sort keys %{$perf{$p}{$c}}) {
            my $v = $p ? 2*$c : 2*$c+$b+1;
            my $t = $perf{$p}{$c}{$b}{'t'} / $perf{$p}{$c}{$b}{'n'} / 1000;
            my $d = $perf{$p}{$c}{$b}{'d'} / $perf{$p}{$c}{$b}{'n'};
            format =
  @##  @######  @#####  @###  @#########.#  @#######.###
  $p,  $c,      $b,     $v,   $d,           $t
.
            write;
        }
    }
}
