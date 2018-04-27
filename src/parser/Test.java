package parser;


interface i {
	default void fun(){
		
	}
}


class MyClass implements i {
	
}

class Temp<T extends i> {
	
}

public class Test {
	public static void main(String args[]){
		Temp<MyClass> temp = new Temp<MyClass>();
		System.out.println(123);
	}
}
