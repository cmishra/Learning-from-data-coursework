import java.util.Arrays;

/**
 * Created by cheta_000 on 5/19/2015.*/



import Jama.Matrix;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.Arrays;


public class LinRegImplementation {

    public static void main(String[] args) {
        int N = 100;
        int Ntest = 1000;
        ArrayList<Double> propIncorrectTrain = new ArrayList<>();
        ArrayList<Double> propIncorrectTest= new ArrayList<>();
        for (int j = 0; j < 1000; j++) {
            // Generate real separating line
            double [] realLine = genRandomLine();

            // Generate training data and test data
            double[][] randPoints = genRandomPoints(N, 2);
            double[][] realClasses = getPointClasses(randPoints, realLine);
            double[][] testData = genRandomPoints(Ntest, 2);
            double[][] testClass = getPointClasses(testData, realLine);

            // Insert output noise
            for (int i = 0; i < N/10; i++) {
                int randIndex = (int)(Math.random()*N);
                if (realClasses[randIndex][0] == 0)
                    realClasses[randIndex][0] = 1;
                else
                    realClasses[randIndex][0] = 0;
            }

            // Estimate parameters, note: .inverse() calculates pseudoinverse if inverse isn't possible
            Matrix inputs = Matrix.constructWithCopy(randPoints);
            Matrix output = Matrix.constructWithCopy(realClasses);
            Matrix weights = inputs.inverse().times(output);


            // How to estimate parameters without .inverse() pseudoinverse shortcut
//            Matrix inputsT = inputs.transpose();
//            Matrix weights = inputsT.times(inputs).inverse().times(inputsT).times(output);

            // Estimating error in training/test set
            propIncorrectTrain.add(evaluate(inputs, realClasses, weights));
            Matrix inputTest = Matrix.constructWithCopy(testData);
            propIncorrectTest.add(evaluate(inputTest, testClass, weights));
        }

        System.out.println("Average training set error:\t" + propIncorrectTrain.stream().mapToDouble(Double::doubleValue).average().getAsDouble());
        System.out.println("Average test set error:\t" + propIncorrectTest.stream().mapToDouble(Double::doubleValue).average().getAsDouble());
    }

    public static double[][] genRandomPoints(int n, int cols) {
        double[][] inputs =  new double [n][cols+1];
        for (int i = 0; i < n; i++) {
            inputs[i][0] = 1; // added so we can treat threshold as w_0
            inputs[i][1] = Math.random() * 2 - 1;
            inputs[i][2] = Math.random() * 2 - 1;
        }
        return inputs;
    }

    public static double[][] getPointClasses(double[][] inputs, double[] realLine) {
        double[][] realClass = new double [inputs.length][1];
        for (int i = 0; i < inputs.length; i++) {
            realClass[i][0] = lineClassify(new double[] {inputs[i][0], inputs[i][1]}, realLine[1], realLine[0]);
        }
        return realClass;
    }

    public static double[] genRandomLine() {
        Point [] realLine = {genRanPoint(), genRanPoint()};
        double m = (realLine[0].y - realLine[1].y)/(realLine[0].x - realLine[1].x); // slope
        double b = realLine[0].y - realLine[0].x*m;
        return new double [] {m, b};
    }

    public static double[] predict(Matrix input, Matrix weights) {
        double scores[][] = input.times(weights).getArray();
        return Arrays.stream(scores).map(n -> n[0] > 0 ? 1.0 : 0.0).mapToDouble(Double::doubleValue).toArray();
    }

    public static double evaluate(Matrix input, double[][] realClasses, Matrix weights) {
        double estimClass[] = predict(input, weights);
        int numIncorrect = 0;
        for (int i = 0; i < estimClass.length; i++)
            if (estimClass[i] != realClasses[i][0])
                numIncorrect++;
        return (double) numIncorrect/estimClass.length;
    }


    public static double lineClassify(double[] p, double b, double m) {
        if (p[1] > (b+ m*p[0]))
            return 1.0;
        else
            return 0.0;
    }

    // from PLA.java in HW1 - PLA Implementation
    public static Point genRanPoint() {
        return new Point(Math.random()*2-1, Math.random()*2-1);
    }


}
