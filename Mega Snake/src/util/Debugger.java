package util;

public class Debugger {
	
	private static boolean debugging;
	
	public static void setMode(boolean debugging){
		Debugger.debugging = debugging;
	}
	
	public static void print(Object o){
		if (debugging) {
			System.out.print(o);
		}
	}
	
	public static void println(Object o){
		if (debugging) {
			System.out.println(o);
		}
	}
	
	public static void println(){
		println("");
	}
	

}
