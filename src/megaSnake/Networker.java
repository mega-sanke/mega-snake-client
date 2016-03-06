package megaSnake;

import java.io.IOException;

import event.NotificationEvent;
import event.NotificationListener;
import tcp.message.Command;
import tcp.message.Notify;
import tcp.messagesConection.Client;
import util.Move;



public class Networker implements NotificationListener {

	private Game board;
	private int id;
	Client socket;

	public Networker(String ip, Game board) throws IOException {
		this.board = board;
		socket = new Client(ip, true);
		socket.addNotificationListener(this);
		startSession();

	}

	@Override
	public void onReceive(NotificationEvent e) {
		Notify n = e.getMessage();
		n.execute();

	}

	public void send(Command m) {
		socket.send(m);
	}

	private void startSession() {
		Command cmd = new Command("sign");
		send(cmd);
	}
}
