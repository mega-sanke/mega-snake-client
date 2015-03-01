package megaSnakeOld;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Delayed;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener, ActionListener {

	Snake snake;
	JFrame frame;
	Link food;
	/**
	 * the numbers of action that have been played and haven't been done.
	 */
	private int pressed;
	/**
	 * true if the player has been done an action, false else.
	 */
	private boolean started;

	Timer t = new Timer(Snake.DELAY, this);
	List<Block> blocks = new ArrayList<Block>();
	List<Gate> gates = new ArrayList<Gate>();
	Connection connection;
	public static final int PORT = 4444;

	/**
	 * Contractor.
	 * 
	 * @param ip
	 *            - the ip of the server
	 */
	public Board(String ip) {
		frame = new BoardFrame(this);
		try {
			connection = new Connection(ip, PORT, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		snake = new Snake(connection);
		food = new Link(0, 0, false);
		setRandomLink(food);
		int i = (int) (Math.random() * 10 + 1);
		/*
		 * for (int k = 0; k < i; k++) { Link l = new Link(0, 0, false);
		 * setRandomLink(l); blocks.add(l); }
		 */
		i = 1;// (int) (Math.random() * 10 + 1);
		for (int k = 0; k < i; k++) {
			Gate l = new Gate(0, 0, 0);
			setRandomLink(l);
			gates.add(l);
		}
		t.start();
	}

	/**
	 * the function get a link and generate to her a random location
	 * 
	 * @param l
	 *            - the link to generate a random location
	 */
	private void setRandomLink(Link l) {
		while (snake.isOn(l) != null) {
			int wc = (int) Math.floor(frame.getWidth() / Link.X_AXIS_SIZE);
			int hc = (int) Math.floor(frame.getHeight() / Link.Y_AXIS_SIZE);
			Random r = new Random();
			int w = r.nextInt(wc);
			l.setX(w * Link.X_AXIS_SIZE);
			int h = r.nextInt(hc);
			l.setY(h * Link.Y_AXIS_SIZE);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// check if the game hasn't began yet (not with the server, only on
		// client)
		if (snake.size() == 2 && snake.get(0).equals(snake.get(1))) {
			System.out.println("vdfvfv");
			started = true;
		}
		// if this client is not the controller or the snake is not alive then
		// it get out of the function
		if (!connection.isConrtroller() || !snake.alive) {
			return;
		}
		// if the timer is not running and the key pressed is not ESCAPE it get
		// out from the function
		if (!t.isRunning() && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
			return;
		}
		// happens only if the size of the snake is not 0
		if (snake.size() != 0) {
			Move thisMove = snake.get(0).moves.peek();
			Move newMove = Move.getNeg(e.getKeyCode());
			// if the player want to go against the direction the snake is going
			if (thisMove == newMove) {
				return;
			}
		}
		// finally! the action
		action(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * the action keep the snake going to the same direction it is going now (if
	 * needed)
	 */
	private void keepGoing() {
		// if the size of the snake is 0 it will go out from the function
		if (snake.size() == 0) {
			return;
		}
		// if the numbers of press (that haven't been done) is 0 it add the last
		// move of the first link to all the links
		if (pressed == 0) {
			snake.addMove(snake.get(0).lastMove);
		}
		pressed--;
		if (pressed < 0) {
			pressed = 0;
		}

	}

	/**
	 * the function go over all links on the board and then the action that
	 * needed will be done
	 */
	private void colide() {
		// check for eating
		snake.Collided(food);
		// go over the gates
		for (Gate g : gates) {
			Collide c = snake.Collided(g);
			if (c != null) {
				break;
			}
		}
		// go over the blocks
		for (Block g : blocks) {
			Collide c = snake.Collided(g);
			if (c != null) {
				break;
			}
		}
		// over the snake
		for (Link g : snake) {
			Collide c = snake.Collided(g);
			if (c != null) {
				break;
			}
		}

	}

	/**
	 * The function performs the moving action (the function not the actually
	 * move the snake, adding the new move to the queue)
	 * 
	 * @param code
	 */
	public void action(int code) {
		switch (code) {
		case KeyEvent.VK_DOWN:
			pressed++;
			snake.addMove(Move.DOWN);
			break;
		case KeyEvent.VK_UP:
			pressed++;
			snake.addMove(Move.UP);
			break;
		case KeyEvent.VK_LEFT:
			pressed++;
			snake.addMove(Move.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
			pressed++;
			snake.addMove(Move.RIGHT);
			break;
		case KeyEvent.VK_SPACE:
			if (snake.get(0).moves.peek() != Move.STAY) {
				snake.addLink();
				Snake.lowerDelay(t);
			}
			break;
		case KeyEvent.VK_BACK_SPACE:
			if (snake.size() != 1) {
				snake.remove(snake.size() - 1);
				Snake.lowerDelay(t);
			}
			break;
		case KeyEvent.VK_ESCAPE:
			if (t.isRunning()) {
				t.stop();
			} else {
				t.start();
			}
			break;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawGrid(g);
		/*
		 * if(!connection.isConrtroller()){ frame.setOpacity(0.7f); } else {
		 * frame.setOpacity(1.0f); }
		 */
		g.setColor(Color.red);
		for (Gate l : gates) {
			l.drawLink(g);
		}
		g.setColor(new Color(150, 75, 0));
		for (Link l : blocks) {
			l.drawLink(g);
		}
		g.setColor(Color.black);
		for (Link i : snake) {
			i.drawLink(g);
		}
		g.setColor(Color.BLUE);
		food.drawLink(g);
		String fontString = "MS Gothic";
		Font font = new Font(fontString, Font.PLAIN, 24);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(font);
		String s = (snake.size() == 1 ? "1 link!" : connection.getSize()
				+ " links!");
		g2d.drawString(s, 10, 30);
	}

	private void drawGrid(Graphics g) {
		for (int i = 0; i < frame.getWidth(); i += Link.X_AXIS_SIZE) {
			g.drawLine(i, 0, i, frame.getHeight());
		}
		for (int i = 0; i < frame.getHeight(); i += Link.Y_AXIS_SIZE) {
			g.drawLine(0, i, frame.getWidth(), i);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// move the snake
		snake.move();
		// add new move in the moving direction (if needed)
		keepGoing();
		// check for collides
		colide();
		// repaint the panel
		repaint();
	}

}
