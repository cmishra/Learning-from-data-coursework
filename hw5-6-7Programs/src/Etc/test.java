package Etc;

import Jama.Matrix;
import SupervisedModels.Utilities;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by chemishra on 6/30/2015.
 */
public class test {

    public static void main(String[] args) {
//        // Testing if Jama.Matrix beginning/end points for sub-Matrixes are inclusive or not
//        SupervisedModels.Utilities.printArr(Matrix.identity(3, 3).getArray());
//        System.out.println("");
//        SupervisedModels.Utilities.printArr(Matrix.identity(3, 3).getMatrix(new int[]{1}, 0, 2).getArray());
//        SupervisedModels.Utilities.printArr(Matrix.identity(3, 3).getMatrix(new int[]{2}, 0, 2).getArray());

//        // Tests to make sure internal logic of checkNumberOfContains work as intended
//        String test = "\t124235\t";
//        SupervisedModels.Utilities.checkNumberOfContains(Etc.test, "\t");

//        // Test fill
//        double[] fill = new double[10];
//        Arrays.fill(fill, 1.0);
//        Utilities.printArr(fill);

//        // Test using java streams to modify arrays inside Matrix objects
//        double b = 0.1;
//        Matrix estims = Matrix.random(10, 1);
//        Utilities.printArr(Arrays.stream(estims.getArray()).map(p -> p[0] > b ? new double [] {1.0} : new double[] {-1.0}).collect(Collectors.toList()).toArray());
//        Utilities.printArr(estims.getArray());
    }
}
