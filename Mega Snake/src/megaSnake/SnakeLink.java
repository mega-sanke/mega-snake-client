package megaSnake;

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
	private Queue<Move> moves = new LinkedBlockingDeque<>();
	private Move lastMove;
	private final boolean head;

	public SnakeLink(int x, int y, boolean head) {
		super(x, y);
		this.head = head;
		moves.add(Move.STAY);
	}

	/**
	 * copy only the position
	 * 
	 * @param s
	 */
	public SnakeLink(SnakeLink s) {
		this(s.getX(), s.getY(), false);
	}
	
	public void addMove(Move m){
		moves.add(m);
	}
	
	public int getMovesCount(){
		return moves.size();
	}
	
	public boolean isHead() {
		return head;
	}
	
	public Queue<Move> cloneMoves(){
		return new LinkedBlockingDeque<>(moves);
	}
	public void move() {
		lastMove = moves.poll();
		if (lastMove == null) {
			return;
		}
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
	public Move getLastMove(){
		return lastMove;
	}

}
