package renderer.shapes;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.util.Arrays;

public class Mesh extends Shape{
    public ArrayList<Double[][]> mesh = new ArrayList<>();
    public double[][][] points;
    public Color color;

    public Mesh(File objFile) throws FileNotFoundException {
        this.load(objFile);
        super.points = this.points;
        init();
    }

    public Mesh(File objFile, double size) throws FileNotFoundException {
        this.load(objFile);
        super.points = this.points;
        init(size);
    }

    public Mesh(File objFile, double size, double[] translate) throws FileNotFoundException {
        this.load(objFile);
        super.points = this.points;
        init(size, translate);
    }

    public void load(File objFile) throws FileNotFoundException, NumberFormatException{
        ArrayList<Double[]> vertices = new ArrayList<>();
        Scanner read = new Scanner(objFile);
        // create my variables up here, then reuse them so I don't run out of memory while parsing
        String line;
        String[] points = new String[3]; // triangles are used in .obj files
        Double[] vertex = new Double[points.length];
        while (read.hasNextLine()){
            line = read.nextLine();
            // creates vertices then adds them a list for future use
            if (line.isEmpty()) {continue;}
            if (line.charAt(0) == 'v' && line.charAt(1) == ' '){
                points = line.substring(2).split(" ");
                vertex = new Double[points.length];
                for (int i = 0; i < vertex.length; i++) {
                    vertex[i] = Double.valueOf(Double.parseDouble(points[i]));
                }
                vertices.add(vertex);
            }

            // creates polygons consisting of the vertices that the face tag specifies
            if (line.charAt(0) == 'f' && line.charAt(1) == ' '){
                String[] faceVertices = line.substring(2).split(" ");
                // faces can consist of a variable number of vertices, so I have to create a new one each time
                Double[][] poly = new Double[faceVertices.length][vertex.length];
                for (int i = 0; i < poly.length; i++) {
                    int index;
                    if (faceVertices[i].contains("/")) {
                        index = Integer.parseInt(faceVertices[i].substring(0, faceVertices[i].indexOf("/"))) - 1;
                    } else {
                        index = Integer.parseInt(faceVertices[i]) - 1;
                    }
                    poly[i] = vertices.get(index);
                }
                this.mesh.add(poly);
            }
        }
        read.close();

        // each face can have a variable number of vertices, so I will create it dynamically
        this.points = new double[this.mesh.size()][][];
        // goes through every polygon in the mesh
        for (int i = 0; i < this.mesh.size(); i++){
            this.points[i] = new double[this.mesh.get(i).length][];
            // goes through every point in the polygon
            for (int j = 0; j < this.points[i].length; j++){
                this.points[i][j] = new double[points.length];
                // goes through every component of the point
                for (int k = 0; k < this.points[i][j].length; k++){
                    this.points[i][j][k] = this.mesh.get(i)[j][k];
                }
            }
        }
    }
}
