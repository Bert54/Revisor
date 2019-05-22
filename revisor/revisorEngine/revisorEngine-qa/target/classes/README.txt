REQUIREMENTS

    - Java 1.7 or more recent
    - Perl 5.8 or more recent
    - Perl module File::Slurp is required for Revisor/PCQA
    - Perl module Clone is highly suggested
    
Perl is installed by default on most POSIX-compliant operating 
systems, including most GNU/Linux distributions, MacOS, and Cygwin.
It is freely available as a convenient binaries bundle for Microsoft 
Windows at www.strawberryperl.com.

The library was actually tested using both Oracle and OpenJDK Java 1.6 and 
1.7, with Perl versions from 5.10 to 5.16, on various GNU/Linux 
installations as well as on Windows 7.

In a Debian-based system, all the dependencies can be installed by typing
	aptitude install openjdk-6-jdk libfile-slurp-perl libclone-perl


INSTALLATION

If you don't need Revisor/PCQA and wish to avoid having to install the Perl
lib File::Slurp, you will need to do the following change in the file
qa-perl-src/qap-backend.pl in revisorEngine-qa-X.X.X.jar :
	- comment or remove the line requiring QA::PCPreprocessor 
	(this will result in a run-time error if you ever try to use R/PCQA).

While this is not a requirement, Revisor/QA is about 10% faster when using an
XS cloning library for Perl. If you don't have the Perl module Clone installed,
you will need to make two small changes in the file qa-perl-src/QA/QCN/Clone.pm
in revisorEngine-qa-X.X.X.jar :
	- Uncomment the "use Storable" line and comment the "use Clone" line
	- Change the penultimate instruction of function pseudo_clone() to use
	dclone() rather than clone(), around line 30
    
You can test that the library is working by invoking:
    java -cp revisorEngine-qa-X.X.X.jar fr.loria.orpailleur.revisor.engine.revisorQA.TestRevisorQA
    java -cp revisorEngine-qa-X.X.X.jar fr.loria.orpailleur.revisor.engine.revisorQA.TestRevisorPCQA


LIBRARY USAGE

In this section, the package fr.loria.orpailleur.revisor.engine.revisorQA 
will be noted [R/QA].

You may either use the set of static methods provided in [R/QA].RevisorQA, 
or implement directly using [R/QA].qa.* classes. 

Because of the complicated interraction between the Java libraries and the 
Perl backend, at this time, we suggest the former. The library is not 
documented yet, but taking a look at classes [R/QA].TestRevisorQA and 
[R/QA].TestRevisorPCQA should be all that you need to start using it. 

The Perl backend is currently being reimplemented in Java, which will result 
in changes in the [R/QA].qa.* classes.


STANDALONE USAGE

In this section, the package fr.loria.orpailleur.revisor.engine.revisorQA 
will be noted [R/QA].

For your convenience, we offer a limited (and not very robust) command-line 
interface through the [R/QA].RevisorQACLI class. It works with QCN or 
generalised QCN files. Adaptation with substitution, as shown in our ICCBR 
2012 and IJCAI 2013 articles, is not available through this interface.

For case-based reasoning adaptation:
    java -cp revisorEngine-qa-X.X.X.jar [R/QA].RevisorQACLI <source> <target> <domain knowledge>
or, to use a propositionally closed algebra:
    java -cp revisorEngine-qa-X.X.X.jar [R/QA].RevisorQACLI PC <source> <target> <domain knowledge>

The revision performed will be
    source AND domaine knowledge REVISED BY target AND domain knowledge

For simple belief revision:
    java -cp revisorEngine-qa-X.X.X.jar [R/QA].RevisorQACLI <psi> <mu>
or, to use a propositionally closed algebra:
    java -cp revisorEngine-qa-X.X.X.jar [R/QA].RevisorQACLI PC <psi> <mu>

The examples from our KR 2014 article are provided. Without propositional 
closure:
    java -cp revisorEngine-qa-X.X.X.jar [R/QA].RevisorQACLI allen qa-example-data/kr-2014/no-pcqa-2/source.qcn qa-example-data/kr-2014/no-pcqa-2/target.qcn qa-example-data/kr-2014/no-pcqa-2/dk.qcn
With propositional closure:
    java -cp revisorEngine-qa-X.X.X.jar [R/QA].RevisorQACLI PC allen qa-example-data/kr-2014/pcqa-2/source.qcn qa-example-data/kr-2014/pcqa-2/target.qcn qa-example-data/kr-2014/pcqa-2/dk.qcn
Contraction:
    java -cp revisorEngine-qa-X.X.X.jar [R/QA].RevisorQACLI PC allen qa-example-data/kr-2014/contraction/psi.qcn qa-example-data/kr-2014/contraction/not-mu.qcn

Note: those examples may take several minutes to execute.


PERFORMANCE TESTS

A Perl script is used to obtain performance data. This has more stringent
requirements than the library:
    - Perl 5.10 or more recent
    - Perl module Java::Inline
      (the author can provide assistance with this in Debian-based systems)

To launch the tests, you need to extract the qa-perl-src directory from the jar 
and put it in the same directory as the jar. Then, you can run the following 
commands:
    perl qa-perl-src/test-rqa.pl > rqa-perf-data
    perl qa-perl-src/analyse-test-rqa.pl rqa-perf-data


AUTHOR

Please do not hesitate to get in touch with the author in case you 
experience difficulties installing or running this library.

Valmi Dufour-Lussier <valmi.dufour@loria.fr> (C) Université de Lorraine, 2012-2013
