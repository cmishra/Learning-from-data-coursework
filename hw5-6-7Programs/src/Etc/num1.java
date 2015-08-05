package Etc;

import SupervisedModels.Utilities;

import java.util.Scanner;

/**
 * Created by chemishra on 6/28/2015.
 *
 * Perhaps overkill doing this in java, but eh I do want to practice java
 */
public class num1 {
    public static void main(String[] args) {
        double d = 8;
        double theta = 0.1;
        double[] Ns = {10, 25, 100, 500, 1000};
        double[] expectedEins = new double[5];
        for (int i = 0; i < Ns.length; i++) {
            expectedEins[i] = Math.pow(theta, 2) * (1 - (d + 1)/ Ns[i] );
        }
        Utilities.printArr(Ns);
        Utilities.printArr(expectedEins);
    }

    public static double readDouble(String prompt, Scanner scan) {
        System.out.println(prompt);
        return scan.nextDouble();
    }



}

