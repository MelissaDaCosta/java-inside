package fr.umlv.java.inside.lab2;

// Ne plus avoir Collectors.
import static java.util.stream.Collectors.joining;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
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
	
	private static Object callInvoke(Object obj, Method method) {
		
		try {
			return method.invoke(obj);
			
			// Pas les exceptions qui héritent de runtime : elles se propagent
		}catch(IllegalAccessException e) {
			throw new IllegalStateException(e);
		}catch(InvocationTargetException e) {
			var cause = e.getCause();
			if(cause instanceof RuntimeException)
				throw (RuntimeException) cause;	// Re-propage l'exception
			if(cause instanceof Error)
				throw (Error) cause;
			
			throw new UndeclaredThrowableException(cause);
				
		}
		
	}
	
	public static String toJSON(Object object) {
		// Obtenir tous les getteurs	
		/*
		return Arrays.stream(object.getClass().getMethods())
					.filter(method->method.getName().startsWith("get"))
					.map(method->propertyName(method.getName()))
					.collect(joining(", ", "{", "}"));
					*/
		return Arrays.stream(object.getClass().getMethods())
				.filter(method->method.getName().startsWith("get"))
				.map(method->{
					var property = propertyName(method.getName());
					var res =callInvoke(object, method);
					return "\""+property +"\"" + " : " + "\"" + res + "\"";
				})				
				.collect(joining(", ", "{", "}"));
	}

	public static void main(String[] args) {
		var person = new Person("John", "Doe");
		System.out.println(toJSON(person));
		
	}
}
