
import Jama.Matrix;

import java.util.Arrays;


/**
// * Created by cheta_000 on 5/29/2015.
// * Initial implementation of answer for num 4/5/6. Had bugs that I didn't fix since all it's function could be
// * performed by the new BiasVar
// */

public class BiasVar {

    public static void main(String[] args) {
        int numIters = 1000;
        double coefs[] = new double [numIters];
        for (int j = 0; j < numIters; j++) {

            // Generate training and testing  data
            double train[][] = genRandPoints(2, 1);
            double trainCls[][]= sineYvals(train);

            // Estimate parameters, note: .inverse() calculates pseudoinverse if inverse isn't possible
            Matrix inputs = Matrix.constructWithCopy(train);
            Matrix output = Matrix.constructWithCopy(trainCls);
            Matrix weights = inputs.inverse().times(output);
            coefs[j] = weights.get(0,0);
        }

        printArr(coefs);

        // Calculate bias (computationally)
        int testPoints = 100000;
        double test[][] = genRandPoints(testPoints , 1);
        double[][] testCls = sineYvals(test);
        double a = getMean(coefs);
        Matrix weights = Matrix.constructWithCopy(new double[][] {{a}});
        double testEstim[][] = Matrix.constructWithCopy(test).times(weights).getArrayCopy();
        double error [] = new double[testPoints ];
        for (int i = 0; i < testPoints; i++) {
            error[i] = Math.pow(testEstim[i][0] - testCls[i][0], 2);
        }
        System.out.println("Mean g(x): " + a);
        System.out.println("Variance g(x): " + getVar(coefs));
        System.out.println("Bias: " + Arrays.stream(error).average().getAsDouble());
    }

    // to turn a n by 1 matrix to a n-element array
    public static double[] flattenArr(double[][]input ) {
        return Arrays.stream(input).mapToDouble(p -> p[0]).toArray();
    }

    public static double getMean(double input[]) {
        return Arrays.stream(input).average().getAsDouble();
    }

    public static double getVar(double input[]) {
        double mean = getMean(input);
        return Arrays.stream(input).map(p -> Math.pow(mean-p, 2)).average().getAsDouble();
    }

    public static double[][] sineYvals(double input[][]) {
        double ret[][] = new double[input.length][1];
        for (int i = 0; i < input.length; i++) {
            ret[i] = new double[]{Math.sin(Math.PI*input[i][0])};
        }
        return ret;
    }

    public static double[][] testYvals(double input[][]) {
        return input;
    }

    public static double[][] genRandPoints(int n, int cols) {
        double randPoints[][] = new double[n][cols];
        for (int i = 0; i < n; i++) {
            randPoints[i] = genRandomVal(cols);
        }
        return randPoints;
    }


    public static double[] genRandomVal(int n) {
        double randVals [] = new double[n];
        for (int i = 0; i < n; i++) {
            randVals[i] = Math.random() * 2 - 1;
        }
        return randVals;
    }


    public static void printArr(double[][] myData)
    {
        for(int i = 0; i < myData.length; i++)
        {
            for(int j = 0; j < myData[0].length; j++)
            {
                System.out.print(myData[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void printArr(double[] myData)
    {
        for(int j = 0; j < myData.length; j++)
        {
            System.out.print(myData[j] + "\t");
        }
        System.out.println();
    }

}

