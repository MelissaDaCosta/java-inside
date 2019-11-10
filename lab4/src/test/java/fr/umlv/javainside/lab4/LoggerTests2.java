package fr.umlv.javainside.lab4;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 * Essaie suite du TP Note de 2018-2019 
 *
 */

public class LoggerTests2 {
	
	 private static class Foo {
		private static final StringBuilder STRING_BUILDER = new StringBuilder();
		private static final Logger2 LOGGER = Logger2.of(Foo.class, (msg, level)->{
			STRING_BUILDER.setLength(0);	// vide le string builder
			STRING_BUILDER.append(msg);		// le remplie
		});
	}
	 
	 private static class Bar {
		 private static final StringBuilder STRING_BUILDER = new StringBuilder();
		 private static final Logger2 LOGGER = Logger2.of(Bar.class, (msg, level)->{
				STRING_BUILDER.setLength(0);	// vide le string builder
				STRING_BUILDER.append(msg);		// le remplie
			});
		}

	
	@Test
	public void testLog() {
		Foo.LOGGER.log("test", Logger2.LEVEL.ERROR);
		assertEquals("test", Foo.STRING_BUILDER.toString());
	}
	
	@Test
	public void testOfNull() {
		assertAll(
				()->assertThrows(NullPointerException.class, ()-> Logger2.of(null, (msg, level)->{}).log("", Logger2.LEVEL.WARNING)),
				()->assertThrows(NullPointerException.class, ()->Logger2.of(Foo.class, null).log(""))
				);
	}
	
	@Test
	public void testLogNull() {
		assertThrows(NullPointerException.class, ()-> Logger2.of(Foo.class, (msg, level)->{}).log(null, null));

	}
	
	// 7.
	
	@Test
	public void testDisableLogger() {
	  Logger2.enable(Bar.class, false); // disable logger
	  Bar.LOGGER.log("testDisableLogger", Logger2.LEVEL.ERROR);
	  assertEquals("", Bar.STRING_BUILDER.toString());
	}
	
	@Test
    public void testEnableLogger() {
      Logger2.enable(Foo.class, true); // enable logger
      Foo.LOGGER.log("testEnableLogger", Logger2.LEVEL.ERROR);
      assertEquals("testEnableLogger", Foo.STRING_BUILDER.toString());
    }	
}
