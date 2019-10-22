package fr.umlv.java.inside;

import static java.lang.invoke.MethodHandles.constant;
import static java.lang.invoke.MethodHandles.dropArguments;
import static java.lang.invoke.MethodHandles.guardWithTest;
import static java.lang.invoke.MethodHandles.insertArguments;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.UndeclaredThrowableException;

public class StringSwitchExample {
	
	private static final MethodHandle STRING_EQUALS;
	
	static {
		var lookup = MethodHandles.lookup();	
		try {
			STRING_EQUALS = lookup.findVirtual(String.class, "equals", MethodType.methodType(boolean.class, Object.class));
		}catch(NoSuchMethodException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}
	
	public static int stringSwitch(String string) {
		switch(string) {
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
	
	private static MethodHandle createMHFromStrings2(String ... strings){
		
		var fallback = constant(int.class, -1);
		fallback = dropArguments(fallback, 0, String.class);
		
		var execution = fallback;
		
				
		for(var i=0; i<strings.length; i++) {
			var target = MethodHandles.constant(int.class, i);
			target = dropArguments(target, 0, String.class);
			
			var test= insertArguments(STRING_EQUALS, 1, strings[i]);
			execution = guardWithTest(test, target, execution); 
			
		}
		
		return execution;
		
	}
	
	public static int stringSwitch2(String string){
	    var mh = createMHFromStrings2("foo", "bar", "bazz");
	    	    
	    try {
	    	return (int)mh.invokeExact(string);
	    	
	    }catch(RuntimeException | Error e) {
	    	throw e;
	    }catch(Throwable t) {
			throw new UndeclaredThrowableException(t);
		}
	}

}
