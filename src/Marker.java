import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Marker {
	
	final public static Point offset = new Point(20,10);
	final public static Dimension size = new Dimension(300,100);
	
	protected String type = "";
	protected String label = "";
	protected String name = "";
	protected String room = "";
	protected String description = "";
	
	protected String path;
	protected BufferedImage image;
	protected Map map;
	
	private boolean selected = false;
	private boolean visible = true;
	private int width = 10;
	private int height = 10;
	protected Display targetDisplay;
	protected Point coord = new Point();		// Coordinates for relative position on map
	protected Entry entry = new Entry();
	
	public Marker() {
		super();
		entry.setPreferredSize(size);
		entry.setBackground(Color.WHITE);
		entry.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		entry.setTarget(this);
		}
	public Marker(String path, Point coord) {
		this.path = path;
		this.coord = coord;
	}
	public Marker(Marker copy) {
		this.name = new String(copy.name);
		this.type = new String(copy.type);
		this.room = new String(copy.room);
		this.coord = new Point(copy.coord);
		this.entry = new Entry();
		this.entry.setTarget(this);
		this.description = new String(copy.description);
		this.map = copy.map;
		this.targetDisplay = copy.targetDisplay;
		this.path = copy.path;
		this.image = copy.image;
	}
	
	public String getType() {return type;}
	public String getLabel() {return label;}
	public String getName() {return name;}
	public String getRoom() {return room;}
	public String getDescription() {return description;}
	public BufferedImage getImage() {return image;}
	public String getPath() {return path;}
	public Map getMap() { return map; }
	public int getX() {return (int) coord.getX();}
	public int getY() {return (int) coord.getY();}
	public int getRelX() {return (int) targetDisplay.scaleMarker(this).getX();}
	public int getRelY() {return (int) targetDisplay.scaleMarker(this).getY();}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public boolean isSelected() {return selected;}
	public boolean isVisible() {return visible;}
	public Point getLocation() {return coord;}
	
	public void setType(String inType) { type = inType;}
	public void setLabel(String inLabel) { label = inLabel;}
	public void setName(String inName) {name = inName;}
	public void setPath(String inPath) {path = inPath;}
	public void setRoom(String inRoom) {room = inRoom;}
	public void setMap(Map inMap) {map = inMap; }
	public void setVisible(boolean inVisible) {visible = inVisible;}
	public void setSelected(boolean inSelect) {selected = inSelect;}
	public void setDescription(String inDesc) {description = inDesc;}
	public void setImage(BufferedImage src) {image = src;}
	public void setDisplay(Display targetDisplay) {this.targetDisplay = targetDisplay;}
	public void setInterface(Interface targetInterface) {this.entry.setTargetInterface(targetInterface);}
	public boolean setX(int x) {
		coord.setLocation(x, coord.getY());
		return true;
	}
	public boolean setY(int y) {
		coord.setLocation(coord.getX(), y);
		return true;
	}
	public boolean setCoord(Point coord) {
		this.coord = coord;
		return true;
	}
	public boolean setCoord(int x, int y) {
		coord.setLocation(x, y);
		return true;
	}	
	public String relabel() {
		String out = name + "." + room + "."
				+ (String) map.getFloor() + "." + map.getBuilding().getAlias()
				+ "." + Loader.clientAlias + "." + Loader.stateName;
		label = out;
		return out;
	}
	public void select() {selected = true;}
	public void deselect() {selected = false;}
	public boolean toggleSelected() {
		selected = !selected;
		if (selected) {
			entry.getPanel().setBackground(Color.GRAY);
			return true;
		}
		else {
			entry.getPanel().setBackground(Color.WHITE);
			return false;
		}
	}
	public Entry getEntry() {return entry;}
	public void popdown() {entry.setVisible(false);}
	
}
