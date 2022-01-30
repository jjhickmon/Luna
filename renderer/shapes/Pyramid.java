package renderer.shapes;

import java.awt.Color;

public class Pyramid extends Shape {
    public Color color = Color.WHITE;
    // cube faces numbered accordingly
    // https://selfreconfigurable.com/?p=760
    // every face point starts at the bottom left,
    // then goes around counter-clockwise
    public double[][][] points = {
        {
            {0, 0, 0},
            {1, 0, 0},
            {0.5, 1, 0.5}
        },{
            {1, 0, 0},
            {1, 0, 1},
            {0.5, 1, 0.5}
        },{
            {1, 0, 1},
            {0, 0, 1},
            {0.5, 1, 0.5}
        },{
            {0, 0, 1},
            {0, 0, 0},
            {0.5, 1, 0.5}
        },{
            {0, 0, 0},
            {1, 0, 0},
            {1, 0, 1},
            {0, 0, 1}
        },
    };

    public Pyramid() {
        super.points = this.points;
        super.color = this.color;
        init();
    }

    public Pyramid(double size) {
        super.points = this.points;
        super.color = this.color;
        init(size);
    }

    public Pyramid(double size, double[] translate) {
        super.points = this.points;
        super.color = this.color;
        init(size, translate);
    }
}
