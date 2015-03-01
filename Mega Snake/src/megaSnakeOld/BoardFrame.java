package megaSnakeOld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class BoardFrame extends JFrame {
	
	public BoardFrame(Board b){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(dim.width / 2, dim.getHeight());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		this.setSize(dim);
		this.setUndecorated(false);
		//f.setBackground(new Color(0, 0, 0, 0));
		
		this.setVisible(true);
		this.add(b);
		this.addKeyListener(b);
		
	}
}
