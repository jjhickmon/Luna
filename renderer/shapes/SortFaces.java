package renderer.shapes;

import java.util.Comparator;

class SortFaces implements Comparator<Polygon3D> {
    public int compare(Polygon3D a, Polygon3D b){
        double a_sum = 0;
        for (double[] point : a.points){
            a_sum += point[2];
        }

        double b_sum = 0;
        for (double[] point : b.points){
            b_sum += point[2];
        }

        int midpoint1 = (int) (a_sum * a.scale) / a.points.length;
        int midpoint2 = (int) (b_sum * b.scale) / b.points.length;
        return midpoint1 - midpoint2;
    }
}
