package fr.umlv.javainside.lab4;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class LoggerTests {
	
	 private static class Foo {
		private static final StringBuilder STRING_BUILDER = new StringBuilder();
		private static final Logger LOGGER = Logger.of(Foo.class, (msg)->{
			STRING_BUILDER.setLength(0);	// vide le string builder
			STRING_BUILDER.append(msg);		// le remplie
		});
	}
	 
	 private static class Bar {
		 private static final StringBuilder STRING_BUILDER = new StringBuilder();
		 private static final Logger LOGGER = Logger.fastOf(Bar.class, (msg)->{
				STRING_BUILDER.setLength(0);	// vide le string builder
				STRING_BUILDER.append(msg);		// le remplie
			});
		}

	
	@Test
	void testLog() {
		Foo.LOGGER.log("test");
		assertEquals("test", Foo.STRING_BUILDER.toString());
	}
	
	@Test
	void testOfNull() {
		assertAll(
				()->assertThrows(NullPointerException.class, ()-> Logger.of(null, (msg)->{}).log("")),
				()->assertThrows(NullPointerException.class, ()->Logger.of(Foo.class, null).log(""))
				);
	}
	
	@Test
	void testLogNull() {
		assertThrows(NullPointerException.class, ()-> Logger.of(Foo.class, (msg)->{}).log(null));

	}

	// 10.
	
	@Test
	void testThreeLoggers1() {
		// émet un message :
		Bar.LOGGER.log("message");
		assertEquals("message", Bar.STRING_BUILDER.toString());
		// n'émet pas de message :
		assertThrows(NullPointerException.class, ()-> Logger.fastOf(Bar.class, (msg)->{}).log(null));
		// n'est pas enable : disable
		Logger.enable(Bar.class, false);

	}
	
}
