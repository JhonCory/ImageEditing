import java.awt.EventQueue;
import java.awt.Container;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/** Class which contains information on error dialogs to be displayed to user */
public class ErrorDialogs {
	

	/** Message displayed if image needs to be loaded first */
	public static void imgMissing(Container parent) {
		try {
			String pathName = "images/forReal.png";
			Icon disappointedFace = new ImageIcon(ImageIO.read(new File(pathName)));
			JOptionPane.showMessageDialog(parent, "You need to load up an image first...","Seriously?",JOptionPane.ERROR_MESSAGE, disappointedFace);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** Message displayed if one or more required text fields is empty */ 
	public static void emptyField(Container parent) {
		JOptionPane.showMessageDialog(parent, "Please enter a value in the associated text field.","You just ain't doing it right!",JOptionPane.ERROR_MESSAGE);
	}

	/** Message displayed if value entered was not between 0 and 1 */
	public static void zeroToOne(Container parent) {
		JOptionPane.showMessageDialog(parent, "Please enter a value between 0 and 1.","Oops!",JOptionPane.ERROR_MESSAGE);

	}	

	/** Message displayed if dimensions entered are negative */
	public static void negDimension(Container parent) {
		JOptionPane.showMessageDialog(parent, "Please enter positive dimensions! You know; height and width over 0? Thanks!","Oops!",JOptionPane.ERROR_MESSAGE);

	}

	/** Message displayed if user tries to have program display an overly large image */
	public static void tooBig(Container parent) {
		JOptionPane.showMessageDialog(parent, "Please make a smaller selection. This program cannot display images larger than 1200x650.","Dang nabbit!",JOptionPane.ERROR_MESSAGE);
	}
}








