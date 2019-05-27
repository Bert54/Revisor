The purpose of this README is to tell you how to launch and use Revisor/PCSFC.

In order to launch Revisor/PCSFC, all you need to do is to go in the folder called "jar" located at the root
of the projet folder and launch either RevisorPCSFC.jar if you want to use Revisor/PCSFC within the console
of your operating system or RevisorPlatform.jar if you want to use Revisor/PCSFC in a GUI (also includes
Revisor/PL and Revisor/PLAK).
In order to execute both JAR, you will need Java 7 or newer installed. YOu will then need to open a console
command from where the JAR you want to execute is located and input the following command : 

   "java -jar <NameOfJar>.jar"

where "<NameOfJar>" is the name of the JAR file you want to execute (here: "RevisorPCSFC" or
"RevisorPlatform").

If you are using RevisorPlatform, don't forget to switch the active tab, as it defaults to Revisor/PL's tab
upon launch.

------------------------------------------------------------------------------------------------------------

Here is the list of every commands you can input :

   - Declare a single variable
      + <name_of_variable> : integer; // declares an integer
      + <name_of_variable> : real; // declares a real
      + <name_of_variable> : formula; // declares a formula which is a tautology by default
      + <name_of_variable> : boolean; // declares a boolean
      + enum <name_of_variable>("<first_modality>", "<second_modality>", ..., "<nth_modality>"); // declares
        an enumeration with n modalities.
      + const <name_of_constant> = <value_of_constant>; // declares a constant

   - Declare multiple variables
      + <name_of_first_variable>, <name_of_second_variable>, ..., <name_of_nth_variable>: integer;
        // declares n integers
      + <name_of_first_variable>, <name_of_second_variable>, ..., <name_of_nth_variable>: real;
        // declares n reals
      + <name_of_first_variable>, <name_of_second_variable>, ..., <name_of_nth_variable>: formula;
        // declares n formulas ; all of them are tautologies by default
      + <name_of_first_variable>, <name_of_second_variable>, ..., <name_of_nth_variable>: boolean;
        // declares n booleans

   - Assign a formula to a formula variable
     <name_of_the_formula_variable> := <formula>;
     Where <formula> can be :
      + true // tautology
      + <name_of_a_formula_variable> // another formula variable (or the same)
      + a linear constraint // an example (assuming variables x, y, z and a have been declared) :
        3x - .46y + -a - -.1z >= 4
      + a boolean
      + an enumeration associated with one of its modalities // an example with the enumeration color and the
        modality "red":
        color = "red"
      + !<formula> // negative formula
      + <formula> <binary_connector> <formula> // creates a relation between two formulas with a binary connector
        which can be: `&` (AND), `|` (OR), `=>` (IMPLICATION), `<=>` (EQUIVALENCE), `^` (EXCLUSIVE OR)

   - Launch the revision algorithm and assign the result to a formula variable
     <name_of_the_formula_variable> := revise(<name_of_a_formula_variable>, <name_of_a_formula_variable>, <constant>);
     Where <constant> can be :
      + a previously declared constant 
      + an integer
      + a real

   - Print a variable with its type
     <name_of_the_variable>
     If the variable is a constant, its value wil be printed as well. If it's a formula, its associated formula
     will be printed. If it's an enumeration, its modalities will be printed.

   - Print all variables with their type (and content if possible)
     printvars

   - Load commands from a file
     load <file>
     Where file if the name of the file you wish to load (with its path if it is in a different folder)

   - Reset the environment (deletes every declared variables)
     clear

   - Quit the environment properly
     exit
