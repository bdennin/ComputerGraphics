package RayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Disc implements Surface {

	private static Color radialColor;

	private Vector3D center;
	private Vector3D normal;
	private double innerRadius;
	private double outerRadius;
	private boolean isTextured;
	private boolean isReflective;
	private BufferedImage texture;

	public Disc(Vector3D center, Vector3D p1, Vector3D p2, double innerRadius, double outerRadius, String imagePath) {
		this.center = center;
		this.normal = p1.subtract(center).cross(p2.subtract(center)).normalize();
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
		return radialColor;
	}

	@Override
	public void intersect(Ray ray) {

		double normalDirection = ray.getDirection().dot(this.normal);
		if(normalDirection == 0) {

		}	
		else {

			double t = this.normal.dot(this.center.subtract(ray.getOrigin()))/normalDirection;		
			Vector3D hitPoint = ray.getRayAt(t);
			double radialOffset = this.center.getMagnitude(hitPoint);

			if(radialOffset <= this.outerRadius && radialOffset > this.innerRadius && t < ray.getT() && t > 0.01) {
				int x = (int)(radialOffset/outerRadius * (this.texture.getWidth() - 1));
				radialColor = new Color(this.texture.getRGB(x, 1));

				if(radialColor.getRed() > 75) {
					ray.setT(t);
					ray.setSurface(this);
				}
			}
		}
	}
}
