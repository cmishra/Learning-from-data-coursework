package SupervisedModels;

import Jama.Matrix;
import com.joptimizer.functions.ConvexMultivariateRealFunction;
import com.joptimizer.functions.LinearMultivariateRealFunction;
import com.joptimizer.functions.PDQuadraticMultivariateRealFunction;
import com.joptimizer.optimizers.JOptimizer;
import com.joptimizer.optimizers.OptimizationRequest;
import org.codehaus.groovy.runtime.powerassert.SourceText;

import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;


/**q
 * Created by cheta_000 on 7/19/2015.
 * Not fully complete 7/25/15
 */


public class SVM {

    // Geared to answer #8, 9, 10 on HW# 7
    // https://work.caltech.edu/homework/hw7.pdf
    public static void main(String[] args) {
        // Parameters of experiment
        int numIters = 1000;
        int sampleSizePerIter = 100;

        // Arrays to store key metrics
        int numTimesSVMBetter = 0;
        int[] numSV = new int[numIters];

        int j = 0;
        while (j < numIters) {
            // Generate experiment data
            double[] randLine = Utilities.genRandomLine();
            Matrix trainX = Utilities.genPointsWithIntercept(sampleSizePerIter, 2);
            Matrix trainY = Utilities.getActualY(trainX, randLine);
            Matrix trainXNoIntercept = Utilities.getCol(trainX, IntStream.range(1, trainX.getColumnDimension()).toArray());

            Matrix testX = Utilities.genPointsWithIntercept(10000, 2);
            Matrix testY = Utilities.getActualY(testX, randLine);
            Matrix testXNoIntercept = Utilities.getCol(testX, IntStream.range(1, testX.getColumnDimension()).toArray());

            // Train weights
            Matrix alphas = null;
            try {
                alphas = computeQPcoefsHardSVM(trainX, trainY);
                j++;
            } catch (Exception excep) {
                continue;
            }
            if (alphas != null) {
                numSV[j - 1] = Arrays.stream(Utilities.flattenArr(alphas)).mapToInt(a -> a > 0.001 ? 1 : 0).sum();

                // Make predictions (PLA)
                Matrix weights = PLA.computeWeights(trainX, trainY);
                Matrix predsPLA = PLA.predict(testX, weights);
//                System.out.println("PLA E_out:\t" + LinReg.classifError(testY, predsPLA));

                // Make predictions (SVM)
                Matrix predsSVM = predict(trainXNoIntercept, trainY, alphas, testXNoIntercept);
//                System.out.println("SVM E_out:\t" + LinReg.classifError(testY, predsSVM));

                if (LinReg.classifError(testY, predsSVM) < LinReg.classifError(testY, predsPLA))
                    numTimesSVMBetter++;
            }
        }

        System.out.println("\nProportion of time SVM is better:\t" + (double) numTimesSVMBetter/numIters);
        System.out.println("Average num of Support Vectors:\t" + Arrays.stream(numSV).average().getAsDouble());
    }

    // Computes the variables in a KKT QP optimization for a hard SVM model
    // returns a N by 1 matrix of the solution of the optimization (does NOT return weights)
    public static Matrix computeQPcoefsHardSVM(Matrix x, Matrix y) {
        // Objective function
        double[][] qc = new double[x.getRowDimension()][x.getRowDimension()];
        for (int i = 0; i < x.getRowDimension(); i++) {
            for (int j = 0; j < x.getRowDimension(); j++) {
                qc[i][j] = y.get(i, 0)*y.get(j, 0)*Utilities.dotProduct(Utilities.getRow(x, i), Utilities.getRow(x, j));
            }
        }
        double[] lc = new double[x.getRowDimension()];
        Arrays.fill(lc, -1.0);
        PDQuadraticMultivariateRealFunction objectiveFunction = new PDQuadraticMultivariateRealFunction(qc, lc, 0);

        //equalities
        double[][] yTranspose = y.transpose().getArray();
        double[] zeroTranspose = {0};

        //inequalities (constraint to keep alpha positive)
        ConvexMultivariateRealFunction[] inequalities = new ConvexMultivariateRealFunction[x.getRowDimension()];
        for (int i = 0; i < x.getRowDimension(); i++) {
            double[] G_i = new double[x.getRowDimension()];
            G_i[i] = -1.0;
            inequalities[i] = new LinearMultivariateRealFunction(G_i, 0.0);
        }

        //optimization problem construction
        OptimizationRequest or = new OptimizationRequest();
        or.setF0(objectiveFunction);
        or.setFi(inequalities);
        or.setA(yTranspose);
        or.setB(zeroTranspose);
        or.setToleranceFeas(1.E-4);
        or.setTolerance(1.E-4);

        //optimization
        JOptimizer opt = new JOptimizer();
        opt.setOptimizationRequest(or);
        try {
            opt.optimize();
        } catch (Exception ecp) {
//            ecp.printStackTrace();
        }
        return Matrix.constructWithCopy(new double [][] {opt.getOptimizationResponse().getSolution()}).transpose();
    }

    // Takes QP coefficients and training data (inputs + output) and predicts class for
    // another set of inputs passed in ("predictX")
    public static Matrix predict(Matrix trainX, Matrix trainY, Matrix alphas, Matrix predictX) {
        // Generate w, b
        int[] supportVectorIndexes = IntStream.range(0, trainX.getRowDimension()).filter(p -> alphas.get(p, 0) > 0.001).toArray();
        Matrix w = Utilities.genZeroMatrix(1, trainX.getColumnDimension());
        for (int i : supportVectorIndexes) {
            w.plusEquals(Utilities.getRow(trainX, i).timesEquals(alphas.get(i, 0)).timesEquals(trainY.get(i, 0)));
        }

        double b = -1*(1 - Utilities.dotProduct(w, Utilities.getRow(trainX, supportVectorIndexes[0])) *
                trainY.get(supportVectorIndexes[0],0))/trainY.get(supportVectorIndexes[0], 0);

        // Predict
        Matrix estims = predictX.times(w.transpose());
        for (int i = 0; i < estims.getRowDimension(); i++) {
            estims.set(i, 0, estims.get(i, 0) > b ? 1.0 : -1.0);
        }
        return estims;
    }

}
