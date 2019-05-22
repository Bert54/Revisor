
%%% ----------- %%%
% Installation
%%% ----------- %%%

La librairie LPSOLVE est composée de deux partie : la librairie C et un wrapper Java.
La librairie C doit être installée sur la machine et la variable d'environnement LD_LIBRARY_PATH configurée pour permettre au wrapper java a y faire appel :
* copier les fichiers liblpsolve55.so et liblpsolve55j.so dans un répertoire [RepLib]
(les binaires pour différentes architectures se trouvent dans les répertoires lib/[archi]/LPSOLVE, ils peuvent être téléchargés depuis http://lpsolve.sourceforge.net/5.5/)
* ajouter l'adresse de [RepLib] à la variable d'environnement LD_LIBRARY_PATH 
* exécuter ldconfig pour prendre en compte ces valeurs



%%% ----------- %%%
% Utilisation
%%% ----------- %%%

Pour effectuer une adaptation il faut définir :
* l'espace métrique (classe fr.inria.cbr2lp.representationSpace.LinearizableAttributeValueMetricSpace):
  - l'ensemble des variables et leur domaine
  - la distance utilisée pour l'adaptation (fr.inria.cbr2lp.representationSpace.LinearizableDistance)
* le cas source
* le cas cible
* les connaissances du domaine

Voir l'exemple donné dans la classe revisor_clc.TestRevisorCLC



%%%
% Définir l'espace métrique
%%%

(Ré)Initialiser l'espace :
	RevisorCLC.resetSpace();

L'espace est défini par le produit cartésien de sous-espace associés à des attributs.
Chaque attribut est définit par un objet de type fr.inria.cbr2lp.representationSpace.Variable, typiquement RealVariable ou IntegerVariable.
La définition de l'espace est faite à travers le choix du type de variable et l'ajout de contraintes dans les connaissances du domaine.

A la création d'une variable il faut lui donner un nom:
	RealVariable fruitMass = new RealVariable("fruit mass");

L'adaptation est guidée par une distance du type d(x,y) = somme_i c_i*|y_i-x_i| où i parcours la liste des attributs, x_i et y_i sont les composantes de x et y correspondant à l'attribut i et c_i est un coefficient (>=0).
Pour définir cette distance, il faut associer un coefficient c de type Value (typiquement RealValue) à chaque variable :
	RealValue fruitMassVarCost = new RealValue(5);
	RevisorCLC.setWeight(fruitMass, fruitMassVarCost);


%%%
% Définir les connaissances du domaine et les cas
%%%

Les cas et les connaissances du domaine sont représentés par des objets de la classe fr.inria.cbr2lp.cbrPackage.SimplexCase.
Ils sont construits à partir d'un objet fr.inria.cbr2lp.linearProgramming.com.Simplex, lui même défini à partir d'une liste de contraintes linéaires (fr.inria.cbr2lp.linearProgramming.com.LinearConstraint).

	Simplex s =  new Simplex();

- Pour créer une contrainte linéaire :
	LinearConstraint constraint = new LinearConstraint();
- Les contraintes linéaires sont du type somme_i a_i*x_i >= v où le type de la contrainte peut être '>=', '=', '<='
	les coefficients a_i sont des objets de type Value et les variables x_i sont de type Variable.
- Pour donne le type de contrainte:
    constraint.setType(ConstraintType.EQUAL);
- Pour donner le terme constant de l'inégalité :
    constraint.setOffset(new RealValue(v));
- Pour définir les termes de la partie gauche :
    constraint.addTerm(x_i, a_i);
- Pour ajouter la contrainte au simplexe s :
    s.addConstraint(constraint);

Un cas est représenté par un objet SimplexCase :
	SimplexCase srce = new SimplexCase(s);
Les connaissances du domaine sont représentées par un objet SimplexDomainKnowledge:
	SimplexDomainKnowledge dk = new SimplexDomainKnowledge(s);


%%%
% Lancer l'adaptation
%%%

Le résultat de l'adaptation est lui-même un objet de type SimplexCase:
	SimplexCase tgtAdapted = RevisorCLC.adapt(srce, tgt, dk);

Pour afficher le résultat :
	System.out.println(tgtCompleted);
