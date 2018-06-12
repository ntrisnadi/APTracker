import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Controller extends JPanel{

	private Display targetDisplay;
	final private static int STEP = 100;
	
	private int ddx = 3;			// Acceleration
	private int ddy = 3;
	private int d_dx = 6;			// Deceleration
	private int d_dy = 6;
	private int max_dx = 50;
	private int max_dy = 50;
	
	private int dx = 0;
	private int dy = 0;
	
	private double max_dz = .04;
	private double dz = 0;
	private double ddz = .005;
	private double d_dz = .002;
	
	// Cursor velocity parameters
	private int c_dx = 0;
	private int c_dy = 0;
	private int c_ddx = 6;
	private int c_ddy = 6;
	private int c_d_dx = 3;
	private int c_d_dy = 3;
	private int c_max_dx = 15;
	private int c_max_dy = 15;

	public Controller(Display target) {
		targetDisplay = target;
		
		/* ================================================
		 * 				DEFINE TRANSLATION BUTTONS
		 * ================================================*/
		/*JButton up = new JButton("UP");
		JButton down = new JButton("DOWN");
		JButton left = new JButton("LEFT");
		JButton right = new JButton("RIGHT");
		JButton home = new JButton("HOME");
		
		ActionListener translate = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent action) {
				if (action.getSource() == down)
					targetDisplay.translate(0,-STEP);
				if (action.getSource() == up)
					targetDisplay.translate(0,STEP);
				if (action.getSource() == right)
					targetDisplay.translate(-STEP,0);
				if (action.getSource() == left)
					targetDisplay.translate(STEP, 0);
				if (action.getSource() == home)
					home();
				targetDisplay.repaint();
			}
		};
		up.addActionListener(translate); up.setFocusable(false);
		down.addActionListener(translate); down.setFocusable(false);
		left.addActionListener(translate); left.setFocusable(false);
		right.addActionListener(translate); right.setFocusable(false);
		home.addActionListener(translate); home.setFocusable(false);
		
		Box transButt = Box.createVerticalBox();
		transButt.add(up);
		transButt.add(down);
		transButt.add(left);
		transButt.add(right);
		transButt.add(home);
		
		this.add(transButt);*/
		this.setBackground(Color.GRAY);
		this.setVisible(true);
	}
	
	public Display getTargetDisplay() { return targetDisplay;}
	public void setTargetDisplay(Display inTarget) { targetDisplay = inTarget;}

	public enum DIR {
		UP, DOWN, LEFT, RIGHT, OUT, IN
	}
	public void translate(DIR direction) {
		switch (direction) {
		case UP :
			if (dy > -max_dy)
				dy -= ddy;
			break;
		case DOWN :
			if (dy < max_dy)
				dy += ddy;
			break;
		case LEFT :
			if (dx > -max_dx)
				dx -= ddx;
			break;
		case RIGHT :
			if (dx < max_dx)
				dx += ddx;
			break;
		}
		targetDisplay.translate(dx, dy, true);
	}
	public void translateCursor(DIR direction) {
		switch (direction) {
		case UP :
			if (c_dy > -c_max_dy)
				c_dy -= c_ddy;
			break;
		case DOWN :
			if (c_dy < c_max_dy)
				c_dy += c_ddy;
			break;
		case LEFT :
			if (c_dx > -c_max_dx)
				c_dx -= c_ddx;
			break;
		case RIGHT :
			if (c_dx < c_max_dx)
				c_dx += c_ddx;
			break;
		}
		targetDisplay.translateCursor(c_dx, c_dy);
	}
	public void scale(DIR direction) {
		switch (direction) {
		case OUT:
			if (dz < max_dz)
				dz += ddz;
			break;
		case IN:
			if (dz > -max_dz)
				dz -= ddz;
		}
		targetDisplay.scale(dz);
	}
	public void setVelocity(DIR direction) {
		int initTrans = 30;
		double initZoom = .03;
		switch (direction) {
		case UP :
			dy = -initTrans;
			break;
		case DOWN :
			dy = initTrans;
			break;
		case LEFT :
			dx = -initTrans;
			break;
		case RIGHT :
			dx = initTrans;
			break;
		case OUT :
			dz = initZoom;
			break;
		case IN :
			dz = -initZoom;
		}
	}
	public void setVelocity(DIR direction, int vel) {
		switch (direction) {
		case UP :
			if (vel > max_dy)
				dy = -vel;
			break;
		case DOWN :
			if (vel < max_dy)
				dy = vel;
			break;
		case LEFT :
			if (vel > max_dx)
				dx = -vel;
			break;
		case RIGHT :
			if (vel < max_dx)
				dx = vel;
			break;
		case OUT :
			if (vel < max_dz)
				dz = vel;
			break;
		case IN :
			if (vel > max_dz)
				dz = -vel;
		}
	}
	public void slow() {
		dx = dx - Integer.signum(dx) * d_dx;
		dy = dy - Integer.signum(dy) * d_dy;
		dz = dz - Integer.signum((int) (dz*100)) * d_dz;
		if (dx < 5 && dx > -5)
			dx = 0;
		if (dy < 5 && dy > -5)
			dy = 0;
		if (dz < .01 && dz > -.01)
			dz = 0;
		targetDisplay.translate(dx,dy,true);
		targetDisplay.scale(dz);
	}
	public void slowCursor() {
		c_dx = c_dx - Integer.signum(c_dx) * c_d_dx;
		c_dy = c_dy - Integer.signum(c_dy) * c_d_dy;
		if (c_dx < 5 && c_dx > -5)
			c_dx = 0;
		if (c_dy < 5 && c_dy > -5)
			c_dy = 0;
		targetDisplay.translateCursor(c_dx, c_dy);
	}
	public void home() {
		dz = 0;
		dy = 0;
		dx = 0;
		targetDisplay.home();
	}
	public void stop() {
		dx = 0;
		dy = 0;
		dz = 0;
		targetDisplay.translate(dx,dy,true);
	}
	public boolean isStatic() {
		if (dx == 0 && dy == 0 && dz == 0)
			return true;
		return false;
	}
	public boolean isCursorStatic() {
		if (c_dx == 0 && c_dy == 0)
			return true;
		return false;
	}
	public void getVelocity() {
		System.out.print(dx + "|");
		System.out.print(dy + "|");
		System.out.print(dz + "|");
		System.out.print(c_dx + "|");
		System.out.println(c_dy + "|");
		System.out.println("====");
	}
}
