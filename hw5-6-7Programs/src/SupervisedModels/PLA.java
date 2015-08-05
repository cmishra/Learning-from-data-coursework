package SupervisedModels;

import Jama.Matrix;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by cheta_000 on 7/19/2015.
 * Re-implementation of PLA with better design to make it easier to use
 */
public class PLA {
    public static Matrix computeWeights(Matrix x, Matrix y) {
        Matrix w = Utilities.genZeroMatrix(x.getColumnDimension(), 1);
        Matrix estims = predict(x, w);
        int i;
        while(LinReg.classifError(y, estims) != 0.0) {
            final Matrix z = y.minus(estims);
            i = IntStream.range(0, x.getRowDimension()).filter(p -> z.get(p, 0) != 0).findFirst().getAsInt();
            w.plusEquals(Utilities.getRow(x, i).times(y.get(i, 0)).transpose());
            estims = predict(x, w);
        }
        return w;
    }

    public static Matrix predict(Matrix x, Matrix w) {
        Matrix estims = x.times(w);
        for (int i = 0; i < x.getRowDimension(); i++) {
            if (estims.get(i, 0) > 0)
                estims.set(i, 0, 1);
            else
                estims.set(i, 0, -1);
        }
        return estims;
    }
}
