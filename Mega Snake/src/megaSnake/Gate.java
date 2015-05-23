package megaSnake;

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
	
	

}
