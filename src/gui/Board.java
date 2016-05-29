package gui;

import util.Point;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@SuppressWarnings("serial")
public class Board extends JPanel {

    private final int SPACE, X_AXIS_SIZE, Y_AXIS_SIZE;
    private final int HEIGHT, LENGTH;
    private Point[] snakes, gates, food;

    public Board(int heigth, int length, int space, int xAxis, int yAxis) {
        SPACE = space;
        X_AXIS_SIZE = xAxis;
        Y_AXIS_SIZE = yAxis;
        HEIGHT = heigth;
        LENGTH = length;
        snakes = new Point[0];
        gates = new Point[0];
        food = new Point[0];


    }

    @Override
    protected void paintComponent(Graphics g) {
        // TODO Auto-generated method stub
        super.paintComponent(g);

        fillGrid(g);


        for (Point p : snakes) {
            fillSnake(g, p);
        }

        for (Point p : gates) {
            fillGate(g, p);
        }

        for (Point p : food) {
            fillFood(g, p);
        }


    }

    public void update(Point[] snakes, Point[] gates, Point[] food) {
        this.snakes = snakes;
        this.gates = gates;
        this.food = food;
        repaint();
    }

    public void fillSlot(Graphics g, Color c, Point p) {
        Color cur = g.getColor();
        g.setColor(c);
        g.fillRect(p.getX() * X_AXIS_SIZE + SPACE, p.getY() * Y_AXIS_SIZE + SPACE, X_AXIS_SIZE - 2 * SPACE, Y_AXIS_SIZE - 2 * SPACE);
        g.setColor(cur);
    }

    public void fillSnake(Graphics g, Point point) {
        // TODO Auto-generated method stub
        this.fillSlot(g, Color.GREEN, point);
    }

    public void fillGate(Graphics g, Point p) {
        this.fillSlot(g, Color.RED, p);
    }

    public void fillFood(Graphics g, Point p) {
        this.fillSlot(g, Color.BLUE, p);
    }

    private void fillGrid(Graphics g) {
        for (int i = 1; i < LENGTH; i++) {
            g.drawLine(i * X_AXIS_SIZE, 0, i * X_AXIS_SIZE, HEIGHT * X_AXIS_SIZE);
        }

        for (int i = 1; i < HEIGHT; i++) {
            g.drawLine(0, i * Y_AXIS_SIZE, LENGTH * Y_AXIS_SIZE, i * Y_AXIS_SIZE);
        }

    }

}
