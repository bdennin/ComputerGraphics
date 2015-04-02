package RayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SimpleRayTracing {

	private static final Vector3D ORIGIN = new Vector3D(0, 0, 0);
	private static final BufferedImage FRAME = new BufferedImage(1256, 1256, BufferedImage.TYPE_INT_RGB);
	private static final ArrayList<Surface> SURFACES = initializeSurfaces();
	private static final ArrayList<Light> LIGHTS = initializeLights();
	private static final int TOTAL_SURFACES = SURFACES.size(); 
	private static final int LIGHT_ITERATIONS = 100;

	private static int counter = 0;

	public static void main(String[] args) throws IOException {

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
				Color pixelColor = determinePixelColor(SURFACES, ray, b);
				FRAME.setRGB(i, j,  pixelColor.getRGB());
			}
		}

		ImageIO.write(FRAME, "png", new File("/Users/jimbob/Downloads/sphere.png"));
		double endTime = System.currentTimeMillis();
		double totalTime = endTime - startTime;
		System.out.println("Execution completed in " + totalTime + "ms.");
	}

	private static Color determinePixelColor(ArrayList<Surface> surfaces, Ray ray, double b) {

		Color pixelColor;

		if(hasIntersect(surfaces, ray)) {

			Vector3D hitPoint = ray.getRayAtT();

			if(ray.getSphere().isReflective()) {

				Vector3D normal = hitPoint.subtract(ray.getSphere().getCenter());
				Vector3D direction = hitPoint.subtract(normal.multiply(2).multiply(hitPoint.dot(normal)));
				Vector3D complementaryVector = hitPoint.add(direction);
				Sphere angleVariance = new Sphere(complementaryVector, (float)0.1, new Color((float)1.0, (float)1.0, (float)1.0));
				Vector3D angle = angleVariance.generateRandomPoint();
				direction = angle.subtract(hitPoint).normalize();
				ray = new Ray(hitPoint, direction);	
				pixelColor = determinePixelColor(surfaces, ray, ray.getDirection().getY());
			}
			else {

				pixelColor = ray.getSphere().getColor();
				Color sphereColor = ray.getSphere().getColor();

				if(ray.getSphere() instanceof Light) {

				}
				else {

					hitPoint = ray.getRayAtT();
					Vector3D normal = hitPoint.subtract(ray.getSphere().getCenter());
					Vector3D direction = hitPoint.add(normal.multiply(2));
					Sphere angle = new Sphere(direction, normal.getMagnitude(), new Color((float)1, (float)1, (float)1));
					Vector3D randomPoint = angle.generateRandomPoint();
					direction = randomPoint.subtract(hitPoint).normalize();
					ray = new Ray(hitPoint, direction);	

					pixelColor = determinePixelColor(surfaces, ray, ray.getDirection().getY());
					
					float[] colorComponents = new float[3];
					float[] sphereComponents = new float[3];
				//	pixelColor.
					pixelColor.getColorComponents(colorComponents);
					sphereColor.getColorComponents(sphereComponents);

					for(int i = 0; i < 3; i++) {
//						if(colorComponents[i] == 0)
//							colorComponents[i] = 1;
//						if(sphereComponents[i] == 0)
//							sphereComponents[i] = 1;
						colorComponents[i] = (float)((colorComponents[i] + sphereComponents[i]))/2;

					}

//					System.out.println("sphere color: " + sphereColor);
//					System.out.println("random ray object color: " + pixelColor);

					pixelColor =  new Color(colorComponents[0], colorComponents[1], colorComponents[2]);

//					System.out.println("new color" + pixelColor);
					pixelColor = normalShade(pixelColor, hitPoint);
				}
			}
		}
		else {
			pixelColor = new Color((float).5, (float)(1 - b)/2, (float).5);
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
		surfaces.add(new Sphere(new Vector3D(0, 0, -3), 1, Color.BLUE));
		surfaces.add(new Sphere(new Vector3D(2, 0, -4), 1, true));
		surfaces.add(new Sphere(new Vector3D(-2, 0, -3), 1, Color.RED));
		surfaces.add(new Sphere(new Vector3D(-1, 3, 5), 1, Color.ORANGE));
		surfaces.add(new Sphere(new Vector3D(-4, 5, -14), 2, Color.YELLOW));
		surfaces.add(new Sphere(new Vector3D(0, -100, 0), 98.5, Color.WHITE));
		surfaces.add(new Sphere(new Vector3D(-6, -1, -9), 1, true));
		surfaces.add(new Orb(new Vector3D(0, 10, 0), 1));
		//surfaces.add(new Orb(new Vector3D(4, 3, -5), 1));

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