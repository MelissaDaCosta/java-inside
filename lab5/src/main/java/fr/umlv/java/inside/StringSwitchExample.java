package fr.umlv.java.inside;

import static java.lang.invoke.MethodHandles.constant;
import static java.lang.invoke.MethodHandles.dropArguments;
import static java.lang.invoke.MethodHandles.guardWithTest;
import static java.lang.invoke.MethodHandles.insertArguments;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

public class StringSwitchExample {

	// Exercice 1 - Switch on strings

	private static final MethodHandle STRING_EQUALS;

	// bloc static pour être appelée une fois
	static {
		var lookup = MethodHandles.lookup();
		try {
			// stocke la méthode equals de la classe String
			// qui renvoie un boolean et prend en argument un Object
			STRING_EQUALS = lookup.findVirtual(String.class, "equals",
					MethodType.methodType(boolean.class, Object.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError(e); // erreur "grave"
		}
	}

	// switch de base
	public static int stringSwitch(String string) {
		switch (string) {
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

	// Exercice 2 - Inlining Cache

	private static MethodHandle createMHFromStrings2(String... strings) {

		var fallback = constant(int.class, -1); // Constante qui vaut -1
		fallback = dropArguments(fallback, 0, String.class); // Retire a l'éxécution la String donnée au test

		var execution = fallback; // Si aucun argument (on ne passe pas la boucle fo)r -> renvoie constante
									// fallback -1

		for (var i = 0; i < strings.length; i++) {
			var target = MethodHandles.constant(int.class, i); // Constante qui vaut la position de la string dans le
																// varargs
			target = dropArguments(target, 0, String.class); // Retire a l'éxécution la String donnée au test

			// string.equals(argument ajoutée qui est strings[i] le varargs)
			var test = insertArguments(STRING_EQUALS, 1, strings[i]);
			execution = guardWithTest(test, target, execution);
		}
		return execution;
	}

	// switch 2 qui utilise method handle
	public static int stringSwitch2(String string) {
		var mh = createMHFromStrings2("foo", "bar", "bazz");

		try {
			return (int) mh.invokeExact(string); // appelle la méthode equals

		} catch (RuntimeException | Error e) {
			throw e;
		} catch (Throwable t) {
			throw new UndeclaredThrowableException(t);
		}
	}

	// switch 3 pour utilise inling cache
	public static int stringSwitch3(String string) {
		var mh = createMHFromStrings3("foo", "bar", "bazz");

		try {
			return (int) mh.invokeExact(string); // appelle la méthode equals

		} catch (RuntimeException | Error e) {
			throw e;
		} catch (Throwable t) {
			throw new UndeclaredThrowableException(t);
		}
	}

	public static MethodHandle createMHFromStrings3(String... matches) {
		return new InliningCache(matches).dynamicInvoker();
	}

	
	static class InliningCache extends MutableCallSite {
		private static final MethodHandle SLOW_PATH;

		static {
			var lookup = MethodHandles.lookup();
			try {
				// Récupère la méthode slowPath de la classe InliningCache qui renvoie un int et prend un String
				SLOW_PATH = lookup.findVirtual(InliningCache.class, "slowPath",
						MethodType.methodType(int.class, String.class));
			} catch (NoSuchMethodException | IllegalAccessException e) {
				throw new AssertionError(e);
			}
		}

		private final List<String> matches; // List des paramètres

		private InliningCache(String... matches) {	//  On le met private dans un second temps
			/*
			super(MethodType.methodType(int.class, String.class));	// Appelle le constructeur de MutableCallSite
			// Crée un MutableCallSite vierge avec un type de retour int et un argument String
			this.matches = List.of(matches);
			// maj le MutableCallSite pour qu'il devienne un MethodHandle
			// de la méthode slowPath avec l'argument this passé en position 0
			setTarget(MethodHandles.insertArguments(SLOW_PATH, 0, this));
			*/
			
			this(List.of(matches));	// délègue au constructeur principal
		}
		
		// Autre constructeur pour le second arbre :
		public InliningCache(List<String> matches) {
			super(MethodType.methodType(int.class, String.class));	// Appelle le constructeur de MutableCallSite
			// Crée un MutableCallSite vierge avec un type de retour int et un argument String
			this.matches = matches;
			// maj le MutableCallSite pour qu'il devienne un MethodHandle
			// de la méthode slowPath avec l'argument this passé en position 0
			setTarget(MethodHandles.insertArguments(SLOW_PATH, 0, this));
		}

		private int slowPath(String value) {
			// var index = stringSwitch2(s);
			var index = matches.indexOf(value);

			// On veut changer l'arbre :
			var test = insertArguments(STRING_EQUALS, 1, value);	// Appelle equals avec la value donnée
			var target = MethodHandles.constant(int.class, index);
			target = dropArguments(target, 0, String.class);
			
			
			//var mh = MethodHandles.guardWithTest(test, target, getTarget());
			// Dans un second temps :
			var mh = MethodHandles.guardWithTest(test, target, new InliningCache(matches).dynamicInvoker());
			// si guardWithTest rate : setTarget du getTarget -> ne change rien
			setTarget(mh);

			return index;

		}
	}

}

/*
 * 
 * Dans Mutablecallsite parmi mutable = method handle 
 * Au debut pas d'arbre, pointe sur slowpath Puis si même chaîne de caract renvoie direct sinon slowPath
 * 
 * 
 * Inlining car machine génère instruction pour se souvenir
 * 
 * 5.
 * Sachant que statistiquement la première chaine de caractère que l'on demande est celle qui est le plus demandée.
 * -> Il faudrait construire l'arbre en commençant par le equals de la première chaîne de caractèer donnée à createMHFromStrings.
 * 
 * Arbre :
 * MutableCallSite 
 * /1\
 * -> retourne valeur si dispo
 * -> slowPath sinon /!\ On doit ajouter les nouvelles branches de l'arbre ici car avant il se mettait ici : /1\
 * On doit donc mettre ici une boite mutable
 * 
 */