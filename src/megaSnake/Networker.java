package megaSnake;

import event.NotificationEvent;
import event.NotificationListener;
import tcp.message.Command;
import tcp.message.Notify;
import tcp.messagesConection.Client;
import util.Point;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Optional;

public class Networker implements NotificationListener {

    Client socket;
    private Game board;
    private Hashtable<String, String> values;
    private int id;

    public Networker(String ip, int x, int y, Game board) throws IOException {
        this.board = board;
        socket = new Client(ip, true);
        socket.addNotificationListener(this);
        this.values = new Hashtable<>();
        startSession(x, y);
        execute("join-room", "test", "1234");

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
                System.out.println(n.content[0] + n.content[1]);
                values.put(n.content[0], n.content[1]);
                break;
        }

    }

    public double getDouble(String s, double default_v) {
        return values.containsKey(s) ? Double.parseDouble(values.get(s)) : default_v;
    }

    public boolean getBoolean(String s, boolean default_v) {
        return values.containsKey(s) ? Boolean.parseBoolean(values.get(s)) : default_v;
    }

    public int getInt(String s, int default_v) {
        return values.containsKey(s) ? Integer.parseInt(values.get(s)) : default_v;
    }

    public Point[] getSnake() {
        return getSlots("snake");


    }

    public Point[] getGates() {

        return getSlots("gates");

    }

    public Point[] getFood() {

        return getSlots("food");
    }

    private Point[] getSlots(String key) {
        if (!values.containsKey(key)) {
            return new Point[0];
        }
        String s = values.get(key).replaceAll(" ", "").substring(2);
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

    private void startSession(int x, int y) {
        Command cmd = new Command("sign", x, y);
        send(cmd);
    }
}
