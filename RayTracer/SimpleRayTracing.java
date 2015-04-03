package RayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SimpleRayTracing {

	private static final Vector3D ORIGIN = new Vector3D(0, -12, -15);
	private static final BufferedImage FRAME = new BufferedImage(1256, 1256, BufferedImage.TYPE_INT_RGB);
	private static final ArrayList<Surface> SURFACES = initializeSurfaces();
	private static final ArrayList<Light> LIGHTS = initializeLights();
	private static final double X_VIEWING_ANGLE = 0;
	private static final double Y_VIEWING_ANGLE = 0;
	private static final double Z_VIEWING_ANGLE = 0;
	private static final int TOTAL_SURFACES = SURFACES.size(); 
	private static final int LIGHT_ITERATIONS = 100;

	private static BufferedImage backgroundTexture;

	public static void main(String[] args) throws IOException {

		backgroundTexture = ImageIO.read(new File("stars.png"));
		double startTime = System.currentTimeMillis();
		int y = FRAME.getHeight();
		int x = FRAME.getWidth();

		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {

				double a = (-1 + 2 * (double)i/x);
				double b = (1 - 2 * (double)j/y);
				double c = -1;

				Vector3D direction = new Vector3D(a, b, c);
				Ray ray = new Ray(ORIGIN, direction);
				Color pixelColor = determinePixelColor(SURFACES, ray);
				FRAME.setRGB(i, j,  pixelColor.getRGB());
			}
		}

		ImageIO.write(FRAME, "png", new File("/Users/jimbob/Downloads/sphere.png"));
		double endTime = System.currentTimeMillis();
		double totalTime = endTime - startTime;
		System.out.println("Execution completed in " + totalTime + "ms.");
	}

	private static Color determinePixelColor(ArrayList<Surface> surfaces, Ray ray) {

		Color pixelColor;

		if(hasIntersect(surfaces, ray)) {

			Vector3D hitPoint = ray.getRayAtT();
			Vector3D normal = hitPoint.subtract(ray.getSphere().getCenter()).normalize();
			Sphere intersectedObject = ray.getSphere();

			if(intersectedObject.isReflective()) {

				Vector3D direction = hitPoint.subtract(normal.multiply(2).multiply(hitPoint.dot(normal)));
				Vector3D complementaryVector = hitPoint.add(direction);
				Sphere angleVariance = new Sphere(complementaryVector, (float)0.1, Color.WHITE);
				Vector3D angle = angleVariance.generateRandomPoint();
				direction = angle.subtract(hitPoint).normalize();
				ray = new Ray(hitPoint, direction);	
				pixelColor = determinePixelColor(surfaces, ray);
			}
			else {

				pixelColor = intersectedObject.getColor(normal);

				if(intersectedObject instanceof Light) {

				}
				else if(intersectedObject.isTextured()) {
					pixelColor = normalShade(pixelColor, hitPoint);
				}
				else {
					
					hitPoint = ray.getRayAtT();
					normal = hitPoint.subtract(ray.getSphere().getCenter()).normalize();
					Vector3D direction = hitPoint.add(normal.multiply(2));
					Sphere angle = new Sphere(direction, normal.getMagnitude(), Color.WHITE);
					Vector3D randomPoint = angle.generateRandomPoint();
					direction = randomPoint.subtract(hitPoint).normalize();
					ray = new Ray(hitPoint, direction);	

					Color diffuseColor = determinePixelColor(surfaces, ray);
					
					pixelColor =  new Color((diffuseColor.getRed() + pixelColor.getRed())/2, (diffuseColor.getGreen() + pixelColor.getGreen())/2, (diffuseColor.getBlue() + pixelColor.getBlue())/2);
					pixelColor = normalShade(pixelColor, hitPoint);
				}
			}
		}
		else {
			Vector3D direction = ray.getDirection();
			int x = (int)((direction.getX() + 1)/2 * FRAME.getWidth());
			int y = (int)((direction.getY() + 1)/2 * FRAME.getHeight());
			pixelColor = new Color(backgroundTexture.getRGB(x % backgroundTexture.getWidth(), y % backgroundTexture.getHeight()));
		}
		return pixelColor;
	}

	private static Color normalShade(Color original, Vector3D hitPoint) {

		double maxLight = 0;
		int length = LIGHTS.size();

		for(int i = 0; i < length; i++) {

			Light light = LIGHTS.get(i);
			double temp = 0;
			double radius = light.getRadius();

			for(int j = 0; j < LIGHT_ITERATIONS; j++) {

				Vector3D direction;
				do {
					direction = light.generateRandomPoint().subtract(hitPoint).normalize();
				}
				while(direction.getMagnitude() > radius);

				Ray lightDetectionRay = new Ray(hitPoint, direction);

				if(hasIntersect(SURFACES, lightDetectionRay) && lightDetectionRay.getSphere() == light) {
					temp++;
				}
			}
			temp = temp/LIGHT_ITERATIONS;
			if(temp > maxLight) {
				maxLight = temp;
			}
		}

		float[] colorComponents = new float[3];
		original.getColorComponents(colorComponents);

		for(int i = 0; i < 3; i++) {
			colorComponents[i] = (float)(colorComponents[i] * maxLight);
		}

		return new Color(colorComponents[0], colorComponents[1], colorComponents[2]);
	}

	private static boolean hasIntersect(ArrayList<Surface> surfaces, Ray ray) {

		for(int i = 0; i < TOTAL_SURFACES; i++) {
			surfaces.get(i).intersect(ray);
		}

		return (ray.getT() < Double.MAX_VALUE);
	}

	private static ArrayList<Surface> initializeSurfaces() {

		ArrayList<Surface> surfaces = new ArrayList<Surface>();
		surfaces.add(new Orb(new Vector3D(0, 50, 0), 3));
		//surfaces.add(new Orb(new Vector3D(0, -3.5, 0), 3, "sun.jpg"));
		//		surfaces.add(new Sphere(new Vector3D(-4, -4, -8), .25, "mercury.png"));
		//		surfaces.add(new Sphere(new Vector3D(-13, -6, -15), .55, "venus.jpg"));
		//		surfaces.add(new Sphere(new Vector3D(0, -4, -20), 1.5, "earth.jpg"));
		//		surfaces.add(new Sphere(new Vector3D(-.5, -4.8, -17), .33, "moon.jpg"));
		//		surfaces.add(new Sphere(new Vector3D(22, -2, -26), 1, "mars.jpg"));
		//		surfaces.add(new Sphere(new Vector3D(-10, 10, -14), 1, "jupiter.jpg"));
		surfaces.add(new Sphere(new Vector3D(0, -115, -25), 98.5, Color.WHITE));
		surfaces.add(new Sphere(new Vector3D(-5, -14, -22), 1, Color.BLUE));
		surfaces.add(new Sphere(new Vector3D(-2, -15, -23), 1, Color.RED));
		surfaces.add(new Sphere(new Vector3D(-5, -10, -22), 1, true));
		surfaces.add(new Sphere(new Vector3D(8, -16, -18), 1, true));


		return surfaces;
	}

	private static ArrayList<Light> initializeLights() {

		ArrayList<Light> lights = new ArrayList<Light>();
		for(Surface el : SURFACES) {
			if(el instanceof Light)
				lights.add((Light)el);
		}

		return lights;
	}
}