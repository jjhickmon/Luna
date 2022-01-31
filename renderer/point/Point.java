package renderer.point;

import java.util.Arrays;
import renderer.math.Matrix;

public class Point {
    public static int COMPONENTS = 3;
    // Converts a point in 3D space to a point in 2D space
    public static double[] convertTo2d(double[] point3d) {
        double[] point2d = new double[2];
        double [][] vector = toVector(point3d);
        double[][] product = Matrix.multiplyMatrices(Matrix.transformationMatrix(2, 3), vector);
        for (int i = 0; i < product.length; i++) {
            point2d[i] = product[i][0];
        }

        // Sets the screen coordinates of the point, with (0, 0) being center screen,
        // the right side being the positive x axis, the top side being the positive y
        // axis, and the screen being the positive z axis
        // point2d[0] += (int)(Display.WIDTH / 2.0);
        // point2d[1] = (int)(Display.HEIGHT / 2.0) - point2d[1]; // flip y coordinates for screen
        return point2d;
    }

    // converts a point into a vector
    public static double[][] toVector(double[] point) {
        double[][] vector = new double[4][1];
        // points have no w component by default. if the point has never represented a vector
        // I need to add a w component, otherwise I will keep the current w component
        if (point.length == 4) {
            vector = new double[][]{
                {point[0]},
                {point[1]},
                {point[2]},
                {point[3]}
            };
        } else {
            vector = new double[][]{
                {point[0]},
                {point[1]},
                {point[2]},
                {1}
            };
        }
        
        return vector;
    }

    // public static double[] rotate(double[] point3d, double theta, char axis) {
    //     double[] point = new double[3];
    //     double [][] vector = toVector(point3d);
    //     double[][] product = Matrix.multiplyMatrices(Matrix.rotationMatrix(axis, theta), vector);
        
    //     for (int i = 0; i < product.length; i++) {
    //         point[i] = product[i][0];
    //     }
    //     return point;
    // }

    public static double[] translate(double[] point, double[] translation) {
        double[] p = Arrays.copyOf(point, point.length);
        for (int i = 0; i < p.length; i++) {
            p[i] += translation[i];
        }
        return p;
    }

    public static double[] scale(double[] point, double scale) {
        for (int i = 0; i < point.length; i++) {
            point[i] *= scale;
        }
        return point;
    }

    
}
