package renderer.shapes;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Mesh extends Shape{
    public ArrayList<ArrayList<Double[]>> mesh = new ArrayList<>();
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

        while (read.hasNextLine()){
            String line = read.nextLine();
            if (line.isEmpty()) {continue;}
            if (line.charAt(0) == 'v' && line.charAt(1) == ' '){
                String[] points = line.substring(2).split(" ");
                Double[] vertex = new Double[points.length];
                for (int i = 0; i < points.length; i++) {
                    vertex[i] = Double.valueOf(Double.parseDouble(points[i]));
                }
                vertices.add(vertex);
            }
            if (line.charAt(0) == 'f' && line.charAt(1) == ' '){
                ArrayList<Double[]> face = new ArrayList<>();
                String[] faceVertices = line.substring(2).split(" ");
                for (int j = 0; j < faceVertices.length; j++) {
                    int index;
                    if (faceVertices[j].contains("/")) {
                        index = Integer.parseInt(faceVertices[j].substring(0, faceVertices[j].indexOf("/"))) - 1;
                    } else {
                        index = Integer.parseInt(faceVertices[j]) - 1;
                    }
                    
                    face.add(vertices.get(index));
                }
                this.mesh.add(face);
            }
        }
        read.close();

        this.points = new double[this.mesh.size()][this.mesh.get(0).size()][this.mesh.get(0).get(0).length];
        for (int i = 0; i < this.mesh.size(); i++){
            for (int j = 0; j < this.mesh.get(0).size(); j++){
                for (int k = 0; k < this.mesh.get(0).get(0).length; k++){
                    this.points[i][j][k] = this.mesh.get(i).get(j)[k];
                }
            }
        }
    }
}
