package RayTracer;

import java.awt.Color;

public interface Surface {
	
	public void intersect(Ray ray);

	public boolean isReflective();

	public boolean isTextured();

	public Color getColor(Vector3D normal);

	public Vector3D getCenter();
}
