import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Saver {

	private ArrayList<Map> maps;
	private ArrayList<Marker> markers;
	private ArrayList<Marker> types;
	private ArrayList<Building> buildings;
	private String projectPath;
	private String mapsPath;
	private String markersPath;
	private String corePath;
	
	public Saver(File src, String fileName) {
		projectPath = src.getAbsolutePath();
		corePath = projectPath.substring(0,src.getAbsolutePath().indexOf(fileName));
		System.out.println(corePath);
		mapsPath = corePath + fileName.substring(0, fileName.indexOf('.'))	+ "_maps" + fileName.substring(fileName.indexOf('.'));
		markersPath = corePath + fileName.substring(0, fileName.indexOf('.'))	+ "_markers" + fileName.substring(fileName.indexOf('.'));
		maps = Loader.mapArray;
		markers = Loader.markerArray;
		types = Loader.markerTypes;
		buildings = Loader.buildingArray;
		try {
			
		BufferedWriter bw = new BufferedWriter(new FileWriter(src));
		
		// WRITE PROJECT FILE
		compileProj(bw);
		bw.close();
		
		
		// WRITE MAPS FILE
		bw = new BufferedWriter(new FileWriter(new File(mapsPath)));
		for (int i = 0; i < buildings.size(); i++) {
			compileBuilding(bw, buildings.get(i));
			for (int j = 0; j < maps.size(); j++) {
				if (buildings.get(i).equals(maps.get(j).getBuilding()))
					compileMap(bw, maps.get(j));
			}
		}
		bw.close();
		
		// WRITE MARKERS
		bw = new BufferedWriter( new FileWriter(new File(markersPath)));
		bw.write("========MARKER DEFS========");		bw.newLine();
		for (int i = 0; i < types.size(); i++) {
			bw.write(Loader.getOpenTag(Tags.MARKER));											bw.newLine();
			bw.write("\t" + applyTag(Tags.TYPE, types.get(i).getType()));						bw.newLine();
			bw.write("\t" + applyTag(Tags.PATH, removeCorePath(types.get(i).getPath())));		bw.newLine();
			bw.write(Loader.getCloseTag(Tags.MARKER));											bw.newLine();
			
		}
		bw.newLine();  bw.write("========MARKER DATA=========");  bw.newLine();
		for (int i = 0; i < buildings.size(); i++) {
			for (int j = 0; j < markers.size(); j++) {
				if (buildings.get(i).equals(markers.get(j).getMap().getBuilding()))
					compileMarker(bw, markers.get(j));
			}
		}
		bw.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String applyTag(Pattern regex, String src) {
		return regex.toString().replace(Tags.REPLACE.toString(),src);
	}
	public void compileProj(BufferedWriter bw) {
		try {
			bw.write(applyTag(Tags.NAME,Loader.projectName));			bw.newLine();
			bw.write(applyTag(Tags.MAPS_PATH, removeCorePath(mapsPath)));	 		bw.newLine();
			bw.write(applyTag(Tags.MARKERS_PATH, removeCorePath(markersPath)));		bw.newLine();
			bw.write(Loader.getOpenTag(Tags.CLIENT));					bw.newLine();
			bw.write("\t" + applyTag(Tags.NAME, Loader.clientName));	bw.newLine();
			bw.write("\t" + applyTag(Tags.ALIAS, Loader.clientAlias));	bw.newLine();
			bw.write("\t" + applyTag(Tags.STATE, Loader.stateName));	bw.newLine();
			bw.write(Loader.getCloseTag(Tags.CLIENT));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void compileBuilding(BufferedWriter bw, Building src) {
		try {
			bw.write(Loader.getOpenTag(Tags.BUILDING));					bw.newLine();
			bw.write("\t" + applyTag(Tags.NAME, src.getName()));		bw.newLine();
			bw.write("\t" + applyTag(Tags.ALIAS, src.getAlias()));		bw.newLine();
			bw.write(Loader.getCloseTag(Tags.BUILDING));				bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void compileMap(BufferedWriter bw, Map src) {
		try {
			bw.write(Loader.getOpenTag(Tags.MAP));										bw.newLine();
			bw.write("\t" + applyTag(Tags.NAME, src.getName()));						bw.newLine();
			bw.write("\t" + applyTag(Tags.PATH, removeCorePath(src.getPath())));		bw.newLine();
			bw.write("\t" + applyTag(Tags.BUILDING, src.getBuilding().getName()));		bw.newLine();
			bw.write("\t" + applyTag(Tags.FLOOR, src.getFloor()));						bw.newLine();
			bw.write(Loader.getCloseTag(Tags.MAP));										bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void compileMarker(BufferedWriter bw, Marker src) {
		try {
			bw.write("<" + src.getType() + ">");						bw.newLine();
			bw.write("\t" + applyTag(Tags.LABEL, src.getLabel()));		bw.newLine();
			bw.write("\t" + applyTag(Tags.NAME, src.getName()));		bw.newLine();
			bw.write("\t" + applyTag(Tags.ROOM, src.getRoom()));		bw.newLine();
			bw.write("\t" + applyTag(Tags.MAP, src.getMap().getName()));			bw.newLine();
			bw.write("\t" + applyTag(Tags.COORD, src.getX() + "," + src.getY()));	bw.newLine();
			bw.write("\t" + applyTag(Tags.DESCRIPT, src.getDescription()));			bw.newLine();
			bw.write("</" + src.getType() + ">");			bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String removeCorePath(String src) {
		return src.substring(corePath.length());
	}
}
