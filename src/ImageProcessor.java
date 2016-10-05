import java.awt.EventQueue;
import java.awt.Container;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import java.lang.Math;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Basic image editing software. Add further features as desired. */
public class ImageProcessor {

	/** Mirrors an image horizontally */
	protected static BufferedImage mirrorHzl(BufferedImage img) {
		System.out.println("Mirroring image horizontally...");
		int height = img.getHeight();
		int width = img.getWidth();
		BufferedImage newImg = new BufferedImage(width, height, img.getType());

		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				Color color = new Color(img.getRGB(w,h));
				newImg.setRGB((width-1)-w,h, color.getRGB());
			}
		}
		System.out.println("...Horizontal mirroring complete.");
		return newImg;		
	}

	/** Mirrors an image vertically */
	protected static BufferedImage mirrorVtl(BufferedImage img) {
		System.out.println("Mirroring image horizontally...");
		int height = img.getHeight();
		int width = img.getWidth();
		BufferedImage newImg = new BufferedImage(width, height, img.getType());

		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				Color color = new Color(img.getRGB(w,h));
				newImg.setRGB(w,(height-1)-h, color.getRGB());
			}
		}
		System.out.println("...Horizontal mirroring complete.");
		return newImg;		
	}


	/** Adjusts the brightness of an image by the given factor */
	protected static BufferedImage brighten(BufferedImage img, Double factor) {
		System.out.println("Brightening image by "+factor+"...");
		int height = img.getHeight();
		int width = img.getWidth();
		BufferedImage newImg = new BufferedImage(width, height, img.getType());

		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				Color oldColor = new Color(img.getRGB(w,h));
				int red = oldColor.getRed();
				int green = oldColor.getGreen();
				int blue = oldColor.getBlue();

				// Add to R,G,B the factor specified times times their difference from 255
				red = (int) Math.round(red + (255-red)*factor);
				green = (int) Math.round(green + (255-green)*factor);
				blue = (int) Math.round(blue + (255-blue)*factor);

				Color newColor = new Color(red,green,blue);
				newImg.setRGB(w,h, newColor.getRGB());
			}
		}
		System.out.println("...Brightening complete.");
		return newImg;		
	}


	/** Resizes the image to width x height */
	protected static BufferedImage resize(Image img, int width, int height) {
		System.out.println("Scaling image by "+width+"x"+height+"...");
		Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage newImg = toBufferImage(resizedImg); // convert Image to BufferedImage

		System.out.println("...Scaling complete.");
		return newImg;
	}


	/** Swirls the image to the extent determined by the factor */
	protected static BufferedImage swirl(BufferedImage img, double factor) {
		System.out.println("Swirling image by a factor of "+factor+"...");
		int height = img.getHeight();
		int width = img.getWidth();
		BufferedImage newImg = new BufferedImage(width, height, img.getType());
		double cx = (width-1) / 2.0;
		double cy = (height-1) / 2.0;
		for (int h = 0; h < height; h++) {
			for (int w = 0; w < width; w++) {
				double dx = w - cx;
				double dy = h - cy;
				double r = Math.sqrt(dx*dx + dy*dy);
				double angle = (factor/20) * r;
				int tx = (int) (dx * Math.cos(angle) - dy * Math.sin(angle) + cx);
 				int ty = (int) (dx * Math.sin(angle) + dy * Math.cos(angle) + cy);
 				if (isValid(tx, ty, width, height))
 					newImg.setRGB(w, h, img.getRGB(tx, ty));
				else
 					newImg.setRGB(w, h, img.getRGB(w,h));
			}
		}
		System.out.println("...Swirling complete.");
		return newImg;
	}




	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// XXXXXXXXXXXXXXXXX HELPER FUNCTIONS XXXXXXXXXXXXXXXXXX
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

	/** Returns true iff (tx,ty) is a valid pixel */
	protected static Boolean isValid(int tx, int ty, int width, int height) {
		return (0<=tx && tx < width && 0<=ty && ty<height);
	}

	/** Copies and returns a buffered image */
	protected static BufferedImage imgCopy(BufferedImage img) {
		return new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
	}

	/** Ensures a value for Red, Green, or Blue is within 0-255 */
	protected static int truncateRGB(int x) {
		if (x < 0) 
			return 0;
		else if (x > 255)
			return 255;
		else 
			return x;
	}

	/** Converts a given Image into a BufferedImage */
	protected static BufferedImage toBufferImage(Image img) {
    		if (img instanceof BufferedImage) {
        		return (BufferedImage) img;
    		}

    		// Create a buffered image with transparency
    		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    		// Draw the image on to the buffered image
    		Graphics2D bGr = bimage.createGraphics();
    		bGr.drawImage(img, 0, 0, null);
    		bGr.dispose();

    		// Return the buffered image
    		return bimage;
	}







}