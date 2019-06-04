package engine;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public int mapWidth;
	public int mapHeight;
	private Thread thread;
	private boolean running;
	public int width;
	public int height;
	private BufferedImage image;
	public int[] pixels;
	public ArrayList<Texture> textures;
	public Camera camera;
	public Screen screen;
	public MazeGen mg = new MazeGen(10);
	public static int[][] map;
	public Game() throws AWTException {
		map = mg.getMaze(4, 10);
		mapWidth = map[0].length;
		mapHeight = map.length;
		String[] strArr = new String[map[0].length];
		
		for (int x = 0; x < map[0].length; x++) {
			String row = "";
			for (int y = 0; y < map.length; y++) {
				row += map[y][x];
			}
			strArr[x] = row;
		}
		for (String s : strArr) {
			System.out.println(s);
		}
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		width = (gd.getDisplayMode().getWidth() - 100);
		height = (gd.getDisplayMode().getHeight() - 50);
		thread = new Thread(this);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		textures = new ArrayList<Texture>();
		textures.add(Texture.wood);
		textures.add(Texture.brick);
		textures.add(Texture.bluestone);
		textures.add(Texture.stone);
		camera = new Camera(1.5, 1.5, 1, 0, 0, -.66);
		screen = new Screen(map, mapWidth, mapHeight, textures, width, height);
		addKeyListener(camera);
		addMouseMotionListener(camera);
		this.addMouseListener(camera);
		setSize(width, height);
		setResizable(false);
		setTitle("3D Engine");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
		getContentPane().setCursor(blankCursor);
		start();
	}
	private synchronized void start() {
		running = true;
		thread.start();
	}
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void render() throws IOException {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		//System.out.println(mapHeight);
		//Draw minimap
		for ( int x = 30; x <= 15 * (mapWidth + 1); x += 15 ) {
			 for ( int y = 30; y <= 15 * (mapHeight + 1); y += 15 ) {
				 if (map[(y/15) - 2][(x/15) - 2] == 0) {
					 g.drawRect( x, y, 15, 15 );
				 } else {
					 g.fillRect(x, y, 15, 15);
				 }
			 }
		}
		g.setColor(Color.RED);
		g.fillOval((int)Math.round((camera.yPos * 15) + 25), (int)Math.round((camera.xPos * 15) + 25), 10, 10);
		//g.drawChars(Double.toString(camera.xPos).toCharArray(), 1, 30, 500, 500);
		//System.out.println("CameraX: " + (int)Math.round(camera.xPos * 30));
		//System.out.println("CameraY: " + (int)Math.round(camera.yPos * 30));
		//System.out.println("fire:" + camera.fire);
		if (!camera.fire) {
			g.drawImage(ImageIO.read(new File("res/rifleStill.png")), (width/2) - 415, height - 382, null);
		} else {
			g.drawImage(ImageIO.read(new File("res/rifleFireBig.png")), (width/2) - 415, height - 497, null);
		}
		
		g.fillRect((width/2) - 1, (height/2) - 6, 2, 12);
		g.fillRect((width/2) - 6, (height/2) - 1, 12, 2);
		bs.show();
	}
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 45.0;//60 times per second
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				screen.update(camera, pixels);
				camera.update(map);
				delta--;
			}
			try {
				render();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//displays to the screen unrestricted time
		}
	}
	public static void main(String [] args) throws AWTException {
		Game game = new Game();
	}
}