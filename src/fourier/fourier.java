package fourier;

import complex.Complex;
import static java.lang.Math.*;

public final class fourier {
	public static final Complex[] DFT(Complex[] input, boolean inverse) {
		int length = input.length;
		Complex[] output = new Complex[length];
		for (int k = 0; k < length; k++) {
			Complex sum = new Complex(0);
			for (int n = 0; n < length; n++) {
				double power = (inverse ? -1 : 1) * 2 * PI * k * n / length;
				sum = sum.add(input[n].mult(new Complex(cos(power), sin(power))));

			}
			if (inverse) {
				sum = new Complex(sum.re / length, sum.im / length);
			}
			output[k] = sum;
		}
		return output;
	}

	public static final Complex[][] DFT2D(Complex[][] input, int width, int height, boolean inverse) {
		Complex[][] output = new Complex[width][height];
		for (int y = 0; y < height; y++) {
			output[y] = DFT(input[y], inverse);// rows

		}
		for (int x = 0; x < width; x++) {
			Complex[] buffer = new Complex[height];
			for (int y = 0; y < height; y++) {
				buffer[y] = output[y][x];// copying
			}
			buffer = DFT(buffer, inverse);
			for (int y = 0; y < height; y++) {
				output[y][x] = buffer[y];// copying
			}
		}
		return output;
	}

	public static final Complex[] FFT(Complex[] input, boolean inverse) {
		int length = input.length;
		if (length == 1) {
			Complex[] out = { input[0] };
			return out;
		}
		Complex[] output = new Complex[length];
		for (int i = 0; i < length; i++) {
			output[i] = input[i];
			if (inverse) {
				output[i] = output[i].conj();
			}
		}
		Complex[] odd = new Complex[length / 2];
		Complex[] even = new Complex[length / 2];
		for (int i = 0; i < length / 2; i++) {
			even[i] = input[2 * i];
			odd[i] = input[2 * i + 1];
		}
		even = FFT(even, false);
		odd = FFT(odd, false);
		for (int i = 0; i < length; i++) {
			output[i] = even[i % (length / 2)]
					.add(odd[i % (length / 2)].mult(new Complex(cos(PI * 2 / length * i), sin(PI * 2 / length * i))));
		}
		if (inverse) {
			for (int i = 0; i < length; i++) {
				output[i] = output[i].conj();
				output[i] = new Complex(output[i].re / length, output[i].im / length);
			}
		}
		return output;
	}

	public static final Complex[][] FFT2D(Complex[][] input, int width, int height, boolean inverse) {
		Complex[][] output = new Complex[width][height];
		for (int x = 0; x < width; x++) {
			output[x] = FFT(input[x], inverse);
		}
		for (int y = 0; y < height; y++) {
			Complex[] buffer = new Complex[width];
			for (int x = 0; x < width; x++) {
				buffer[x] = output[x][y];
			}
			buffer = FFT(buffer, inverse);
			for (int x = 0; x < width; x++) {
				output[x][y] = buffer[x];
			}
		}
		return output;
	}

	public static Complex[] align(Complex[] input) {
		int length = input.length;
		Complex[] output = new Complex[length];
		int shift = length/2;
		for(int i = 0;i<length;i++){
			output[(i+shift)%length]=input[i];
		}
		return output;
	}

}
