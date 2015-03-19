package RayTracer;

import java.awt.Color;

public class Sphere implements Surface {

	protected Vector3D center;
	protected double radius;
	protected double radiusSquared;
	private Color color;
	private boolean reflective;
	
	public Sphere(Vector3D center, double radius, Color color) {
		this.center = center;
		this.radius = radius;
		this.radiusSquared = this.radius * this.radius;
		this.color = color;
		this.reflective = false;
	}

	public Sphere(Vector3D center, double radius, boolean reflective) {
		this.center = center;
		this.radius = radius;
		this.radiusSquared = this.radius * this.radius;
		this.reflective = reflective;
	}
	
	public boolean isReflective() {
		return this.reflective;
	}

	public Color getColor() {
		return this.color;
	}

	public Vector3D getCenter() {
		return this.center;
	}
	
	public double getRadius() {
		return this.radius;
	}

	public void intersect(Ray ray) {
		Vector3D negativeCenter = ray.getOrigin().subtract(center);

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
