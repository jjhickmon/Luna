package renderer.math;

public class Matrix {
    public static double[][] identityMatrix(int n) {
        return transformationMatrix(n, n);
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
    public static double[][] rotationMatrix (char axis, double theta) {
        if (axis == 'x') {
            return new double[][] {
                {1, 0, 0},
                {0, Math.cos(theta), -Math.sin(theta)},
                {0, Math.sin(theta), Math.cos(theta)}
            };
        } else if (axis == 'y') {
            return new double[][] {
                {Math.cos(theta), 0, Math.sin(theta)},
                {0, 1, 0},
                {-Math.sin(theta), 0, Math.cos(theta)}
            };
        } else if (axis == 'z') {
            return new double[][] {
                {Math.cos(theta), -Math.sin(theta), 0},
                {Math.sin(theta), Math.cos(theta), 0},
                {0, 0, 1}
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

    // normalizes a vector (turns it into a unit vector)
    public static double[] normalize(double[] vector) {
        double[] unitVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            unitVector[i] = vector[i] / Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2) + Math.pow(vector[2], 2));
        }
        return unitVector;
    }

    public static double dotProduct(double[] a, double[] b) {
        double product = 0;
        for (int i = 0; i < a.length; i++) {
            product += a[i] * b[i];
        }
        return product;
    }

    public static double[] crossProduct(double[] a, double[] b) {
        double[] c = new double[3];
        c[0] = a[1] * b[2] - a[2] * b[1];
        c[1] = a[2] * b[0] - a[0] * b[2];
        c[2] = a[0] * b[1] - a[1] * b[0];
        return c;
    }
}
