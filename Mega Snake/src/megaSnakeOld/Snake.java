package megaSnakeOld;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.Timer;


@SuppressWarnings("serial")
public class Snake extends ArrayList<Link> {
	/*
	 * TODO: 1. Organize the code
	 *  	 2. Make it work
	 */
	public boolean alive;
	private static int  delay = 300;
	public static final  int DELAY = delay;
	private static final int dt = 10;
	Connection con;
	

	public Snake(Connection con) {
		alive = true;
		add(new Link(0,0, true));
		this.con = con;
	}
	
	public Link isOn(Link l) {
		for (Link i : this) {
			if (i.equals(l)) {
				return i;
			}
		}
		return null;
	}

	public void addMove(Move m) {
		for (Link i : this) {
			i.moves.add(m);
		}
	}
	
	/**
	 * the function performs movement of all the links.
	 */
	public void move() {
		if (alive) {
			for (Link l : this) {
				l.move();
			}
		}
	}
	
	private void deliver(Link l, int code) {
		remove(0);
		con.sendLink(l, code);

	}

	public boolean addLink() {
		Link head = get(0);
		Queue<Move> q = new LinkedList<Move>(head.moves);
		Link l = new Link(head);
		for(@SuppressWarnings("unused") Link vbfud : this){
			l.moves.add(Move.STAY);
		}
		for (Move m : q) {
			l.moves.add(m);
		}
		con.notifyGrowing();
		return add(l);
	}

	public Collide Collided(Link l) {
		Link i = isOn(l);
		if (i == get(0)) {
			if (l instanceof Food) {
				addLink();
				return Collide.FOOD;
			} else if(l  instanceof Block){
				kill();
				return Collide.BLOCK;
			} else if(l instanceof Gate){
				Gate temp = (Gate) l;
				deliver(l, temp.getCode());
				return Collide.GATE;
			} else {
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
	
	

}
