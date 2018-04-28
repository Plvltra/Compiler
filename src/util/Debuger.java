package util;

public class Debuger {
	boolean debug;
	
	public Debuger(boolean debug) {
		this.debug = debug;
	}
	public void print(String s){
		if(debug){
			System.out.print(s);
		}
	}
	public void println(String s){
		if(debug){
			System.out.println(s);
		}
	}
	public void print(int s){
		if(debug){
			System.out.print(s);
		}
	}
	public void println(int s){
		if(debug){
			System.out.println(s);
		}
	}

	public void println(){
		if(debug){
			System.out.println();
		}
	}
}
