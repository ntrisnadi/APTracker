import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class Loader {
	
	public static String mapsPath;
	public static String markersPath;
	private static Matcher matcher;
	public static ArrayList<Building> buildingArray = new ArrayList<Building>();
	public static ArrayList<Map> mapArray = new ArrayList<Map>();
	public static ArrayList<Marker> markerArray = new ArrayList<Marker>();
	public static ArrayList<Marker> markerTypes = new ArrayList<Marker>();
	public static String projectName;
	public static String clientName;
	public static String clientAlias;
	public static String stateName;

	public ArrayList<Map> getMapArray() { return mapArray; }
	public ArrayList<Marker> getMarkerArray() {	return markerArray;	}
	public ArrayList<Marker> getMarkerTypes() { return markerTypes; }
	public String getProjectName() { return projectName;}
	public static String getProjectInfo() {
		String out = "Project Name: \t" + projectName;
		out = out + "\nClient Name: \t" + clientName;
		out = out + "\nClient Alias: \t" + clientAlias;
		out = out + "\nClient State: \t" + stateName;
		return out;
	}
	
	public Loader(File src, String corePath) {
		load(src, corePath);
	}
	@SuppressWarnings("resource")
	public static boolean load(File src, String corePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(src));
			String inData = null;
			// =======PARSE PROJECT FILE=========
			while((inData = br.readLine()) != null) {
				if (inData.contains(getOpenTag(Tags.MAPS_PATH))) 
					mapsPath = corePath + deTag(inData, Tags.MAPS_PATH);
				if (inData.contains(getOpenTag(Tags.MARKERS_PATH))) 
					markersPath = corePath + deTag(inData, Tags.MARKERS_PATH);
				if (inData.contains(getOpenTag(Tags.NAME)))
					projectName = deTag(inData,Tags.NAME);
				if (inData.contains(getOpenTag(Tags.CLIENT)))
					while (!(inData = br.readLine()).contains(getCloseTag(Tags.CLIENT))) {
						if (inData.contains(getOpenTag(Tags.NAME)))
							clientName = deTag(inData,Tags.NAME);
						if (inData.contains(getOpenTag(Tags.ALIAS)))
							clientAlias = deTag(inData,Tags.ALIAS);
						if (inData.contains(getOpenTag(Tags.STATE)))
							stateName = deTag(inData, Tags.STATE);
					}
			}
			br.close();

			// ===========PARSE MAPS FILE===========
			br = new BufferedReader(new FileReader(new File(mapsPath)));
			mapArray  = new ArrayList<Map>();
			while ((inData = br.readLine()) != null) {
				if (inData.contains(getOpenTag(Tags.BUILDING))) {
					Building inBuilding = new Building();
					while (!(inData = br.readLine()).contains(getCloseTag(Tags.BUILDING))) {
						if (inData.contains(getOpenTag(Tags.NAME)))
							inBuilding.setName(deTag(inData,Tags.NAME));
						if (inData.contains(getOpenTag(Tags.ALIAS)))
							inBuilding.setAlias(deTag(inData,Tags.ALIAS));
					}
					buildingArray.add(inBuilding);
				}
				if (inData.contains(getOpenTag(Tags.MAP))) {
					Map inMap = new Map();
					while (!(inData = br.readLine()).contains(getCloseTag(Tags.MAP))) {
						if (inData.contains(getOpenTag(Tags.PATH))) {
							inMap.setPath(corePath + deTag(inData, Tags.PATH));
							try {
								inMap.setImage(ImageIO.read(new File(inMap.getPath())));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						// =================================
						// Define specific tag searches here
						// =================================
						if (inData.contains(getOpenTag(Tags.NAME))) {
							inMap.setName(deTag(inData,Tags.NAME));
						}
						if (inData.contains(getOpenTag(Tags.BUILDING))) {
							for (int i = 0; i < buildingArray.size(); i++) {
								if (buildingArray.get(i).getName().equals(deTag(inData,Tags.BUILDING)))
									inMap.setBuilding(buildingArray.get(i));
							}
						}
						if (inData.contains(getOpenTag(Tags.FLOOR))) {
							inMap.setFloor(deTag(inData,Tags.FLOOR));
						}
					}
					if(inMap.isDefined())
						mapArray.add(inMap);
					else
						System.out.println("ERROR:Invalid Map Definition");
				}

			}
			br.close();
			
			// =========PARSE MARKERS FILE=========
			br = new BufferedReader(new FileReader(new File(markersPath)));
			Marker out;
			// Read for marker definitions
			while ((inData = br.readLine()) != null) {
				if (inData.contains(getOpenTag(Tags.MARKER))) {
					Marker outMark = new Marker();
					while (!(inData = br.readLine()).contains(getCloseTag(Tags.MARKER))) {
						if (inData.contains(getOpenTag(Tags.TYPE))) {
							outMark.setName(deTag(inData,Tags.TYPE));
							outMark.setType(deTag(inData,Tags.TYPE));
							outMark.getEntry().setTextLabel(outMark.getType());
						}
						if (inData.contains(getOpenTag(Tags.PATH))) {
							outMark.setPath(corePath + deTag(inData, Tags.PATH));
							try {
								outMark.setImage(ImageIO.read(new File(outMark.getPath())));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					markerTypes.add(outMark);
				}
			}

			br = new BufferedReader(new FileReader(new File(markersPath)));
			while ((inData = br.readLine()) != null) {
				if (markerDefined(markerTypes,inData)) {
					markerArray.add(defineMarker(inData, br));
				}
			}
			
			// =========FINISH INITIALIZING
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String getOpenTag(Pattern src) {
		return src.pattern().substring(0,src.pattern().indexOf('('));
	}
	public static String getCloseTag(Pattern src) {
		return src.pattern().substring(src.pattern().indexOf("</"));
	}
	public static String deTag(String src, Pattern regex) {
		if (src.contains(getOpenTag(regex) + getCloseTag(regex)))
			return "";
		matcher = regex.matcher(src);
		matcher.find();
		return matcher.group(1);
	}
	public static boolean markerDefined(ArrayList<Marker> markerTypes, String tag) {
		if (tag.isEmpty())
			return false;
		for (int i = 0; i < markerTypes.size(); i++)
			if (tag.substring(1,tag.length()-1).equals(markerTypes.get(i).getType()))
				return true;
		return false;
	}
	public static Marker defineMarker(String tag, BufferedReader br) throws Exception {
		Marker outMark = findMarker(new Marker(), tag.substring(1,tag.length()-1));
		
		String inData = "";
		
		// =================================
		// Define specific tag searches here
		// =================================
		while (!(inData = br.readLine()).contains(tag.substring(0, 1) + "/" + tag.substring(1))) {
			if (inData.contains(getOpenTag(Tags.MAP))) {
				String mapName = deTag(inData, Tags.MAP);
				for (int i = 0; i < mapArray.size(); i++) {
					if (mapName.equals(mapArray.get(i).getName()))
							outMark.setMap(mapArray.get(i));
				}
			}
			if (inData.contains(getOpenTag(Tags.COORD))) {
				String in = deTag(inData,Tags.COORD);
				Point coord = new Point(Integer.parseInt(in.split(",")[0]),Integer.parseInt(in.split(",")[1]));
				outMark.setCoord(coord);
			}
			if (inData.contains(getOpenTag(Tags.DESCRIPT))) 
				outMark.setDescription(deTag(inData,Tags.DESCRIPT));
			if (inData.contains(getOpenTag(Tags.NAME))) 
				outMark.setName(deTag(inData,Tags.NAME));
			if (inData.contains(getOpenTag(Tags.ROOM))) 
				outMark.setRoom(deTag(inData, Tags.ROOM));
			if (inData.contains(getOpenTag(Tags.SIDE))) 
				outMark.setRoom(deTag(inData, Tags.SIDE));
		}
		outMark.relabel();
		return outMark;
	}
	public static Marker findMarker(Marker in, String tag) {
		for (int i = 0; i < markerTypes.size(); i++) {
			if (tag.contains(markerTypes.get(i).getType())) {
				in.setType(tag);
				in.setPath(markerTypes.get(i).getPath());
				try {
					in.setImage(ImageIO.read(new File(in.getPath())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return in;
	}
}
