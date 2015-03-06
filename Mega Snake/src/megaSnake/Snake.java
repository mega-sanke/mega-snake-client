package megaSnake;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.Timer;


@SuppressWarnings("serial")
public class Snake extends ArrayList<SnakeLink> {
	/*
	 * TODO: 1. Organize the code
	 *  	 2. Make it work
	 */
	private boolean alive = true;
	private static int  delay = 300;
	public static final  int DELAY = delay;
	private static final int dt = 10;
	

	public Snake() {
		alive = true;
		add(new SnakeLink(Slot.X_AXIS_SIZE ,Slot.Y_AXIS_SIZE , true));
	}
	
	public SnakeLink getSnakeLinkOn(Slot l) {
		for (SnakeLink i : this) {
			if (i.equals(l)) {
				return i;
			}
		}
		return null;
	}

	public void addMove(Move m) {
		for (SnakeLink i : this) {
			i.addMove(m);
		}
	}
	
	/**
	 * the function performs movement of all the links.
	 */
	public void move() {
		if (alive) {
			for (SnakeLink l : this) {
				l.move();
			}
		}
	}

	public boolean addLink() {
		Queue<Move> q = get(size() - 1).cloneMoves();
		SnakeLink l = new SnakeLink(get(size()-1));
		for (Move m : q) {
			l.addMove(m);
		}
		return add(l);
	}
	
	public Collide collided(Slot l) {
		Slot i = getSnakeLinkOn(l);
		if(size() == 0){
				return null;
		}
		if (i == get(0)) {
			if (l instanceof Food) {
				addLink();
				return Collide.FOOD;
			} else if(l  instanceof Block){
				kill();
				return Collide.BLOCK;
			} else if(l instanceof Gate){
				return Collide.GATE;
			} else {
				if(l instanceof SnakeLink){
					SnakeLink s = (SnakeLink) l;
					if(s.equals(get(0))){
						return null;
					}
				}
				kill();
				return Collide.SNAKE;
			}
		}
		return null;
	}

	/*
	 * getters and setters
	 */

	public boolean isAlive() {
		return alive;
	}

	private void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	

	public void kill() {
		setAlive(false);
	}

	public static int getDelay() {
		return delay;
	}

	private static void setDelay(int d) {
		delay = d;
	}
	
	public static void resetDelay(){
		setDelay(DELAY);
	}
	
	public static void lowerDelay(Timer t) {
		setDelay(getDelay() - dt);
		t.setDelay(getDelay());
	}

	public void empty() {
		while(!isEmpty()){
			remove(0);
		}
		
	}

	
	

}
