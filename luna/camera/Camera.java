package luna.camera;

import luna.Display;
import luna.math.Matrix;
import luna.point.Point;

public class Camera {
    // will eventually need to convert camera location to the
    // default location of {0, 0, 0} and direction of {1, 0, 0}
    public static double[][] location = Point.toVector(new double[]{0, 0, 0});
    public static double[][] direction = Point.toVector(new double[]{0, 0, 1});
    public static double[][] up = Point.toVector(new double[]{0, 1, 0});
    // the matrix representing the location and direction of the camera
    public static double[][] cameraMatrix;

    // need to make these doubles to prevent integer division
    public static boolean WIREFRAME = false;
    private static double FOV = 45; // in degrees
    private static double DNEAR = .1;
    private static double DFAR = 1000;
    private static double ASPECT_RATIO = ((double)Display.WIDTH) / ((double)Display.HEIGHT);
    private static double SCALE = 1 / Math.tan(Math.toRadians(FOV * 0.5));
    public static double[][] projectionMatrix = Matrix.projectionMatrix(SCALE, ASPECT_RATIO, DFAR, DNEAR);

    // updates the aspect ratio based on the display
    public static void update() {
        ASPECT_RATIO = ((double)Display.WIDTH) / ((double)Display.HEIGHT);
        projectionMatrix = Matrix.projectionMatrix(SCALE, ASPECT_RATIO, DFAR, DNEAR);
    }
}
