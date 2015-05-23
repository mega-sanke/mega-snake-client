package megaSnake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class BoardFrame extends JFrame {
	
	public BoardFrame(Board b){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		this.setSize(dim);
//		this.setUndecorated(true);
//		this.setBackground(new Color(0, 0, 0, 0));
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		b.setOpaque(false);
		
		this.setVisible(true);
		this.add(b);
		this.addKeyListener(b);
	}
}
