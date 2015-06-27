/**
 * Created by cheta_000 on 6/6/2015.
 * Dynamic Programming implementation of a recursively defined dichotomies growth function with respect to N (sample size)
 * Problem can be found here: https://work.caltech.edu/homework/hw4.pdf (number 8)
 */
public class VCDimen {
    final static int maxVCDim = 1000;
    final static int maxQ = 10;

    public static double[][] storedVals = new double [maxQ][maxVCDim];

    public static void main(String[] args) {
        System.out.println("Q = x:\tBreak Point, difference between 2^N and m_H(N)");
        for (int q = 1; q < maxQ + 1; q++) {
            System.out.print("Q = " + q + ":\t");
            int i = 1;
            double numDichotomies = mH(i, q);
            while (numDichotomies == Math.pow(2, i)) {
//                System.out.print(i + ", " + mH(i, q) + " vs " + Math.pow(2, i) + "; ");
                i++;
                numDichotomies = mH(i, q);
            }
            System.out.print(i + ", " + mH(i, q) + " vs " + Math.pow(2, i) + "; ");
            System.out.println("");
        }
    }

    public static double mH(int N, int q) {
        // Alerts to make maxVCDim/maxQ larger
        if (N > maxVCDim)
            System.out.println("N is greater than " + maxVCDim);
        if (q > maxQ)
            System.out.println("q is greater than " + maxQ);

        // Compute value if parameters are accurate + it's not pre-calculated
        if (N == 1 && storedVals[q - 1][N - 1] == 0) {
            storedVals[q - 1][N - 1] = 2;
//            System.out.print("BC");
        }
        else if (storedVals[q - 1][N - 1] == 0) {
            storedVals[q - 1][N - 1] = 2 * mH(N - 1, q) - choose(N - 1, q);
//            System.out.print("already there, subtracting  " + choose(N - 1, q) + ", " + (N-1) + " " + q + " ");
        }
        return storedVals[q - 1][N - 1];
    }

    public static double choose(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k > n/2) {
            // choose(n,k) == choose(n,n-k),
            // so this could save a little effort
            k = n - k;
        }

        double denominator = 1.0, numerator = 1.0;
        for (int i = 1; i <= k; i++) {
            denominator *= i;
            numerator *= (n + 1 - i);
        }
        return numerator / denominator;
    }
}
