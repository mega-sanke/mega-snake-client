package util;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

/**
 * A slot in the game board
 * 
 * @author obama
 *
 */
public abstract class Slot {

	/**
	 * Current position
	 */
	protected Point position;

	/**
	 * the width ,height and a space in the sides
	 */

	public Slot(int x, int y) {
		position = new Point(x, y);
	}

	public Slot(Slot s) {
		this(s.getX(), s.getY());
	}

	public void fillLink(Graphics g, Color c, int space, int x_size, int y_size) {
		Color cur = g.getColor();
		g.setColor(c);
		g.fillRect(position.getX() * x_size + space, position.getY() * y_size + space, x_size - 2 * space, y_size - 2 * space);
		g.setColor(cur);
	}

	public void fillLink(Graphics g, int space, int x_size, int y_size) {
		// TODO Auto-generated method stub
		this.fillLink(g, g.getColor(), space, x_size, y_size);
	}

	public int getX() {
		return position.getX();
	}

	public void setX(int x) {
		position.setLocation(x, getY());
	}

	public int getY() {
		return position.getY();
	}

	public void setY(int y) {
		position.setLocation(getX(), y);
	}

	public boolean equals(Slot obj) {
		
		return obj != null && obj.getX() == getX() && obj.getY() == getY();
	}
	
	public Point getPosition(){
		return new Point(position);
	}

	@Override
	public String toString() {
		return "(" + getX() + "," + getY() + ")";
	}

}
