package gui;

import util.Point;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@SuppressWarnings("serial")
public class Frame extends JFrame {

    public static final int X_AXIS_SIZE = 20, Y_AXIS_SIZE = 20, SPACE = 2;

    StatusPanel status;
    Board board;
    MegaSnakeMenuBar menuBar;

    Vector<String> rooms_name;


    public Frame(String title, int i, int j, KeyListener key, ListSelectionListener roomSelectionListener, ActionListener roomCreationListener) {
        super(title);

        rooms_name = new Vector<>();
        menuBar = new MegaSnakeMenuBar(rooms_name, roomSelectionListener,roomCreationListener);
        setJMenuBar(menuBar);



        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dim.setSize(i * X_AXIS_SIZE, j * Y_AXIS_SIZE + 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(0, 0);
        setSize(dim);
        //setLayout(null);

        //status
        status = new StatusPanel(i * X_AXIS_SIZE, 50);

        //board
        board = new Board(i, j, SPACE, X_AXIS_SIZE, Y_AXIS_SIZE);
        addKeyListener(key);


        //adds
        add(status, BorderLayout.NORTH);
        add(board);


        setVisible(true);
        setFocusable(true);
        validate();
    }

    public void update(Point[] snake, Point[] gates, Point[] food, boolean alive, int linkCount, Vector<String> rooms) {
        status.update(alive, linkCount);
        board.update(snake, gates, food);
        rooms_name.clear();
        rooms_name.addAll(rooms);
    }
}
