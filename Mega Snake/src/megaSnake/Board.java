package megaSnake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements KeyListener, ActionListener {

	private Snake snake;
	private JFrame frame;
	private Food food;
	private Networker netWorker;
	public boolean controller = false;
	/**
	 * the numbers of action that have been played and haven't been done.
	 */
	private int pressed;
	/**
	 * true if the player has been done an action, false else.
	 */

	Timer t = new Timer(Snake.DELAY, this);
	public List<Block> blocks = new ArrayList<Block>();
	public List<Gate> gates = new ArrayList<Gate>();
	public boolean justadded = false;
	public static final int PORT = 4444;

	/**
	 * Contractor.
	 * 
	 * @param ip
	 *            - the ip of the server
	 */
	public Board(String ip) {
		frame = new BoardFrame(this);
		snake = new Snake();
		food = new Food(0, 0);
		setRandomLocation(food);
		int i = (int) (Math.random() * 10 + 1);

		for (int k = 0; k < i; k++) {
			Block l = new Block(0, 0);
			setRandomLocation(l);
			blocks.add(l);
		}

		i = 1;// (int) (Math.random() * 10 + 1);
		for (int k = 0; k < i; k++) {
			Gate l = new Gate(0, 0, 0);
			setRandomLocation(l);
			gates.add(l);
		}
		t.start();
		if(!controller){
			while(!snake.isEmpty()){
				snake.remove(0);
			}
		}
		try {
			netWorker = new Networker(this, ip, PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * the function get a link and generate to her a random location
	 * 
	 * @param l
	 *            - the link to generate a random location
	 */
	private void setRandomLocation(Slot l) {
		while (snake.isOn(l) != null) {
			int wc = (int) Math.floor(frame.getWidth() / Slot.X_AXIS_SIZE);
			int hc = (int) Math.floor(frame.getHeight() / Slot.Y_AXIS_SIZE);
			Random r = new Random();
			int w = r.nextInt(wc);
			l.setX(w * Slot.X_AXIS_SIZE);
			int h = r.nextInt(hc);
			l.setY(h * Slot.Y_AXIS_SIZE);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!controller) {
			return;
		}
		// if this client is not the controller or the snake is not alive then
		// it get out of the function
		if (!snake.isAlive()) {
			return;
		}
		// if the timer is not running and the key pressed is not ESCAPE it get
		// out from the function
		if (!t.isRunning() && e.getKeyCode() != KeyEvent.VK_ESCAPE) {
			return;
		}
		// happens only if the size of the snake is not 0
		System.out.println(snake.size());
		try{
		if (snake.size() != 0) {
			Move thisMove = snake.get(0).cloneMoves().peek();
			Move newMove = Move.getNeg(e.getKeyCode());
			// if the player want to go against the direction the snake is going
			if (thisMove == newMove) {
				return;
			}
		}}catch(NullPointerException e1){
			//e1.printStackTrace();
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
		if (controller && snake.size() != 0) {
			// if the numbers of press (that haven't been done) is 0 it add the
			// last
			// move of the first link to all the links
			if (pressed == 0) {
				snake.addMove(snake.get(0).getLastMove());
			}
			pressed--;
			if (pressed < 0) {
				pressed = 0;
			}
		}

	}

	/**
	 * the function go over all links on the board and then the action that
	 * needed will be done
	 */
	private void colide() {
		if(justadded){
			netWorker.sendMassege("ok");
			justadded = false;
			return;
			
		}
		// check for eating
		Collide f = snake.collided(food);
		if (f != null) {
			setRandomLocation(food);
			netWorker.sendMassege("ok");
			return;
		}
		// go over the gates
		for (Gate g : gates) {
			Collide c = snake.collided(g);
			if (c != null) {
				netWorker.sendMassege("gate:" + g.getCode() + ":"
						+ snake.get(0).cloneMoves().peek());
				if (snake.get(0).isHead()) {
					controller = false;
				}
				snake.remove(0);
				return;
			}
		}
		// go over the blocks
		for (Block g : blocks) {
			Collide c = snake.collided(g);
			if (c != null) {
				netWorker.sendMassege("kill");
				break;
			}
		}
		// over the snake
		for (SnakeLink g : snake) {
			Collide c = snake.collided(g);
			if (c != null) {
				netWorker.sendMassege("kill");
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
			if (snake.get(0).cloneMoves().peek() != Move.STAY) {
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
		g.clearRect(0, 0, getWidth(), getHeight());
		drawGrid(g);
		/*
		 * if(!connection.isConrtroller()){ frame.setOpacity(0.7f); } else {
		 * frame.setOpacity(1.0f); }
		 */
		for (Gate l : gates) {
			l.fillLink(g, Color.RED);
		}
		Color a = new Color(150, 75, 0);
		for (Block l : blocks) {
			l.fillLink(g, a);
		}
		for (SnakeLink i : snake) {
			i.fillLink(g, Color.BLACK);
		}
		food.fillLink(g, Color.BLUE);
		String fontString = "MS Gothic";
		Font font = new Font(fontString, Font.PLAIN, 24);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(font);
		String s = (snake.size() == 1 ? "1 link!" : snake.size() + " links!");
		g2d.drawString(s, 10, 30);
	}

	private void drawGrid(Graphics g) {
		for (int i = 0; i < frame.getWidth(); i += Slot.X_AXIS_SIZE) {
			g.drawLine(i, 0, i, frame.getHeight());
		}
		for (int i = 0; i < frame.getHeight(); i += Slot.Y_AXIS_SIZE) {
			g.drawLine(0, i, frame.getWidth(), i);
		}

	}

	public Snake getSnake() {
		return snake;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// move the snake.
		snake.move();
		// add new move in the moving direction (if needed).
		keepGoing();
		// check for collides.
		colide();
		// repaint the panel.
		repaint();
	}

	public static void main(String[] args) {
		new Board(JOptionPane.showInputDialog("enter ip"));
	}

	public void start() {
		controller = true;
		
	}
}
