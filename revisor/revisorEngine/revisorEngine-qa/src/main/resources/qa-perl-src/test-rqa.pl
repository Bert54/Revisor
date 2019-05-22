#!/usr/bin/perl

use 5.010;
use strict;
use warnings;

use File::Temp;
use List::Util qw( min );
use POSIX;
use Time::HiRes qw( gettimeofday tv_interval );

use RQAJ;

my $algebra = RQAJ::loadAlgebra('allen');

my $min_c = 0;
my $max_c = 5;
my $min_b = 0;
my $max_b = 3;

# note: numbering courses and periods from 1 but storing them in arrays
#       numbered from 0 wasn't smart. must be careful when handling this...

for my $pc (0..1) {
    for my $c ($min_c..$max_c) {
        for my $b ($min_b..min($max_b,$c-2)) {

            # either create filehandles outside of the subroutines that write
            # to them or return the handle rather than the filename
            # (otherwise the handle goes out of scope and the file is deleted)
            
            my $fh1 = File::Temp->new;
            my $dk = make_dk( $fh1, $pc, $c ,$b );
            
            my $fh2 = File::Temp->new;
            my $source = make_source( $fh2, $c ,$b );
            
            for my $c1 (1..$c-1) {
                my $c2 = $c1 + 1;
                
                my $fh3 = File::Temp->new;
                my $target = make_target( $fh3, $c1 ,$c2 );
                
                make_experiment( $pc, $source, $target, $dk, $algebra, $c, $b, $c1 );
                
            }
            
        }
    }
}


sub make_source {
    my ($fh, $c, $b) = @_;

    select $fh;
    
    my $p = $b + $c;
    my @c = ('C1'.."C$c");
    my @b = map { ceil($_*$p/($b+1)) } (1..$b) if $b; # break periods
    my @r = grep {!( $_ ~~ @b )} (1..$p);             # regular periods
    
    for my $i (1..$c) {
        say $c[$i-1], '{eq}P', $r[$i-1];
    }
    
    select STDOUT;
    $fh->flush;
    return $fh->filename;    
}

sub make_target {
    my ($fh, $c1, $c2) = @_;
    say $fh 'C', $c1, '{b,bi}C', $c2;
    $fh->flush;
    return $fh->filename;    
}

sub make_dk {
    my ($fh, $pc, $c, $b) = @_;

    select $fh;
    
    my $p = $b + $c;
    my @c = ('C1'.."C$c");
    my @p = ('P1'.."P$p");
    my @b = map { ceil($_*$p/($b+1)) } (1..$b) if $b; # break periods
    my @r = grep {!( $_ ~~ @b )} (1..$p);             # regular periods
           
    # temporal organisation of time periods
    if ($pc) {
        say $p[$_], '{m}', $p[$_+1] for (0..$#p-1);
    }
    else {
        say $p[$_], '{m}', $p[$_+1] for (0..$#p-1);
    }
    
    # special global period for regular Allen
    if (!$pc) {
        say $p[0],   '{s}PP';
        say $p[$_],  '{d}PP' for (1..$#p-1);
        say $p[$#p], '{f}PP';
    }
    
    # no two courses at the same time
    for my $c1 (@c) {
        for my $c2 (@c) {
            next if $c1 ge $c2;
            if ($pc) {
                say 'not(', $c1, '{eq}', $c2, ')';
            }
            else {
                say $c1, '{b,bi,m,mi}', $c2;
            }
        }
    }
    
    # all courses during the global period for regular Allen
    if (!$pc) {
        say $_, '{s,d,f}PP' for (@c);
    }
    
    # every course equal (unless it's a break) or distinct from every time
    # period for regular Allen
    if (!$pc) {
        for my $c (@c) {
            for my $i (0..$#p) {
                if ($i+1 ~~ @b) {
                    say $c, '{b,bi,m,mi}', $p[$i];
                }
                else {
                    say $c, '{eq,b,bi,m,mi}', $p[$i];
                }
            }
        }
    }
    
    # every course occurs over an allowable period for PC Allen
    if ($pc) {
        for my $c (@c) {
            say 'or(', (join ';', map { $c . '{eq}' . $p[$_-1] } @r), ')';
        }
    }
    
    select STDOUT;
    $fh->flush;
    return $fh->filename;
}

sub time_revision {
    my ($pc, $source_f, $target_f, $dk_f, $algebra) = @_;
    
    my ($source, $target, $dk);
       
    if ($pc) {
        
        $source = RQAJ::parseFilePC($source_f, $algebra);
        $target = RQAJ::parseFilePC($target_f, $algebra);
        $dk     = RQAJ::parseFilePC($dk_f, $algebra);
    }
    else {
        $source = RQAJ::parseFile($source_f, $algebra);
        $target = RQAJ::parseFile($target_f, $algebra);
        $dk     = RQAJ::parseFile($dk_f, $algebra);
    }
    
    my $t0 = [gettimeofday];
    
    my $solution = RQAJ::adaptExhaustively( $source, $target, $dk );
    
    my $el = int tv_interval ( $t0, [gettimeofday] ) * 1000;
    
    my $d;
    my $s = $solution->size;
    
    if ($solution->isEmpty) {
        $d = -1;
    }
    else {
        $d = $solution->get(0)->getDistance;
    }
    
    return ($el, $d, $s);
}

sub make_experiment {
    my ($pc, $source, $target, $dk, $algebra, $c, $b, $c1) = @_;
    local $| = 1;
    print "$pc:$c:$b:$c1:";
    my ($t, $d, $s) = eval { time_revision( $pc, $source, $target, $dk, $algebra ) };
    if ($@) {
        print "\n\n";
        say $source;
        say qx(cat $source);
        say $target;
        say qx(cat $target);
        say $dk;
        say qx(cat $dk);
        die $@;
    }
    say "$d:$s:$t";
}
