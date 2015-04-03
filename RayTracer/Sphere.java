package RayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sphere implements Surface {

	protected Vector3D center;
	protected double radius;
	protected double radiusSquared;
	private Color color;
	private boolean isReflective;
	private boolean isTextured;
	private BufferedImage texture;
	
	public Sphere(Vector3D center, double radius, Color color) {
		this.center = center;
		this.radius = radius;
		this.radiusSquared = this.radius * this.radius;
		this.color = color;
		this.isReflective = false;
		this.isTextured = false;
	}

	public Sphere(Vector3D center, double radius, boolean isReflective) {
		this.center = center;
		this.radius = radius;
		this.radiusSquared = this.radius * this.radius;
		this.isReflective = isReflective;
		this.isTextured = false;
	}
	
	public Sphere(Vector3D center, double radius, String imagePath) {
		this.center = center;
		this.radius = radius;
		this.radiusSquared = this.radius * this.radius;
		this.isReflective = false;
		this.isTextured = true;
		try {
			this.texture = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isTextured() {
		return this.isTextured;
	}
	
	public boolean isReflective() {
		return this.isReflective;
	}

	public Color getColor(Vector3D normalPoint) {
		
		if(this.isTextured) {
			
			double u = ((Math.atan2(normalPoint.getX(), normalPoint.getZ())/(2*Math.PI)) + .5);
			double v = (.5 - Math.asin(normalPoint.getY())/Math.PI);
			int x = (int)(u * (this.texture.getWidth() - 1));
			int y = (int)(v * (this.texture.getHeight() - 1));
			return new Color(this.texture.getRGB(x, y));
		}
		else
			return this.color;
	}

	public Vector3D getCenter() {
		return this.center;
	}
	
	public double getRadius() {
		return this.radius;
	}

	public void intersect(Ray ray) {
		if(this.isTextured) 
		{
			
		}
		Vector3D negativeCenter = ray.getOrigin().subtract(this.center);

		double a = ray.getDirection().dot(ray.getDirection());
		double b = 2 * ray.getDirection().dot(negativeCenter);
		double c = negativeCenter.dot(negativeCenter) - radiusSquared;
		double discriminant = b * b - 4 * a * c;

		if (discriminant >= 0) {
			
			double discrimRoot = Math.sqrt(discriminant);
			double divisor = 1/(2 * a);
			double d = (-b - discrimRoot) * divisor;
			if(d <= .01)
				d = (-b + discrimRoot) * divisor;
			
			if(d < ray.getT() && d > 0.01) {
				ray.setT(d);
				ray.setSphere(this);
			}	
		}	
	}
	
	public Vector3D generateRandomPoint() {
		
		Vector3D center = this.getCenter();
		double x = center.getX() + ((Math.random() * 2) - 1) * radius;
		double y = center.getY() + ((Math.random() * 2) - 1) * radius;
		double z = center.getZ() + ((Math.random() * 2) - 1) * radius;

		return new Vector3D(x, y, z);	
	}
	
	public String toString() {
		return String.format("Radius: %s Center: %s", radius, center);
	}
}
