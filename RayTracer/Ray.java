package RayTracer;

public class Ray {

	private Vector3D origin;
	private Vector3D direction;
	private double t;
	private Sphere sphere;
	
	public Ray(Vector3D origin, Vector3D direction) {
		this.origin = origin;
		this.direction = direction;
		this.t = Double.MAX_VALUE;
	}
	
	public Vector3D getRayAtT() {
		return this.origin.add(this.direction.multiply(this.t));
	}
	
	public Vector3D getRayAt(double t) {
		return this.origin.add(this.direction.multiply(t));
	}
	
	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public Sphere getSphere() {
		return this.sphere;
	}

	public void setSphere(Sphere sphere) {
		this.sphere = sphere;
	}

	public Vector3D getOrigin() {
		return origin;
	}
	
	public Vector3D getDirection() {
		return direction;
	}
}
