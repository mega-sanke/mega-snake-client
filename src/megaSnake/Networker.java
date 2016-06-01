package megaSnake;

import event.NotificationEvent;
import event.NotificationListener;
import tcp.message.Command;
import tcp.message.Notify;
import tcp.messagesConection.Client;
import util.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Networker implements NotificationListener {

    Client socket;
    private Game board;
    private Hashtable<String, String> values;
    private Hashtable<String, Point[]> slots;
    private Hashtable<String, Boolean> booleans;
    private Hashtable<String, Integer> numbers;
    private Hashtable<String, Double> floats;
    private Vector<String> rooms;
    private int id;

    public Networker(String ip, String username, int x, int y, Game board) throws IOException {
        this.board = board;
        socket = new Client(ip, true);
        socket.addNotificationListener(this);
        this.values = new Hashtable<>();
        this.slots = new Hashtable<>();
        this.booleans = new Hashtable<>();
        this.numbers = new Hashtable<>();
        this.floats = new Hashtable<>();
        this.rooms = new Vector<>();


        startSession(username, x, y);
//        execute("join-room", "test", "1234");

    }

    @Override
    public void onReceive(NotificationEvent e) {
        Notify n = e.getNotification();
        switch (n.type) {
            case ERROR:
                System.err.println(n.content[0]);
                break;
            case MESSAGE:
                System.out.println(n.content[0]);
                break;
            case VALUE:
//                System.out.println(n.content[0] + n.content[1]);

                switch (n.content[2]){
                    case "slots":
                        slots.put(n.content[0], parseSlots(n.content[1]));
                        break;
                    case "boolean":
                        booleans.put(n.content[0], Boolean.parseBoolean(n.content[1]));
                        break;
                    case "number":
                        numbers.put(n.content[0], Integer.parseInt(n.content[1]));
                    case "floats":
                        floats.put(n.content[0], Double.parseDouble(n.content[1]));
                    case "room":
                        boolean b = Boolean.parseBoolean(n.content[1]);
                        if(b){
                            rooms.add(n.content[0]);
                        } else {
                            rooms.remove(n.content[0]);
                        }

                    default:
                        values.put(n.content[0], n.content[1]);
                }

                break;
        }

    }

    public String get(String key, String default_v){
        return values.containsKey(key) ? values.get(key) : default_v;
    }

    public double getDouble(String s, double default_v) {
        return floats.containsKey(s) ? floats.get(s) : default_v;
    }

    public boolean getBoolean(String s, boolean default_v) {
        return booleans.containsKey(s) ? booleans.get(s) : default_v;
    }

    public int getInt(String s, int default_v) {
        return numbers.containsKey(s) ? numbers.get(s) : default_v;
    }

    public Point[] getSlots(String key){
        return slots.containsKey(key) ? slots.get(key) : new Point[0];
    }



    private Point[] parseSlots(String key) {

        String s = key.replaceAll(" ", "").substring(2);
        if (s.length() == 0) {
            return new Point[0];
        }
        s = s.substring(0, s.length() - 2);

        String sep = "\\]+\\,+\\[+"; // this regex for "),("

        String[] dots = s.split(sep);

        Point[] points = new Point[dots.length];
        for (int i = 0; i < points.length; i++) {
            String comma = ",";
            String[] coordinates = dots[i].split(comma);
            int x = Integer.parseInt(coordinates[0]), y = Integer.parseInt(coordinates[1]);
            points[i] = new Point(x, y);
        }
        return points;
    }

    public void send(Command m) {
        socket.send(m);
    }

    public void execute(String name, Object... p) {
        send(new Command(name, p));
    }

    private void startSession(String username, int x, int y) {
        Command cmd = new Command(username, x, y);
        send(cmd);
    }

    public Vector<String> getRooms() {
        return rooms;
    }
}
