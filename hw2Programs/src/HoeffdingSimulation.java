import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by chetan on 5/16/15.
 * Simulation demonstrating the Hoeffding Inequality
 * and its generalized form (corresponding to multiple bins)
 */
public class HoeffdingSimulation {

    public static void main(String[] args) {
        ArrayList<Double> v1HeadsProp = new ArrayList<>();
        ArrayList<Double> vRandHeadsProp = new ArrayList<>();
        ArrayList<Double> vMinHeadsProp = new ArrayList<>();
        Random rand = new Random();
        double minFrac = 1;
        int randCoin = 0;
        double curFrac;
        for (int i = 0; i < 100000; i++) {
            minFrac = 1; //stores minimum number of times heads has occured so far
            randCoin = (int) (Math.random()*1000);
            for (int j = 0; j < 1000; j++) {
                curFrac = (double) rand.ints(10, 0, 2).sum()/10;
                if (curFrac < minFrac)
                    minFrac = curFrac;
                if (j == randCoin)
                    vRandHeadsProp.add(curFrac);
                if (j == 0)
                    v1HeadsProp.add(curFrac);
            }
            vMinHeadsProp.add(minFrac);
        }
        System.out.println("Coin 1 Average:\t" + arrayListAvg(v1HeadsProp));
        System.out.println("Coin Random Average:\t" + arrayListAvg(vRandHeadsProp));
        System.out.println("Coin Min Average:\t" + arrayListAvg(vMinHeadsProp));
    }

    public static double arrayListAvg(ArrayList<Double> list) {
        return list.stream().collect(Collectors.averagingDouble(Double::doubleValue));
    }
}
