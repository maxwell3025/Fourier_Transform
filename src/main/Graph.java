package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import complex.Complex;

import static main.IntMath.*;

public class Graph extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Complex[] data;
	int width;
	int height;
	BufferedImage frame;
	BufferedImage framebuf;
	Graphics2D graphics;
	boolean paintdone = false;

	public Graph() {

	}

	public void resetDimensions(int width, int height) {
		this.width = width;
		this.height = height;
		setPreferredSize(new Dimension(width, height));
		frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		framebuf = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphics = framebuf.createGraphics();
	}

	public void graph(Complex[] input) {
		while (!paintdone) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
		int length = input.length;
		for (int i = 0; i < width * height; i++) {
			frame.setRGB(i % width, i / width, -1);
		}
		for (int i = 0; i < length; i++) {
			int x1 = i * width / length;
			int x2 = (i + 1) * width / length;
			double im = -input[i].im * 0.5 + 0.5;
			double imnext = -input[(i + 1) % length].im * 0.5 + 0.5;
			int imy1 = (int) (im * height);
			int imy2 = (int) (imnext * height);
			drawLine(x1, imy1, x2, imy2, 0xff00ff00);
			double real = -input[i].re * 0.5 + 0.5;
			double realnext = -input[(i + 1) % length].re * 0.5 + 0.5;
			int realy1 = (int) (real * height);
			int realy2 = (int) (realnext * height);
			drawLine(x1, realy1, x2, realy2, 0xff0000ff);
			double abs = -input[i].rad() * 0.5 + 0.5;
			double absnext = -input[(i + 1) % length].rad() * 0.5 + 0.5;
			int absy1 = (int) (abs * height);
			int absy2 = (int) (absnext * height);
			drawLine(x1, absy1, x2, absy2, 0xffff0000);
		}
		graphics.drawImage(frame, 0, 0, null);
		paintdone = false;
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(framebuf, 0, 0, width, height, null);
		paintdone = true;
	}

	public void drawLine(int x1, int y1, int x2, int y2, int color) {
		if (x1 == x2 && y1 == y2) {
			frame.setRGB(x1, y1, color);
		} else if (abs(x1 - x2) < abs(y1 - y2)) {
			drawLineY(x1, y1, x2, y2, color);
		} else {
			drawLineX(x1, y1, x2, y2, color);
		}

	}

	public void drawLineX(int x1, int y1, int x2, int y2, int color) {
		int minx = min(x1, x2);
		int maxx = max(x1, x2);
		int miny = x1 < x2 ? y1 : y2;
		int maxy = x1 > x2 ? y1 : y2;
		int difx = maxx - minx;
		for (int x = minx; !(x > maxx); x++) {
			float shiftx = ((float) x - minx) / difx;
			int y = (int) (maxy * shiftx + miny * (1.0f - shiftx) + 0.5f);
			if ((x < 0 != x < width) && (y < 0 != y < height)) {
				frame.setRGB(x, y, color);
			}
		}
	}

	public void drawLineY(int x1, int y1, int x2, int y2, int color) {
		int miny = min(y1, y2);
		int maxy = max(y1, y2);
		int minx = y1 < y2 ? x1 : x2;
		int maxx = y1 > y2 ? x1 : x2;
		int dify = maxy - miny;
		for (int y = miny; !(y > maxy); y++) {
			float shifty = ((float) y - miny) / dify;
			int x = (int) (maxx * shifty + minx * (1.0f - shifty) + 0.5f);
			if ((x < 0 != x < width) && (y < 0 != y < height)) {
				frame.setRGB(x, y, color);
			}
		}
	}

}
