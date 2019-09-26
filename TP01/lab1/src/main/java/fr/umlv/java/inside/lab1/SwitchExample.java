package fr.umlv.java.inside.lab1;

public class SwitchExample {
	/*
	static int switchExample(String s) {
		  int result;
		  switch(s) {
		    case "dog":
		      result = 1;
		      break;
		    case "cat":
		      result = 2;
		      break;
		    default:
		      result = 4;
		      break;
		  }
		  return result;
		}
	*/
	static int switchExample(String s) {
		  return switch(s) {
		    case "dog" -> 1;
		    case "cat" -> 2;
		    default -> 4;
		  };
		}
		    
}
