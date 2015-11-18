package megaSnake;

import java.io.IOException;

import event.MessageEvent;
import event.MessageListener;
import tcp.message.Message;
import tcp.message.Message.Target;
import tcp.message.Message.Type;
import tcp.messagesConection.ChatClient;
import util.Gate;
import util.Move;
import util.Slot;
import util.SnakeLink;
import util.Winds;

public class Networker implements MessageListener {

	private Game board;
	private int id;
	ChatClient socket;

	public static final String FOOD = "food", DEAD = "dead", OK = "ok", GATE = "gate", START = "start", ID = "id",
			FIRST_CONNECT = "first", WIDTH = "first/width", HEIGHT = "first/height", NEIGHBOR_ADD = "neighbor/add",
			NEIGHBOR_ADD_WIND = "neighbor/add/wind", NEIGHBOR_ADD_COUNT = "neighbor/add/count",
			NEIGHBOR_ADD_SIZE = "neighbor/add/size", GATE_ID = "gate/id", GATE_PLYER_ID = "gate/plyer/id",
			GATE_PREV_MOVE = "gate/prev/move";

	public Networker(String ip, Game board) throws IOException {
		this.board = board;
		socket = new ChatClient(ip, true);
		socket.addMessageListener(this);
		startSession();

	}

	@Override
	public void onReceive(MessageEvent e) {
		Message message = e.getMessage();
		if (message.getData(ID) != null) {
			id = Integer.parseInt(message.getData(ID));
		}
		if (message.getData(DEAD) != null) {
			board.kill();
		}

		if (message.getData(GATE) != null) {
			int c = Integer.parseInt(message.getData(GATE_ID));
			for (Slot[] slots : board.getSlots()) {
				for (Slot slot : slots) {
					if (slot != null && slot instanceof Gate) {
						Gate g = (Gate) slot;
						if (g.getCode() == c && Integer.parseInt(message.getData(GATE_PLYER_ID)) != id) {
							boolean head = false;
							if (board.isEmpty()) {
								head = true;
								Move move = Move.valueOf(message.getData(GATE_PREV_MOVE));
								int dx, dy;
								dx = dy = 0;
								switch (move) {
									case DOWN:
										dy = 1;
										break;
									case UP:
										dy = -1;
										break;
									case LEFT:
										dx = -1;
										break;
									case RIGHT:
										dx = 1;
										break;
								case STAY:
									break;
								}
								SnakeLink s = new SnakeLink(g.getX() + dx, g.getY() + dy, head);
								s.addMove(Move.valueOf(message.getData(GATE_PREV_MOVE)));
								s.move();
								board.addLink(s);
							} else {
								board.addLink();
							}
							board.setController(true);
							board.updateGraphics();
							System.out.println(board.getSnake());
							break;
						}
					}
				}
			}

		}
		if (message.getData(START) != null) {
			board.addLink(new SnakeLink(1, 1, true));
			board.applaySnake();
			board.setController(true);
		}

		if (message.getData(NEIGHBOR_ADD) != null) {

			Winds w = Winds.valueOf(message.getData(NEIGHBOR_ADD_WIND)).getNeg();
			w.conquer();
			int count = Integer.parseInt(message.getData(NEIGHBOR_ADD_COUNT)),
					startingID = Integer.parseInt(message.getData(NEIGHBOR_ADD_SIZE));

			for (int i = 0; i < count; i++) {
				int x = 0, y = 0;
				switch (w) {
				case EAST:
					x = board.getSlots().length - 1;
					y = i;
					break;
				case NORTH:
					y = 0;
					x = i;
					break;
				case SOUTH:
					y = board.getSlots()[0].length - 1;
					x = i;
					break;
				case WEST:
					x = 0;
					y = i;
					break;

				}
				Gate g = new Gate(x, y, startingID + i);
				board.getSlots()[x][y] = g;
			}
		}
		if (message.getData(FOOD) != null) {
			board.addLinkCount();
		}
	}

	public void send(Message m) {
		socket.send(m);
	}

	private void startSession() {
		Message m = Message.create(Target.friend(""), Type.DATA);
		m.putData(FIRST_CONNECT, "");
		m.putData(HEIGHT, board.getSlots()[0].length + "");
		m.putData(WIDTH, board.getSlots().length + "");
		send(m);

	}

	public void gateSession(int gateId, Move lastMove) {
		board.setController(false);
		Message m = Message.create(Target.BROADCAST, Type.DATA);
		m.putData(GATE, "");
		m.putData(GATE_PLYER_ID, id + "");
		m.putData(GATE_ID, gateId + "");
		m.putData(GATE_PREV_MOVE, lastMove.toString());
		send(m);

	}

	public void blockSession() {
		Message m = Message.create(Target.BROADCAST, Type.DATA);
		m.putData(DEAD, "");
		send(m);

	}

	public void foodSession() {
		Message m = Message.create(Target.BROADCAST, Type.DATA);
		board.addLink();
		m.putData(FOOD, "");
		send(m);
	}

	public void snakeSession() {
		Message m = Message.create(Target.BROADCAST, Type.DATA);
		m.putData(DEAD, "");
		send(m);
	}
}
