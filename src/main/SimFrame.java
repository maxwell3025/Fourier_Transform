package main;
import javax.swing.JFrame;

public class SimFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimFrame() {
		SimPanel sim = new SimPanel(1080, 720, this);
		add(sim);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(3);
		setVisible(true);
	}

}
