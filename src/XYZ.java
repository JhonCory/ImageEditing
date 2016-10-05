// Represents a colour in the XYZ colour space
public class XYZ extends ColourSpace {
	public double X, Y, Z;

	public XYZ(double x, double y, double z) {
		X = x;
		Y = y;
		Z = z;
	}

	public RGB toRGB() {
		double R = 3.2404542*X + -1.5371385*Y + -0.4985314*Z;
		double G = -0.9692660*X + 1.8760108*Y + 0.0415560*Z;
		double B = 0.0556434*X + -0.2040259*Y + 1.0572252*Z; 

		return new RGB(R*255,G*255,B*255);
	}

	public CMYK toCMYK() { return toRGB().toCMYK(); }
	public XYZ toXYZ() { return this; }
	public HSV toHSV() { return toRGB().toHSV(); }

	public String toString() {
		return String.format("X: %.2f, Y: %.2f, Z: %.2f", X, Y, Z);
	}

}