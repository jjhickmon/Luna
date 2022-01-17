package renderer.shapes;

import java.awt.Graphics;
import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import renderer.point.PointConverter;

public class Shape {
    public List<Polygon3D> faces = new ArrayList<>();
    public Color[] colors;
    public double[][][] points;

    public void init() {
        int i = 0;
        for (double[][] face : this.points) {
            this.faces.add(i, new Polygon3D(
                colors[i % colors.length],
                face
            ));
            i++;
        }
    }

    public void init(double size) {
        int i = 0;
        for (double[][] face : this.points) {
            this.faces.add(i, new Polygon3D(
                colors[i % colors.length],
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
                colors[i % colors.length],
                face,
                size,
                translate
            ));
            i++;
        }
    }
    
    public void scale(double scale) {
        for (Polygon3D face : this.faces) {
            for (int i = 0; i < face.points.length; i++) {
                face.points[i] = PointConverter.scalePoint(face.points[i], scale);
            }
        }
    }

    public void rotate(double theta, char axis) {
        for (Polygon3D face : this.faces) {
            for (int i = 0; i < face.points.length; i++) {
                face.points[i] = PointConverter.rotatePoint(face.points[i], theta, axis);
            }
        }
    }

    public void translate(double[] translate) {
        for (Polygon3D face : this.faces) {
            for (int i = 0; i < face.points.length; i++) {
                face.points[i] = PointConverter.translatePoint(face.points[i], translate);
            }
        }
    }

    public void render(Graphics g) {
        // Sorts faces according to the painter's algorithm
        Collections.sort(faces, Collections.reverseOrder(new SortFaces()));
		for (Polygon3D face : faces) {
            if (face != null) {
                face.render(g);
            }
        }
	}
}
