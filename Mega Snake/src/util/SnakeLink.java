package util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * A link of the snake
 * 
 * @author obama
 *
 */
public class SnakeLink extends Slot {
	/**
	 * a queue of the link
	 */
	private volatile  Queue<Move> moves = new LinkedBlockingDeque<>();
	private Move lastMove;
	private final boolean head;

	public SnakeLink(int x, int y, boolean head) {
		super(x, y);
		this.head = head;
		moves.add(Move.STAY);
	}

	public void fillLink(Graphics g, int space, int x_size, int y_size) {
		// TODO Auto-generated method stub
		super.fillLink(g, Color.GREEN, space, x_size, y_size);
	}

	/**
	 * copy only the position
	 * 
	 * @param s
	 */
	public SnakeLink(Slot s) {
		this(s.getX(), s.getY(), false);
	}

	public void addMove(Move m) {
		moves.add(m);
	}

	public int getMovesCount() {
		return moves.size();
	}

	public boolean isHead() {
		return head;
	}

	public Queue<Move> cloneMoves() {
		return new LinkedBlockingDeque<>(moves);
	}

	public void move() {
		lastMove = moves.poll();
		if (lastMove == null) {
			return;
		}
		switch (lastMove) {

		case UP:
			position.translate(0, -1);
			break;
		case DOWN:
			position.translate(0, 1);
			break;
		case LEFT:
			position.translate(-1, 0);
			break;
		case RIGHT:
			position.translate(1, 0);
			break;
		case STAY:
			break;
		default:
			break;
		}
	}

	public Move getLastMove() {
		return lastMove == null ? Move.STAY : lastMove;
	}

	public Point getNextPosition() {
		Move move = moves.peek();
		if(move == null){
			return new Point(position);
		}
		switch (move) {
			case UP:
				return new Point(getX(), getY() - 1);
			case DOWN:
				return new Point(getX(), getY() + 1);
			case LEFT:
				return new Point(getX() - 1, getY());
			case RIGHT:
				return new Point(getX() + 1, getY());
			default:
				return new Point(position);
		}
	}
}
