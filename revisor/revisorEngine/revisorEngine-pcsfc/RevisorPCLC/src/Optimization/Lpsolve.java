package Optimization;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

import java.util.ArrayList;

public class Lpsolve extends Optimizer {

    // Instance of the class
    private static Lpsolve instance = new Lpsolve();

    /**
     * Constructor of the class
     */
    protected Lpsolve() {
        super("lpsolve");
    }

    /**
     * @return the instance of the class
     */
    public static Lpsolve getInstance() {
        return instance;
    }

    @Override
    public Object solve(ArrayList< ArrayList<Double> > listX, ArrayList< ArrayList<Double> > listY, ArrayList<Double> weight) {
        try {
            // Initialize variables
            LpSolve solver = LpSolve.makeLp(0,3);
            // Add constraints

            ArrayList<Double> coeff;
            // Psy's constraints
            for(int j = 0; j < listX.size(); j++) {
                coeff = listX.get(j);
                for(int k = 1; k < coeff.size(); k++) {
                    solver.strAddConstraint(coeff.get(k)+ " ", LpSolve.LE, coeff.get(0));
                }
            }
            // Mu's constraints
            for(int j = 0; j < listY.size(); j++) {
                coeff = listY.get(j);
                for(int k = 1; k < coeff.size(); k++) {
                    solver.strAddConstraint(coeff.get(k)+ " ", LpSolve.LE, coeff.get(0));
                }
            }
            //solver.strAddConstraint(listX.get(i)+ " "+listY.get(i),LpSolve.LE,0);
            // The order of the variable is: X, Y, Z
            // Z_j >= Y_j - X_j   =>   Z_j - Y_j + X_j <= 0
            solver.strAddConstraint("-1 1 -1",LpSolve.GE,0);
            // Z_j >= X_j - Y_j   =>   Z_j - X_j + Y_j <= 0
            solver.strAddConstraint("1 -1 -1",LpSolve.GE,0);

            // Set the objective function
            solver.strSetObjFn( "1 1");
            // delete the problem and free memory
            solver.deleteLp();
        } catch (LpSolveException e) {
            e.printStackTrace();
        }
        return null;
    }

}
