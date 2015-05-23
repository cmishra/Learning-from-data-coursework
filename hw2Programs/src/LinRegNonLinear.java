import Jama.Matrix;

import java.util.ArrayList;

/**
 * Created by cheta_000 on 5/22/2015.
 */
public class LinRegNonLinear {

    public static void main(String[] args) {
        int N = 1000;
        int iters = 1000;
        ArrayList<Double> props = new ArrayList<>();
        for (int j = 0; j < iters; j++) {
            // Generate training data set
            double[][] train = LinRegImplementation.genRandomPoints(N, 2);
            double[][] trainCls = LinRegTransformed.assignClassWithNoise(train, N);

            // Fit parameters
            Matrix inputs = Matrix.constructWithCopy(train);
            Matrix outputs = Matrix.constructWithCopy(trainCls);
            Matrix weights = inputs.inverse().times(outputs);

            // Assess probability
            props.add(LinRegImplementation.evaluate(inputs, trainCls, weights));
        }
        System.out.println(props.stream().mapToDouble(Double::doubleValue).average().getAsDouble());


    }

}
