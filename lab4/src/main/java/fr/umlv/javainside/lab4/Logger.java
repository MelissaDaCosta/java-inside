package fr.umlv.javainside.lab4;

import static java.lang.invoke.MethodType.methodType;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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

	private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
		var lookup = MethodHandles.lookup();
		MethodHandle methodhandle;
		try {
			// (Consumer, Object) void, Consumer = this
			methodhandle = lookup.findVirtual(Consumer.class, "accept", methodType(void.class, Object.class));
		}catch(IllegalAccessException | NoSuchMethodException e) {
			throw new AssertionError(e);
		}
			methodhandle = MethodHandles.insertArguments(methodhandle, 0, consumer);	// == methodhandle.bindTo(consumer)
			// On veut (String) void
			methodhandle = methodhandle.asType(methodType(void.class, String.class));
			return methodhandle;
			
		
	}
}
