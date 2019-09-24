package main;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class sound {

	public static void main(String[] args) throws Exception {
		byte[] buf = new byte[1];
		AudioFormat af = new AudioFormat((float) 65536, 8, 1, true, false);
		SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
		sdl.open();
		sdl.start();
		for (int i = 0; i < 65536*100; i++) {
			double angle = i/65536.0 * (i/1000.0) * Math.PI;
			buf[0] = (byte) ((Math.sin(angle)) * 128);
			sdl.write(buf, 0, 1);
		}
		sdl.drain();
		sdl.stop();

	}

}
