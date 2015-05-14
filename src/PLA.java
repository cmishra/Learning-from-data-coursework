import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chetan on 5/13/15.
 */
public class PLA {

    //implements the perceptrons learning algorithm on an easily changeable number of data points
    //(for Learning From Data course HW#1)
    public static void main(String[] args) {
        int n = 100;
        ArrayList<Integer> counters = new ArrayList<>();
        ArrayList<Double> propDiffs = new ArrayList<>();
        ArrayList<Point> estimGError = new ArrayList<>();
        for (int i = 0; i < 1E6; i++)
            estimGError.add(genRanPoint());
        System.out.println("iterations starting");
        for (int i = 0; i < 1000; i++) {
            ArrayList<Point> realLine = new ArrayList<>();
            realLine.add(genRanPoint());
            realLine.add(genRanPoint());
            double m = (realLine.get(0).y - realLine.get(1).y)/(realLine.get(0).x - realLine.get(1).x); // slope
            double b = realLine.get(0).y - realLine.get(0).x*m; //y-intercept

            for (Point p : estimGError) {
                if (p.y > (b+ m*p.x))
                    p.val = 1;
                else
                    p.val = 0;
            }

            ArrayList<Point> points = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                Point toAdd = genRanPoint();
                toAdd.val = lineClassify(toAdd, b, m);
                points.add(toAdd);
            }
            try {
                OutputStream output = Files.newOutputStream(Paths.get("pointOutput.csv"));
                output.write(("x,y,val\n").getBytes(Charset.defaultCharset()));
                for (Point p : points) {
                    output.write((p.x + "," + p.y + "," + p.val + "\n").getBytes(Charset.defaultCharset()));
                }
                output.close();
            } catch (IOException excp) { excp.printStackTrace();}

            double weights[] = new double[] {0.0, 0.0, 0.0};
            List<Point> misclassified = points.stream()
                    .filter(p -> p.val != vecClassify(p, weights) )
                    .collect(Collectors.toList());
            int counter = 0;
            while (misclassified.size() > 0) {
                counter++;
                Point p = misclassified.get((int) Math.floor(Math.random() * misclassified.size()));
                //Actual PLA algorithm below:
                if (p.val == 1) {
                    weights[0] += p.x;
                    weights[1] += p.y;
                    weights[2] += 1;
                } else {
                    weights[0] -= p.x;
                    weights[1] -= p.y;
                    weights[2] -= 1;
                }
                misclassified = points.stream()
                        .filter(q -> vecClassify(q, weights) != q.val)
                        .collect(Collectors.toList());
            }
            counters.add(counter);
            propDiffs.add((double) estimGError.stream()
                    .filter(p -> vecClassify(p, weights) != lineClassify(p, b, m))
                    .count()
                    /estimGError.size());
        }
        System.out.println(counters);
        System.out.println((double) counters.stream().mapToInt(Integer::intValue).sum() / counters.size());
        System.out.println(propDiffs.stream().mapToDouble(Double::doubleValue).sum()/propDiffs.size());
    }

    public static Point genRanPoint() {
        return new Point(Math.random()*2-1, Math.random()*2-1);
    }

    public static int lineClassify(Point p, double b, double m) {
        if (p.y > (b+ m*p.x))
            return 1;
        else
            return 0;
    }

    public static int vecClassify(Point p, double[] weights) {
        if (p.x*weights[0] + p.y*weights[1] + weights[2] > 0)
            return(1);
        else
            return 0;
    }
}
