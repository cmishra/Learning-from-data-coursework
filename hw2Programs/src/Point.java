/**
 * Created by chetan on 5/13/15.
 * duplicate of Point.java in HW1 - PLA Implementation
 */

public class Point {

    public double x;
    public double y;
    public int val;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static void main(String[] args) {
        System.out.println(2 << 1);
//        System.out.println(new Integer(2 > 3)); (confirmed, doesn't work')
        System.out.println((int) 1.9);
    }
}
