package luna.math;

import java.util.Arrays;

import luna.point.Point;
import luna.vector.Vector;

public class Matrix {
    public static double[][] identityMatrix(int n) {
        return transformationMatrix(n, n);
    }

    /*
     * Creates and returns the matrix to make the camera point at a target
     */
    public static double[][] pointAtMatrix(double[][] position, double[][] target, double[][] world_up) {
        double[][] forward = Vector.normalize(Vector.subtract(target, position));
        // the rotated up direction
        double[][] rot_up = Vector.multiply(forward, Vector.dotProduct(world_up, forward));
        double[][] up = Vector.subtract(world_up, rot_up);
        double[][] right = Vector.crossProduct(up, forward);
        double[][] pointAtMatrix = {
            {right[0][0], up[0][0], forward[0][0], position[0][0]},
            {right[1][0], up[1][0], forward[1][0], position[1][0]},
            {right[2][0], up[2][0], forward[2][0], position[2][0]},
            {0, 0, 0, 1}
        };
        return pointAtMatrix;
    }

    /*
     * The view matrix is just the inverse of the pointAtMatrix,
     * This method is just a shorthand method of finding the inverse of a given pointAt matrix
     */
    public static double[][] lookAtMatrix(double[][] pointAt) {
        double[][] lookAtMatrix = {
            {pointAt[0][0], pointAt[1][0], pointAt[2][0], -(pointAt[0][3] * pointAt[0][0] + pointAt[1][3] * pointAt[1][0] + pointAt[2][3] * pointAt[2][0])},
            {pointAt[0][1], pointAt[1][1], pointAt[2][1], -(pointAt[0][3] * pointAt[0][1] + pointAt[1][3] * pointAt[1][1] + pointAt[2][3] * pointAt[2][1])},
            {pointAt[0][2], pointAt[1][2], pointAt[2][2], -(pointAt[0][3] * pointAt[0][2] + pointAt[1][3] * pointAt[1][2] + pointAt[2][3] * pointAt[2][2])},
            {0, 0, 0, 1}
        };
        return lookAtMatrix;
    }

    public static double[][] projectionMatrix(double SCALE, double ASPECT_RATIO, double DFAR, double DNEAR) {
        return new double[][] {
            {SCALE / ASPECT_RATIO, 0, 0, 0},
            {0, SCALE, 0, 0},
            {0, 0, (DFAR + DNEAR) / (DNEAR - DFAR), (-2 * DFAR * DNEAR) / (DFAR - DNEAR)},
            {0, 0, 1, 0},
        };
    }

    // n is the dimension of the output, m is the dimension of the input
    // n is the number of rows, m is the number of columns
    public static double[][] transformationMatrix(int n, int m) {
        double[][] transform = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j == i) {
                    transform[i][j] = 1;
                }
            }
        }
        return transform;
    }

    public static double[][] translationMatrix(double[] translation) {
        return new double[][] {
            {1, 0, 0, translation[0]},
            {0, 1, 0, translation[1]},
            {0, 0, 1, translation[2]},
            {0, 0, 0, 1}
        };
    }

    public static double[][] rotationMatrix (char axis, double theta) {
        theta = Math.toRadians(theta);
        if (axis == 'x') {
            return new double[][] {
                {1, 0, 0, 0},
                {0, Math.cos(theta), -Math.sin(theta), 0},
                {0, Math.sin(theta), Math.cos(theta), 0},
                {0, 0, 0, 1}
            };
        } else if (axis == 'y') {
            return new double[][] {
                {Math.cos(theta), 0, Math.sin(theta), 0},
                {0, 1, 0, 0},
                {-Math.sin(theta), 0, Math.cos(theta), 0},
                {0, 0, 0, 1}
            };
        } else if (axis == 'z') {
            return new double[][] {
                {Math.cos(theta), -Math.sin(theta), 0, 0},
                {Math.sin(theta), Math.cos(theta), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
            };
        }
        return null;
    }

    public static double[][] multiplyMatrices(double[][] a, double[][] b) {
        int r1 = a.length;
        int r2 = b.length;
        int c1 = a[0].length;
        int c2 = b[0].length;
        double[][] product = new double[r1][c2];
        if (c1 != r2) {
            throw new IllegalArgumentException("a:m : " + c1 + " did not match b:n " + r2 + ".");
        }

        for(int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    product[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return product;
    }

    public static String toString(double[][] matrix) {
        String str = "";
        for (int i = 0; i < matrix.length; i++) {
            str += Arrays.toString(matrix[i]) + "\n";
        }
        return str;
    }
}
