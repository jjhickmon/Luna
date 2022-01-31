package renderer.shapes;

import java.util.Comparator;

class SortFaces implements Comparator<Polygon3D> {
    public int compare(Polygon3D a, Polygon3D b){
        // double a_sum = 0;
        // for (double[] point : a.points){
        //     a_sum += point[2];
        // }

        // double b_sum = 0;
        // for (double[] point : b.points){
        //     b_sum += point[2];
        // }

        // int midpoint1 = (int) (a_sum * a.scale) / a.points.length;
        // int midpoint2 = (int) (b_sum * b.scale) / b.points.length;
        // return midpoint1 - midpoint2;
        
        double a_sum = a.points[0][2] + a.points[1][2] + a.points[2][2];
        double b_sum = b.points[0][2] + b.points[1][2] + b.points[2][2];
        double midpoint1 = (a_sum * a.scale) / a.points.length;
        double midpoint2 = (b_sum * b.scale) / b.points.length;
        boolean compare = midpoint1 < midpoint2;
        return ((midpoint1 == midpoint2 ? 0 : compare ? 1 : -1));
    }
}
