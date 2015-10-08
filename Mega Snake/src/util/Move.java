package util;

import java.awt.event.KeyEvent;

/**
 * there is three kinds of movement: up, down, right, left and stay (not to
 * move)
 * 
 * @author barak
 *
 */
public enum Move {

	// TODO: add some functions
	UP, DOWN, RIGHT, LEFT, STAY;

	public static Move getNeg(Move v) {
		if(v == null)
			return null;
		switch (v) {
		case UP:
			return DOWN;
		case DOWN:
			return UP;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case STAY:
			return STAY;
		}
		return null;
	}
	
	public  static Move getNeg(int code){
		return Move.getNeg(valueOf(code));
	}

	public static Move valueOf(int code) {
		Move newMove = null;
		switch (code) {
		case KeyEvent.VK_DOWN:
			newMove = Move.DOWN;
			break;
		case KeyEvent.VK_UP:
			newMove = Move.UP;
			break;
		case KeyEvent.VK_LEFT:
			newMove = Move.LEFT;
			break;
		case KeyEvent.VK_RIGHT:
			newMove = Move.RIGHT;
			break;
		}
		return newMove;
	}

}
