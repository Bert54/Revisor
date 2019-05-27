package Optimization;

import Optimization.*;

public class LinearOptimization {

    // The optimizer
    private Optimizer optimizer;

    /**
     * Constructor of the class, if you want to use another solver, just add/delete some here
     * @param o name(in lowercase) of the optimizer we want to use
     * @throws Exception if the name is not good
     */
    public LinearOptimization(String o) {
        if(o.equals("lpsolve")){
            //optimizer = Lpsolve.getInstance();
        }else{
            try {
                throw new Exception();
            } catch (Exception e) {
                System.out.println("Give a correct name of optimisator in lowercase: " + e.getMessage());
            }
        }
    }

    public Optimizer getOptimizer() {
        return optimizer;
    }
}