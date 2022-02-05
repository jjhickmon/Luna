package luna.shapes;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;
import java.util.Arrays;

import luna.Display;
import luna.camera.Camera;
import luna.light.Luminance;
import luna.math.Matrix;
import luna.point.Point;
import luna.vector.Vector;

public class Polygon3D {
	public Color color;
    public double[][] points;
    public double scale = 1; // scale is 1 pixel by default

    public Polygon3D (Color color, double[][] points) {
        this.color = color;
        this.points = points;
    }

    public Polygon3D (Color color, double[][] points, double scale) {
        this.color = color;
        this.points = points;
        this.scale = scale;
    }

    public Polygon3D (Color color, double[][] points, double scale, double[] translate) {
        this.color = color;
        this.points = points;
        this.scale = scale;
        for (int i = 0; i < this.points.length; i++) {
            this.points[i] = Point.translate(this.points[i], translate);
        }
    }

    public double[][] getSurfaceNormal() {
        double[][] line1 = Vector.subtract(Point.toVector(this.points[1]), Point.toVector(this.points[0]));
        double[][] line2 = Vector.subtract(Point.toVector(this.points[2]), Point.toVector(this.points[0]));
        double[][] unitNormal = Vector.normalize(Vector.crossProduct(line1, line2));
        return unitNormal;
    }

    public Polygon3D transform(double[][] worldMatrix) {
        return this.convert(worldMatrix);
    }

    public Polygon3D view(double[][] viewMatrix) {
        return this.convert(viewMatrix);
    }

    public Polygon3D project(double[][] projectionMatrix) {
        Polygon3D polyProjected = this.convert(projectionMatrix);;
        // Convert points to 2D space
        double[] offset = {1, 1, 0};
        for (int i = 0; i < this.points.length; i++) {
            if (polyProjected.points[i][3] != 0) {
                polyProjected.points[i] = Vector.toPoint(Vector.divide(Point.toVector(polyProjected.points[i]), polyProjected.points[i][3]));
            }

            // X/Y are inverted so put them back
            polyProjected.points[i][0] = -polyProjected.points[i][0];
            polyProjected.points[i][1] = -polyProjected.points[i][1];

            polyProjected.points[i] = Vector.toPoint(Vector.add(Point.toVector(polyProjected.points[i]), Point.toVector(offset)));// scale
            polyProjected.points[i] = Point.scale(polyProjected.points[i], this.scale);
            
            // zoom kinda
            polyProjected.points[i][0] *= 0.5 * Display.WIDTH;
            polyProjected.points[i][1] *= 0.5 * Display.HEIGHT;
        }

        return polyProjected;
    }

    /*
     * Converts a polygon from one space to another using the provided matrix
     */
    private Polygon3D convert(double[][] newSpace) {
        Polygon3D newPoly = new Polygon3D(this.color, new double[this.points.length][3]);
        for (int i = 0; i < this.points.length; i++) {
            newPoly.points[i] = Vector.toPoint(Matrix.multiplyMatrices(newSpace, Point.toVector(this.points[i])));
        }
        return newPoly;
    }

    public void render(Graphics g) {
		Polygon poly = new Polygon();

        for (double[] point : points) {
            poly.addPoint((int)point[0], (int)point[1]);
        }

        if (Camera.WIREFRAME) {
            g.setColor(Color.WHITE);
            g.drawPolygon(poly);
        } else {
            g.setColor(this.color);
            g.fillPolygon(poly);
        }
	}
}
