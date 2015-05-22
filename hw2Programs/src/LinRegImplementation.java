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
        ArrayList<Double> propIncorrect = new ArrayList<>();
        for (int j = 0; j < 1000; j++) {
            Point [] realLine = {genRanPoint(), genRanPoint()};
            double m = (realLine[0].y - realLine[1].y)/(realLine[0].x - realLine[1].x); // slope
            double b = realLine[0].y - realLine[0].x*m;
            double[][] randPoints = new double[N][3];
            double[][] realClasses = new double[N][1];
            for (int i = 0; i < N; i++) {
                double[] newPoint = {Math.random() * 2 - 1, Math.random() * 2 - 1};
                randPoints[i][0] = 1;
                randPoints[i][1] = newPoint[0];
                randPoints[i][2] = newPoint[1];
                realClasses[i][0] = lineClassify(newPoint, b, m);
            }
            Matrix inputs = Matrix.constructWithCopy(randPoints);
            Matrix output = Matrix.constructWithCopy(realClasses);
            Matrix inputsT = inputs.transpose();
//            Matrix weights = inputsT.times(inputs).inverse().times(inputsT).times(output);
            Matrix weights = inputs.inverse().times(output);
//            weights = weights.inverse();
//            weights.arrayTimesEquals(inputsT);
//            weights.arrayTimesEquals(output);
            double outputNums[][] = inputs.times(weights).getArray();
            double outputsEstim[] = Arrays.stream(outputNums).map(n -> n[0] > 0 ? 1.0 : 0.0).mapToDouble(Double::doubleValue).toArray();
//            Arrays.stream(outputNums).forEach(n -> {
//                System.out.println(n + " " + n[0]);
//            });
            int numIncorrect = 0;
            for (int i = 0; i < outputsEstim.length; i++)
                if (realClasses[i][0] != outputsEstim[i])
                    numIncorrect++;
            propIncorrect.add((double) numIncorrect / N);
        }

        System.out.println("\n" + propIncorrect.stream().mapToDouble(Double::doubleValue).average().getAsDouble());
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
