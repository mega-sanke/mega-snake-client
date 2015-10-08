package megaSnake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.Timer;

import gui.Board;
import gui.Frame;
import util.Block;
import util.Debugger;
import util.Food;
import util.Gate;
import util.Move;
import util.Point;
import util.Slot;
import util.Snake;
import util.SnakeLink;

public class Game implements KeyListener, ActionListener {

	private Snake snake;
	private Food food;
	//private Networker networker;
	private Slot[][] slots;
	Frame frame;
	private int moveCount = 1;
	private int linkCount = 1;
	Timer t;

	public Game(String ip, int i, int j) throws IOException {
		snake = new Snake();
		snake.add(new SnakeLink(1, 1, true));
		snake.addMove(Move.STAY);
		food = new Food(0, 0);
		
		slots = new Slot[i][j];
		for (int p = 0; p < slots.length; p++) {
			for (int k = 0; k < slots[p].length; k++) {
				if(p == 0 || p == slots.length - 1 || k == 0 || k == slots[p].length - 1){
					slots[p][k] = new Block(p, k);
				}
			}
		}
		setRandomLocation(food, false);
		// networker = new networker(ip, this);
		frame = new Frame("Mega Snake", i, j, slots, this);
		t = new Timer(500, this);
		t.start();

	}

	public void setRandomLocation(Slot s, boolean exists) {
		if(exists)
			slots[s.getX()][s.getY()] = null;
		Random r = new Random();
		int i = r.nextInt(slots.length);
		int j = r.nextInt(slots[0].length);
		while (slots[i][j] != null || snake.getLinkOn(i, j) != null) {
			i = r.nextInt(slots[0].length);
			j = r.nextInt(slots.length);
		}
		s.setX(i);
		s.setY(j);
		slots[i][j] = s;
	}

	public void kill() {
		snake.kill();
	}

	public Slot[][] getSlots() {
		return slots;
	}

	public void moveSnake() {
		if (moveCount == 0) {
			snake.addMove(snake.get(0).cloneMoves().peek());
		} else {
			moveCount--;
		}

		int[] xs = new int[snake.size()];
		int[] ys = new int[snake.size()];
		for (int i = 0; i < snake.size(); i++) {
			xs[i] = snake.get(i).getX();
			ys[i] = snake.get(i).getY();
		}

		

		Point head = snake.get(0).getNextPosition();
		Slot currentSlot = slots[head.getX()][head.getY()];
		if (currentSlot != null && head.equals(currentSlot.getPosition())) {
			
			switch (slots[head.getX()][head.getY()].getClass().getSimpleName()) {
			case "Gate":
				Gate g = (Gate) slots[head.getX()][head.getY()];
				//TODO networker.gateSession(g.getCode(), head.getLastMove());
				break;
			case "Block":
				//TODO networker.blockSession();
				kill();
				break;
			case "Food":
				//TODO networker.foodSession();
				addLink();
				linkCount++;
				setRandomLocation(food, true);
				break;
			}
		} else {
			for (SnakeLink link : snake) {
				if (link != snake.get(0) && link.getNextPosition().equals(head)) {
					//TODO networker.snakeSession();
					kill();
				}
			}

		}
		snake.move();
		for (int i = 0; i < xs.length; i++) {
			slots[xs[i]][ys[i]] = null;
			SnakeLink link = snake.get(i);
			slots[link.getX()][link.getY()] = link;
		}

	}

	public boolean isAlive() {
		return snake.isAlive();
	}

	public boolean isEmpty() {
		return snake.isEmpty();
	}

	public boolean addLink(SnakeLink e) {
		return snake.add(e);
	}

	public boolean addLink() {
		if(t.getDelay() >= 20)
			t.setDelay((int)(t.getDelay() / 1.2));
		return snake.addLink();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		Move v = Move.valueOf(e.getKeyCode());
		if (v != null && v != snake.get(0).getLastMove() && v != Move.getNeg(snake.get(0).getLastMove())) {
			moveCount++;
			snake.addMove(v);
			//Debugger.println(v);

		}
		
	}
	

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		moveSnake();
		frame.update(isAlive(), getLinkCount());
	}

	public void addLinkCount() {
		linkCount++;

	}

	public int getLinkCount() {
		return linkCount;
	}

	public static void main(String[] args) throws IOException {
		Debugger.setMode(true);
		new Game("127.0.0.1", 20, 40);
	}

}
