/*import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AP_Rep extends Marker{
	final public static Pattern TAG = Pattern.compile("<AP_Rep>(.+?)</AP_Rep>");
	
	private Dimension size = new Dimension(200,20);

	public AP_Rep() {
		super();
		String path = "";
		for (int i = 0; i < Loader.markerTypes.size(); i++) {
			if (Loader.markerTypes.get(i).getClass().getName().equals(getClass().getName()))
				path = Loader.markerTypes.get(i).getPath();
		}
		
		try {
			setImage(ImageIO.read(new File(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public AP_Rep(String path) {
		super();
		this.path = path;
		setPath(path);
		try {
			setImage(ImageIO.read(new File(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public AP_Rep(String path, Point coord) {
		super();
		this.setCoord(coord);
		this.setPath(path);
	}
	
	@Override
	void popup() {
	}
	
}*/
