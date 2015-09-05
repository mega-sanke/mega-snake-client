package megaSnake;

import java.io.IOException;
import java.util.ArrayList;

import event.MessageEvent;
import event.MessageListener;
import tcp.message.Message;
import tcp.messagesConection.ChatClient;

public class Networker implements MessageListener {

	private Board board;
	private int id;
	ChatClient socket;

	public static final String DEAD = "dead", OK = "ok", GATE = "gate", START = "start", ID = "id",
			FIRST_CONNECT = "firstConnect", NEIGHBOR_ADD = "neighbor/add", NEIGHBOR_ADD_WIND = "neighbor/add/wind",
			NEIGHBOR_ADD_COUNT = "neighbor/add/count", NEIGHBOR_ADD_SIZE = "neighbor/add/size", GATE_ID = "gate/id",
			GATE_PLYER_ID = "gate/plyer/id", GATE_PREV_MOVE = "gate/prev/move";

	public Networker(String ip, Board board) throws IOException {
		this.board = board;
		socket = new ChatClient(ip, true);
		socket.addMessageListener(this);

	}

	@Override
	public void onReceive(MessageEvent e) {

		Message message = e.getMessage();

		if (message.getData(ID) != null) {
			id = Integer.parseInt(message.getData(ID));
		}
		if (message.getData(DEAD) != null) {
			board.getSnake().kill();
		}
		if (message.getData(OK) != null) {
			// WTF?!?!
		}
		if (message.getData(GATE) != null) {
			int c = Integer.parseInt(message.getData(GATE_ID));
			for (Winds w : Winds.values()) {
				if (board.gates.get(w) == null)
					continue;
				for (Gate g : board.gates.get(w)) {
					if (g.getCode() == c && Integer.parseInt(message.getData(GATE_PLYER_ID)) != id) {
						boolean head = false;
						if (board.getSnake().isEmpty()) {
							head = true;
							board.justadded = true;
							SnakeLink s = new SnakeLink(g.getX(), g.getY(), head);
							s.addMove(Move.valueOf(message.getData(GATE_PREV_MOVE)));
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
		}
		if (message.getData(START) != null) {
			board.controller = true;
			board.getSnake().add(new SnakeLink(Slot.X_AXIS_SIZE, Slot.Y_AXIS_SIZE, true));
		}
		
		if (message.getData(NEIGHBOR_ADD) != null) {
			Winds w = Winds.valueOf(message.getData(NEIGHBOR_ADD_WIND)).getNeg();
			w.conquer();
			board.gates.put(w, new ArrayList<Gate>());
			ArrayList<Block> blocks = board.getBlocksOn(w);
			int count = Integer.parseInt(message.getData(NEIGHBOR_ADD_COUNT)),
					startingID = Integer.parseInt(NEIGHBOR_ADD_SIZE);
			// System.out.println("size " + blocks.size() + " count " + count);
			for (int i = 0; i < count; i++) {
				Block b = blocks.get(0);
				blocks.remove(0);
				board.gates.get(w).add(new Gate(b.x, b.y, startingID + i));
			}
			System.out.println(board.gates);
		}
	}

	
	public void send(Message m){
		socket.send(m);
	}
}
