package renderer.shapes;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import renderer.Display;
import renderer.camera.Camera;
import renderer.math.Matrix;
import renderer.point.Point;
import renderer.vector.Vector;
import renderer.light.Luminance;

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
                face.points[i] = Point.scalePoint(face.points[i], scale);
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

    // public void translate(double[] translate) {
    //     for (Polygon3D face : this.faces) {
    //         for (int i = 0; i < face.points.length; i++) {
    //             face.points[i] = PointConverter.translatePoint(face.points[i], translate);
    //         }
    //     }
    // }

    public void render(Graphics g) {
        List<Polygon3D> projectedFaces = new ArrayList<>();

        // translate into view
        double[] translation = {0, 0, 16};
        // for(int j = 0; j < Camera.location.length; j++) {
        //     translation[j] -= Camera.location[j];
        // }
        double[][] translationMatrix = Matrix.translationMatrix(translation);
        // rotate about the origin
        double[][] rotZ = Matrix.rotationMatrix('z', Display.time * 0.5 % 360);
        double[][] rotY = Matrix.rotationMatrix('y', Display.time % 360);
        // double[][] rotY = Matrix.rotationMatrix('y', 45);
        // double[][] rotX = Matrix.rotationMatrix('x', Display.time % 360);
        // needs to be 4x4 to account for translation and projection
        double[][] worldMatrix = Matrix.identityMatrix(4);
        worldMatrix = Matrix.multiplyMatrices(worldMatrix, rotY);
        worldMatrix = Matrix.multiplyMatrices(translationMatrix, worldMatrix);

		for (Polygon3D poly : faces) {
            Polygon3D polyTransformed = poly.transform(worldMatrix);
            double[][] normal = polyTransformed.getSurfaceNormal();
            double[][] cameraRay = Vector.subtract(Point.toVector(polyTransformed.points[0]), Camera.direction);
            if (Vector.dotProduct(normal, cameraRay) < 0) {
                // Illumination
                double[][] light_direction = Vector.normalize(Point.toVector(Display.light.direction));
                double intensity = Math.max(0.1, Vector.dotProduct(light_direction, normal));
                polyTransformed.color = Luminance.illuminate(polyTransformed.color, intensity);

                // Convert World Space to View Space
                // Figure out matView first

                // Project from 3D to 2D
                Polygon3D polyProjected = polyTransformed.project(Camera.projectionMatrix);
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
