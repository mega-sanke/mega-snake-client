package megaSnake;

import gui.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Game implements KeyListener, ActionListener {

    private Networker networker;
    private Frame frame;
    private Timer t;

    public Game(String ip, int i, int j) throws IOException {
        networker = new Networker(ip, this);
        frame = new Frame("Mega Snake", i, j, this);
        t = new Timer(500, this);
        t.start();

    }

    public static void main(String[] args) throws IOException {
        new Game("127.0.0.1", 30, 30);
    }

    public boolean isAlive() {
        return networker.getBoolean("alive");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.print("start");
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
        System.out.println("end");
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.update(networker.getSnake(), networker.getGates(), networker.getFood(), isAlive(), getLinkCount());
    }

    public int getLinkCount() {
        return networker.getInt("link-count");
    }

    public boolean isController() {
        return networker.getBoolean("controller");
    }

}
