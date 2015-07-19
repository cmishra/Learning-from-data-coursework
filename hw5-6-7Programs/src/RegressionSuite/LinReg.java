package RegressionSuite;

import Jama.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by chemishra on 7/13/2015.
 * Re-implemented instead of reusing hw2Program's to be
 * consistent with Matrix class-based programs
 */
public class LinReg {

    public static void main(String[] args) {
        // Read in data and extract outputs
        Matrix in = Matrix.constructWithCopy(Utilities.readDoubleTable("in.ssv", " +").getArray());
        Matrix inY = Utilities.getCol(in, 2);
        Matrix out = Matrix.constructWithCopy(Utilities.readDoubleTable("out.ssv", " +").getArray());
        Matrix outY = Utilities.getCol(out, 2);

        // Transform inputs
        Matrix inExpanded = inputTrans(in);
        Matrix outExpanded = inputTrans(out);

        // Train model
        double lambda = Math.pow(10, -1);
        Matrix wLin = computeWLin(inExpanded, inY);
        Matrix wWeightDecay = computeWLinWeightDecay(inExpanded, inY, lambda);

        // Get Accuracy
        Matrix inEstims = predictLinRegClass(inExpanded, wLin);
        Matrix outEstims = predictLinRegClass(outExpanded, wLin);
        System.out.println("Normal Regression, E_in:\t" + classifError(inY, inEstims));
        System.out.println("Normal Regression, E_out:\t" + classifError(outY, outEstims));
        Matrix inEstimsReg = predictLinRegClass(inExpanded, wWeightDecay);
        Matrix outEstimsReg = predictLinRegClass(outExpanded, wWeightDecay);
        System.out.println("Regularized Regression, E_in:\t" + classifErrorWeightDecay(inY, inEstimsReg, wWeightDecay, lambda));
        System.out.println("Regularized Regression, E_out:\t" + classifError(outY, outEstimsReg));
    }

    // Performs necessary nonlinear transformations to input data, as specified by HW #6
    // Overrites column 3, output as it has already been copied
    public static Matrix inputTrans(Matrix in) {
        in = Utilities.addInterceptPoints(in);
        Matrix inExpanded = Matrix.identity(in.getRowDimension(), 8);
        inExpanded.setMatrix(0, in.getRowDimension() - 1, 0, 2, in);
        Utilities.setCol(inExpanded, 3, Utilities.getCol(in, 1).arrayTimes(Utilities.getCol(in, 1)));
        Utilities.setCol(inExpanded, 4, Utilities.getCol(in, 2).arrayTimes(Utilities.getCol(in, 2)));
        Utilities.setCol(inExpanded, 5, Utilities.getCol(in, 1).arrayTimes(Utilities.getCol(in, 2)));
        Utilities.setCol(inExpanded, 6, Utilities.absoluteVal(Utilities.getCol(in, 1).minus(Utilities.getCol(in, 2))));
        Utilities.setCol(inExpanded, 7, Utilities.absoluteVal(Utilities.getCol(in, 1).plus(Utilities.getCol(in, 2))));
        return inExpanded;
    }


    public static Matrix computeWLin(Matrix inputs, Matrix outputs) {
        Matrix inputT = inputs.transpose();
        return inputT.times(inputs).inverse().times(inputT).times(outputs);
    }

    // Last column assumed to be y. If you have data in separate columns, combine with "cbind"
    public static Matrix computeWLinWeightDecay(Matrix inputs, Matrix outputs, double weightDecay) {
        Matrix lambdaFactor = Matrix.identity(inputs.getColumnDimension(), inputs.getColumnDimension()).timesEquals(weightDecay);
        Matrix inputT = inputs.transpose();
        return inputT.times(inputs).plusEquals(lambdaFactor).inverse().times(inputT).times(outputs);
    }

    public static Matrix predictLinRegClass(Matrix inputs, Matrix weights) {
        Matrix estims = inputs.times(weights);
        for (int i = 0; i < inputs.getRowDimension(); i++) {
            if (estims.get(i, 0) < 0)
                estims.set(i, 0, -1);
            else
                estims.set(i, 0, 1);
        }
        return estims;
    }

    public static double classifError(Matrix outputs, Matrix estims) {
        double numWrong = 0.0;
        for (int i = 0; i < outputs.getRowDimension(); i++) {
            if (outputs.get(i, 0) != estims.get(i, 0))
                numWrong++;
        }
        return numWrong/outputs.getRowDimension();
    }

    // Computer E_augmented (better predictor of E_out than E_in)
    public static double classifErrorWeightDecay(Matrix outputs, Matrix estims, Matrix weight, double lambda) {
        double numWrong = 0.0;
        for (int i = 0; i < outputs.getRowDimension(); i++) {
            if (outputs.get(i, 0) != estims.get(i, 0))
                numWrong++;
        }
        System.out.println(numWrong + " / " + outputs.getRowDimension());
        return (numWrong + Utilities.dotProduct(weight, weight)*lambda)/outputs.getRowDimension();
    }



}
