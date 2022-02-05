package luna;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

import luna.camera.Camera;
import luna.light.PointLight;
import luna.shapes.Cube;
import luna.shapes.Mesh;
import luna.shapes.Pyramid;
import luna.shapes.Shape;

import java.io.File;
import java.io.FileNotFoundException;

/*
 * Citation for creating Display template:
 * https://www.youtube.com/watch?v=KA6rJZOfTTQ&list=PLgRPwj3No0VLXFoqYnL2aYhczXB2qwKvp&index=2
 */

public class Display extends Canvas implements Runnable {
	private static String title = "Luna";
	private static boolean running = false;
	public static int WIDTH = 512; // not final because I want to allow for window resizing
	public static int HEIGHT = 512;
	private static final double FPS = 60.0;

	private Thread thread;
	private JFrame frame;

	private Shape cube;
	private Shape pyramid;
	private Mesh mesh;
	public static PointLight light = new PointLight(new double[] { 0, 0, -1 });
	public static int time = 0;

	public Display() {
		this.frame = new JFrame();
		Dimension size = new Dimension(WIDTH, HEIGHT);
		this.setPreferredSize(size);
        this.frame.setTitle(title);
		this.frame.add(this);
		this.frame.pack();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(true);
		this.frame.setVisible(true);
	}

	public synchronized void start() {
		running = true;
		this.thread = new Thread(this, "Display");
		this.thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void quit() {
		System.exit(0);
	}

	@Override
	public void run() {
		long start_time = System.currentTimeMillis();
		// can get rid of frames and timer once fps is set
		int frames = 0;
		long timer = System.currentTimeMillis();

		try {
			init();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (running) {
			this.frame.addComponentListener(new ComponentAdapter() {  
				public void componentResized(ComponentEvent evt) {
					WIDTH = frame.getWidth();
					HEIGHT = frame.getHeight();
					Camera.update();
				}
			});

            this.frame.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) { System.out.println( "tester"); }
            
                public void keyReleased(KeyEvent e) { System.out.println("2test2"); }
            
                public void keyTyped(KeyEvent e) { System.out.println("3test3"); }
            });

			render();

			// can get rid of this block once fps is set
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				this.frame.setTitle(title + ": " + frames + " fps");
				frames = 0;
			}

			// set framerate
			double totalTime = (System.currentTimeMillis() - start_time) / 1000.0;
			if (totalTime < (1 / FPS)) {
				try {
					Thread.sleep((long) (((1 / FPS) - totalTime) * 1000.0));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			start_time = System.currentTimeMillis();
		}
		stop();
	}

	private void init() throws FileNotFoundException {
		// +y
		// |--- +x
		// +z/
		this.cube = new Cube();
		// this.cube.translate(new double[] {0, -1.5, 0});

		// this.pyramid = new Pyramid();
		// this.pyramid.translate(new double[] {-2.5, -.5, -4.5});

		String filePath = "axis.obj";
		File cubeMesh = new File("luna/src/" + filePath);
		this.mesh = new Mesh(cubeMesh);
		// this.mesh.rotate(Math.toRadians(30), 'y');
		// this.mesh.rotate(Math.toRadians(20), 'x');
		this.mesh.scale(.50);
		// this.mesh.translate(new double[]{0, 0, 0});
	}

	private void render() {
		BufferStrategy b = this.getBufferStrategy();
		if (b == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = b.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// this.cube.rotate(Math.toRadians(.5), 'x');
		// this.cube.rotate(Math.toRadians(1), 'y');
		// this.cube.render(g);

		// this.pyramid.rotate(Math.toRadians(1), 'y');
		// this.pyramid.render(g);

		// this.mesh.rotate(Math.toRadians(.5), 'x');
		// this.mesh.rotate(Math.toRadians(1), 'y');
		this.mesh.render(g);
		// time++;

		g.dispose();
		b.show();
	}
}
