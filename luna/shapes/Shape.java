package luna.shapes;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Collections;
import java.util.List;

import luna.Display;
import luna.camera.Camera;
import luna.light.Luminance;
import luna.math.Matrix;
import luna.point.Point;
import luna.vector.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class Shape {
    public List<Polygon3D> faces = new ArrayList<>();
    public Color color = Color.WHITE;
    public double[][][] points;
    public double[] rotation = {0, 0, 0}; // the amount to rotate on the x, y, and z axes;

    public void init() {
        int i = 0;
        for (double[][] face : this.points) {
            this.faces.add(i, new Polygon3D(
                this.color,
                face
            ));
            i++;
        }
    }

    public void init(double size) {
        int i = 0;
        for (double[][] face : this.points) {
            this.faces.add(i, new Polygon3D(
                this.color,
                face,
                size
            ));
            i++;
        }
    }

    public void init(double size, double[] translate) {
        int i = 0;
        for (double[][] face : this.points) {
            this.faces.add(i, new Polygon3D(
                this.color,
                face,
                size,
                translate
            ));
            i++;
        }
    }

    public void setColor(Color color) {
        this.color = color;
        for (Polygon3D face : this.faces) {
            face.color = this.color;
        }
    }
    
    public void scale(double scale) {
        for (Polygon3D face : this.faces) {
            for (int i = 0; i < face.points.length; i++) {
                face.points[i] = Point.scale(face.points[i], scale);
            }
        }
    }

    // public void rotate(double theta, char axis) {
    //     for (Polygon3D face : this.faces) {
    //         for (int i = 0; i < face.points.length; i++) {
    //             face.points[i] = PointConverter.rotatePoint(face.points[i], theta, axis);
    //         }
    //     }
    // }

    public void translate(double[] translate) {
        for (Polygon3D face : this.faces) {
            for (int i = 0; i < face.points.length; i++) {
                face.points[i] = Point.translate(face.points[i], translate);
            }
        }
    }

    public void render(Graphics g) {
        List<Polygon3D> projectedFaces = new ArrayList<>();

        double[] translation = {0, -1, 16};
        double[][] translationMatrix = Matrix.translationMatrix(translation);
        // rotate about the origin
        double[][] rotY = Matrix.rotationMatrix('y', Display.time % 360);
        // create the world matrix
        double[][] worldMatrix = Matrix.identityMatrix(4);
        worldMatrix = Matrix.multiplyMatrices(rotY, worldMatrix);
        worldMatrix = Matrix.multiplyMatrices(translationMatrix, worldMatrix);
        // create the view matrix
        double[][] target = Vector.add(Camera.location, Camera.direction);
        Camera.cameraMatrix = Matrix.pointAtMatrix(Camera.location, target, Camera.up);
        double[][] viewMatrix = Matrix.lookAtMatrix(Camera.cameraMatrix);

		for (Polygon3D poly : faces) {
            Polygon3D polyTransformed = poly.transform(worldMatrix);
            double[][] normal = polyTransformed.getSurfaceNormal();
            double[][] cameraRay = Vector.subtract(Point.toVector(polyTransformed.points[0]), Camera.direction);
            if (Vector.dotProduct(normal, cameraRay) < 0) {
                // Illumination
                double[][] light_direction = Vector.normalize(Point.toVector(Display.light.direction));
                double intensity = Math.max(0.1, Vector.dotProduct(light_direction, normal));
                polyTransformed.color = Luminance.illuminate(polyTransformed.color, intensity);
                // Convert from world space to view space
                Polygon3D polyViewed = polyTransformed.view(viewMatrix);
                // Project from 3D to 2D
                Polygon3D polyProjected = polyViewed.project(Camera.projectionMatrix);
                // store for sorting
                projectedFaces.add(polyProjected);
            }
        }

        // Sorts faces according to the painter's algorithm
        Collections.sort(projectedFaces, new SortFaces());
        for (Polygon3D poly : projectedFaces) {
            poly.render(g);
        }
	}
}
