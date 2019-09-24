package main;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import complex.Complex;
import fourier.fourier;

public class imagetrans {

	public static void main(String[] args) {
		BufferedImage subject = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		JFileChooser j = new JFileChooser();
		j.showOpenDialog(null);
		try {
			subject = ImageIO.read(j.getSelectedFile());
		} catch (IOException e) {
		}
		int width = subject.getWidth();
		int height = subject.getHeight();	
		System.out.println(width);
		System.out.println(height);
		Complex[][] red = new Complex[width][height];
		Complex[][] green = new Complex[width][height];
		Complex[][] blue = new Complex[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int color = subject.getRGB(x, y);
				red[x][y] = new Complex(((double) ((color >> 16) & 0xff) / 256 * 2) - 1);
				green[x][y] = new Complex(((double) ((color >> 8) & 0xff) / 256 * 2) - 1);
				blue[x][y] = new Complex(((double) ((color) & 0xff) / 256 * 2) - 1);
			}
		}
		System.out.println("starting");
		Complex[][] transred = fourier.FFT2D(red, width, height, true);
		Complex[][] transgreen = fourier.FFT2D(green, width, height, true);
		Complex[][] transblue = fourier.FFT2D(blue, width, height, true);
		transred = inverted(transred, width, height);
		transgreen = inverted(transgreen, width, height);
		transblue = inverted(transblue, width, height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int dx = x - width / 2;
				int dy = y - height / 2;
				int dist = dx * dx + dy * dy;
				if (dist<4|dist>64) {
					transred[x][y] = new Complex(0);
					transgreen[x][y] = new Complex(0);
					transblue[x][y] = new Complex(0);
				}
			}
		}
		transred = inverted(transred, width, height);
		transgreen = inverted(transgreen, width, height);
		transblue = inverted(transblue, width, height);
		Complex[][] revred = fourier.FFT2D(transred, width, height, false);
		Complex[][] revgreen = fourier.FFT2D(transgreen, width, height, false);
		Complex[][] revblue = fourier.FFT2D(transblue, width, height, false);
		System.out.println("end");
		BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double shadered = revred[x][y].re;
				int r = clipColor((int) ((shadered + 1) * 128));
				double shadegreen = revgreen[x][y].re;
				int g = clipColor((int) ((shadegreen + 1) * 128));
				double shadeblue = revblue[x][y].re;
				int b = clipColor((int) ((shadeblue + 1) * 128));
				output.setRGB(x, height-y-1, new Color(r, g, b, 255).getRGB());
			}
		}
		j.showSaveDialog(null);
		try {
			ImageIO.write(output, "png", j.getSelectedFile());
		} catch (IOException e) {
		}
	}

	static int clipColor(int a) {
		if (a > 255) {
			return 255;
		}
		if (a < 0) {
			return 0;
		}
		return a;
	}

	static Complex[][] inverted(Complex[][] input, int width, int height) {
		Complex[][] output = new Complex[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				output[x][y] = input[(x + width / 2) % width][(y + height / 2) % height];
			}
		}
		return output;
	}

}
