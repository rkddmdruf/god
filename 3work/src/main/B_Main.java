package main;

public class B_Main {

	B_Main(boolean user, int number, String name){
		if(user==true) {
			new B_Umain(number);
		}else {
			new B_Tmain(number, name);
		}
	}
}
