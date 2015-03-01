package megaSnakeOld;

public class Gate extends Link {

	private static final long serialVersionUID = -7197180970292578430L;

	private int code;

	public Gate(Link l, int code) {
		super(l);
		this.code = code;
		// TODO Auto-generated constructor stub
	}

	public Gate(int x, int y, int code) {
		super(x, y , false);
		this.code = code;
		// TODO Auto-generated constructor stub
	}
	
	public int getCode(){
		return code;
	}

}
