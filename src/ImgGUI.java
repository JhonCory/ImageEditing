import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.lang.Exception;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.GroupLayout.*;

/** Generates an applet which can perform some basic image editing
  * Created by Jhon Cory on 5 May 2016 */
public class ImgGUI extends JFrame {
	protected ImgGUI thisFrame; // an reference to this object
	protected Boolean imageSet; // true iff an image is being displayed
	protected BufferedImage currentImage; // the image on display
	protected Boolean debugMode;

	public ImgGUI() {
		debugMode = false;

		thisFrame = this;
		imageSet = false;
		currentImage = null;

		if (debugMode) {
			try {
				// Load and display the selected image
				String filePath = "images/MarioMaker.png";
				currentImage = ImageIO.read(new File(filePath));
				replaceImage(currentImage);
				imageSet = true;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		initUI();
	}

	private void initUI() {
		setTitle("Basic Image Processing");
		setSize(1024, 768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		initMenuBar();
	}

	// Initializes the menu bar
	private void initMenuBar() {
		// create and set empty menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Drop-down menus
		JMenu fileMenu = new JMenu("File"); 
		JMenu imageMenu = new JMenu("Image");
		menuBar.add(fileMenu);
		menuBar.add(imageMenu);


		// Options that are part of the drop-down menus
		JMenuItem openAction = new JMenuItem("Open");
		JMenuItem exitAction = new JMenuItem("Exit");
		JMenuItem imgProcessAction = new JMenuItem("Process");

		// Add options to their respective menus
		fileMenu.add(openAction);
		fileMenu.add(exitAction);
		imageMenu.add(imgProcessAction);


		// 'Open' lets user choose an image file to load
        	openAction.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				// display file chooser for PNG images
                		JFileChooser imgChooser = new JFileChooser();
				imgChooser.setFileFilter(new FileNameExtensionFilter("PNG file", "png"));
				int returnVal = imgChooser.showOpenDialog(thisFrame); 
				

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						// Load and display the selected image
						File file = imgChooser.getSelectedFile();
						BufferedImage newImage = ImageIO.read(file);
						if (newImage.getWidth() > 1200 || newImage.getHeight() > 650) {
							ErrorDialogs.tooBig(thisFrame);
						} else {
							replaceImage(newImage);
							System.out.println("Opening : "+file.getName()+".");
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} else {
					System.out.println("Open command cancelled by user.");
				}
            		}
        	});

		// 'Exit' closes the window
        	exitAction.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
				System.out.println("Closing application.");
                		dispatchEvent(new WindowEvent(thisFrame, WindowEvent.WINDOW_CLOSING));
            		}
        	});

		// 'Process' opens the image processing window
		imgProcessAction.addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) {
                		System.out.println("Opening image processing dialog...");
				imgProcessPanel();
            		}
        	}); 
	}

	// Displays an image processing panel which can be used to alter the image
	private void imgProcessPanel() {
		if (imageSet) {
			JFrame pFrame = new JFrame("Image Processing");
			pFrame.add(new ProcessPanel());
			pFrame.pack();
			pFrame.setLocationRelativeTo(null);
			pFrame.setVisible(true);
		} else {
			ErrorDialogs.imgMissing(thisFrame);
		}
	}


	/** Image processing window which allows user to adjust the settings of 
	  * the displayed image */
	private class ProcessPanel extends JPanel {
		protected ProcessPanel thisPanel; // self-reference for use of listeners

       		public ProcessPanel() {
			thisPanel = this; 

			GroupLayout layout = new GroupLayout(this);
            		setLayout(layout);	
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);			
			
			JButton brightButton = new JButton("Brighten");
			JButton resizeButton = new JButton("Resize");
			JButton swirlButton = new JButton("Swirl");
			JButton mirrorXButton = new JButton("Mirror X");
			JButton mirrorYButton = new JButton("Mirror Y");

			JTextField brightField = new JTextField();
			JTextField widthField = new JTextField();
			JTextField heightField = new JTextField();
			JTextField swirlField = new JTextField();
			
			JLabel brightLabel = new JLabel("Brighten by (0-1):");
			JLabel resizeLabel = new JLabel("Resize to (WxH):");
			JLabel swirlLabel = new JLabel("Swirl with factor (0-1):");

			brightButton.addActionListener(new ActionListener() {
                		@Override
                		public void actionPerformed(ActionEvent e) {
					String inputB = brightField.getText();
					if (inputB.isEmpty()) {
						ErrorDialogs.emptyField(thisPanel);
					} else {
						Double factor = Double.valueOf(inputB);
						if (factor < 0 || factor > 1) {
							ErrorDialogs.zeroToOne(thisPanel);
						} else {
                    					replaceImage(ImageProcessor.brighten(currentImage,factor));
						}
					}
                		}
            		});
            		resizeButton.addActionListener(new ActionListener() {
                		@Override
                		public void actionPerformed(ActionEvent e) {
					String inputW = widthField.getText();
					String inputH = heightField.getText();
					if (inputW.isEmpty() || inputH.isEmpty()) {
						ErrorDialogs.emptyField(thisPanel);
					} else {
						// values must be rounded to the nearest pixel
						Integer width = Math.round(Float.valueOf(inputW));
						Integer height = Math.round(Float.valueOf(inputH));

						if (width <= 0 || height <= 0) {
							ErrorDialogs.negDimension(thisPanel);
						} else if (width > 1200 || height > 650) {
							ErrorDialogs.tooBig(thisPanel);
						} else {
                    					replaceImage(ImageProcessor.resize(currentImage,width,height));
						}
					}
                		}
            		});
            		swirlButton.addActionListener(new ActionListener() {
                		@Override
                		public void actionPerformed(ActionEvent e) {
					String inputS = swirlField.getText();
					if (inputS.isEmpty()) {
						ErrorDialogs.emptyField(thisPanel);
					} else {
						Double factor = Double.valueOf(inputS);
						if (factor < 0 || factor > 1) {
							ErrorDialogs.zeroToOne(thisPanel);
						} else {
                    					replaceImage(ImageProcessor.swirl(currentImage,factor));
						}
					}
                		}
            		});
            		mirrorXButton.addActionListener(new ActionListener() {
                		@Override
                		public void actionPerformed(ActionEvent e) {
                    			replaceImage(ImageProcessor.mirrorVtl(currentImage));
                		}
            		});
            		mirrorYButton.addActionListener(new ActionListener() {
                		@Override
                		public void actionPerformed(ActionEvent e) {
                    			replaceImage(ImageProcessor.mirrorHzl(currentImage));
                		}
            		});

			
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(mirrorXButton)
					.addComponent(mirrorYButton)
				) 
				.addGroup(layout.createSequentialGroup() 
					.addComponent(brightLabel)	
					.addComponent(brightField,60,80,100)	
					.addComponent(brightButton)
				)
				.addGroup(layout.createSequentialGroup() 
					.addComponent(resizeLabel)	
					.addComponent(widthField,30,40,50)	
					.addComponent(heightField,30,40,50)
					.addComponent(resizeButton)
				)
				.addGroup(layout.createSequentialGroup() 
					.addComponent(swirlLabel)	
					.addComponent(swirlField,60,80,100)	
					.addComponent(swirlButton)
				)
			);
			layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(mirrorXButton)
					.addComponent(mirrorYButton)
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(brightLabel)
					.addComponent(brightField)	
					.addComponent(brightButton)	
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(resizeLabel)
					.addComponent(widthField)	
					.addComponent(heightField)	
					.addComponent(resizeButton)	
				)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(swirlLabel)
					.addComponent(swirlField)	
					.addComponent(swirlButton)	
				)
			);
	      	}

        	protected void paintComponent(Graphics g) {
            		super.paintComponent(g);
            		Graphics2D g2d = (Graphics2D) g.create();
            		g2d.dispose();
        	}        
    	} 

	/** Replaces the current background image with a new one */
	protected void replaceImage(BufferedImage image) {
		currentImage = image;
        	JLabel label = new JLabel(new ImageIcon(image));
		thisFrame.setContentPane(label);
		thisFrame.pack();						
		if (!imageSet)
			imageSet = true; 
	}
	
	


	public static void main(String[] args) {
		// Set the "Look and Feel" of everything
		try {
			UIManager.setLookAndFeel(
        		UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception ex) {
       			System.out.println("Whoops");
    		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				ImgGUI p1Window = new ImgGUI();
				p1Window.setVisible(true);	
			}
		});
	}
}
