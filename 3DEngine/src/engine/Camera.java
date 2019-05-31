package engine;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class Camera implements KeyListener, MouseListener, MouseMotionListener {
	public double xPos, yPos, xDir, yDir, xPlane, yPlane;
	public boolean left, right, forward, back, esc;
	public final double MOVE_SPEED = .08;
	public final double ROTATION_SPEED = .045;
	public double mouseX = 0;
	public Robot robot;
	public Camera(double x, double y, double xd, double yd, double xp, double yp) throws AWTException {
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
		robot = new Robot();
	}
	public void keyPressed(KeyEvent key) {
		//System.out.println("KeyPressed");
		if((key.getKeyCode() == KeyEvent.VK_A))
			left = true;
		if((key.getKeyCode() == KeyEvent.VK_D))
			right = true;
		if((key.getKeyCode() == KeyEvent.VK_W))
			forward = true;
		if((key.getKeyCode() == KeyEvent.VK_S))
			back = true;
		if((key.getKeyCode() == KeyEvent.VK_ESCAPE))
			esc = true;
	}
	public void keyReleased(KeyEvent key) {
		if((key.getKeyCode() == KeyEvent.VK_A))
			left = false;
		if((key.getKeyCode() == KeyEvent.VK_D))
			right = false;
		if((key.getKeyCode() == KeyEvent.VK_W))
			forward = false;
		if((key.getKeyCode() == KeyEvent.VK_S))
			back = false;
		if((key.getKeyCode() == KeyEvent.VK_ESCAPE))
			esc = false;
	}
	public void update(int[][] map) {
		if (!esc) {
			double x = (MouseInfo.getPointerInfo().getLocation().getX() - 1000)/1000.0;
			//System.out.println(x);
			double oldxDir=xDir;
			xDir=xDir*Math.cos(x) - yDir*Math.sin(-x);
			yDir=oldxDir*Math.sin(-x) + yDir*Math.cos(-x);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(-x) - yPlane*Math.sin(-x);
			yPlane=oldxPlane*Math.sin(-x) + yPlane*Math.cos(-x);
			robot.mouseMove(1000, 500);
		}
		if(forward) {
			//System.out.println("Forward");
			if(map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0) {
				xPos+=xDir*MOVE_SPEED;
			}
			if(map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] == 0)
				yPos+=yDir*MOVE_SPEED;
		}
		if(back) {
			//System.out.println("Back");
			if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0)
				xPos-=xDir*MOVE_SPEED;
			if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)]== 0)
				yPos-=yDir*MOVE_SPEED;
		}
		
		
		
		if(left) {
			//System.out.println("Right");
			if(   map[(int)(yPos + xDir * MOVE_SPEED)] [(int)xPos] == 0) {
				yPos+=xDir*MOVE_SPEED;
			}
			if(map[(int)yPos][(int)(xPos + yDir * MOVE_SPEED)] == 0)
				xPos+=yDir*MOVE_SPEED;
		}
		
		
		System.out.println("xDir: " + xDir);
		System.out.println("yDir: " + yDir);
		System.out.println("yPos: " + yPos);
		System.out.println("xPos: " + xPos);
		
		if(right) {
			//System.out.println("Left");
			double oldxDir=xDir;
			xDir=xDir*Math.cos(ROTATION_SPEED) - yDir*Math.sin(ROTATION_SPEED);
			yDir=oldxDir*Math.sin(ROTATION_SPEED) + yDir*Math.cos(ROTATION_SPEED);
			double oldxPlane = xPlane;
			xPlane=xPlane*Math.cos(ROTATION_SPEED) - yPlane*Math.sin(ROTATION_SPEED);
			yPlane=oldxPlane*Math.sin(ROTATION_SPEED) + yPlane*Math.cos(ROTATION_SPEED);
		}
		//System.out.println(xDir);
	}
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent me) {
		//System.out.println("Mouse Moved");
		
	}
}
