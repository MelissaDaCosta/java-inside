package fr.umlv.javainside.lab4;

import static java.lang.invoke.MethodType.methodType;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Essaie suite du TP Note de 2018-2019 
 *
 */

public interface Logger2 {
  
    static enum LEVEL{DEBUG, WARNING, ERROR} ;
    
	public void log(String message);
	
	public void log(String message, LEVEL level);
	  
	

	public static Logger2 of(Class<?> declaringClass, BiConsumer<? super String, Logger2.LEVEL> biconsumer) {
		Objects.requireNonNull(declaringClass);
		Objects.requireNonNull(biconsumer);
		var mh = createLoggingMethodHandle(declaringClass, biconsumer);
		return new Logger2() {
			@Override
			public void log(String message) {
				log(message, LEVEL.WARNING);
			};
		

          @Override
          public void log(String message, LEVEL level) {
            Objects.requireNonNull(message);
            Objects.requireNonNull(level);
            try {
                mh.invokeExact(message, level);
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
		};
	}
	
	static final ClassValue<MutableCallSite> ADD_LEVEL = new ClassValue<MutableCallSite>() {
      
      @Override
      protected MutableCallSite computeValue(Class<?> arg0) {
        return new MutableCallSite(MethodHandles.constant(Logger2.LEVEL.class, Logger2.LEVEL.WARNING));
      }
    };
	
	public static void level(Class<?> declaringClass, Logger2.LEVEL level) {
	  ADD_LEVEL.get(declaringClass).setTarget(MethodHandles.constant(Logger2.LEVEL.class, level));
	}
		
	private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, BiConsumer<? super String, Logger2.LEVEL> biconsumer) {
		MethodHandle methodHandleAccept;
		MethodHandle methodHandleTest;
		var lookup = MethodHandles.lookup();
		try {
		    methodHandleTest = ENABLE_CALLSITES.get(declaringClass).dynamicInvoker();
			methodHandleAccept = lookup.findVirtual(BiConsumer.class, "accept", methodType(void.class, Object.class, Object.class));

		} catch (NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
		methodHandleAccept = methodHandleAccept.bindTo(biconsumer);
		// == methodhandle = MethodHandles.insertArguments(methodhandle, 0, consumer);   
		
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
		
	
	public static void enable(Class<?> declaringClass, boolean enable) {
		// update MethodHandles.constant(boolean.class, true) 
		// into MethodHandles.constant(boolean.class, enable)
		  ENABLE_CALLSITES.get(declaringClass).setTarget(MethodHandles.constant(boolean.class, enable));
		}
}
