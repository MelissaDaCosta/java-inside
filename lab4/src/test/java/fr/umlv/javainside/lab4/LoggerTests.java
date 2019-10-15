package fr.umlv.javainside.lab4;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import fr.umlv.javainside.lab4.Logger;

public class LoggerTests {
	
	 private static class Foo {
		private static final StringBuilder STRING_BUILDER = new StringBuilder();
		private static final Logger LOGGER = Logger.of(Foo.class, (msg)->{
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

}
