package RegressionSuite;

import Jama.Matrix;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/**
 * Created by chemishra on 6/28/2015.
 */
public class LogisticReg {

    // Forgive my laziness, global variable:
    public static ArrayList<Integer> numEpochs = new ArrayList<>();

    public static void main(String[] args) {
        int numIters = 100;
        double[] eIns = new double[numIters];
        double[] eOuts = new double[numIters];
        for (int j = 0; j < numIters; j++) {
            double[] randLine = Utilities.genRandomLine();
            Matrix trainPoints = Utilities.genPointsWithIntercept(100, 2);
            Matrix yTrain = Utilities.getActualY(trainPoints, randLine);
//            RegressionSuite.Utilities.writeMatrixToFile(xVals, yActual, "Etc.test.csv");
            Matrix fitW = sgd(trainPoints, yTrain);
//            RegressionSuite.Utilities.writeMatrixToFile(fitW, "testW.csv");
            Matrix testPoints = Utilities.genPointsWithIntercept(1000, 2);
            Matrix yTest = Utilities.getActualY(testPoints, randLine);
            eIns[j] = batchErrorFunc(trainPoints, yTrain, fitW);
            eOuts[j] = batchErrorFunc(testPoints, yTest, fitW);
        }
        System.out.println("Average E_in:\t" + Arrays.stream(eIns).average().getAsDouble());
        System.out.println("Average E_out:\t" + Arrays.stream(eOuts).average().getAsDouble());
        System.out.println("Average number of Epochs:\t" + numEpochs.stream().mapToInt(Integer::intValue).average().getAsDouble());
//        System.out.println(numEpochs.toString());
    }

    // Assumes inputs and outputs have same # of rows
    public static Matrix sgd(Matrix inputs, Matrix outputs) {
        double eta = 0.01;
        Matrix w = Utilities.genZeroMatrix(3, 1);
        Matrix lastW = Matrix.identity(1,1);
        boolean itersRan = false;
        int j = 0;
        int[] indexes = IntStream.range(0, inputs.getRowDimension()).toArray();
        while(!itersRan || !stopCondition(lastW, w)) {
            j++;
            itersRan = true;
            lastW = w.copy();
            Utilities.shuffleArray(indexes);
            for (int i = 0; i < inputs.getRowDimension(); i++) {
                Matrix curInput = Utilities.getRow(inputs, indexes[i]);
                Matrix curOutput = Utilities.getRow(outputs, indexes[i]);
                updateWeights(w, stochasticGradient(curInput, curOutput, w), eta);
            }
//            System.out.print("Epoch Num: " + j + ": " + batchErrorFunc(inputs, outputs, w) + "\t---\t");
//            RegressionSuite.Utilities.printArr(RegressionSuite.Utilities.flattenArr(w));
        }
        numEpochs.add(j);
        return w;
    }

    // Updates weights w in-place
    public static void updateWeights(Matrix w, Matrix gradient, double eta) {
        w.plusEquals(gradient.times(-1*eta));
    }

    public static boolean stopCondition(Matrix w0, Matrix w1) {
        return Utilities.magnitude(w1.minus(w0)) < 0.01;
    }

    // Assumes inputs = x_i, and outputs y_i (only one row in both matrices)
    public static Matrix stochasticGradient(Matrix input, Matrix output, Matrix w) {
        double y = output.get(0, 0);
        double expTerm = Math.exp(-y* Utilities.dotProduct(w, input));
        return input.times(expTerm*-y).timesEquals(1/(1 + expTerm)).transpose();
//        return input.times(y).timesEquals(-1.0).timesEquals(1/(1 + expTerm)).transpose();
    }

    // Assumes inputs = x_i, and outputs y_i (only one row in both matrices)
    public static double stochasticErrorFunc(Matrix input, Matrix output, Matrix w) {
        return Math.log(1 + Math.exp(-output.get(0, 0) * Utilities.dotProduct(input, w)));
    }

    // Untested, implemented before realizing I need to do SGD here
    public static Matrix batchGradient(Matrix inputs, Matrix outputs, Matrix w) {
        Matrix ret = Utilities.genZeroMatrix(w.getRowDimension(), w.getColumnDimension());
        for (int i = 0; i < inputs.getRowDimension(); i++) {
            Matrix curRow = Utilities.getRow(inputs, i);
            ret.plusEquals(stochasticGradient(curRow, w, Utilities.getRow(outputs, i)));
        }
        return ret.timesEquals(1.0/inputs.getRowDimension());
    }

    // Untested, implemented before realizing I need to do SGD here
    public static double batchErrorFunc(Matrix inputs, Matrix outputs, Matrix w) {
        double sum = 0.0;
        for (int i = 0; i < inputs.getRowDimension(); i++) {
            sum += stochasticErrorFunc(Utilities.getRow(inputs, i), Utilities.getRow(outputs, i), w);
        }
        return sum/inputs.getRowDimension();
    }




}
