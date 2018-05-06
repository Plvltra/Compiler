package parser;

import java.util.ArrayList;

interface Base{
	void fun();
}

interface Derive1 extends Base {
	
}

class Derive2 implements Base {

	public int fun(int a) {
		return 3 ;
	}
	
	@Override
	public void fun() {
		System.out.println(2);
	}
	
}

class Entity extends Derive2 implements Derive1 {
	
	public void fun(){
		System.out.println(1);
	}
}

public class Test {
	public static void main(String[] args) {
		
	}
}
