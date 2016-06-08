package megaSnake;

import gui.Frame;
import gui.NewRoomForm;
import util.Point;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

/**
 * The Game class is a class for all (there is not many of it, for the client) game logistics.
 * @author obama
 */
public class Game implements KeyListener, ActionListener, ListSelectionListener, WindowListener {

    /**
     * The networking component.
     */
    private Networker networker;
    /**
     * The graphic component.
     */
    private Frame frame;
    /**
     * The loop components.
     */
    private Timer t;

    /**
     * Constructor - construct new Game object.
     * @param ip - the server's ip.
     * @param i - the width of the user's game board.
     * @param j - the height of the user's game board.
     * @throws IOException
     */
    public Game(String ip, int i, int j) throws IOException {
        String s = JOptionPane.showInputDialog("Enter username!");
        networker = new Networker(ip, s, i, j, this);
        frame = new Frame("Mega Snake", i, j, this, this, this, this);
        t = new Timer(10, this);
        t.setActionCommand("timer");
        t.start();

    }

    //Start of KeyListener

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    /**
     * The function that get the user actions, and send it to the server.
     */
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
     * This function is the timer function or the NewRoom function.
     * According to the actionCommand, it update the graphics, or create new room.
     *
     * @param e - the ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "timer":
                frame.update(getSnake(), getGates(), getFood(), isAlive(), getLinkCount(), getRooms());
                break;
            case "create-room":
                new NewRoomForm(getRooms(), networker, frame).setVisible(true);
                break;
        }
    }

    /**
     * This is the function of joining rooms list.
     * It joins the user to new room.
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

    /**
     *
     * @return the total size of the snake.
     */
    public int getLinkCount() {
        return networker.getInt("link-count", 0);
    }

    /**
     *
     * @return if the user is the controller of the snake.
     */
    public boolean isController() {
        return networker.getBoolean("controller", false);
    }

    /**
     *
     * @return is the snake is alive.
     */
    public boolean isAlive() {
        return networker.getBoolean("alive", false);
    }

    /**
     * @return the vector of the game rooms.
     */
    public Vector<String> getRooms() {
        return networker.getRooms();
    }

    /**
     *
     * @return an array of the user's snake slots.
     */
    public Point[] getSnake() {
        return networker.getSlots("snake");


    }

    /**
     *
     * @return an array of the user's gates.
     */
    public Point[] getGates() {

        return networker.getSlots("gates");

    }

    /**
     *
     * @return an array of the user's food slots.
     */
    public Point[] getFood() {

        return networker.getSlots("food");
    }

    public Frame getFrame(){
        return frame;
    }

    /**
     * Invoked the first time a window is made visible.
     *
     * @param e
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     *
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        networker.endSession();
    }

    /**
     * Invoked when a window has been closed as the result
     * of calling dispose on the window.
     *
     * @param e
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a normal to a
     * minimized state. For many platforms, a minimized window
     * is displayed as the icon specified in the window's
     * iconImage property.
     *
     * @param e
     * @see java.awt.Frame#setIconImage
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * Invoked when a window is changed from a minimized
     * to a normal state.
     *
     * @param e
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * Invoked when the Window is set to be the active Window. Only a Frame or
     * a Dialog can be the active Window. The native windowing system may
     * denote the active Window or its children with special decorations, such
     * as a highlighted title bar. The active Window is always either the
     * focused Window, or the first Frame or Dialog that is an owner of the
     * focused Window.
     *
     * @param e
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * Invoked when a Window is no longer the active Window. Only a Frame or a
     * Dialog can be the active Window. The native windowing system may denote
     * the active Window or its children with special decorations, such as a
     * highlighted title bar. The active Window is always either the focused
     * Window, or the first Frame or Dialog that is an owner of the focused
     * Window.
     *
     * @param e
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }


    // Enf of getters
}
