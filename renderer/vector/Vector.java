package renderer.vector;

public class Vector {
     // converts a vector into a point
     public static double[] toPoint(double[][] vector) {
        double[] point = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            point[i] = vector[i][0];
        }
        return point;
    }

    public static double getLength(double[][] vector) {
        double length = Math.sqrt(dotProduct(vector, vector));
        return length;
    }

    public static double[][] add(double[][] a, double[][] b) {
        return new double[][] {
            {a[0][0] + b[0][0]},
            {a[1][0] + b[1][0]},
            {a[2][0] + b[2][0]},
            {1}
        };
    }

    public static double[][] subtract(double[][] a, double[][] b) {
        return new double[][] {
            {a[0][0] - b[0][0]},
            {a[1][0] - b[1][0]},
            {a[2][0] - b[2][0]},
            {1}
        };
    }

    public static double[][] multiply(double[][] a, double scale) {
        return new double[][] {
            {a[0][0] * scale},
            {a[1][0] * scale},
            {a[2][0] * scale},
            {1}
        };
    }

    public static double[][] divide(double[][] a, double scale) {
        return new double[][] {
            {a[0][0] / scale},
            {a[1][0] / scale},
            {a[2][0] / scale},
            {1}
        };
    }

    public static double dotProduct(double[][] a, double[][] b) {
        double product = 0;
        for (int i = 0; i < 3; i++) {
            product += a[i][0] * b[i][0];
        }
        return product;
    }

    public static double[][] crossProduct(double[][] a, double[][] b) {
        double[][] c = new double[4][1];
        c[0][0] = a[1][0] * b[2][0] - a[2][0] * b[1][0];
        c[1][0] = a[2][0] * b[0][0] - a[0][0] * b[2][0];
        c[2][0] = a[0][0] * b[1][0] - a[1][0] * b[0][0];
        c[3][0] = 1;
        return c;
    }

    // normalizes a vector (turns it into a unit vector)
    public static double[][] normalize(double[][] vector) {
        double[][] unitVector = new double[4][1];
        for (int i = 0; i < 3; i++) {
            unitVector[i][0] = vector[i][0] / getLength(vector);
        }
        unitVector[3][0] = 1;
        return unitVector;
    }
}
