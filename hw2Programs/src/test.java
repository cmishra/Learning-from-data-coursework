import java.util.Arrays;

/**
 * Created by cheta_000 on 5/21/2015.
 */
public class test {

    public static void main(String[] args) {
        double test [][] = {{1, 2}, {3, 4}};
        Arrays.stream(test).forEach(p -> System.out.println(p[0] + "" + p[1]));
//        test[0]={5, 6} confirmed hat this doesn't work
    }

}
