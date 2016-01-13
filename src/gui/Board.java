package gui;


import java.awt.Graphics;

import javax.swing.JPanel;

import util.Slot;

@SuppressWarnings("serial")
public class Board extends JPanel {
	private Slot[][] slots;
	
	private final int SPACE, X_AXIS_SIZE, Y_AXIS_SIZE;


	public Board(int i, int j, Slot[][] slots, int space, int xAxis, int yAxis) {
		this.slots = slots;
		SPACE = space;
		X_AXIS_SIZE = xAxis;
		Y_AXIS_SIZE = yAxis;
	
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		fillGrid(g);
		

		for (Slot[] slots : slots) {
			for (Slot s : slots) {

				if (s != null) {
					
						
					s.fillLink(g, SPACE, X_AXIS_SIZE, Y_AXIS_SIZE);

				}
			}
		}
	}
	
	public  void update(){
//		for (Slot[] slots : slots) {
//			for (Slot s : slots) {
//
//				if (s != null && s.getClass().getSimpleName().equals("Gate")) {
//					
//						
//					
//
//				}
//			}
//		}
		repaint();
	}

	private void fillGrid(Graphics g) {
		for (int i = 1; i < slots.length; i ++) {
			g.drawLine(i * X_AXIS_SIZE, 0, i * X_AXIS_SIZE, slots[0].length * X_AXIS_SIZE);
		}

		for (int i = 1; i < slots[0].length; i ++) {
			g.drawLine(0, i * Y_AXIS_SIZE,  slots.length * Y_AXIS_SIZE, i * Y_AXIS_SIZE);
		}

	}

}
