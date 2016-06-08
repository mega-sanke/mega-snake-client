package gui;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
/**
 * This class is a class for the status panel (the panel on the top of the window that show if the snake is alive and how long the snake is).
 */
public class StatusPanel extends JPanel {

    /**
     * The lable for the alive information.
     */
    JLabel aliveLable;
    /**
     * The lable for the length information.
     */
    JLabel lengthLable;

    /**
     * Constructor - construct new status panel.
     * @param w - the wight of the panel.
     * @param h - new height of the panel.
     */
    public StatusPanel(int w, int h) {
        setLayout(new GridLayout(1, 2));
        setSize(w, h);
        Font f = new Font("Barak", Font.PLAIN, 18);
        lengthLable = new JLabel("443");
        lengthLable.setHorizontalAlignment(JLabel.CENTER);
        lengthLable.setFont(f);
        lengthLable.setForeground(Color.MAGENTA);
        aliveLable = new JLabel("43");
        aliveLable.setFont(f);
        aliveLable.setHorizontalAlignment(JLabel.CENTER);
        aliveLable.setForeground(Color.MAGENTA);
        add(aliveLable);
        add(lengthLable);
    }

    /**
     * This function update the panel with fresh information.
     * @param alive - is the snake alive of dead.
     * @param length - the total length of the snake.
     */
    public void update(boolean alive, int length) {
        aliveLable.setText(alive ? "Alive :)" : "Dead :(");
        lengthLable.setText(length == 1 ? "1 link!" : length + " links!");
    }
}
