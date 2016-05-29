package gui;

import util.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Optional;

@SuppressWarnings("serial")
public class Frame extends JFrame {

    public static final int X_AXIS_SIZE = 20, Y_AXIS_SIZE = 20, SPACE = 2;

    StatusPanel status;
    Board board;

    public Frame(String title, int i, int j, KeyListener key) {
        super(title);


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

    public void update(Point[] snake, Point[] gates, Point[] food, boolean alive, int linkCount) {
        status.update(alive, linkCount);
        board.update(snake, gates, food);
    }
}
