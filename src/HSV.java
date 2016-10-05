import java.lang.Math;

// Represents a colour in the HSV colour space
public class HSV extends ColourSpace {
	public double H, S, V;

	public HSV(double h, double s, double v) {
		H = h;
		S = s;
		V = v;
	}

	public RGB toRGB() {
		if (S == 0) {
			return new RGB(V*255, V*255, V*255);
		} else {
			double R;
			double G;
			double B;

			double hex = H/60;
			double primary = Math.floor(hex);
			double secondary = hex - primary;
			double a = V*(1-S);
			double b = V*(1-S*secondary);
			double c = V*(1-S*(1-secondary));

			if (primary == 0) {
				R = V;
				G = c;
				B = a;
			} else if (primary == 1) {
				R = b;
				G = V;
				B = a;
			} else if (primary == 2) {
				R = a;
				G = V;
				B = c;
			} else if (primary == 3) {
				R = a;
				G = b;
				B = V;
			} else if (primary == 4) {
				R = c;
				G = a;
				B = V;
			} else {
				R = V;
				G = a;
				B = b;
			}
			return new RGB(R*255, G*255,B*255);
		}
	}

	public CMYK toCMYK() { 
		return toRGB().toCMYK();
	}
	public XYZ toXYZ() { 
		return toRGB().toXYZ();
	}
	public HSV toHSV() { 
		return this;
	}

	public String toString() {
		return String.format("H: %.2f, S: %.2f, V: %.2f", H, S, V);
	}

}