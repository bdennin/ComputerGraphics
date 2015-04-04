package RayTracer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SimpleRayTracing {

	private static final Vector3D ORIGIN = new Vector3D(0, 0, 0);
	private static final BufferedImage FRAME = new BufferedImage(1200, 1200, BufferedImage.TYPE_INT_RGB);
	private static final ArrayList<Surface> SURFACES = initializeSurfaces();
	private static final ArrayList<Light> LIGHTS = initializeLights();
	private static final int TOTAL_SURFACES = SURFACES.size(); 
	private static final int LIGHT_ITERATIONS = 100;

	private static BufferedImage backgroundTexture;

	public static void main(String[] args) throws IOException {

		backgroundTexture = ImageIO.read(new File("stars.png"));
		
		double startTime = System.currentTimeMillis();
		int y = FRAME.getHeight();
		int x = FRAME.getWidth();
		int percentage = x/10;
		
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {

				if(i%percentage == 0 && i != 0 && j == 0)
					System.out.printf("%d0 percent completed\n", i/percentage);
					
				double a = (-1 + 2 * (double)i/x);
				double b = (1 - 2 * (double)j/y);
				double c = -1;

				Vector3D direction = new Vector3D(a, b, c);
				Ray ray = new Ray(ORIGIN, direction);
				Color pixelColor = determinePixelColor(ray, 1);
				FRAME.setRGB(i, j,  pixelColor.getRGB());
			}
		}

		ImageIO.write(FRAME, "png", new File("C:/Users/Jimbob/Documents/GitHub/Sphere.png"));
		double endTime = System.currentTimeMillis();
		double totalTime = endTime - startTime;
		System.out.println("Execution completed in " + totalTime + "ms.");
	}

	private static Color determinePixelColor(Ray ray, int depth) {

		Color pixelColor = new Color(0, 0, 0);

		if(depth > 10) {
			
		}
		else if(hasIntersect(SURFACES, ray)) {

			Vector3D hitPoint = ray.getRayAtT();
			Sphere intersectedObject = ray.getSphere();

			if(intersectedObject.isReflective()) {
				
				Vector3D normal = hitPoint.subtract(ray.getSphere().getCenter()).normalize();
				Vector3D direction = hitPoint.subtract(normal.multiply(2).multiply(hitPoint.dot(normal)));
				Vector3D complementaryVector = hitPoint.add(direction);
				Sphere angleVariance = new Sphere(complementaryVector, .1, Color.WHITE);
				Vector3D angle = angleVariance.generateRandomPoint();
				direction = angle.subtract(hitPoint).normalize();
				ray = new Ray(hitPoint, direction);	
				pixelColor = determinePixelColor(ray, ++depth);

			}
			else {

				Vector3D normal = hitPoint.subtract(ray.getSphere().getCenter());
				pixelColor = intersectedObject.getColor(normal);
				
				if(intersectedObject instanceof Light) {

				}
				else {

					Vector3D direction = hitPoint.add(normal.multiply(2));
					Sphere angle = new Sphere(direction, 1, Color.WHITE);
					Vector3D randomPoint = angle.generateRandomPoint();
					direction = randomPoint.normalize();
					ray = new Ray(hitPoint, direction);	
					
					Color diffuseColor = determinePixelColor(ray, ++depth);
				
					pixelColor =  new Color((diffuseColor.getRed() + pixelColor.getRed())/depth, (diffuseColor.getGreen() + pixelColor.getGreen())/depth, (diffuseColor.getBlue() + pixelColor.getBlue())/depth);
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

			for(int j = 0; j < LIGHT_ITERATIONS; j++) {

				Vector3D direction = light.generateRandomPoint().subtract(hitPoint).normalize();
				Ray lightDetectionRay = new Ray(hitPoint, direction);

				if(hasIntersect(SURFACES, lightDetectionRay) && lightDetectionRay.getSphere() == light) {
					temp++;
				}
			}
			temp = temp/LIGHT_ITERATIONS;
			if(temp > maxLight) {
				maxLight = temp;
			}
			
			if(maxLight > .9)
				maxLight = 1;
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
	//	surfaces.add(new Orb(new Vector3D(0, 50, 0), 3));
		surfaces.add(new Orb(new Vector3D(3, 12.5, 10), 3));
//		surfaces.add(new Sphere(new Vector3D(-1, 11, 7), .25, "mercury.png"));
//		surfaces.add(new Sphere(new Vector3D(-10, 9, 0), .55, "venus.jpg"));
//		surfaces.add(new Sphere(new Vector3D(3, 11, -5), 1.5, "earth.jpg"));
//		surfaces.add(new Sphere(new Vector3D(2.5, 10.2, -2), .33, "moon.jpg"));
//		surfaces.add(new Sphere(new Vector3D(25, 13, -11), 1, "mars.jpg"));
//		surfaces.add(new Sphere(new Vector3D(-7, 25, 1), 1, "jupiter.jpg"));
		surfaces.add(new Sphere(new Vector3D(3, -100, -10), 98.5, Color.WHITE));
		surfaces.add(new Sphere(new Vector3D(-1, 0, -7), 1, Color.BLUE));
		surfaces.add(new Sphere(new Vector3D(1, 0, -7), 1, Color.RED));
		surfaces.add(new Sphere(new Vector3D(-3, -.15, -7), 1, Color.GREEN));
		surfaces.add(new Sphere(new Vector3D(-5, -.3, -7), 1, Color.YELLOW));
	//	surfaces.add(new Sphere(new Vector3D(-2, 3, -4.2), 1, Color.CYAN));
	//	surfaces.add(new Sphere(new Vector3D(0, 3, -4.2), 1, "jupiter.jpg"));
		//surfaces.add(new Sphere(new Vector3D(3, 0, -5), 1, true));
		//surfaces.add(new Sphere(new Vector3D(5, -1, -3), 1, true));

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