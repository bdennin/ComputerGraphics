package RayTracer;

public interface Light {

	public double getRadius();
	
	public Vector3D getCenter();
	
	public Vector3D generateRandomPoint();
	
}
