package fr.umlv.java.inside.lab1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SwitchExampleTest {
	
	@Test
	void testSwitchDog() {
		assertEquals(1, SwitchExample.switchExample("dog"));
	}
	
	@Test
	void testSwitchCat() {
		assertEquals(2, SwitchExample.switchExample("cat"));
	}
	@Test
	void testSwitchDefault() {
		assertEquals(4, SwitchExample.switchExample("default"));
	}
	
	@Test
	void testSwitchNull() {
		assertThrows(NullPointerException.class, ()->SwitchExample.switchExample(null));
	}


}
