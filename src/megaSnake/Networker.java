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

    public Networker(String ip, Game board) throws IOException {
        this.board = board;
        socket = new Client(ip, true);
        socket.addNotificationListener(this);
        this.values = new Hashtable<>();
        startSession();
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
                values.put(n.content[0], n.content[1]);
                break;
        }

    }

    public double getDouble(String s) {
        return Double.parseDouble(values.get(s));
    }

    public boolean getBoolean(String s) {
        return Boolean.parseBoolean(values.get(s));
    }

    public int getInt(String s) {
        return Integer.parseInt(values.get(s));
    }

    public Point[] getSnake() {
        return getSlots("snake");


    }

    private Point[] getSlots(String key) {
        String s = values.get(key).replaceAll(" ", "").substring(2);
        s = s.substring(0, s.length() - 2);

        String sep = "\\)+\\,+\\(+"; // this regex for "),("

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

    public Point[] getGates() {

        return getSlots("gates");

    }

    public Optional<Point> getFood() {

        if (!values.containsKey("food")) {
            return Optional.empty();
        }

        String s = values.get("food").replaceAll("\\(|\\)", "");

        String[] coordinates = s.split(",");
        int x = Integer.parseInt(coordinates[0]), y = Integer.parseInt(coordinates[1]);


        return Optional.of(new Point(x, y));
    }

    public void send(Command m) {
        socket.send(m);
    }

    public void execute(String name, Object... p) {
        send(new Command(name, p));
    }

    private void startSession() {
        Command cmd = new Command("sign");
        send(cmd);
    }
}
