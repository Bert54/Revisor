Author: William Philbert

This README explains how to install Revisor.
For help about how to work on the project, see README_PROJECT.

///////////////////////////////////
////// Summary ////////////////////
///////////////////////////////////

- Requirements
- Usage

///////////////////////////////////
////// Requirements ///////////////
///////////////////////////////////

To use Revisor, you need:
- For all engines:
	- JRE7 (Java SE Runtime Environement 7) or more recent.
- For Revisor/QA:
	- Perl 5.8 or more recent.
	- The Perl bin must be in your "PATH" environment variable (or equivalent variable on your OS).
	- You need to install some Perl libs with the Perl Package Manager (see RevisorQA README for details):
		- Perl module File::Slurp is required for Revisor/PCQA
		- Perl module Clone is highly suggested
- For Revisor/CLC:
	- Install the C Library lp_solve and its Java wrapper:
		- See README.txt and README.html in lib/lp_solve_5.5.2.0/ to install lp_solve.
		- Create an environment variable "LP_SOLVE" containing ONLY the path to the directory where you installed lp_solve and its Java wrapper.
		- Add the "LP_SOLVE" variable to your "PATH" environment variable (or equivalent variable on your OS).

///////////////////////////////////
////// Usage //////////////////////
///////////////////////////////////

The bin folder of the zip contains all the jars required to use Revisor.
- You can launch the graphical user interface with: java -jar revisorPlatform-swing-1.0.0.jar
- You can launch the console mode of engines which have one. Ex for Revisor/PL: java -jar revisorEngine-pl-3.0.0.jar
- Engines which don't have a console mode yet display the result of a test by default.
- You can use a specific class in a jar like, for example: java -cp revisorEngine-qa-3.0.0.jar fr.loria.orpailleur.revisor.engine.revisorQA.RevisorQACLI <psi> <mu>

///////////////////////////////////
