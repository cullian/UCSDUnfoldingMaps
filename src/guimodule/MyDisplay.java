package guimodule;

import processing.core.PApplet;

public class MyDisplay extends PApplet {

	/**
	 * required by PApplet
	 */
	private static final long serialVersionUID = 1L;

	public void setup() {
		size(400, 400);
		background(51, 204, 51);
	}

	public void draw() {
		fill(255,255,0);
		ellipse(200, 200, 390, 390);
		fill(0,0,0);
		ellipse(120, 130, 50, 70);
		ellipse(280, 130, 50, 70);
		noFill();
		strokeWeight(4);
		arc(200,280,150,75,0.3f,PI - 0.3f);
	}
}
