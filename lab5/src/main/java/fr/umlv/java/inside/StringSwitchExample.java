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

	private static final MethodHandle STRING_EQUALS;

	static {
		var lookup = MethodHandles.lookup();
		try {
			STRING_EQUALS = lookup.findVirtual(String.class, "equals",
					MethodType.methodType(boolean.class, Object.class));
		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}

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

	private static MethodHandle createMHFromStrings2(String... strings) {

		var fallback = constant(int.class, -1);
		fallback = dropArguments(fallback, 0, String.class);

		var execution = fallback;

		for (var i = 0; i < strings.length; i++) {
			var target = MethodHandles.constant(int.class, i);
			target = dropArguments(target, 0, String.class);

			var test = insertArguments(STRING_EQUALS, 1, strings[i]);
			execution = guardWithTest(test, target, execution);

		}

		return execution;

	}

	public static int stringSwitch2(String string) {
		var mh = createMHFromStrings2("foo", "bar", "bazz");

		try {
			return (int) mh.invokeExact(string);

		} catch (RuntimeException | Error e) {
			throw e;
		} catch (Throwable t) {
			throw new UndeclaredThrowableException(t);
		}
	}

	// q3.

	public static int stringSwitch3(String string) {
		var mh = createMHFromStrings3("foo", "bar", "bazz");

		try {
			return (int) mh.invokeExact(string);

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
				SLOW_PATH = lookup.findVirtual(InliningCache.class, "slowPath",
						MethodType.methodType(int.class, String.class));
			} catch (NoSuchMethodException | IllegalAccessException e) {
				throw new AssertionError(e);
			}
		}

		private final java.util.List<String> matches;

		public InliningCache(String... matches) {
			super(MethodType.methodType(int.class, String.class));
			this.matches = List.of(matches);
			setTarget(MethodHandles.insertArguments(SLOW_PATH, 0, this));
		}

		private int slowPath(String value) {
			// var index = stringSwitch2(s);
			var index = matches.indexOf(value);

			// On veut changer l'arbre :
			var test = insertArguments(STRING_EQUALS, 1, value);
			var target = MethodHandles.constant(int.class, index);
			target = dropArguments(target, 0, String.class);
			var mh = MethodHandles.guardWithTest(test, target, getTarget());
			// si ca rate : setTarget du getTarget -> ne change rien
			setTarget(mh);

			return index;

		}
	}

}

/*
 * 
 * Dans Mutablecall site parmi mutable = method handle Au debut pas d'arbre,
 * pointe sur slowpath Puis si même cha de caract renvoie direct sinon slowPath
 * 
 * 
 * Inlining car machine génère instruction pour se souvenir
 */