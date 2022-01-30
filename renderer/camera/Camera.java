package renderer.camera;

import renderer.Display;
import renderer.math.Matrix;
import renderer.point.Point;

public class Camera {
    // will eventually need to convert camera location to the
    // default location of {0, 0, 0} and direction of {1, 0, 0}
    public static double[] location = {0, 0, 0, 1};
    public static double[][] direction = Point.toVector(new double[]{0, 0, 0, 1});
    // need to make these doubles to prevent integer division
    private static double FOV = 45; // in degrees
    private static double DNEAR = .1;
    private static double DFAR = 1000;
    private static double ASPECT_RATIO = ((double)Display.WIDTH) / ((double)Display.HEIGHT);
    private static double SCALE = 1 / Math.tan(Math.toRadians(FOV * 0.5));
    // f - SCALE, a - ASPECT_RATIO, 

    // public static double[][] projectionMatrix = 
    // {
    //     {SCALE / ASPECT_RATIO, 0, 0, 0},
    //     {0, SCALE, 0, 0},
    //     {0, 0, (DFAR + DNEAR) / (DNEAR - DFAR), (-2 * DFAR * DNEAR) / (DFAR - DNEAR)},
    //     {0, 0, -1, 0},
    // };

    public static double[][] projectionMatrix = Matrix.projectionMatrix(SCALE, ASPECT_RATIO, DFAR, DNEAR);

    // Converts a point in camera space to a point in canonical space
    public static double[] convertToCanonical(double[] point3d) {
        // need to swap order of components to convert from my space to the space in the paper
        double[][] vector = {
            {point3d[0]},
            {point3d[1]},
            {point3d[2]},
            {1}
        };
        double[][] product = Matrix.multiplyMatrices(projectionMatrix, vector);
        if (product[3][0] != 0) {
            product[0][0] /= product[3][0];
            product[1][0] /= product[3][0];
            product[2][0] /= product[3][0];
        }
        double[] tPoint3d = {product[0][0], product[1][0], product[2][0]};
        return tPoint3d;
    }


}
