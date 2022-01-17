package renderer;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import java.io.File;
import java.io.FileNotFoundException;

import renderer.shapes.Shape;
import renderer.shapes.Cube;
import renderer.shapes.Mesh;
import renderer.shapes.Pyramid;
import renderer.light.PointLight;

/*
 * Citation for creting Display template:
 * https://www.youtube.com/watch?v=KA6rJZOfTTQ&list=PLgRPwj3No0VLXFoqYnL2aYhczXB2qwKvp&index=2
 */

public class Display extends Canvas implements Runnable{
	private static String title = "Luna";
	private static boolean running = false;
	public static final int WIDTH = 512;
	public static final int HEIGHT = 512;
	private static final double FPS = 60.0;

	private Thread thread;
	private JFrame frame;

	private Shape cube;
    private Shape pyramid;
    private Mesh mesh;
    public static PointLight light = new PointLight(new double[]{0, 0, 1});

	public Display(){
		this.frame  = new JFrame();
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setPreferredSize(size);
	}

	public static void main(String[] args){
		Display display = new Display();
		display.frame.setTitle(title);
		display.frame.add(display);
		display.frame.pack();
		display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.frame.setLocationRelativeTo(null);
		display.frame.setResizable(false);
		display.frame.setVisible(true);
		display.start();
	}

	public synchronized void start(){
		running = true;
		this.thread = new Thread(this, "Display");
		this.thread.start();
	}

	public synchronized void stop(){
		running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void quit(){
		System.exit(0);
	}

	@Override
	public void run(){
		long start_time = System.currentTimeMillis();
		// can get rid of frames and timer once fps is set
		int frames = 0;
		long timer = System.currentTimeMillis();

        try {
		    init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

		while(running){
			update();
			render();

			// can get rid of this block once fps is set
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				this.frame.setTitle(title + ": " + frames + " fps");
				frames = 0;
			}

			// set framerate
			double totalTime = (System.currentTimeMillis() - start_time)/1000.0;
			if(totalTime < (1/FPS)){
				try {
					Thread.sleep((long)(((1/FPS) - totalTime)*1000.0));
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			start_time = System.currentTimeMillis();
		}
		stop();
	}

    private void init() throws FileNotFoundException {
        //   +y
        //    |--- +x 
        // +z/
		this.cube = new Cube();
        this.cube.translate(new double[] {0, -1.5, 0});

        this.pyramid = new Pyramid();
        this.pyramid.translate(new double[] {-2.5, -.5, -4.5});

        String filePath = "textured.obj";
        File cubeMesh = new File("renderer/src/" + filePath);
        this.mesh = new Mesh(cubeMesh);
        // this.mesh.rotate(Math.toRadians(30), 'y');
        // this.mesh.rotate(Math.toRadians(20), 'x');
        this.mesh.scale(20);
        this.mesh.translate(new double[]{0, 0, 0});
	}

	private void render(){
		BufferStrategy b = this.getBufferStrategy();
		if(b == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = b.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

        this.cube.rotate(Math.toRadians(.5), 'x');
        this.cube.rotate(Math.toRadians(1), 'y');
        this.cube.render(g);

        // this.pyramid.rotate(Math.toRadians(1), 'y');
        // this.pyramid.render(g); 

        // this.mesh.rotate(Math.toRadians(.5), 'x');
        // this.mesh.rotate(Math.toRadians(1), 'y');
        // this.mesh.render(g);

		g.dispose();
		b.show();
	}

	private void update(){

	}
}
