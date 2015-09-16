package util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * A slot in the game board
 * 
 * @author obama
 *
 */
public class Slot {
	
	
	/**
	 * Current position
	 */
	protected int x, y;
	/**
	 * the width ,height and a space in the sides
	 */
	public static final int X_AXIS_SIZE = 20, Y_AXIS_SIZE = 20, SPACE = 2;
	
	
	public Slot(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Slot(Slot s) {
		this(s.getX(), s.getY());
	}

	public void fillLink(Graphics g, Color c) {
		Color cur = g.getColor();
		g.setColor(c);
		g.fillRect(x + SPACE, y + SPACE, X_AXIS_SIZE - 2 * SPACE, Y_AXIS_SIZE
				- 2 * SPACE);
		g.setColor(cur);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean equals(Slot obj) {
		return obj.getX() == getX() && obj.getY() == getY();
	}

}
