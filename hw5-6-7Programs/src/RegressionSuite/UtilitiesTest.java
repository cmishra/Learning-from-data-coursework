package RegressionSuite;

import Jama.Matrix;
import RegressionSuite.Utilities;
import junit.framework.TestCase;

/**
 * Created by chemishra on 7/1/2015.
 */
public class UtilitiesTest extends TestCase {

    public void testDotProduct1() throws Exception {
        assertEquals(Utilities.dotProduct(Matrix.constructWithCopy(new double[][] {{1, 2, 3}}),
                Matrix.constructWithCopy(new double[][] {{4, -5, 6}})), 12.0);
    }

    public void testDotProduct2() throws Exception {
        assertEquals(Utilities.dotProduct(Matrix.constructWithCopy(new double[][]{{-4, -9}}),
                Matrix.constructWithCopy(new double[][]{{-1, 2}})), -14.0);
    }

}