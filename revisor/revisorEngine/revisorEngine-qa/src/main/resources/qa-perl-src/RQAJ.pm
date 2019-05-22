# Ironically, this package makes it possible to use in Perl the Java 
# bindings for the Perl implementation of Revisor/QA+PCQA. This is achieved 
# through the use of Inline::Java.

# Note 1: Only some methods from revisor_qa.RevisorQA are available
# Note 2: No attempt at parameter checking is performed in this module,
#         therefore any error will result in a JVM runtime and a Perl croak

package RQAJ;

use strict;
use warnings;

use Carp;

# initialisation of $CLASSPATH. this must be BEGIN in order for the
# use Inline below to see it.
BEGIN {
    my @dirs = qw(
      ./../
    );
    my @jars;
    for my $dir (@dirs) {
        opendir DIR, $dir or die;
        while ( my $file = readdir(DIR) ) {
            next unless $file =~ m{\.jar$};
            push @jars, $dir . "/" . $file;
        }
    }
    $ENV{'CLASSPATH'} = join ':', @jars;
}

# load the requires Java classes
use Inline (
    Java  => 'STUDY',
    STUDY => [
        qw(
          fr.loria.orpailleur.revisor.engine.revisorQA.RevisorQA
          fr.loria.orpailleur.revisor.engine.revisorQA.qa.QAPBackendRequest
          fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeAlgebra
          fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintFormula
          fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintNetwork
          fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintDisjunction
          fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeConstraintScenario
          fr.loria.orpailleur.revisor.engine.revisorQA.qa.QualitativeVariableSubstitution
          )
    ],
    AUTOSTUDY => 1,
    DEBUG     => 0,

    #CLASSPATH => $ENV{'CLASSPATH'},
);

# caught makes it possible to catch Java exceptions in Perl
use Inline::Java qw(caught);


## ONLY BINDINGS TO revisor_qa.RevisorQA BELOW THIS LINE ##

sub loadAlgebra {
    my $r = eval {
        return RQAJ::revisor_qa::RevisorQA->loadAlgebra(@_);
    };
    if ($@) {
        if (caught("java.lang.Exception")){
            die 'Java exception: ' . $@->getMessage . "\n";
        }
        else {
            die 'Perl Inline exception: ' . $@ . "\n";
        }
    }
    else {
        return $r;
    }
}

sub parseFile {
    my $r = eval {
        return RQAJ::revisor_qa::RevisorQA->parseFile(@_);
    };
    if ($@) {
        if (caught("java.lang.Exception")){
            croak 'Java exception: ' . $@->getMessage;
        }
        else {
            croak 'Perl Inline exception: ' . $@;
        }
    }
    else {
        return $r;
    }
}

sub parseFilePC {
    my $r = eval {
        return RQAJ::revisor_qa::RevisorQA->parseFilePC(@_);
    };
    if ($@) {
        if (caught("java.lang.Exception")){
            croak 'Java exception: ' . $@->getMessage;
        }
        else {
            croak 'Perl Inline exception: ' . $@;
        }
    }
    else {
        return $r;
    }
}

sub adaptExhaustively {
    my $r = eval {
        return RQAJ::revisor_qa::RevisorQA->adaptExhaustively(@_);
    };
    if ($@) {
        if (caught("java.lang.Exception")){
            croak 'Java exception: ' . $@->getMessage;
        }
        else {
            croak 'Perl Inline exception: ' . $@;
        }
    }
    else {
        return $r;
    }
}



1;
