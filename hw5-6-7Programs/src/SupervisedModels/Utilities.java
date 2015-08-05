package SupervisedModels;

import Etc.Point;
import Jama.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by chemishra on 6/28/2015.
 */



public class Utilities {

    // Pain in the rear to code. More work than the rest of SupervisedModels.LinReg.java combined
    public static Matrix readDoubleTable(String filename, String sep) {
        try {
            // Get number of rows
            BufferedReader file = new BufferedReader(new FileReader(filename));
            int numLines = 0;
            while (file.ready()) {
                if (file.readLine() != "")
                    numLines++;
            }
            file.close();

            // Get number of columns, (number of times sep is in a line) + 1
            file = new BufferedReader(new FileReader(filename));
            String[] firstLine = sepSSV(file.readLine(), sep);
            int numCols = firstLine.length;

            // Extract and format data
            Matrix data = Matrix.identity(numLines, numCols);
            setRow(data, 0, parseDoubles(firstLine));
            String line;
            int i = 1;
            while (file.ready()) {
                line = file.readLine();
                if (line != "") {
                    setRow(data, i, parseDoubles(sepSSV(line, sep)));
                    i++;
                }
            }
            return data;

        } catch (IOException excp) {
            excp.printStackTrace();
        }
            return null;
    }

    // Since the file is space-separated, splitting on space
    // always leads to a arr[0] spot with an empty string
    // this eliminates that
    public static String[] sepSSV(String line, String sep) {
        String[] splitVals = line.split(sep);
        String[] ret = new String[splitVals.length - 1];
        for (int i = 1; i < splitVals.length; i++) {
            ret[i - 1] = splitVals[i];
        }
        return ret;
    }

    public static double[] parseDoubles(String[] arr) {
        double[] ret = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ret[i] = Double.valueOf(arr[i]);
        }
        return ret;
    }

    // Returns absolute value of Matrix m
    // In-place absolute computation
    public static Matrix absoluteVal(Matrix m) {
        double val;
        for (int j = 0; j < m.getColumnDimension(); j++) {
            for (int i = 0; i < m.getRowDimension(); i++) {
                val = m.get(i, j);
                if (val < 0)
                    m.set(i, j, -val);
            }
        }
        return m;
    }

    public static Matrix getCol(Matrix m, int j) {
        return m.getMatrix(0, m.getRowDimension() - 1, new int[] {j});
    }

    public static Matrix getCol(Matrix m, int [] j) {
        return m.getMatrix(0, m.getRowDimension() - 1, j);
    }


    // Assumes number of rows in m1, m2 are identical
    // column j of m2 is put into m1
    public static void setCol(Matrix m1, int j, Matrix m2) {
        m1.setMatrix(0, m1.getRowDimension() - 1, new int[] {j}, m2);
    }

    // Puts in values within vals into row i of Matrix m
    // Assumes the number of rows in
    public static void setRow(Matrix m, int i, double[] vals) {
        for (int j = 0; j < m.getColumnDimension(); j++) {
            m.set(i, j, vals[j]);
        }
    }

    public static Matrix getRow(Matrix m, int i) {
        return m.getMatrix(new int[] {i}, 0, m.getColumnDimension() - 1);
    }

    public static Matrix getRow(Matrix m, int [] i) {
        return m.getMatrix(i, 0, m.getColumnDimension() - 1);
    }

    // Checks the number of times 'containee' is contained within 'str'
    public static int checkNumberOfContains(String str, String containee) {
        int i = 1;
        while (str.contains(containee)) {
            i++;
            str = str.substring(str.indexOf(containee) + containee.length());
        }
        return i;
    }

    public static double[] genRandomLine() {
        Point[] realLine = {genRanPoint(), genRanPoint()};
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
        System.out.print("{");
        for(int i = 0; i < myData.length; i++) {
            printArr(myData[i], "");
            if (i + 1 < myData.length)
                System.out.print(", ");
        }
        System.out.println("}");
    }

    public static void printArr(double[] myData, String endChar) {
        System.out.print("{");
        for(int j = 0; j < myData.length; j++) {
            System.out.print(myData[j]);
            if (j + 1 < myData.length)
                System.out.print(", ");
        }
        System.out.print("}" + endChar);
    }

    public static void printArrWithLabel(double[][] myData, String label) {
        System.out.print(label);
        printArr(myData);
    }

    public static void printArrWithLabel(double[] myData, String label) {
        System.out.print(label);
        printArr(myData);
    }

    public static void printArr(double[] myData) {
        printArr(myData, "\n");
    }

    // Assumes necessary x_0 (the column of 1's) has already been put in and is the first column in inputs
    public static Matrix getActualY(Matrix inputs, double[] randLine) {
        Matrix actualY = Matrix.identity(inputs.getRowDimension(), 1);
        for (int i = 0; i < inputs.getRowDimension(); i++) {
            double[] point = Utilities.getRow(inputs, i).getArray()[0];
            actualY.set(i, 0, Utilities.lineClassify(point, randLine, -1));
        }
        return actualY;
    }

    // Open source code found here: http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
    static void shuffleArray(int[] ar) {
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
