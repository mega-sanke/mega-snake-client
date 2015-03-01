package megaSnakeOld;

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

	/**
	 * return the negative movement the the movement that suit to the code
	 * 
	 * @param code
	 *            - the code (by key value) of the movement
	 * @return - the negative movement the the movement that suit to the code
	 */
	public static Move negOf(int code) {
		Move newMove = null;
		switch (code) {
		case KeyEvent.VK_DOWN:
			newMove = Move.UP;
			break;
		case KeyEvent.VK_UP:
			newMove = Move.DOWN;
			break;
		case KeyEvent.VK_LEFT:
			newMove = Move.RIGHT;
			break;
		case KeyEvent.VK_RIGHT:
			newMove = Move.LEFT;
			break;
		}
		return newMove;
	}

	public static Move negOf(Move v) {
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
	
	public Move negOf(){
		return negOf(this);
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

	public static Move getNeg(int keyCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
