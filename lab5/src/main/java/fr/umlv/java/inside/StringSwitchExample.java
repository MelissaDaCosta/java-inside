package fr.umlv.java.inside;

public class StringSwitchExample {
	
	public static int stringSwitch(String st) {
		switch(st) {
			case "foo":
				return 0;
			case "bar":
				return 1;
			case "bazz":
				return 2;
			default:
				return -1;
		}
	}
	
	public static int stringSwitch2(String st) {
		switch(st) {
			case "foo":
				return 0;
			case "bar":
				return 1;
			case "bazz":
				return 2;
			default:
				return -1;
		}
	}

}
