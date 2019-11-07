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
	public void testLog() {
		Foo.LOGGER.log("test");
		assertEquals("test", Foo.STRING_BUILDER.toString());
	}
	
	@Test
	public void testOfNull() {
		assertAll(
				()->assertThrows(NullPointerException.class, ()-> Logger.of(null, (msg)->{}).log("")),
				()->assertThrows(NullPointerException.class, ()->Logger.of(Foo.class, null).log(""))
				);
	}
	
	@Test
	public void testLogNull() {
		assertThrows(NullPointerException.class, ()-> Logger.of(Foo.class, (msg)->{}).log(null));

	}
	
	// 7.
	
	@Test
	public void testDisableLogger() {
	  Logger.enable(Bar.class, false); // disable logger
	  Bar.LOGGER.log("testDisableLogger");
	  assertEquals("", Bar.STRING_BUILDER.toString());
	}
	
	@Test
    public void testEnableLogger() {
      Logger.enable(Foo.class, true); // enable logger
      Foo.LOGGER.log("testEnableLogger");
      assertEquals("testEnableLogger", Foo.STRING_BUILDER.toString());
    }	
}
