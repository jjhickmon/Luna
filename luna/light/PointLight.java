package luna.light;

public class PointLight extends Light{
    public double[] direction;

    public PointLight(double[] direction) {
        this.direction = direction;
    }
}
