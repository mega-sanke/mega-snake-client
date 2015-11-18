package megaSnake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Queue;
import java.util.Random;

import javax.swing.Timer;

import gui.Frame;
import util.Block;
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
	private Networker networker;
	private Slot[][] slots;
	private Frame frame;
	private int moveCount = 0;
	private int linkCount = 1;
	private Timer t;
	private boolean controller = false;
	private boolean transgerGate = false;
	int i = 0;

	public Game(String ip, int i, int j) throws IOException {
		snake = new Snake();
		snake.addMove(Move.STAY);
		food = new Food(0, 0);

		slots = new Slot[i][j];
		for (int p = 0; p < slots.length; p++) {
			for (int k = 0; k < slots[p].length; k++) {
				if (p == 0 || p == slots.length - 1 || k == 0 || k == slots[p].length - 1) {
					slots[p][k] = new Block(p, k);
				}
			}
		}
		setRandomLocation(food, false);
		networker = new Networker(ip, this);
		frame = new Frame("Mega Snake", i, j, slots, this);
		t = new Timer(500, this);
		t.start();

	}

	public void setRandomLocation(Slot s, boolean exists) {
		if (exists)
			slots[s.getX()][s.getY()] = null;
		Random r = new Random();
		int i = r.nextInt(slots.length);
		int j = r.nextInt(slots[0].length);
		while (slots[i][j] != null || snake.getLinkOn(i, j) != null) {
			i = r.nextInt(slots.length);
			j = r.nextInt(slots[0].length);
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
			Queue<Move> moves = snake.get(0).cloneMoves();
			snake.addMove(moves.poll());

		} else {
			moveCount--;
			moveCount = Math.max(0, moveCount);
		}

		boolean flag = true;
		boolean gate = false;
		Point head = snake.get(0).getNextPosition();
		if (slots[head.getX()][head.getY()] != null) {
			switch (slots[head.getX()][head.getY()].getClass().getSimpleName()) {
			case "Gate":
				gate = true;
				setTransgerGate(true);
				System.out.println("gate!! " + (i++));
				Gate g = (Gate) slots[head.getX()][head.getY()];
				networker.gateSession(g.getCode(), snake.get(0).getLastMove());
				break;
			case "Block":
				flag = false;
				networker.blockSession();
				break;
			case "Food":
				networker.foodSession();
				setRandomLocation(food, true);
				break;
			}
		}

		for (SnakeLink link : snake) {
			if (link != snake.get(0) && link.getNextPosition().equals(head)) {
				flag = false;
				networker.snakeSession();
				break;
			}
		}
		if (flag) {
			if (gate)
				snake.remove(0);

			int[] xs = new int[snake.size()];
			int[] ys = new int[snake.size()];
			for (int i = 0; i < snake.size(); i++) {
				xs[i] = snake.get(i).getX();
				ys[i] = snake.get(i).getY();
			}
			snake.move();
			for (int i = 0; i < xs.length; i++) {
				slots[xs[i]][ys[i]] = null;
				SnakeLink link = snake.get(i);
				slots[link.getX()][link.getY()] = link;
			}
		}
		if (isEmpty()) {
			clear();
			setTransgerGate(false);
		}
	}

	private void clear() {
		for (int i = 0; i < slots.length; i++) {
			for (int j = 0; j < slots[i].length; j++) {
				if (slots[i][j] instanceof SnakeLink) {
					slots[i][j] = null;
				}
			}
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
		if (t.getDelay() >= 20)
			t.setDelay((int) (t.getDelay() / 1.2));
		return snake.addLink();
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.print("start ");
		if (isController()) {
			Move v = Move.valueOf(e.getKeyCode());
			System.out.print(v + " ");
			if (v != null && v != snake.get(0).getLastPlanedMove() && v != Move.getNeg(snake.get(0).getLastPlanedMove())) {
				moveCount++;
				snake.addMove(v);
				System.out.print("turned ");
			}
			System.out.print(snake.get(0).cloneMoves());
		}
		System.out.println(" end");
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (isController() || isTransgeringGate())
			moveSnake();
		frame.update(isAlive(), getLinkCount());
	}

	public void applaySnake() {
		for (SnakeLink link : snake) {
			slots[link.getX()][link.getY()] = link;
		}

	}

	public void addLinkCount() {
		linkCount++;
	}

	public int getLinkCount() {
		return linkCount;
	}

	public boolean isController() {
		return controller;
	}

	public void setController(boolean controller) {
		this.controller = controller;
	}

	public boolean isTransgeringGate() {
		return transgerGate;
	}

	public void setTransgerGate(boolean transgerGate) {
		this.transgerGate = transgerGate;
	}

	public static void main(String[] args) throws IOException {
		new Game("127.0.0.1", 30, 30);
	}

	public Snake getSnake() {
		return snake;
	}

	public void updateGraphics() {
		frame.update(isAlive(), getLinkCount());

	}

}
