package gui;

import util.Point;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Board extends JPanel {

    /**
     * Size of the space between slots.
     */
    private final int SPACE;

    /**
     * The vertical size of slot.
     */
    private final int X_AXIS_SIZE;

    /**
     * The horizontal size of slot.
     */
    private final int Y_AXIS_SIZE;

    /**
     * The height (is slots) of the user's board.
     */
    private final int HEIGHT;

    /**
     * The length (is slots) of the user's board.
     */
    private final int LENGTH;

    /**
     * The user's snake's links.
     */
    private Point[] snakes;
    /**
     * The user's gates (only slots).
     */
    private Point[] gates;

    /**
     * The user's food slots.
     */
    private Point[] food;

    /**
     * Constructor - construct the Board.
     * @param heigth - The height (is slots) of the user's board.
     * @param length - The length (is slots) of the user's board.
     * @param space - Size of the space between slots.
     * @param xAxis - The vertical size of slot.
     * @param yAxis - The horizontal size of slot.
     */
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
    /**
     * The function paints the current state of the game on the panel.
     */
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

    /**
     * The function updates the board.
     * @param snakes - the current snakes's slots.
     * @param gates - the current gates.
     * @param food - the current food.
     */
    public void update(Point[] snakes, Point[] gates, Point[] food) {
        this.snakes = snakes;
        this.gates = gates;
        this.food = food;
        repaint();
    }

    /**
     * The function fill a Slot
     * @param g - the Graphics object, for painting.
     * @param c - the color to fill with.
     * @param p - the slot (Point object) to fill.
     */
    public void fillSlot(Graphics g, Color c, Point p) {
        Color cur = g.getColor();
        g.setColor(c);
        g.fillRect(p.getX() * X_AXIS_SIZE + SPACE, p.getY() * Y_AXIS_SIZE + SPACE, X_AXIS_SIZE - 2 * SPACE, Y_AXIS_SIZE - 2 * SPACE);
        g.setColor(cur);
    }

    /**
     * The function fill snake slot.
     * @param g - the Graphics object, for painting.
     * @param point - the slot (Point object) to fill.
     */
    public void fillSnake(Graphics g, Point point) {
        // TODO Auto-generated method stub
        this.fillSlot(g, Color.GREEN, point);
    }

    /**
     * The function fill gate slot.
     * @param g - the Graphics object, for painting.
     * @param p - the slot (Point object) to fill.
     */
    public void fillGate(Graphics g, Point p) {
        this.fillSlot(g, Color.RED, p);
    }


    /**
     * The function fill snake slot.
     * @param g - the Graphics object, for painting.
     * @param p - the slot (Point object) to fill.
     */
    public void fillFood(Graphics g, Point p) {
        this.fillSlot(g, Color.BLUE, p);
    }

    /**
     * the function fill the board grid.
     * @param g - the Graphics object, for painting.
     */
    private void fillGrid(Graphics g) {
        for (int i = 1; i < LENGTH; i++) {
            g.drawLine(i * X_AXIS_SIZE, 0, i * X_AXIS_SIZE, HEIGHT * X_AXIS_SIZE);
        }

        for (int i = 1; i < HEIGHT; i++) {
            g.drawLine(0, i * Y_AXIS_SIZE, LENGTH * Y_AXIS_SIZE, i * Y_AXIS_SIZE);
        }

    }

}
