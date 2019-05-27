package Optimization;

import java.util.ArrayList;

public abstract class Optimizer {

    // name of the optimizer
    private String name;

    /**
     * Constructor of the class
     * @param name of the optimizer
     */
    public Optimizer(String name){
        this.name = name;
    }

    public abstract Object solve(ArrayList< ArrayList<Double> > listX, ArrayList< ArrayList<Double> > listY, ArrayList<Double> weight);

}
