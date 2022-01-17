package renderer.point;

import java.util.Arrays;
import renderer.math.Matrix;

public class PointConverter {
    // Converts a point in 3D space to a point in 2D space
    public static double[] convertTo2d(double[] point3d) {
        double[] point2d = new double[2];
        double [][] vector = {
            {point3d[0]},
            {point3d[1]},
            {point3d[2]}
        };
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

    public static double[] rotatePoint(double[] point3d, double theta, char axis) {
        double[] point = new double[3];
        double [][] vector = {
            {point3d[0]},
            {point3d[1]},
            {point3d[2]}
        };
        double[][] product = Matrix.multiplyMatrices(Matrix.rotationMatrix(axis, theta), vector);
        
        for (int i = 0; i < product.length; i++) {
            point[i] = product[i][0];
        }
        return point;
    }

    public static double[] translatePoint(double[] point, double[] translation) {
        double[] p = Arrays.copyOf(point, point.length);
        for (int i = 0; i < p.length; i++) {
            p[i] += translation[i];
        }
        return p;
    }

    public static double[] scalePoint(double[] point, double scale) {
        double[] p = Arrays.copyOf(point, point.length);
        for (int i = 0; i < p.length; i++) {
            p[i] *= scale;
        }
        return p;
    }

    
}
