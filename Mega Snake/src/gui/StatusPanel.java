package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel{
	JLabel aliveLable, lengthLable;
	
	public StatusPanel(int w, int h){
		setLayout(new GridLayout(1, 2));
		setSize(w,h);
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
	
	
	public void update(boolean alive, int length){
		aliveLable.setText(alive ? "Alive :)": "Dead :(");
		lengthLable.setText(length == 1 ? "1 link!" : length + " links!");
	}
}
