package util;

public enum Winds {
	NORTH, EAST, SOUTH, WEST;
	
	private boolean occupied;
	
	
	public boolean isFree(){
		return occupied;
	}
	
	public void conquer(){
		occupied = true;
	}
	
	public void leave(){
		occupied = false;
	}
	
	public Winds getNeg(){
		switch (this) {
		case NORTH:
			return SOUTH;
		case EAST:
			return WEST;
		case SOUTH:
			return NORTH;
		case WEST:
			return EAST;
		}
		return null;
	}
}
