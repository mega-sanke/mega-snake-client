package megaSnake;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Networker implements Runnable {
	private Socket socket;
	public PrintWriter writer;
	public BufferedReader reader;
	private Board board;
	private Thread thread = new Thread(this);
	int id;
	public static final String DEAD = "dead", OK = "ok", GATE = "gate",
			START = "start", ID = "id", FIRST_CONNECT = "firstConnect",
			ADD_NEIGHBOR = "add neighbor";

	public Networker(Board b, String ip, int port) throws UnknownHostException,
			IOException {
		socket = new Socket(ip, port);
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		this.board = b;
		Dimension screen = board.getBoardSize();
		int h = screen.height, w = screen.width;
		int wCount = w / Slot.X_AXIS_SIZE + (w % Slot.X_AXIS_SIZE == 0 ? 0 : 1);
		int hCount = h / Slot.Y_AXIS_SIZE + (h % Slot.Y_AXIS_SIZE == 0 ? 0 : 1);
		sendMassege(FIRST_CONNECT + ":[" + wCount + "," + hCount + "]");
		thread.start();
	}

	public void work(String massege) {
		String[] p = massege.split(":");
		switch (p[0]) {
		case ID:
			id = Integer.parseInt(p[1]);
			break;
		case DEAD:
			board.getSnake().kill();
			break;
		case OK:
			break;
		case GATE:
			int c = Integer.parseInt(p[1]);
			for (Winds w : Winds.values()) {
				if (board.gates.get(w) == null)
					break;
				for (Gate g : board.gates.get(w)) {
					if (g.getCode() == c && Integer.parseInt(p[3]) != id) {
						boolean head = false;
						if (board.getSnake().isEmpty()) {
							head = true;
							board.justadded = true;
							SnakeLink s = new SnakeLink(g.getX(), g.getY(),
									head);
							s.addMove(Move.valueOf(p[2]));
							s.move();
							board.getSnake().add(s);
						} else {
							board.getSnake().addLink();
						}
						board.start();
						break;

					}
				}
			}
			break;
		case START:
			board.controller = true;
			board.getSnake().add(
					new SnakeLink(Slot.X_AXIS_SIZE, Slot.Y_AXIS_SIZE, true));
			break;
		case ADD_NEIGHBOR:
			
			Winds w = Winds.valueOf(p[1]).getNeg();
			w.conquer();
			board.gates.put(w, new ArrayList<Gate>());
			ArrayList<Block> blocks = board.getBlocksOn(w);
			int count = Integer.parseInt(p[3]),
			startingID = Integer.parseInt(p[2]);
			System.out.println("size " + blocks.size() + " count " + count);
			for (int i = 0; i < count; i++) {
				Block b = blocks.get(0);
				blocks.remove(0);
				board.gates.get(w).add(new Gate(b.x, b.y, startingID + i));
			}
			break;
		}

	}

	public void sendMassege(String massege) {
		writer.println(massege);
		writer.flush();
	}

	@Override
	public void run() {
		String g = null;
		while (true) {
			try {
				g = reader.readLine();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (g != null) {
				work(g);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
