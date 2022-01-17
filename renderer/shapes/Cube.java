package renderer.shapes;

import java.awt.Color;

public class Cube extends Shape {
    private Color[] colors = {
        Color.red,
        Color.green,
        Color.yellow,
        Color.white,
        Color.blue,
        Color.orange
    };
    // cube faces numbered accordingly
    // https://selfreconfigurable.com/?p=760
    // every face point starts at the bottom left,
    // then goes around counter-clockwise
    public double[][][] points = {
        {
            {0, 0, 0},
            {1, 0, 0},
            {1, 1, 0},
            {0, 1, 0}
        },{
            {1, 0, 0},
            {1, 0, 1},
            {1, 1, 1},
            {1, 1, 0}
        },{
            {0, 0, 1},
            {1, 0, 1},
            {1, 0, 0},
            {0, 0, 0}
        },{
            {0, 1, 0},
            {1, 1, 0},
            {1, 1, 1},
            {0, 1, 1}
        },{
            {0, 0, 1},
            {0, 0, 0},
            {0, 1, 0},
            {0, 1, 1}
        },{
            {1, 0, 1},
            {0, 0, 1},
            {0, 1, 1},
            {1, 1, 1}
        },
    };

    public Cube() {
        super.points = this.points;
        super.colors = this.colors;
        init();
    }

    public Cube(double size) {
        super.points = this.points;
        super.colors = this.colors;
        init(size);
    }

    public Cube(double size, double[] translate) {
        super.points = this.points;
        super.colors = this.colors;
        init(size, translate);
    }
}
