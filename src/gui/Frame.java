package gui;

import util.Point;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@SuppressWarnings("serial")
public class Frame extends JFrame {

    public static final int X_AXIS_SIZE =  20, Y_AXIS_SIZE = 20, SPACE = 2;

    StatusPanel status;
    Board board;
    MegaSnakeMenuBar menuBar;



    public Frame(String title, int i, int j, KeyListener key, ActionListener roomCreationListener, WindowListener windowListener, ActionListener joinRoomListener, ActionListener restartActionListener, ActionListener exitActionListener) {
        super(title);
        menuBar = new MegaSnakeMenuBar(roomCreationListener, joinRoomListener, restartActionListener, exitActionListener);
        setJMenuBar(menuBar);



        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dim.setSize(i * X_AXIS_SIZE, j * Y_AXIS_SIZE + 70);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);
        setSize(dim);
        //setLayout(null);

        //status
        status = new StatusPanel(i * X_AXIS_SIZE, 50);

        //board
        board = new Board(i, j, SPACE, X_AXIS_SIZE, Y_AXIS_SIZE);
        addKeyListener(key);
        addWindowListener(windowListener);


        //adds
        add(status, BorderLayout.NORTH);
        add(board);


        setVisible(true);
        setFocusable(true);
        validate();
    }

    /**
     *
     * The function updates the frame with fresh information.

     * @param snake - the user's slots.
     * @param gates - the users's gates.
     * @param food - the user's food slots.
     * @param alive - is the snake alive or dead.
     * @param linkCount - the total size of the snake.
     * @param rooms - list of all game rooms.
     */
    public void update(Point[] snake, Point[] gates, Point[] food, boolean alive, int linkCount, Vector<String> rooms) {
        status.update(alive, linkCount);
        board.update(snake, gates, food);
    }
}
