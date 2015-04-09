package RayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Disc implements Surface {

	private Vector3D center;
	private Vector3D point;
	private Vector3D normal;
	private double innerRadius;
	private double outerRadius;
	private Color color;
	private boolean isTextured;
	private boolean isReflective;
	private BufferedImage texture;
	
	public Disc(Vector3D center, Vector3D point, double innerRadius, double outerRadius, Color color) {
		this.center = center;
		this.point = point;
		this.normal = center.cross(point);
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
		this.color = color;
		this.isReflective = false;
		this.isTextured = false;
	}
	
	public Disc(Vector3D center, Vector3D point, double innerRadius, double outerRadius, String imagePath) {
		this.center = center;
		this.point = point;
		this.normal = center.cross(point).normalize();
		this.innerRadius = innerRadius;
		this.outerRadius = outerRadius;
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
	
	@Override
	public boolean isReflective() {
		return this.isReflective;
	}
	
	@Override
	public Vector3D getCenter() {
		return this.center;
	}
	
	public double getInnerRadius() {
		return this.innerRadius;
	}

	public double getOuterRadius() {
		return this.outerRadius;
	}
	
	@Override
	public Color getColor(Vector3D hitPoint) {
		
		System.out.println(hitPoint.getX());
		if(this.isTextured()) {
			
			double u = ((Math.atan2(hitPoint.getX(), hitPoint.getZ())/(2*Math.PI)) + .5);
			double v = (.5 - Math.asin(hitPoint.getY())/Math.PI);
			int x = (int)(u * (this.texture.getWidth() - 1));
			int y = (int)(v * (this.texture.getHeight() - 1));
			return new Color(this.texture.getRGB(x, y));
		}
		else
			return this.color;
	}

	@Override
	public void intersect(Ray ray) {
	
		double normalDirection = ray.getDirection().dot(this.normal);
		if(normalDirection == 0) {
			
		}	
		else {
			
			double t = (this.normal.dot(ray.getRayAt(5))/normalDirection);		
			Vector3D hitPoint = ray.getRayAtT();
			
			double magnitude = this.center.subtract(hitPoint).getMagnitude();
//			
			//System.out.println(magnitude);
			if(magnitude < this.outerRadius && magnitude > this.innerRadius && t < ray.getT() && t > 0.01) {
				System.out.println("this never happens");
				ray.setT(t);
				ray.setSurface(this);
			}
		}
	}
}
