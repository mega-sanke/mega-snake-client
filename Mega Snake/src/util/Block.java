package util;

import java.awt.Color;
import java.awt.Graphics;

public class Block extends Slot {

	public Block(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	public void fillLink(Graphics g,  int space, int x_size, int y_size) {
		// TODO Auto-generated method stub
		super.fillLink(g, Color.BLACK, space, x_size, y_size);
	}

}
