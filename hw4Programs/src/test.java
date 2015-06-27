import Jama.Matrix;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by cheta_000 on 5/29/2015.
 */
public class test {

    public static void main(String[] args) {
//        BiasVar.printArr(BiasVar.sineYvals(new double[][]{{-1}, {-0.5}, {0}, {0.5}, {1}}));
//        Matrix test1 = Matrix.random(2, 2);
//        Matrix test2 = Matrix.random(2, 2);
//        BiasVar.printArr(test1.getArray());
//        System.out.println("");
//        BiasVar.printArr(test2.getArray());
//        System.out.println("");
//        BiasVar.printArr(test1.arrayTimes(test2).getArray());
//        System.out.println("");
//        BiasVar.printArr(test1.times(test2).getArray());
//        BiasVar.printArr(test1.plus(test2.times(-1.0)).getArray());

//        System.out.println(VCDimen.choose(50, 30));
//        System.out.println(VCDimen.choose(4, 2));

        double[][] testArr = new double[10000][1];
        IntStream.range(0, testArr.length).forEach(i -> testArr[i][0] = Math.random());
        System.out.println(Num7.getVar(new Matrix(testArr)));
    }
}
