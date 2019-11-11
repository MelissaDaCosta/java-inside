package fr.umlv.javainside.lab4;

import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;
import java.util.function.Consumer;

// Ctrl + shift + m : import static

public interface Logger {
	public void log(String message);

	public static Logger of(Class<?> declaringClass, Consumer<? super String> consumer) {
		Objects.requireNonNull(declaringClass);
		Objects.requireNonNull(consumer);
		var mh = createLoggingMethodHandle(declaringClass, consumer);
		return new Logger() {
			@Override
			public void log(String message) {
				Objects.requireNonNull(message);
				try {
					mh.invokeExact(message);
				} catch (Throwable t) {
					if (t instanceof RuntimeException) {
						throw (RuntimeException) t;
					}
					if (t instanceof Error) {
						throw (Error) t;
					}
					throw new UndeclaredThrowableException(t);
				}
			}
		};
	}
	
	// 3. 
	/*
	private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
		var lookup = MethodHandles.lookup();
		MethodHandle methodhandle;
		try {
			// (Consumer, Object) void, Consumer = this
			// Consumer ne retourne rien (void.class)et prend qlqchose (Object.class)
			methodhandle = lookup.findVirtual(Consumer.class, "accept", methodType(void.class, Object.class));
		}catch(IllegalAccessException | NoSuchMethodException e) {
			throw new AssertionError(e);
		}
		    // On appelle accept avec : consumer.accept. Donc on ajoute l'argument consumer
			methodhandle = MethodHandles.insertArguments(methodhandle, 0, consumer);	// == methodhandle.bindTo(consumer)
			// On veut que le consumer prenne des string (String) void
			methodhandle = methodhandle.asType(methodType(void.class, String.class));
			return methodhandle;		
	}
	*/	
	
	public static Logger fastOf(Class<?> declaringClass, Consumer<? super String> consumer) {
		Objects.requireNonNull(declaringClass);
		Objects.requireNonNull(consumer);
		var mh = createLoggingMethodHandle(declaringClass, consumer);
		// <=> méthode log
		return (message)->{
			Objects.requireNonNull(message);
			try {
				mh.invokeExact(message);
			} catch (Throwable t) {
				if (t instanceof RuntimeException) {
					throw (RuntimeException) t;
				}
				if (t instanceof Error) {
					throw (Error) t;
				}
				throw new UndeclaredThrowableException(t);
			}
		};
	}

	// 7.
		
	private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
		MethodHandle methodHandleAccept;
		MethodHandle methodHandleTest;
		var lookup = MethodHandles.lookup();
		try {
		    methodHandleTest = ENABLE_CALLSITES.get(declaringClass).dynamicInvoker();
		    // (Consumer, Object) void, Consumer = this
            // Consumer ne retourne rien (void.class)et prend qlqchose (Object.class)
			methodHandleAccept = lookup.findVirtual(Consumer.class, "accept", methodType(void.class, Object.class));

		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
		// On appelle accept avec : consumer.accept. Donc on ajoute l'argument consumer
		methodHandleAccept = methodHandleAccept.bindTo(consumer);
		// == methodhandle = MethodHandles.insertArguments(methodhandle, 0, consumer);   
		
		// On veut que le consumer prenne des string (String) void
		methodHandleAccept = methodHandleAccept.asType(methodType(void.class, String.class));
		var fallback = MethodHandles.empty(methodType(void.class, String.class));
		return MethodHandles.guardWithTest(methodHandleTest, methodHandleAccept, fallback);
	}

	
	//private static final ClassValue<MutableCallSite> ENABLE_CALLSITES = new ClassValue<MutableCallSite>() {
	static final ClassValue<MutableCallSite> ENABLE_CALLSITES = new ClassValue<MutableCallSite>() {
		  protected MutableCallSite computeValue(Class<?> type) {
		    return new MutableCallSite(MethodHandles.constant(boolean.class, true));
		  }
		};
		
	/*
	public static void enable(Class<?> declaringClass, boolean enable) {
		// update MethodHandles.constant(boolean.class, true) 
		// into MethodHandles.constant(boolean.class, enable)
		  ENABLE_CALLSITES.get(declaringClass).setTarget(MethodHandles.constant(boolean.class, enable));
		}
	*/
	
		/*
		 * La classe MutableCallSite est asynchrone. Un modification peut
          ne pas être prise en compte par les autres Threads : syncAll pour forcer les threads à maj leurs valeurs
		 */
	public static void enable(Class<?> declaringClass, boolean enable) {
	  MutableCallSite mutableCallSites[] = new MutableCallSite[] {ENABLE_CALLSITES.get(declaringClass)};
	  MutableCallSite.syncAll(mutableCallSites);   // Pour que ca fonctionne avec plusieurs threads
	  mutableCallSites[0].setTarget(MethodHandles.constant(boolean.class, enable));
  }
}
