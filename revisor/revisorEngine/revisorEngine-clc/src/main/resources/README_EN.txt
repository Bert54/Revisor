
%%% ----------- %%%
% Installation
%%% ----------- %%%

LPSOLVE library is composed of two parts : the library in C and a Java wrapper.

The C library must be installed on the machine and the environment variable LD_LIBRARY_PATH set to the installation dir in order for the Java wrapper to call it :
* copy the files liblpsolve55.so and liblpsolve55j.so in a directory [RepLib]
(binaries for several architectures can be found in the directories lib/[arch]/LPSOLVE, or they can be downloaded from http://lpsolve.sourceforge.net/5.5/)
* Add the path to [RepLib] to the environment variable LD_LIBRARY_PATH (separated by a ':' if it already contains some values)
* run ldconfig to have the libraries found be the OS (just needed once).

Now LPSOLVE should be functional, the java wrapper appears as a jar library that should be in the classpath.
The executable class revisor_clc. TestRevisorCLC should run without exception.



%%% ----------- %%%
% Usage
%%% ----------- %%%

To run an adaptation, the following elements must be defined:
* the metric space (class fr.inria.cbr2lp.representationSpace.LinearizableAttributeValueMetricSpace):
  - the set of variables and their domain
  - the distance used for the adaptation (fr.inria.cbr2lp.representationSpace.LinearizableDistance)
* the source case
* the target case
* the domain knowledge

See the example given in revisor_clc.TestRevisorCLC



%%%
% Definition of the metric space
%%%

(Re)Initialize the representation space:
	RevisorCLC.resetSpace();

The representation space is defined as a Cartesian product of the variable domains.
Every variable is defined by an object fr.inria.cbr2lp.representationSpace.Variable, currently two subclasses exist RealVariable and IntegerVariable.
The definition of the representation space is done through the choice of the variable type and through the introduction of constraints in the domain knowledge.

To create a variable, a name should be given:
	RealVariable fruitMass = new RealVariable("fruit mass");

The adaptation is guided by a distance defined by d(x,y) = sum_i w_i*|y_i-x_i| where i goes through the list of variables, x_i and y_i are the components of x and y for the variable i and w_i>=0 is a coefficient (called weight).
To define such a distance, a coefficients w (an object of type Value, Sally RealValue) must be associated to each variable :
	RealValue fruitMassVarCost = new RealValue(5);
	RevisorCLC.setWeight(fruitMass, fruitMassVarCost);


%%%
% Definition of the domain knowledge and the cases
%%%

The domain knowledge are build from fr.inria.cbr2lp.linearProgramming.com.Simplex objects, themselves defined from a set of linear constraints (fr.inria.cbr2lp.linearProgramming.com.LinearConstraint).

	Simplex s =  new Simplex();

- To create a linear constraint:
	LinearConstraint constraint = new LinearConstraint();
- The linear constraints are defined by (in)equalities like sum_i a_i*x_i >= v where the constraint type can be '>=', '=', '<='
	The coefficients a_i are Value objects and the variables x_i are Variable objects.
_ The constraint type is set with:
    constraint.setType(ConstraintType.EQUAL);
_ The right hand term (constant value v) is set with:
    constraint.setOffset(new RealValue(v));
_ The left hand terms are added to the linear constraint with:
    constraint.addTerm(x_i, a_i);
- Finally to add the constraint to the simplex s:
    s.addConstraint(constraint);

A case is represented by a SimplexCase object:
	SimplexCase srce = new SimplexCase(s);
The domain knowledge are represented by a SimplexDomainKnowledge object:
	SimplexDomainKnowledge dk = new SimplexDomainKnowledge(s);


%%%
% Running the adaptation
%%%

The result of an adaptation is itself a SimplexCase object:
	SimplexCase tgtAdapted = RevisorCLC.adapt(srce, tgt, dk);

To display the result:
	System.out.println(tgtCompleted);
