package renderer.shapes;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;

import renderer.Display;
import renderer.camera.Camera;
import renderer.point.PointConverter;
import renderer.light.Luminance;
import renderer.math.Matrix;

public class Polygon3D {
	public Color color;
    public double[][] points;
    public double scale = 100; // scale is 100 pixels by default
    
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
            this.points[i] = PointConverter.translatePoint(this.points[i], translate);
        }
    }

    public double[] getSurfaceNormal(double[][] pointsCanonical) {
        double[] line1 = new double[pointsCanonical[0].length];
        for (int i = 0; i < line1.length; i++) {
            line1[i] = pointsCanonical[1][i] - pointsCanonical[0][i];
        }

        double[] line2 = new double[pointsCanonical[0].length];
        for (int j = 0; j < line2.length; j++) {
            line2[j] = pointsCanonical[2][j] - pointsCanonical[0][j];
        }

        double[] unitNormal = Matrix.normalize(Matrix.crossProduct(line1, line2));
        return unitNormal;
    }

    public void render(Graphics g) {
		Polygon poly = new Polygon();
        double[][] pointsCanonical = new double[this.points.length][this.points[0].length];
        // Convert points to canonical coordinates
        for (int i = 0; i < this.points.length; i++) {
            // translate into view
            double[] translation = new double[Camera.location.length];
            for(int j = 0; j < Camera.location.length; j++) {
                translation[j] = -Camera.location[j];
            }
            double[] newPoint = PointConverter.translatePoint(this.points[i], translation);
            // project point
            newPoint = Camera.convertToCanonical(newPoint);
            newPoint = PointConverter.scalePoint(newPoint, this.scale);
            pointsCanonical[i] = newPoint;
        }

        // Get the normal of the plane
        double[] normal = this.getSurfaceNormal(pointsCanonical);

        // Convert from canonical to screen coordinates
        for (double[] point : pointsCanonical) {
            // Convert to screen coordinates
            double[] point2d = PointConverter.convertTo2d(point);
            // flip y axis to convert to canonical coordinates, then translate into view
            point2d[1] = -point2d[1];
            point2d = PointConverter.translatePoint(point2d, new double[]{Display.WIDTH/2.0, Display.HEIGHT/2.0});
            poly.addPoint((int)point2d[0], (int)point2d[1]);
        }

        // The dot product shows how similar two vectors are to each other. If the view line and
        // the normal are not similar at all, then don't drow the plane
        double[] translatedLocation = new double[pointsCanonical[0].length];
        for (int k = 0; k < translatedLocation.length; k++) {
            translatedLocation[k] = pointsCanonical[0][k] - Camera.location[k];
        }
        // Only draw the polygon if it is visible
        if (Matrix.dotProduct(normal, translatedLocation) < 0) {
            g.setColor(Color.WHITE);
            g.drawPolygon(poly);
            // Illumination
            double[] light_direction = Matrix.normalize(Display.light.direction);
            double intensity = Matrix.dotProduct(normal, light_direction);
            g.setColor(Luminance.illuminate(this.color, intensity));
            // g.fillPolygon(poly);
        }
	}
}
