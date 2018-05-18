/*import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class AP_New extends Marker{
	final public static Pattern TAG = Pattern.compile("<AP_New>(.+?)</AP_New>");

	public AP_New() {
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
	public AP_New(String path) {
		super();
		setPath(path);
		try {
			setImage(ImageIO.read(new File(path)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public AP_New(String path, Point coord) {
		super();
		this.setCoord(coord);
	}
	void popup() {
		// TODO Auto-generated method stub
		
	}
} */
