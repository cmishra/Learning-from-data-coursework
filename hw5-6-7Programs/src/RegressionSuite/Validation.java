package RegressionSuite;

import Jama.Matrix;

import java.util.stream.IntStream;

/**
 * Created by chemishra on 7/17/2015.
 */
public class Validation {

    public static void main(String[] args) {
        num1_2_3_4();
    }

    public static void num1_2_3_4() {

        int[] validationRows = IntStream.range(25, 35).toArray();
        int[] trainingRows = IntStream.range(0, 25).toArray();

        // Get inputs
        Matrix in = Matrix.constructWithCopy(Utilities.readDoubleTable("in.ssv", " +").getArray());
        Matrix inY = Utilities.getCol(in, 2);
        in = LinReg.inputTrans(in);
        Matrix validation = Utilities.getRow(in, validationRows);
        Matrix validationY = Utilities.getRow(inY, validationRows);
        in = Utilities.getRow(in, trainingRows);
        inY = Utilities.getRow(inY, trainingRows);

        // Get outputs
        Matrix out = Matrix.constructWithCopy(Utilities.readDoubleTable("out.ssv", " +").getArray());
        Matrix outY = Utilities.getCol(out, 2);
        out = LinReg.inputTrans(out);


        // Train model
        for (int i = 3; i < 8; i++) {
            int[] cols = IntStream.rangeClosed(0, i).toArray();
            Matrix weights = LinReg.computeWLin(Utilities.getCol(in, cols), inY);
            Matrix estims = LinReg.predictLinRegClass(Utilities.getCol(validation, cols), weights);
            System.out.print("Using " + i + " predictors; E_eval:\t" + LinReg.classifError(validationY, estims));

            Matrix estimsOut = LinReg.predictLinRegClass(Utilities.getCol(out, cols), weights);
            System.out.println("\t\tE_out\t" + LinReg.classifError(outY, estimsOut));
        }
    }
}
