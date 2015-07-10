/**
 * Created by chemishra on 6/28/2015.
 */
public class gradientDescent {

    public static void mainNum5(String[] args) {
        double eta = 0.1;
        double u = 1.0;
        double v = 1.0;

        double error = errorFunction(u, v);
        double tempU;
        int i = 0;
        while(error > Math.pow(10, -14)) {
            System.out.println("u: " + u + ",\tv: " + v + ",\terror: " + error);
            tempU = u;
            u -= gradientU(u, v)*eta;
            v -= gradientV(tempU, v)*eta;
            error = errorFunction(u, v);
            i++;
        }
        System.out.println("u: " + u + ",\tv: " + v + ",\terror: " + error);
    }

    public static void main(String[] args) {
        mainNum7(new String[]{});
    }

    public static void mainNum7(String[] args) {
        double eta = 0.1;
        double u = 1.0;
        double v = 1.0;

        double error = errorFunction(u, v);
        double tempU;
        int i = 0;
        while(i < 15) {
            System.out.println("u: " + u + ",\tv: " + v + ",\terror: " + error);
            u -= gradientU(u, v)*eta;
            v -= gradientV(u, v)*eta;
            error = errorFunction(u, v);
            i++;
        }
        System.out.println("u: " + u + ",\tv: " + v + ",\terror: " + error);
    }

    public static double errorFunction(double u, double v) {
        return Math.pow(insideTerm(u, v), 2);
    }

    public static double insideTerm(double u, double v) {
        return u * Math.exp(v) - 2 * v * Math.exp(-u);
    }

    public static double gradientU(double u, double v) {
        return 2 * insideTerm(u, v) * (Math.exp(v) + 2 * v * Math.exp(-u));
    }

    public static double gradientV(double u, double v) {
        return 2 * insideTerm(u, v) * (u * Math.exp(v) - 2 * Math.exp(-u));
    }
}
