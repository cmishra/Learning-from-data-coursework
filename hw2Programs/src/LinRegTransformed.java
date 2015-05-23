import Jama.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by cheta_000 on 5/22/2015.
 */
public class LinRegTransformed {

    public static void main(String[] args) {
        int N =1000;
        int iters = 1000;
        double[][] weightArr = new double[6][iters];
        ArrayList<Double> normalReg = new ArrayList<>();
        for (int j = 0; j < iters; j++) {
            // Generate training/test set and assign classes
            double[][] training = LinRegImplementation.genRandomPoints(N, 2);
            double[][] realClass = assignClassWithNoise(training, N);
            double[][] test = LinRegImplementation.genRandomPoints(N, 2);
            double[][] testClass = assignClassWithNoise(test, N);

            // Expand training/test vector into nonlinear transformed feature vector
            double[][] expandedTraining = nonlinearTransform(training, N);
            double[][] expandedTest = nonlinearTransform(test, N);

            // Fit parameters
            Matrix inputs = Matrix.constructWithCopy(expandedTraining);
            Matrix outputs = Matrix.constructWithCopy(realClass);
            Matrix weights = inputs.inverse().times(outputs);
            for (int i = 0; i < 6; i++) {
                weightArr[i][j] = weights.getArray()[i][0];
            }

            // Estimate error on test set
            normalReg.add(LinRegImplementation.evaluate(Matrix.constructWithCopy(expandedTest), testClass, weights));
        }
        double[] avgWeights = Arrays.stream(weightArr).mapToDouble(w -> Arrays.stream(w).average().getAsDouble()).toArray();
        printArr(avgWeights);
        System.out.println("Average proportion of test sets misclassified: " +
                normalReg.stream().mapToDouble(Double::doubleValue).average().getAsDouble());
    }

    public static double[][] assignClassWithNoise(double[][] training, int N) {
        double[][] realClass = new double[N][1];
        for (int i = 0; i < N; i++) {
            realClass[i][0] = Math.pow(training[i][1], 2) + Math.pow(training[i][2], 2) - 0.6;
            realClass[i][0] = realClass[i][0] >= 0 ? 1 : -1;

            if (i % 10 == 0) { // Inducing noise in 10% of cases
                realClass[i][0] = realClass[i][0]*-1;
            }
        }
        return realClass;
    }

    public static double[][] nonlinearTransform(double[][] data, int N) {
        double[][] expandedTraining = new double[N][6];
        for(int i = 0; i < N; i++) {
            expandedTraining[i][0] = data[i][0];
            expandedTraining[i][1] = data[i][1];
            expandedTraining[i][2] = data[i][2];
            expandedTraining[i][3] = data[i][1]*data[i][2];
            expandedTraining[i][4] = Math.pow(data[i][1], 2);
            expandedTraining[i][5] = Math.pow(data[i][2], 2);
        }
        return expandedTraining;
    }

    public static void printArr(double[][] myData, int numRows, int numCols)
    {
        for(int i = 0; i < numRows; i++)
        {
            for(int j = 0; j < numCols; j++)
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
