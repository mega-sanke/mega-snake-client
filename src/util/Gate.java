package util;

import java.awt.Color;
import java.awt.Graphics;

public class Gate extends Slot{
	
	private final int code;
	
	public Gate(int x, int y, int code) {
		super(x, y);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	@Override
	public String toString(){
		return "Gate{code="+code+"}";
	}
	
	public void fillLink(Graphics g,  int space, int x_size, int y_size) {
		// TODO Auto-generated method stub
		super.fillLink(g, Color.RED, space, x_size, y_size);
	}
	
	

}
