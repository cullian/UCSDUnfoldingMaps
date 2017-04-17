package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.List;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/**
 * EarthquakeCityMap An application with an interactive map displaying
 * earthquake data. Author: UC San Diego Intermediate Software Development MOOC
 * team
 * 
 * @author William Cullian Date: March 31, 2017
 */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this. It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;

	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/**
	 * This is where to find the local tiles, for working without an Internet
	 * connection
	 */
	public static String mbTilesString = "blankLight-1-3.mbtiles";

	// The map
	private UnfoldingMap map;

	// feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	public void setup() {
		size(950, 600, OPENGL);

		if (offline) {
			map = new UnfoldingMap(this, 200, 50, 700, 500, new MBTilesMapProvider(mbTilesString));
			earthquakesURL = "2.5_week.atom"; // Same feed, saved Aug 7, 2015,
												// for working offline
		} else {
			// map = new UnfoldingMap(this, 200, 50, 700, 500, new
			// Google.GoogleMapProvider());
			map = new UnfoldingMap(this, 200, 50, 700, 500, new Microsoft.AerialProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			// earthquakesURL = "2.5_week.atom";
		}
		// to get the map to pan and zoom correctly
		// I had to zoom to 1 the setRectangularPanningRestriction
		// to the corners at zoom 1, then zoom to 2 and set restriction
		// to zoom 2 - 10
		// this seems to work best, without trails
		map.zoomToLevel(1);
		map.setRectangularPanningRestriction(map.getTopLeftBorder(), map.getBottomRightBorder());
		;
		map.zoomToLevel(2);
		map.setZoomRange(2, 10);
		MapUtils.createDefaultEventDispatcher(this, map);

		// The List you will populate with new SimplePointMarkers
		List<Marker> markers = new ArrayList<Marker>();

		// Use provided parser to collect properties for each earthquake
		// PointFeatures have a getLocation method
		List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);

		// These print statements show you (1) all of the relevant properties
		// in the features, and (2) how to get one property and use it
		if (earthquakes.size() > 0) {
			PointFeature f = earthquakes.get(0);
			System.out.println(f.getProperties());
			getMagnatude(f);
		}

		for (PointFeature feature : earthquakes) {
			SimplePointMarker marker = createMarker(feature);
			markers.add(marker);
		}
		map.addMarkers(markers);
	}

	private float getMagnatude(PointFeature feature) {
		Object magObj = feature.getProperty("magnitude");
		float mag = Float.parseFloat(magObj.toString());
		return mag;
	}

	// A suggested helper method that takes in an earthquake feature and
	// returns a SimplePointMarker for that earthquake
	private SimplePointMarker createMarker(PointFeature feature) {
		// Here is an example of how to use Processing's color method to
		// generate
		// an int that represents the color blue.
		int blue = color(0, 0, 255);
		// an int that represents the color yellow.
		int yellow = color(255, 255, 0);
		// an int that represents the color red.
		int red = color(255, 0, 0);
		SimplePointMarker marker = new SimplePointMarker(feature.getLocation());
		float mag = getMagnatude(feature);
		if (mag < THRESHOLD_LIGHT) {
			marker.setColor(blue);
			marker.setRadius(4);
		} else if (mag >= THRESHOLD_LIGHT && mag < THRESHOLD_MODERATE) {
			marker.setColor(yellow);
			marker.setRadius(7);
		} else if (mag >= THRESHOLD_MODERATE) {
			marker.setColor(red);
			marker.setRadius(10);
		}
		return marker;
	}

	public void draw() {
		background(13);
		map.draw();
		addKey();
	}

	// helper method to draw key in GUI
		private void addKey() {
		// Remember you can use Processing's graphics methods here
		fill(240);
		rect(25, 50, 150, 250, 7);

		fill(0);
		textAlign(LEFT, CENTER);
		textSize(10);
		text("Earthquake Key", 50, 75);

		fill(color(255, 0, 0));
		ellipse(50, 125, 10, 10);
		fill(color(255, 255, 0));
		ellipse(50, 175, 7, 7);
		fill(color(0, 0, 255));
		ellipse(50, 225, 4, 4);

		fill(0, 0, 0);
		text("5.0+ Magnitude", 75, 125);
		text("4.0+ Magnitude", 75, 175);
		text("Below 4.0", 75, 225);

	}
}