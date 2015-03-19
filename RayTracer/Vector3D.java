package RayTracer;

import java.awt.Color;

public class Vector3D {

	private double x;
	private double y;
	private double z;

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getMagnitude() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public double getMagnitudeSquared() {
		return (x * x + y * y + z * z);
	}

	public Vector3D add(Vector3D other) {
		double a = this.x + other.x;
		double b = this.y + other.y;
		double c = this.z + other.z;
		return new Vector3D(a, b, c);
	}

	public Vector3D add(double x, double y, double z) {
		double a = this.x + x;
		double b = this.y + y;
		double c = this.z + z;
		return new Vector3D(a, b, c);
	}

	public Vector3D subtract(Vector3D other) {
		double a = this.x - other.x;
		double b = this.y - other.y;
		double c = this.z - other.z;
		return new Vector3D(a, b, c);
	}

	public Vector3D subtract(double x, double y, double z) {
		double a = this.x - x;
		double b = this.y - y;
		double c = this.z - z;
		return new Vector3D(a, b, c);
	}

	public Vector3D multiply(Vector3D other) {
		double a = this.x * other.x;
		double b = this.y * other.y;
		double c = this.z * other.z;
		return new Vector3D(a, b, c);
	}


	public Vector3D multiply(double scalar) {
		double a = this.x * scalar;
		double b = this.y * scalar;
		double c = this.z * scalar;
		return new Vector3D(a, b, c);
	}

	public double dot(Vector3D other) {
		return this.x * other.x 
				+ this.y * other.y 
				+ this.z * other.z;
	}
	
	public double dot(double x, double y, double z) {
		return this.x * x 
				+ this.y * y 
				+ this.z * z;
	}
	
	public Vector3D normalize() {
		double scalar = this.dot(this);

		if(scalar != 0 && scalar != 1)
			scalar = 1/Math.sqrt(scalar);

		return this.multiply(scalar); 
	}

	public Color toColor() {
		return new Color((float)(x + 1)/2, (float)(y + 1)/2, (float)(z + 1)/2);
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	@Override
	public String toString() {
		return String.format("(%f, %f, %f)", this.x, this.y, this.z);
	}
}