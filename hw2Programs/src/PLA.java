import Jama.Matrix;

import java.util.ArrayList;

/**
 * Created by cheta_000 on 5/22/2015.
 * Implements the PLA algorithm with weights initialized by linear regression
 */

public class PLA {

    public static void main(String[] args) {
        int N = 10;
        ArrayList<Double> errorRatesInitially = new ArrayList<>();
        ArrayList<Integer> iterations = new ArrayList<>();
        for (int j = 0; j < 1000; j++) {
            // Generate f(x)
            double[] realLine = LinRegImplementation.genRandomLine();

            // Generate training data
            double[][] training = LinRegImplementation.genRandomPoints(N, 2);
            double[][] trainingClass = LinRegImplementation.getPointClasses(training, realLine);

            // Initialize weights
            Matrix inputs = Matrix.constructWithCopy(training);
            Matrix output = Matrix.constructWithCopy(trainingClass);
            Matrix weights = inputs.inverse().times(output);

//            System.out.println("Initial error: " + LinRegImplementation.evaluate(inputs, trainingClass, weights) + ", ");
            errorRatesInitially.add(LinRegImplementation.evaluate(inputs, trainingClass, weights));

            // Get error and start iterations
            double[] error = getErrorArray(weights, inputs, output);
            int wrongIndex = findWrongIndex(error);
            int counter = 0;
            while(wrongIndex != -1) {
                counter++;
                weights.getArray()[0][0] += error[wrongIndex]*inputs.getArray()[wrongIndex][0];
                weights.getArray()[1][0] += error[wrongIndex]*inputs.getArray()[wrongIndex][1];
                weights.getArray()[2][0] += error[wrongIndex]*inputs.getArray()[wrongIndex][2];
                error = getErrorArray(weights, inputs, output);
                wrongIndex = findWrongIndex(error);
            }
            iterations.add(counter);
//            System.out.println("Iterations for PLA to perfect: " + counter);
        }
        System.out.println("Average initial error: " + errorRatesInitially.stream().mapToDouble(Double::doubleValue).average().getAsDouble());
        System.out.println("Average num of iterations: " + iterations.stream().mapToInt(Integer::intValue).average().getAsDouble());

    }

    public static double[] getErrorArray(Matrix weights, Matrix inputs, Matrix outputs) {
        double[][] realClass = outputs.getArray();
        double[] estimOutput = LinRegImplementation.predict(inputs, weights);
        double[] error = new double[realClass.length];
        for (int i = 0; i < realClass.length; i++) {
            error[i] = realClass[i][0] - estimOutput[i];
        }
        return error;
    }

    public static int findWrongIndex(double[] error) {
        for (int i = 0; i < error.length; i++) {
            if (error[i] != 0) {
                return i;
            }
        }
        return -1;
    }


}
