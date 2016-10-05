import java.lang.Math;

// Represents a colour in the RGB colour space
public class RGB extends ColourSpace {
	public double R, G, B; // stores values as percents

	// (input will be between 0 and 255)
	public RGB(double r, double g, double b) {
		R = r;
		G = g;
		B = b;
	}

	public RGB toRGB() {
		return this;
	}

	public CMYK toCMYK() { 
		double C = 1 - R/255;
		double M = 1 - G/255;
		double Y = 1 - B/255;

		double K = C;
		if (K > M)
			K = M;
		if (K > Y)
			K = Y;
		C = (C-K)/(1-K);
		M = (M-K)/(1-K);
		Y = (Y-K)/(1-K);
		
		return new CMYK(C,M,Y,K);
	}
	public XYZ toXYZ() { 
		double X = (0.4124564*R + 0.3575761*G + 0.1804375*B)/255;
		double Y =  (0.2126729*R + 0.7151522*G + 0.0721750*B)/255;
		double Z = (0.0193339*R + 0.1191920*G + 0.9503041*B)/255; 

		return new XYZ(X,Y,Z);
	}
	public HSV toHSV() {
		double decR = R/255;
		double decG = G/255;		
		double decB = B/255;

		double max = decR;
		if (max < decG) 
			max = decG;
		if (max < decB)
			max = decB;
		double min = decR;
		if (min > decG)
			min = decG;
		if (min > decB)
			min = decB;
		
		double S = (max-min)/max;
		double V = max;
		double H;

		double R0 = (max-decR)/(max-min);
		double G0 = (max-decG)/(max-min);
		double B0 = (max-decB)/(max-min);
		
		if (decR == max && decG == min) 
			H = 5 + B0;
		else if (decR == max && decG != min)
			H = 1 - G0;
		else if (decG == max && decB == min)
			H = R0 + 1;
		else if (decG == max && decB != min)
			H = 3 - B0;
		else if (decR == max)
			H = 3 + G0;
		else
			H = 5 - R0;

		return new HSV(H*60,S,V);
	}

	public String toString() {
		return String.format("R: %d%n, G: %d%n, B: %d%n", ((Long) Math.round(R)).intValue(), ((Long) Math.round(G)).intValue(), ((Long) Math.round(B)).intValue());

	}

}