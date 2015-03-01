package megaSnakeOld;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection implements Runnable{
	Socket socket;
	Thread t;
	ObjectOutputStream writer;
	ObjectInputStream reader;
	Thread listener = new Thread(this);
	private boolean controller;
	private int size;
	Board board;
	
	/**
	 * connect the computer to the server.
	 * 
	 * @param ip - the ip of the server
	 * @param port - the port in the server
	 * @param b the - board that the game is played on
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Connection(String ip, int port,Board b) throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		t = new Thread(this);
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
		board = b;
		t.start();
		
	}
	
	public void sendLink(Link l,int code){
		try {
			writer.writeObject(code+ ":SHIP_SNAKE");
			writer.flush();
			writer.writeObject(l);
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void notifyDeath() {
		try {
			writer.writeObject("-1:NOTIFY_DEATH");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void notifyGrowing(){
		try {
			writer.writeObject("-1:NOTIFY_GROWING");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isConrtroller(){
		return controller;
	}
	
	public int getSize(){
		return size;
	}
	
	private boolean link;
	private int code;
	
	@Override
	public void run() {
		try{
		while(true){
			Object o = reader.readObject();
			if(link){
				Link l = (Link) o;
				for(Gate g: board.gates){
					if(g.getCode() == code){
						if(l.head == true)
							controller = true;
							l.setX(g.getX());
							l.setY(g.getY());
						board.snake.add(l);
						link = false;
						break;
					}
				}
			} else {
				String[] data = ((String) o).split(":");
				if(data[1].equals("link")){
					link = true;
				}else if (data[1].equals("RIP")){
					board.snake.alive = false;
				} else if(data[1].equals("start")){
					System.out.println(board.snake.size());
					board.snake.add(new Link(0,0,true));
					controller = true;
				}
			}
			Thread.sleep(10);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	

	
}
