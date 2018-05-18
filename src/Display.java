import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Display extends JPanel implements MouseListener{
	
	public static Dimension windowSize = new Dimension(0,0);
	private Interface target;
	private Map displayMap;
	private ArrayList<Marker> markerArray;
	private Controller control;
	private MarkerList markerList;
	private Graphics g;
	
	private int x = 0;
	private int y = 0;
	public double z = .5;
	
	private static int imageWidth;
	private int imageHeight;
	private double minZ = 0.3;
	private double maxZ = 1.5;
	
	// CURSOR
	public int c_x = 0;
	public int c_y = 0;
	
	// Displays the map and all the markers
	// Handles the transformation of the map
	public Display(Interface target, Map active, double resolution) {
		super();
		this.target = target;
		markerArray = target.getMarkerList();
		displayMap = active;
		displayMap.reload();
		this.setBackground(Color.LIGHT_GRAY);
		windowSize.setSize(displayMap.getImage().getWidth(), displayMap.getImage().getHeight());
		imageWidth = (int) (windowSize.getWidth()*z);
		imageHeight = (int) (windowSize.getHeight()*z);
		addMouseListener(this);
	}
	public static Display createDisplay(Interface target, Map active, double resolution) {
		Display newDisp = new Display(target, active, resolution);
		newDisp.setMarkers(active);
		return newDisp;
	}
	public void setMarkers(Map active) {
		for (int i = 0; i < Loader.markerArray.size(); i++) {
			if (Loader.markerArray.get(i).getMap().getName().equals(active.getName()))
					Loader.markerArray.get(i).setDisplay(this);
		}
	}
	
	public void setController(Controller control) { this.control = control;}
	public void setLocation(int x, int y) { this.x = x; this.y = y;}
	public void setLocation(Point in) { this.x = (int)in.getX(); this.y = (int)in.getY();}
	public void setMarkerList(MarkerList markerList) { this.markerList = markerList; }
	public void setCursor(int c_x, int c_y) {this.c_x = c_x; this.c_y = c_y;}
	public void setImageSize(int width, int height) {imageWidth = width; imageHeight = height;}
	public Dimension getImageSize() {return new Dimension(imageWidth, imageHeight);}
	public Point getLocation() {return new Point(x, y);}
	public Map getDisplayMap() {return displayMap;}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(displayMap.getImage(),x,y,imageWidth,imageHeight,null);
		for (int i = 0; i < filterMarkers(Interface.activeMap).size(); i++) {
			Marker mark = filterMarkers(Interface.activeMap).get(i);
			g.drawImage(mark.getImage(),(int)scaleMarker(mark).getX(),(int)scaleMarker(mark).getY(),mark.getWidth(),mark.getHeight(),null);
		}
		g.drawRect(0,0,(int)windowSize.getWidth(),(int)windowSize.getHeight());
		g.setColor(Color.red);
		g.drawLine(c_x, 0, c_x, (int)windowSize.getHeight());
		g.drawLine(0, c_y, (int)windowSize.getWidth(), c_y);
		this.getParent().repaint();
	}
	public void refresh() {
		this.paintComponent(this.getGraphics());
		this.revalidate();
	}
	public void centerCursor() {
		x = c_x - imageWidth / 2;
		y = c_y - imageHeight / 2;
	}
	
	public boolean translate(int dx, int dy, boolean translateCursor) {
		x += dx;
		y += dy;
		if (translateCursor)
			translateCursor(dx,dy);
		return true;
	}
	public boolean translateCursor(int c_dx, int c_dy) {
		c_x += c_dx;
		c_y += c_dy;
		return true;
	}
	public boolean scale(double dz) {
		if (z < minZ && dz < 0 || z > maxZ && dz > 0)
			return false;			
		z+=dz;
		imageWidth = (int) (windowSize.getWidth()*z);
		imageHeight = (int) (windowSize.getHeight()*z);
		int xAnchor = (int) (windowSize.getWidth() / 2);
		int yAnchor = (int) (windowSize.getHeight() / 2);
		int dx = (int) (dz*(x - xAnchor));
		int dy = (int) (dz*(y - yAnchor));
		translate(dx,dy, false);
		return true;
	}
	public Point scaleMarker(Marker marker) {
		int markX = (int) (x + marker.getX() * imageWidth / windowSize.getWidth() - marker.getImage().getWidth() / 2);
		int markY = (int) (y + marker.getY() * imageHeight / windowSize.getHeight() - marker.getImage().getHeight() / 2);
		return new Point(markX, markY);
	}
	public Point unscaleCursor() {
		int markX = (int) ((c_x - x) * windowSize.getWidth()/imageWidth);
		int markY = (int) ((c_y - y) * windowSize.getHeight()/imageHeight);
		return new Point(markX, markY);
	}
	public boolean isClicked(Marker marker, int x, int y) {
		if (x > marker.getRelX() && x < marker.getRelX() + marker.getWidth() &&
				y > marker.getRelY() && y < marker.getRelY() + marker.getHeight())
			return true;
		return false;
	}
	public ArrayList<Marker> filterMarkers(Map target) {
		ArrayList<Marker> out = new ArrayList<Marker>();
		for (int i = 0; i < markerArray.size(); i++)
			if (markerArray.get(i).getMap().equals(target))
				out.add(markerArray.get(i));
		return out;
	}
	public void showLocation() {
		System.out.print(x + "|");
		System.out.print(y + "|");
		System.out.println(z);
		System.out.println("====");
	}
	public void home() {
		x = 0;
		y = 0;
		z = 0.5;
		imageWidth = (int)(windowSize.width * z);
		imageHeight = (int)(windowSize.height * z);
		refresh();
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		setCursor(e.getX(), e.getY());
		ArrayList<Marker> markers = filterMarkers(displayMap);
		for (int i = 0; i < markers.size(); i++) {
			if(isClicked(markers.get(i), e.getX(), e.getY())) {
				markers.get(i).toggleSelected();
				target.activeMarker = markers.get(i);
				markerList.findActive();
			}
		}
		markerList.refresh();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
