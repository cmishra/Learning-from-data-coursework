import Jama.Matrix;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by chemishra on 6/28/2015.
 */



public class Utilities {

    public static double[] genRandomLine() {
        Point [] realLine = {genRanPoint(), genRanPoint()};
        double m = (realLine[0].y - realLine[1].y)/(realLine[0].x - realLine[1].x); // slope
        double b = realLine[0].y - realLine[0].x*m;
        return new double [] {m, b};
    }

    // for PLA/Logistic regression implementations, assumes:
    //      2-dimension points
    //      p[0] is the intercept dimension
    // falseVal = 0 for logistic, 1 for PLA
    public static double lineClassify(double[] p, double[] line, int falseVal) {
        if (p[2] > (line[1]+ line[0]*p[1]))
            return 1.0;
        else
            return falseVal;
    }



    public static Point genRanPoint() {
        return new Point(Math.random()*2-1, Math.random()*2-1);
    }

    public static Matrix genPointsWithIntercept(int n, int numDims) {
        Matrix m = genRandPoints(n, numDims);
        return addInterceptPoints(m);
    }

    public static Matrix addInterceptPoints(Matrix m) {
        Matrix m2 = Matrix.identity(m.getRowDimension(), m.getColumnDimension() + 1);
        for (int i = 0; i < m.getRowDimension(); i++) {
            m2.set(i, 0, 1);
            m2.setMatrix(new int[] {i}, 1, m2.getColumnDimension() - 1, getRow(m, i));
        }
        return m2;
    }

    public static void writeMatrixToFile(Matrix inputs, Matrix outputs, String filename) {
        writeMatrixToFile(cbind(inputs, outputs), filename);
    }

    // Assumes same number of rows
    public static Matrix cbind(Matrix m1, Matrix m2) {
        Matrix m3 = Matrix.identity(m1.getRowDimension(), m1.getColumnDimension() + m2.getColumnDimension());
        for (int i = 0; i < m1.getRowDimension(); i++) {
            int[] curRowIndex = new int[] {i};
            m3.setMatrix(curRowIndex, 0, m1.getColumnDimension() - 1, getRow(m1, i));
            m3.setMatrix(curRowIndex, m1.getColumnDimension(), m1.getColumnDimension() + m2.getColumnDimension() - 1,
                    getRow(m2, i));
        }
        return m3;
    }

//    public static void matrixToString(Matrix m) {
//
//    }

    // Assumes matrices are vectors, assumes m1/m2 have same number of elements
    public static double dotProduct(Matrix m1, Matrix m2) {
        double[] arr1 = flattenArr(m1);
        double[] arr2 = flattenArr(m2);
        double sum = 0.0;
        for (int i = 0; i < arr1.length; i++) {
            sum += arr1[i]*arr2[i];
        }
        return sum;
    }

    public static Matrix genZeroMatrix(int i, int j) {
        return Matrix.identity(i, j).timesEquals(0.0);
    }

    public static double magnitude(Matrix m) {
        return Math.pow(Arrays.stream(flattenArr(m)).map(p -> Math.pow(p, 2)).sum(), 1.0 / 2);
    }

    public static Matrix getRow(Matrix m, int i) {
        return m.getMatrix(new int[] {i}, 0, m.getColumnDimension() - 1);
    }

    // Assumes one of the dimensions of m is 1
    public static double[] flattenArr(Matrix m) {
        if (m.getColumnDimension() == 1)
            return flattenArr(m.getArray());
        else
            return m.getArray()[0];
    }

    // to turn a n by 1 matrix to a n-element array
    public static double[] flattenArr(double[][]input ) {
        return Arrays.stream(input).mapToDouble(p -> p[0]).toArray();
    }

    public static void writeMatrixToFile(Matrix m, String filename) {
        try { // to make sure points are in fact linearally separable
            OutputStream output = Files.newOutputStream(Paths.get(filename));
            for (int i = 0; i < m.getRowDimension(); i++) {
                for (int j = 0; j < m.getColumnDimension(); j++) {
                    output.write((m.get(i, j) + ",").getBytes(Charset.defaultCharset()));
                }
                output.write("\n".getBytes(Charset.defaultCharset()));
            }
            output.close();
        } catch (IOException excp) { excp.printStackTrace();}
    }


    public static Matrix genRandPoints(int n, int cols) {
        double randPoints[][] = new double[n][cols];
        for (int i = 0; i < n; i++) {
            randPoints[i] = genRandomVal(cols);
        }
        return Matrix.constructWithCopy(randPoints);
    }

    public static double[] genRandomVal(int n) {
        double randVals [] = new double[n];
        for (int i = 0; i < n; i++) {
            randVals[i] = Math.random() * 2 - 1;
        }
        return randVals;
    }

    public static void printArr(double[][] myData) {
        for(int i = 0; i < myData.length; i++)
        {
            for(int j = 0; j < myData[0].length; j++) {
                System.out.print(myData[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void printArr(double[] myData) {
        for(int j = 0; j < myData.length; j++) {
            System.out.print(myData[j] + "\t");
        }
        System.out.println();
    }

    // Open source code found here: http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
    static void shuffleArray(int[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
