import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class PrintRGB 
{	
	
	private static void fadeToBlack(BufferedImage img) throws IOException
	{
		for(int i = 0; i <= img.getWidth() - 1; i++) 
		{
			for(int j = 0; j <= img.getHeight() - 1; j++) 
			{
				Color color;
				
				if(i == 0)
					color = new Color(255 - j, 255- j , 255 - j);
				else if(i > 128)
					color = new Color(j%2*255, j%2*255, j%2*255);
				else
					color = new Color(j, j, j);
				
				int rgb = color.getRGB();
				img.setRGB(i, j, rgb);
			}	
		}
		File imageOutput = new File("/Users/jimbob/Downloads/fadeToBlack.png");
		ImageIO.write(img, "png", imageOutput);
	}
	
	private static void manyColors(BufferedImage img) throws IOException
	{
		for(int i = 0; i <= img.getWidth() - 1; i++) 
		{
			for(int j = 0; j <= img.getHeight() - 1; j++) 
			{
				Color color;
				
				if(i%(img.getWidth()/4) == 0 || j%(img.getHeight()/3) == 0)
					color = new Color(0, 0, 0);
				else if(i < img.getWidth()/4 && j < img.getHeight()/3)
					color = new Color(255, 0, 0);
				else if(i < img.getWidth()/2 && j < img.getHeight()/3)
					color = new Color(0, 255, 0);
				else if(i < img.getWidth()*3/4 && j < img.getHeight()/3)
					color = new Color(0, 0, 255);
				else if(j < img.getHeight()/3)
					color = new Color(255, 255, 0);
				else if(i < img.getWidth()/4 && j < img.getHeight()*2/3)
					color = new Color(255, 128, 0);
				else if(i < img.getWidth()/2 && j < img.getHeight()*2/3)
					color = new Color(0, 255, 255);
				else if(i < img.getWidth()*3/4 && j < img.getHeight()*2/3)
					color = new Color(255, 0, 255);
				else if(j < img.getHeight()*2/3)
					color = new Color(128, 128, 128);
				else 
					color = new Color(255, 255, 255);
				
				int rgb = color.getRGB();
				img.setRGB(i, j, rgb);
			}	
		}
		File imageOutput = new File("/Users/jimbob/Downloads/manyColors.png");
		ImageIO.write(img, "png", imageOutput);
	}
	
	public static void inverseColor(BufferedImage img) throws IOException
	{
		int imageWidth = img.getWidth();
		int imageHeight = img.getHeight();
		
		for(int i = 0; i < imageWidth; i++)
		{
			for(int j = 0; j < imageHeight; j++)
			{
				img.setRGB(i, j, 1 -img.getRGB(i, j));
			}
		}
		
		ImageIO.write(img, "png", new File("/Users/jimbob/Downloads/inverse.png"));
	}
	
	public static void colorSwap(BufferedImage img) throws IOException
	{
		int imageWidth = img.getWidth();
		int imageHeight = img.getHeight();
		Color grey = new Color(128, 128, 128);
		Color blue = new Color(0, 0, 255);
		
		for(int i = 0; i < imageWidth; i++)
		{
			for(int j = 0; j < imageHeight; j++)
			{
				
				Color pixel = new Color(img.getRGB(i, j));
				if(pixel.getBlue() > 170)
					img.setRGB(i, j, grey.getRGB());
				else
					img.setRGB(i, j, blue.getRGB());
			}
		}
		
		ImageIO.write(img, "png", new File("/Users/jimbob/Downloads/swap.png"));
	}
	
	public static void main(String[] args) throws IOException
	{
		BufferedImage frame = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		BufferedImage inverse = ImageIO.read(new File("/Users/jimbob/Downloads/Pictures/flamedragon.png"));
		BufferedImage swap = ImageIO.read(new File("/Users/jimbob/Downloads/Pictures/faces.png")); 
		
		fadeToBlack(frame);
		manyColors(frame);
		inverseColor(inverse);
		colorSwap(swap);
	}
}
