package fr.umlv.java.inside.lab2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainJSON {

	// 11. cache pour les méthodes
	
	  private static final ClassValue<Method[]> cache =new ClassValue<>() { //
		  //Redéfinie la méthode car ClassValue est une classe abstraite
		  
		  @Override 
		  protected Method[] computeValue(Class<?> type) { 
			  return type.getMethods(); 
		  } 
	  };
	 

	// 12. cache pour le nom des propriétés 
	 
	private static final ClassValue<Function<Object, String>> fullCache =new ClassValue<Function<Object, String>>() {
		
		@Override
		protected Function<Object, String> computeValue(Class<?> type)
		{
			var methods = Arrays.stream(type.getMethods())
					.filter(method -> method.isAnnotationPresent(JSONProperty.class))
					.sorted(Comparator.comparing(Method::getName))
					.collect(Collectors.toList());

			return object -> methods.stream()
					.map(method-> "\"" + propertyName(method.getName()) + "\"" + " : \"" + callInvoke(object, method) + "\"")
					.collect(Collectors.joining(", ", "{", "}"));
		}
	};
	

	private static String propertyName(String name) {
		return Character.toLowerCase(name.charAt(3)) + name.substring(4);
	}

	/*
	 * public static String toJSON(Person person) { return "{\n" +
	 * "  \"firstName\": \"" + person.getFirstName() + "\"\n" + "  \"lastName\": \""
	 * + person.getLastName() + "\"\n" + "}\n"; }
	 * 
	 * public static String toJSON(Alien alien) { return "{\n" + "  \"planet\": \""
	 * + alien.getPlanet() + "\"\n" + "  \"members\": \"" + alien.getAge() + "\"\n"
	 * + "}\n"; }
	 */

	private static Object callInvoke(Object obj, Method method) {

		try {
			return method.invoke(obj);

			// Pas les exceptions qui héritent de runtime : elles se propagent
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			var cause = e.getCause();
			if (cause instanceof RuntimeException)   // uncehcked exception : faute du programmeur -> à gérer 
				throw (RuntimeException) cause; // Re-propage l'exception
			if (cause instanceof Error)
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
		
		// Avec getMethods :
		/*
		return Arrays.stream(object.getClass().getMethods())
				.filter(method->method.getName().startsWith("get"))
				// Indique comment trier
				// getDeclaringClass donne le type de retour, on ne veut pas qu'il soit egal à Object.class
				//.filter(Predicate.not(method->method.getDeclaringClass().equals(Object.class)))
				.filter(method->method.isAnnotationPresent(JSONProperty.class))
				.sorted(Comparator.comparing(Method::getName))
				.map(method->{
					
					var valueAnnotation = method.getAnnotation(JSONProperty.class).value();
					// Si la value a été remplie on prend sa valeur, sinon on prend la valeur de propertyName
					var property = valueAnnotation.isEmpty() ? propertyName(method.getName()) : valueAnnotation;
					var res =callInvoke(object, method);
					return "\""+property +"\"" + " : " + "\"" + res + "\"";
				})				
				.collect(joining(", ", "{", "}"));
				*/ 
		
		// 11. Avec le cache pour les méthodes :
		
		/*
		return Arrays.stream(cache.get(object.getClass()))
				.filter(method->method.getName().startsWith("get"))
				// Indique comment trier
				// getDeclaringClass donne le type de retour, on ne veut pas qu'il soit egal à Object.class
				//.filter(Predicate.not(method->method.getDeclaringClass().equals(Object.class)))
				.filter(method->method.isAnnotationPresent(JSONProperty.class))
				.sorted(Comparator.comparing(Method::getName))
				.map(method->{
					
					var valueAnnotation = method.getAnnotation(JSONProperty.class).value();
					// Si la value a été remplie on prend sa valeur, sinon on prend la valeur de propertyName
					var property = valueAnnotation.isEmpty() ? propertyName(method.getName()) : valueAnnotation;
					var res =callInvoke(object, method);
					return "\""+property +"\"" + " : " + "\"" + res + "\"";
				})				
				.collect(joining(", ", "{", "}"));
			*/	
		
		// 12. cache pour le nom des propriétés
		return fullCache.get(object.getClass())	// retourne Function qui prend un Object et retourne String
				.apply(object);
	}

	public static void main(String[] args) {
		
		
	}
}

/*
 * 10. L'appel à getMethods est lent car il fait des copies de chaque élément
 * contenue dans le tableau Methods car Methods est mutable car il existe des
 * méthodes héritées set.
 */
