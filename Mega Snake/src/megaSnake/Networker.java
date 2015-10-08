package megaSnake;

import java.io.IOException;
import java.security.MessageDigestSpi;
import java.util.ArrayList;

import javax.annotation.Generated;

import event.MessageEvent;
import event.MessageListener;
import tcp.message.Message;
import tcp.message.Message.Target;
import tcp.message.Message.Type;
import tcp.messagesConection.ChatClient;
import util.Block;
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
			FIRST_CONNECT = "firstConnect", NEIGHBOR_ADD = "neighbor/add", NEIGHBOR_ADD_WIND = "neighbor/add/wind",
			NEIGHBOR_ADD_COUNT = "neighbor/add/count", NEIGHBOR_ADD_SIZE = "neighbor/add/size", GATE_ID = "gate/id",
			GATE_PLYER_ID = "gate/plyer/id", GATE_PREV_MOVE = "gate/prev/move";

	public Networker(String ip, Game board) throws IOException {
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
			board.kill();
		}
		
		if (message.getData(GATE) != null) {
			
			//TODO make the transfer more easy to understand
			int c = Integer.parseInt(message.getData(GATE_ID));
			for (Winds w : Winds.values()) {
				if (board.gates.get(w) == null)
					continue;
				for (Gate g : board.gates.get(w)) {
					if (g.getCode() == c && Integer.parseInt(message.getData(GATE_PLYER_ID)) != id) {
						boolean head = false;
						if (board.isEmpty()) {
							head = true;
							SnakeLink s = new SnakeLink(g.getX(), g.getY(), head);
							s.addMove(Move.valueOf(message.getData(GATE_PREV_MOVE)));
							s.move();
							board.addLink(s);
						} else {
							board.addLink();
						}
						board.start();
						break;
					}
				}
			}
		}
		if (message.getData(START) != null) {
			board.addLink(new SnakeLink(0, 0, true));
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
				board.gates.get(w).add(new Gate(b.getX(), b.getY(), startingID + i));
			}
			System.out.println(board.gates);
		}
		if(message.getData(FOOD) != null){
			board.addLinkCount();
		}
		if (message.getData(OK) == null) {
			throw new IllegalStateException("Got message from the Server with no contact");
		}
	}

	
	public void send(Message m){
		socket.send(m);
	}

	public void gateSession(int gateId, Move lastMove) {
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
		
	}
	
	public void foodSession(){
		Message m = Message.create(Target.BROADCAST, Type.DATA);
		board.addLink();
		m.putData(FOOD, "");
	}
	
	public void snakeSession(){
		Message m = Message.create(Target.BROADCAST, Type.DATA);
		m.putData(DEAD, "");
	}
}
