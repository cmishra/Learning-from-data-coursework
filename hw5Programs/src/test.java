import Jama.Matrix;

/**
 * Created by chemishra on 6/30/2015.
 */
public class test {

    public static void main(String[] args) {
        // Testing if Jama.Matrix beginning/end points for sub-Matrixes are inclusive or not
        Utilities.printArr(Matrix.identity(3, 3).getArray());
        System.out.println("");
        Utilities.printArr(Matrix.identity(3, 3).getMatrix(new int[]{1}, 0, 2).getArray());
        Utilities.printArr(Matrix.identity(3, 3).getMatrix(new int[]{2}, 0, 2).getArray());



    }
}
