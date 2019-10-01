package fr.umlv.java.inside.lab2;

// Ne plus avoir Collectors.
import static java.util.stream.Collectors.joining;

import java.util.Arrays;

public class Main {

	private static String propertyName(String name) {
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}

	/*
	public static String toJSON(Person person) {
		return "{\n" + "  \"firstName\": \"" + person.getFirstName() + "\"\n" + "  \"lastName\": \""
				+ person.getLastName() + "\"\n" + "}\n";
	}

	public static String toJSON(Alien alien) {
		return "{\n" + "  \"planet\": \"" + alien.getPlanet() + "\"\n" + "  \"members\": \"" + alien.getAge() + "\"\n"
				+ "}\n";
	}
	*/
	public static String toJSON(Object object) {
		// Obtenir tous les getteurs	
		return Arrays.stream(object.getClass().getMethods())
					.filter(method->method.getName().startsWith("get"))
					.map(method->propertyName(method.getName()))
					.collect(joining(", ", "{", "}"));
	}

	public static void main(String[] args) {
		var person = new Person("John", "Doe");
		System.out.println(toJSON(person));
		
	}
}
