package util;

import java.awt.Color;
import java.awt.Graphics;

public class Food extends Slot{

	public Food(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	public void fillLink(Graphics g,  int space, int x_size, int y_size) {
		// TODO Auto-generated method stub
		super.fillLink(g, Color.BLUE, space, x_size, y_size);
	}

}
