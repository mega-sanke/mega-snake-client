package megaSnakeOld;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Link implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 634711843999357949L;
	public final Queue<Move> moves = new LinkedList<Move>();
	final boolean head;
	Move lastMove;
	private int x, y;
	public static final int X_AXIS_SIZE = 20, Y_AXIS_SIZE = 20, SPACE = 2;
	
	public Link(Link l) {
		this(l.getX(),l.getY(),false);
	}

	public Link(int x, int y,boolean head) {
		this.x = x;
		this.y = y;
		moves.add(Move.STAY);
		lastMove = Move.STAY;
		this.head = head;
	}

	public void move() {
		lastMove = moves.poll();
		switch (lastMove) {
		
		case UP:
			y -= Y_AXIS_SIZE;
			break;
		case DOWN:
			y += Y_AXIS_SIZE;
			break;
		case LEFT:
			x -= X_AXIS_SIZE;
			break;
		case RIGHT:
			x += X_AXIS_SIZE;
			break;
		case STAY:
			break;
		default:
			break;
		}
	}
	
	public void drawLink(Graphics g){
		g.fillRect(x + SPACE, y + SPACE, X_AXIS_SIZE - 2*SPACE, Y_AXIS_SIZE - 2*SPACE);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean equals(Link obj) {
		return obj.getX() == getX() && obj.getY() == getY();
	}
	
}
