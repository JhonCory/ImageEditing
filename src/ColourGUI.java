import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.LineBorder;
import javax.swing.*;

/** Secondary GUI responsable for colour shifts */
public class ColourGUI extends JFrame implements ActionListener {

    private JPanel display;
    private JTextField[] inputs;
    private JPanel inputPanel;
    private String currentCS;
    private JComboBox convertToList;
    private JLabel outLabel;

    private final String RGB_S = "RGB";
    private final String CMYK_S = "CMYK";
    private final String XYZ_S = "XYZ";
    private final String HSV_S = "HSV";

    public ColourGUI() {
        initUI();
    }

    private void initUI() {
        JPanel stack = new JPanel();
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(150, 190));

        String[] inOptions = {RGB_S, CMYK_S, XYZ_S, HSV_S};

        // Create panel for convert from
        JPanel convertFrom = new JPanel();
        convertFrom.setLayout(new BoxLayout(convertFrom, BoxLayout.X_AXIS));
        JLabel convertFromLabel = new JLabel();
        convertFromLabel.setText("From: ");
        JComboBox convertFromList = new JComboBox(inOptions);
        convertFromList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String sel = (String)cb.getSelectedItem();
                setInputCS(sel);
            }
        });
 		convertFrom.add(convertFromLabel);
 		convertFrom.add(convertFromList);
 		leftPanel.add(convertFrom);

        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        setInputCS(RGB_S);
        leftPanel.add(inputPanel);

        // Create panel for convert to
        String[] outOptions = {RGB_S, CMYK_S, XYZ_S, HSV_S};
        JPanel convertTo = new JPanel();
        convertTo.setLayout(new BoxLayout(convertTo, BoxLayout.X_AXIS));
        JLabel convertToLabel = new JLabel();
        convertToLabel.setText("To: ");
        convertToList = new JComboBox(outOptions);
 		convertTo.add(convertToLabel);
 		convertTo.add(convertToList);
 		leftPanel.add(convertTo);

        // convert button
        JButton convertButton = new JButton("convert");
        convertButton.addActionListener(this);
        leftPanel.add(convertButton);

        // create colour display
        display = new JPanel();
        display.setPreferredSize(new Dimension(110, 110));
        display.setBorder(LineBorder.createGrayLineBorder());
        display.setBackground(Color.black);

        container.add(leftPanel);
        container.add(Box.createRigidArea(new Dimension(10, 10)));
        container.add(display);
        stack.add(container);

        JPanel test = new JPanel();
        test.setLayout(new BoxLayout(test, BoxLayout.X_AXIS));
        outLabel = new JLabel(" ");
        test.add(outLabel);
        stack.add(test);
        add(stack);

        pack();
        
        setTitle("Colour Converter");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private Color getColor(ColourSpace cs) {
    	RGB rgb = cs.toRGB();
    	return new Color((int) rgb.R, (int) rgb.G, (int) rgb.B);
    }

    private void setInputCS(String type) {
        currentCS = type;

        inputPanel.removeAll();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(13, 20, 13, 20));

    	String labels[];
    	if (type.equals(CMYK_S)) {
    		labels = new String[] {"C:", "M:", "Y:", "K:"};
            inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
    	}
    	else if (type.equals(RGB_S)) {
    		labels = new String[] {"R:", "G:", "B:"};
        }
        else if (type.equals(HSV_S)) {
            labels = new String[] {"H:", "S:", "V:"};
        }
        else if (type.equals(XYZ_S)) {
            labels = new String[] {"X:", "Y:", "Z:"};
        }
        else {
            labels = new String[] {"CSV:"};
            inputPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
        }

        inputs = new JTextField[labels.length];    	
    	for (int i = 0; i < labels.length; i++) {
        	JPanel p = new JPanel();
        	p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

        	JLabel label = new JLabel();
        	label.setText(labels[i]);
            label.setPreferredSize(new Dimension(40,10));
        	inputs[i] = new JTextField(5);

        	p.add(label);
        	p.add(inputs[i]);
        	inputPanel.add(p);
        }

        pack();
    }

    private ColourSpace getInputColour() {
        if (currentCS.equals(RGB_S)) {
            int R = Integer.parseInt(inputs[0].getText());
            int G = Integer.parseInt(inputs[1].getText());
            int B = Integer.parseInt(inputs[2].getText());
            return new RGB(R,G,B);
        }

        if (currentCS.equals(CMYK_S)) {
            double C = Double.parseDouble(inputs[0].getText());
            double M = Double.parseDouble(inputs[1].getText());
            double Y = Double.parseDouble(inputs[2].getText());
            double K = Double.parseDouble(inputs[3].getText());
            return new CMYK(C,M,Y,K);
        }

        if (currentCS.equals(XYZ_S)) {
            double X = Double.parseDouble(inputs[0].getText());
            double Y = Double.parseDouble(inputs[1].getText());
            double Z = Double.parseDouble(inputs[2].getText());
            return new XYZ(X,Y,Z);
        }

        if (currentCS.equals(HSV_S)) {
            double H = Double.parseDouble(inputs[0].getText());
            double S = Double.parseDouble(inputs[1].getText());
            double V = Double.parseDouble(inputs[2].getText());
            return new HSV(H,S,V);
        }

        return null;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();

        if (action.equals("convert")) {
        	ColourSpace cs = getInputColour();
            display.setBackground(getColor(cs));

            String convertTo = (String)convertToList.getSelectedItem();
            ColourSpace out;
            if (convertTo.equals(RGB_S))
                out = cs.toRGB();
            else if (convertTo.equals(CMYK_S))
                out = cs.toCMYK();
            else if (convertTo.equals(XYZ_S))
                out = cs.toXYZ();
            else
                out = cs.toHSV();

            outLabel.setText(out.toString());
        }
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ColourGUI ex = new ColourGUI();
                ex.setVisible(true);
            }
        });
    }
}