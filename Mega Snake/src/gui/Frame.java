package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import util.Slot;

public class Frame extends JFrame{
	
	public static final int X_AXIS_SIZE = 20, Y_AXIS_SIZE = 20, SPACE = 2;

	StatusPanel status;
	Board board;
	
	public Frame(String title, int i, int j, Slot[][] slots, KeyListener key) {
		super(title);
		
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dim.setSize(i * X_AXIS_SIZE, j * Y_AXIS_SIZE + 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(0, 0);
		setSize(dim);
		
		//status
		status = new StatusPanel(i * X_AXIS_SIZE, 50);
		
		//board
		board = new Board(i, j, slots, SPACE, X_AXIS_SIZE, Y_AXIS_SIZE);
		addKeyListener(key);
		
		//adds
		add(status, BorderLayout.NORTH);
		add(board);
		
		
		setVisible(true);
		setFocusable(true);
		validate();
	}
	
	
	public void update(boolean alive, int length){
		status.update(alive, length);
		board.update();
	}
}
