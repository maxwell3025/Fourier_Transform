package complex;
import static java.lang.Math.*;
public class Complex {
	public double re;
	public double im;

	public Complex(double real, double imaginary) {
		re = real;
		im = imaginary;
	}

	public Complex(double a) {
		re = a;
		im = 0;
	}

	public static final Complex i() {
		return new Complex(0, 1);
	}

	public Complex add(Complex a) {
		return new Complex(re + a.re, im + a.im);
	}

	public Complex sub(Complex a) {
		return new Complex(re - a.re, im - a.im);
	}

	public Complex mult(Complex a) {
		return new Complex(re * a.re - im * a.im, im * a.re + re * a.im);
	}

	public Complex div(Complex a) {
		return mult(a.inv());
	}

	public Complex conj() {
		return new Complex(re, -im);
	}

	public Complex inv() {
		double rad = re * re + im * im;
		return new Complex(conj().re / rad, conj().im / rad);
	}
	public double rad(){
		return sqrt(re*re+im*im);
	}

	@Override
	public String toString() {
		return re + "+" + im + "i";
	}

}
