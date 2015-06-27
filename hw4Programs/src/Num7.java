import Jama.Matrix;

import java.util.Arrays;


/**
* Created by cheta_000 on 5/29/2015.
*/
public class Num7 {
    public static void main(String[] args) {
        int numIters = 100000;
        Matrix coefsA = Matrix.identity(numIters, 1);
        Matrix coefsB = Matrix.identity(numIters, 1);;
        Matrix coefsC = Matrix.identity(numIters, 2);;
        Matrix coefsD = Matrix.identity(numIters, 1);;
        Matrix coefsE = Matrix.identity(numIters, 2);;

        for (int j = 0; j < numIters; j++) {
            // Generate training and testing data for each answer
            Matrix train = changeRandRanges(Matrix.random(10, 1));
            Matrix realVal = sineYvals(train);

            // Save parameters
            coefsA.set(j,0, testLearningModel(transModelA(train), realVal).get(0,0));
            coefsB.set(j,0, testLearningModel(transModelB(train), realVal).get(0,0));
            twoCoefSet(coefsC, testLearningModel(transModelC(train), realVal), j);
            coefsD.set(j,0, testLearningModel(transModelD(train), realVal).get(0,0));
            twoCoefSet(coefsE, testLearningModel(transModelE(train), realVal), j);
        }

        // Calculate bias (computationally)
        int testPoints = 1000000;
        Matrix test = changeRandRanges(Matrix.random(testPoints, 1));
        Matrix testVal = sineYvals(test);

        // Get g-bar weights for each model and variance for each model
        Matrix gBarA = getMeans(coefsA);
        Matrix gBarB = getMeans(coefsB);
        Matrix gBarC = getMeans(coefsC);
        Matrix gBarD = getMeans(coefsD);
        Matrix gBarE = getMeans(coefsE);

        // Get error for each model
        double mSqErA = evaluateLearningModel(transModelA(test), gBarA, testVal);
        double mSqErB = evaluateLearningModel(transModelB(test), gBarB, testVal);
        double mSqErC = evaluateLearningModel(transModelC(test), gBarC, testVal);
        double mSqErD = evaluateLearningModel(transModelD(test), gBarD, testVal);
        double mSqErE = evaluateLearningModel(transModelE(test), gBarE, testVal);

        // Calculate e_out for each model
        printModelPerf(mSqErA, getVar(coefsA), "A");
        BiasVar.printArr(gBarA.getArray());
        printModelPerf(mSqErB, getVar(coefsB), "B");
        BiasVar.printArr(gBarB.getArray());
        printModelPerf(mSqErC, getVar(coefsC), "C");
        BiasVar.printArr(gBarC.getArray());
        printModelPerf(mSqErD, getVar(coefsD), "D");
        BiasVar.printArr(gBarD.getArray());
        printModelPerf(mSqErE, getVar(coefsE), "E");
        BiasVar.printArr(gBarE.getArray());
    }

    public static void printModelPerf(double sqEr, double var, String modelCode) {
        System.out.println("E_out " + modelCode + ": " + (sqEr + var) + ", sqError: " + sqEr + ", Var: " + var);
    }

    public static Matrix testLearningModel(Matrix input, Matrix realVal) {
        return input.inverse().times(realVal);
    }

    public static void twoCoefSet(Matrix coef, Matrix weights, int j) {
        coef.set(j, 0, weights.get(0,0));
        coef.set(j, 1, weights.get(1,0));
    }

    public static double evaluateLearningModel(Matrix input, Matrix weights, Matrix realVal) {
        Matrix estims = input.times(weights);
        Matrix error = estims.plus(realVal.times(-1));
        double mSqEr = Arrays.stream(error.arrayTimes(error).getArray()).mapToDouble(m -> m[0]).average().getAsDouble();
        return mSqEr;
    }

    public static Matrix transModelA(Matrix input) {
        double[][] test = new double[input.getRowDimension()][1];
        for (int i = 0; i < input.getRowDimension(); i++) {
            test[i][0] = 1;
        }
        return Matrix.constructWithCopy(test);
    }

    public static Matrix transModelB(Matrix input) {
        return input;
    }

    public static Matrix transModelC(Matrix input) {
        double[][] test = new double[input.getRowDimension()][2];
        for (int i = 0; i < input.getRowDimension(); i++) {
            test[i][0] = input.get(i,0);
            test[i][1] = 1;
        }
        return Matrix.constructWithCopy(test);
    }

    public static Matrix transModelD(Matrix input) {
        return input.arrayTimes(input);
    }

    public static Matrix transModelE(Matrix input) {
        Matrix m1 = transModelD(input);
        Matrix ret = Matrix.identity(input.getRowDimension(), 2);
        for (int i = 0; i < input.getRowDimension(); i++) {
            ret.set(i, 0, m1.get(i,0));
            ret.set(i, 1, 1);
        }
        return ret;
    }

    // Takes an n by m matrix and returns an m by 1 matrix of the means
    public static Matrix getMeans(Matrix m) {
        Matrix means = Matrix.identity(m.getColumnDimension(), 1);
        for (int j = 0; j < m.getColumnDimension(); j++) {
            double[][] colJ = m.getMatrix(0, m.getRowDimension()-1, new int []{j}).getArray();
            means.set(j, 0, Arrays.stream(colJ).mapToDouble(p -> p[0]).average().getAsDouble());
        }
        return means;
    }

    // Takes an n by m matrix and returns the mean of each column's variance
    public static double getVar(Matrix m) {
        Matrix vars = Matrix.identity(1, m.getColumnDimension());
        Matrix means = getMeans(m);
        for (int j = 0; j < m.getColumnDimension(); j++) {
            double[][] colJ = m.getMatrix(0, m.getRowDimension()-1, new int []{j}).getArray();
            double mean = means.get(j,0);
            vars.set(0, j, Arrays.stream(colJ).mapToDouble(p -> Math.pow(p[0] - mean, 2)).average().getAsDouble());
        }
        return Arrays.stream(vars.getArray()[0]).sum();
    }

    public static Matrix sineYvals(Matrix input) {
        Matrix ret = Matrix.identity(input.getRowDimension(),1);
        for (int i = 0; i < input.getRowDimension(); i++) {
            ret.set(i,0,Math.sin(Math.PI*input.get(i,0)));
        }
        return ret;
    }

    // changes a 0-1 range random number matrices into -1-1 range random numbers
    public static Matrix changeRandRanges(Matrix m) {
        m = m.times(2.0);
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                m.set(i, j, m.get(i,j)-1);
            }
        }
        return m;
    }


    public static double[][] calcEstim(double[][] weights, double[][] test) {
        return Matrix.constructWithCopy(test).times(Matrix.constructWithCopy(weights)).getArray();
    }

    public static double[] calcError(double[][] testEstim, double[][] testCls) {
        double error[] = new double[testEstim.length];
        for (int i = 0; i < testEstim.length; i++) {
            error[i] = Math.pow(testEstim[i][0] - testCls[i][0], 2);
        }
        return error;
    }

}
