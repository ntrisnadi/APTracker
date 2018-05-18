import java.util.regex.Pattern;

public class Tags {
	final public static Pattern REPLACE = Pattern.compile("(.+?)");
	final public static Pattern NAME = Pattern.compile("<name>(.+?)</name>");
	final public static Pattern MAPS_PATH = Pattern.compile("<mapsPath>(.+?)</mapsPath>"); 
	final public static Pattern MARKERS_PATH = Pattern.compile("<markersPath>(.+?)</markersPath>");
	final public static Pattern PATH = Pattern.compile("<path>(.+?)</path>");
	final public static Pattern ALIAS = Pattern.compile("<alias>(.+?)</alias>");
	final public static Pattern MAP = Pattern.compile("<map>(.+?)</map>");
	final public static Pattern MARKER = Pattern.compile("<marker>(.+?)</marker>");
	final public static Pattern TYPE = Pattern.compile("<type>(.+?)</type>");

	final public static Pattern BUILDING = Pattern.compile("<building>(.+?)</building>");
	final public static Pattern STATE = Pattern.compile("<state>(.+?)</state>");
	final public static Pattern CLIENT = Pattern.compile("<client>(.+?)</client>");
	final public static Pattern COORD = Pattern.compile("<coord>(.+?)</coord>");
	final public static Pattern FLOOR = Pattern.compile("<floor>(.+?)</floor>");
	final public static Pattern SIDE = Pattern.compile("<side>(.+?)</side>");
	final public static Pattern LABEL = Pattern.compile("<label>(.+?)</label>");
	final public static Pattern ROOM = Pattern.compile("<room>(.+?)</room>");
	final public static Pattern DESCRIPT = Pattern.compile("<descript>(.+?)</descript>");
}
