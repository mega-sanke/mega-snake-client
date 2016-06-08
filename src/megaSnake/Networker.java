package megaSnake;

import event.NotificationEvent;
import event.NotificationListener;
import tcp.message.Command;
import tcp.message.Notify;
import tcp.messagesConection.Client;
import util.Point;

import javax.swing.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This class is the class that handel the networking.
 * This class is the first networking class that related to this game.
 *
 * @author  Barak Ohana
 */
public class Networker implements NotificationListener {

    /**
     * The net work component.
     */
    Client socket;

    /**
     * The game object. Handel the game logistics, link communicate with the user.
     */
    private Game board;

    /**
     * Hashtable that contain all the server's variables that doesn't have specific type.
     */
    private Hashtable<String, String> values;
    /**
     * Hashtable that contain all the server's variables from slot type.
     */
    private Hashtable<String, Point[]> slots;
    /**
     * Hashtable that contain all the server's variables from boolean type.
     */
    private Hashtable<String, Boolean> booleans;
    /**
     * Hashtable that contain all the server's variables from integer type.
     */
    private Hashtable<String, Integer> numbers;
    /**
     * Hashtable that contain all the server's variables from float type.
     */
    private Hashtable<String, Double> floats;

    /**
     * Vector that contain all the server's game rooms.
     */
    private Vector<String> rooms;

    /**
     * Constructor - create new user.
     * @param ip - the server's ip.
     * @param username - the user's name.
     * @param x - the width of the user's game board.
     * @param y - the height of the user's game board.
     * @param board - the game object.
     * @throws IOException
     */
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

    }

    @Override
    public void onReceive(NotificationEvent e) {
        Notify n = e.getNotification();
        switch (n.type) {
            case ERROR:
                JOptionPane.showMessageDialog(null, n.content[0]);
                break;
            case MESSAGE:
                System.out.println(n.content[0]);
                if(n.content[0].equals("exit")){
                    board.getFrame().setVisible(false);
                    board.getFrame().dispose();
                }
                break;
            case VALUE:


                switch (n.content[2]) {
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
                        System.out.println(n.content[0] + ' ' + n.content[1]);
                        boolean b = Boolean.parseBoolean(n.content[1]);
                        if (b) {
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

    /**
     * The function return a string variable.
     * @param key - the variable's name.
     * @param default_v - the default value, un case that it does not exits.
     * @return the variable with the specific key, or the default value, if the key does not exist.
     */
    public String get(String key, String default_v) {
        return values.containsKey(key) ? values.get(key) : default_v;
    }

    /**
     * The function return a float variable.
     * @param key - the variable's name.
     * @param default_v - the default value, un case that it does not exits.
     * @return the variable with the specific key, or the default value, if the key does not exist.
     */
    public double getDouble(String key, double default_v) {
        return floats.containsKey(key) ? floats.get(key) : default_v;
    }

    /**
     * The function return a boolean variable.
     * @param key - the variable's name.
     * @param default_v - the default value, un case that it does not exits.
     * @return the variable with the specific key, or the default value, if the key does not exist.
     */
    public boolean getBoolean(String key, boolean default_v) {
        return booleans.containsKey(key) ? booleans.get(key) : default_v;
    }

    /**
     * The function return an integer variable.
     * @param key - the variable's name.
     * @param default_v - the default value, un case that it does not exits.
     * @return the variable with the specific key, or the default value, if the key does not exist.
     */
    public int getInt(String key, int default_v) {
        return numbers.containsKey(key) ? numbers.get(key) : default_v;
    }

    /**
     * The function return a boolean variable.
     * @param key - the variable's name.
     * @return the variable with the specific key, or empty array value, if the key does not exist.
     */
    public Point[] getSlots(String key) {
        return slots.containsKey(key) ? slots.get(key) : new Point[0];
    }

    /**
     * The function parse a String that represent slost to array of Points.
     * @param key
     * @return
     */
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

    /**
     * The method sends a command to the server.
     * @param m the command to send.
     */
    private void send(Command m) {
        socket.send(m);
    }

    /**
     * The method sends a command to the server.
     * @param name - the name of the command.
     * @param p - the parameters to pass the command.
     */
    public void execute(String name, Object... p) {
        send(new Command(name, p));
    }

    /**
     * The method sigh the user to the command.
     * @param username - the user's name
     * @param x - the width of the user's game board.
     * @param y - the height of the user's game board.
     */
    private void startSession(String username, int x, int y) {
        Command cmd = new Command(username, x, y);
        send(cmd);
    }

    /**
     * The function return a Vector of all game rooms.
     * @return a Vector of all game rooms.
     */
    public Vector<String> getRooms() {
        return rooms;
    }

    public void endSession() {
        execute("log-off");
    }
}
