// Represents a colour in the CMYK colour space
public class CMYK extends ColourSpace {
	public double C, M, Y, K;

	public CMYK(double c, double m, double y, double k) {
		C = c;
		M = m;
		Y = y;
		K = k;
	}

	public RGB toRGB() {
		double C0 = C*(1-K)+K;
		double M0 = M*(1-K)+K;
		double Y0 = Y*(1-K)+K;

		if (C0 > 1)
			C0 = 1;
		if (M0 > 1)
			M0 = 1;
		if (Y0 > 1)
			Y0 = 1;
		double R = 1 - C0;
		double G = 1 - M0;
		double B = 1 - Y0;

		return new RGB(R*255,G*255,B*255);
	}

	public CMYK toCMYK() { 
		return this;
	}
	public XYZ toXYZ() { 
		return toRGB().toXYZ();
	}
	public HSV toHSV() { 
		return toRGB().toHSV();
	}

	public String toString() {
		return String.format("C: %.2f, M: %.2f, Y: %.2f, K: %.2f", C, M, Y, K);
	}

}