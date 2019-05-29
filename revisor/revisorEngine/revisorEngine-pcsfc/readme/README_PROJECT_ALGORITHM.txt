The purpose of this README is to tell you how to implement your revision algorithm using Revisor/PCSFC's interface

Before continuing, you must have installed all of revisor by installing the full Maven repository. If you haven't
done so yet, check William PHILBERT's README_PROJECT.txt located in the readme folder at the root of the downloaded
archive.

The link between your algorithm and the interface is made by a single method which is called revise(). This method
is located in the class
`/revisorEngine-pcsfc/src/main/java/fr/loria/k/revisor/engine/revisorPCSFC/RevisorEnginePCSFC`.
This method gives you three parameters. The two first are the formulas psi and mu that need to be revised and the last one is
the constant epsilon used by the revision operator.

These formulas are all instances of PCSFCFormula. These objects are all located in the package
`fr.loria.k.revisor.engine.revisorPCSFC.pcsfc`. Every single implementation represent one kind of formula, whether it is a
linear constraint, a binary formula, a boolean, etc... Note that you do not need to worry whether psi and mu can be revised.
The interface already converts these two formulas to the PCLC formalism and valids that these formulas can be revised using
your algorithm before calling it.
Therefore, you only need to considerate the following formulas : "PCSFCAnd, PCSFCOr, PCSFCNot, PCSFCTautology and
PCSFCConstraint".

You do not need to use instances of PCSFCFormula in your algorithm and can use instead your own system. But please not that if
you do that, you will need to create an intepreter that will convert not only instances of PCSFCFormula to your own formalism,
but also instances of formulas of your own system back to instances of PCSFCFormula.
If you choose to use PCSFCFormulas instances in your algorithm, you are free to add new methods, constructors and fields in the
classes. If you need to modify already existing methods, constructors or fields, please make sure nothing breaks.
Note that you are also free to add new methods and fields in the class `RevisorEnginePCSFC` (the class where is located the
method that links the interface and your algorithm).

Another person has previously worked on the algorithm. You can find his code in the folder called "RevisorPCLC" which is located
at the root of this engine's folder. You may or may not use the code or parts of it, it's up to you.

Finally, if you have succesfully implemented your algorithm, don't forget to update every README files, as your algorithm
probably needs external librarie(s) that will need to be installed by future users and/or developpers.
