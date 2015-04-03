package RayTracer;

import java.awt.Color;

public class Orb extends Sphere implements Surface, Light {

	public Orb(Vector3D center, double radius) {
		super(center, radius, Color.WHITE);
	}
	
	public Orb(Vector3D center, double radius, String imagePath) {
		super(center, radius, imagePath);
	}
 }
