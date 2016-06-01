package megaSnake;

import gui.Frame;
import gui.NewRoomForm;
import util.Point;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Vector;

public class Game implements KeyListener, ActionListener, ListSelectionListener {

    private Networker networker;
    private Frame frame;
    private Timer t;

    public Game(String ip, int i, int j) throws IOException {
        String s = JOptionPane.showInputDialog("Enter username!");
        networker = new Networker(ip, s, i, j, this);
        frame = new Frame("Mega Snake", i, j, this, this, this);
        t = new Timer(10, this);
        t.setActionCommand("timer");
        t.start();

    }

    //Start of KeyListener

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isController()) {
            char move = '-';
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    move = 'S';
                    break;
                case KeyEvent.VK_UP:
                    move = 'N';
                    break;
                case KeyEvent.VK_LEFT:
                    move = 'W';
                    break;
                case KeyEvent.VK_RIGHT:
                    move = 'E';
                    break;
            }
            networker.execute("mv-snake", move);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    //end KeyListener

    /**
     * This function is the timer function, called in infinite loop.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "timer":
                frame.update(getSnake(), getGates(), getFood(), isAlive(), getLinkCount(), getRooms());
                break;
            case "create-room":
                new NewRoomForm(getRooms(), networker, frame).setVisible(true);
        }
    }

    /**
     * this is the function of joining rooms list.
     *
     * @param e
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        JList list = JList.class.cast(e.getSource());
        String room_name = networker.getRooms().get(list.getSelectedIndex());
        String password = JOptionPane.showInputDialog(null, String.format("Enter password for room \'%s\'", room_name));
        networker.execute("join-room", room_name, password);
    }

    //Start of getters

    public int getLinkCount() {
        return networker.getInt("link-count", 0);
    }

    public boolean isController() {
        return networker.getBoolean("controller", false);
    }

    public boolean isAlive() {
        return networker.getBoolean("alive", false);
    }

    public Vector<String> getRooms() {
        return networker.getRooms();
    }

    public Point[] getSnake() {
        return networker.getSlots("snake");


    }

    public Point[] getGates() {

        return networker.getSlots("gates");

    }

    public Point[] getFood() {

        return networker.getSlots("food");
    }


    // Enf of getters
}
