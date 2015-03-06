package megaSnake;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
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
			START = "start", ID = "id", FIRST_CONNECT="firstConnect";

	public Networker(Board b, String ip, int port) throws UnknownHostException,
			IOException {
		socket = new Socket(ip, port);
		writer = new PrintWriter(socket.getOutputStream());
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		this.board = b;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int h = screen.height, w = screen.width;
		int wCount = w / Slot.X_AXIS_SIZE + (w % Slot.X_AXIS_SIZE == 0 ? 0 : 1);
		int hCount = h / Slot.Y_AXIS_SIZE + (h % Slot.Y_AXIS_SIZE == 0 ? 0 : 1);
		sendMassege(FIRST_CONNECT + ":[" + wCount + "," + hCount + "]");
		thread.start();
	}

	public void work(String massege) {
		System.out.println(massege);
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
			for (Gate g : board.gates) {
				if (g.getCode() == c && Integer.parseInt(p[3]) != id) {
					boolean head = false;
					if (board.getSnake().isEmpty()) {
						head = true;
						board.justadded = true;
						SnakeLink s = new SnakeLink(g.getX(), g.getY(), head);
						s.addMove(Move.valueOf(p[2]));
						s.move();
						board.getSnake().add(s);
					} else {
						// SnakeLink h = board.getSnake().get(0);
						// Queue<Move> q = new LinkedList<Move>(board.getSnake()
						// .get(board.getSnake().size() - 1).moves);
						// for (Move m : q) {
						// s.moves.add(m);
						// }
						// SnakeLink h = board.getSnake().get(0);
						// Queue<Move> q = new LinkedList<Move>(h.moves);
						// SnakeLink l = new
						// SnakeLink(board.getSnake().get(board.getSnake().size()-1));
						// l.moves.add(Move.valueOf(p[2]));
						// l.move();
						// for (Move m : q) {
						// l.moves.add(m);
						// }
						board.getSnake().addLink();
					}

					// s.move();
					board.start();
					break;

				}
			}
			break;
		case START:
			board.controller = true;
			board.getSnake().add(new SnakeLink(0, 0, true));
			System.out.println("con");
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
