import Optimization.LinearOptimization;
import Optimization.Optimizer;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        LinearOptimization linearOptimization = new LinearOptimization("lpsolve");
        Optimizer optimizer = linearOptimization.getOptimizer();
    }

}
