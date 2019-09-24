package main;

import static java.lang.Math.*;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import complex.Complex;
import fourier.fourier;

public class SimPanel extends JPanel implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container container;
	BufferedImage frame;
	int screenwidth;
	int screenheight;
	int threadnum;
	int datapoints = 512;
	Graph left;
	Graph right;
	boolean calculated = false;
	Complex[] original = new Complex[datapoints];
	Complex[] transform = new Complex[datapoints];
	long timemilis;

	public SimPanel(int width, int height, Container c) {
		screenwidth = width;
		screenheight = height;
		frame = new BufferedImage(screenwidth, screenheight, BufferedImage.TYPE_INT_ARGB);
		left = new Graph();
		right = new Graph();
		JPanel leftFrame = new JPanel();
		JPanel rightFrame = new JPanel();
		leftFrame.setLayout(new FlowLayout(0, 0, 0));
		rightFrame.setLayout(new FlowLayout(0, 0, 0));
		leftFrame.add(left);
		rightFrame.add(right);
		setLayout(new FlowLayout(0, 0, 0));
		add(leftFrame);
		add(rightFrame);
		Border base = BorderFactory.createEtchedBorder(Color.BLACK, Color.yellow);
		Border border = BorderFactory.createTitledBorder(base, "hi");
		leftFrame.setBorder(border);
		rightFrame.setBorder(border);
		leftFrame.setBackground(Color.WHITE);
		rightFrame.setBackground(Color.WHITE);
		left.resetDimensions(screenwidth / 2, screenheight);
		right.resetDimensions(screenwidth / 2, screenheight);
		// setPreferredSize(new Dimension(screenwidth, screenheight));
		container = c;
		new Thread(this).start();
	}

	public void contentUpdate() {
		calculated = false;
		for (int i = 0; i < datapoints; i++) {
			original[i] = function(((double) i / datapoints));
		}
		transform = fourier.FFT(original, true);
		calculated = true;
	}

	protected Complex function(double in) {
		 double thing = ((timemilis * 0.0001) % 0.1);
		 if (in < thing == in < (0.1 - thing)) {
		 return new Complex(0);
		 } else {
		 return new Complex(1);
		 }
	}

	public void graphicsUpdate() {
		if (calculated) {
			left.graph(fourier.align(original));
			right.graph(fourier.align(transform));
		}
		// setBackground(Color.getHSBColor(timemilis *0.0001f, 1, 1));
	}

	@Override
	public void run() {
		if (threadnum == 0) {
			threadnum++;
			new Thread(this).start();
			for (;;) {
				contentUpdate();
			}
		}
		if (threadnum == 1) {
			threadnum++;
			new Thread(this).start();
			for (;;) {
				repaint();
				graphicsUpdate();

			}
		}
		if (threadnum == 2) {
			threadnum++;
			new Thread(this).start();
			try {
				for (;;) {
					Thread.sleep(1);
					timemilis++;
				}
			} catch (InterruptedException e) {
			}
		}
	}

}
