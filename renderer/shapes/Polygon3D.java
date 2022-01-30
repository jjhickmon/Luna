package renderer.shapes;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;

import renderer.Display;
import renderer.camera.Camera;
import renderer.point.Point;
import renderer.vector.Vector;
import renderer.light.Luminance;
import renderer.math.Matrix;

import java.util.Arrays;

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
            this.points[i] = Point.translatePoint(this.points[i], translate);
        }
    }

    public double[][] getSurfaceNormal() {
        // double[][] line1 = Vector.subtract(Point.toVector(this.points[1]), Point.toVector(this.points[0]));
        // double[][] line2 = Vector.subtract(Point.toVector(this.points[2]), Point.toVector(this.points[0]));
        // double[][] unitNormal = Vector.normalize(Vector.crossProduct(line1, line2));
        // return unitNormal;

        double[] line1 = new double[this.points[0].length];
        for (int i = 0; i < line1.length; i++) {
            line1[i] = this.points[1][i] - this.points[0][i];
        }

        double[] line2 = new double[this.points[0].length];
        for (int j = 0; j < line2.length; j++) {
            line2[j] = this.points[2][j] - this.points[0][j];
        }

        double[][] unitNormal = Vector.normalize(Vector.crossProduct(Point.toVector(line1), Point.toVector(line2)));
        return unitNormal;
    }

    public Polygon3D transform(double[][] worldMatrix) {
        Polygon3D polyTransformed = new Polygon3D(this.color, new double[this.points.length][this.points[0].length]);
        for (int i = 0; i < this.points.length; i++) {
            polyTransformed.points[i] = Vector.toPoint(Matrix.multiplyMatrices(worldMatrix, Point.toVector(this.points[i])));
        }
        return polyTransformed;
    }

    public Polygon3D project(double[][] projectionMatrix) {
        Polygon3D polyProjected = new Polygon3D(this.color, new double[this.points.length][this.points[0].length]);

        // Convert points to 2D space
        double[] offset = {1, 1, 0};
        for (int i = 0; i < 3; i++) {
            polyProjected.points[i] = Vector.toPoint(Matrix.multiplyMatrices(projectionMatrix, Point.toVector(this.points[i])));
            // I might not need this check anymore
            if (polyProjected.points[i][3] != 0) {
                polyProjected.points[i] = Vector.toPoint(Vector.divide(Point.toVector(polyProjected.points[i]), polyProjected.points[i][3]));
            }
            
            // X/Y are inverted so put them back
            polyProjected.points[i][0] = -polyProjected.points[i][0];
            polyProjected.points[i][1] = -polyProjected.points[i][1];

            polyProjected.points[i] = Vector.toPoint(Vector.add(Point.toVector(polyProjected.points[i]), Point.toVector(offset)));// scale
            polyProjected.points[i] = Point.scalePoint(polyProjected.points[i], this.scale);
            
            // zoom kinda
            polyProjected.points[i][0] *= 0.5 * Display.WIDTH;
            polyProjected.points[i][1] *= 0.5 * Display.HEIGHT;
            // newPoint = Camera.convertToCanonical(newPoint);
            // newPoint = PointConverter.scalePoint(newPoint, this.scale);
            // pointsCanonical[i] = newPoint;
        }

        // for (int i = 0; i < 3; i++) {
        //     // Uninvert the x and y coordinates
        //     polyProjected.points[i][0] = -polyProjected.points[i][0];
        //     polyProjected.points[i][1] = -polyProjected.points[i][1];

        //     polyProjected.points[i] = Vector.toPoint(Vector.add(Point.toVector(polyProjected.points[i]), Point.toVector(offset))); 
        //     polyProjected.points[i][0] *= Display.WIDTH * 0.5;
        //     polyProjected.points[i][1] *= Display.HEIGHT * 0.5;
        // }

        return polyProjected;
    }

    public void render(Graphics g) {
		Polygon poly = new Polygon();

        for (double[] point : points) {
            poly.addPoint((int)point[0], (int)point[1]);
        }

        g.setColor(this.color);
        g.fillPolygon(poly);
        // g.setColor(Color.BLACK);
        // g.drawPolygon(poly);
        

        // // Convert from canonical to screen coordinates
        // for (double[] point : points) {
        //     // Convert to screen coordinates
        //     double[] point2d = Point.convertTo2d(point);
        //     // flip y axis to convert to canonical coordinates, then translate into view
        //     point2d[1] = -point2d[1];
        //     point2d = Point.translatePoint(point2d, new double[]{Display.WIDTH/2.0, Display.HEIGHT/2.0});
        //     poly.addPoint((int)point2d[0], (int)point2d[1]);
        // }

        // The dot product shows how similar two vectors are to each other. If the view line and
        // the normal are not similar at all, then don't drow the plane
        

        // // Only draw the polygon if it is visible
        // if (Matrix.dotProduct(normal, translatedLocation) < 0) {
        //     g.setColor(Color.WHITE);
        //     g.drawPolygon(poly);
        //     g.setColor(this.color);
        //     // g.fillPolygon(poly);
        // }
	}
}
