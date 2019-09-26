package fr.umlv.java.inside.lab1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SwitchExampleTest {
	
	@Test
	public void testSwitchDog() {
		assertEquals(1, SwitchExample.switchExample("dog"));
	}
	
	@Test
	public void testSwitchCat() {
		assertEquals(2, SwitchExample.switchExample("cat"));
	}
	@Test
	public void testSwitchDefault() {
		assertEquals(4, SwitchExample.switchExample("default"));
	}
	
	@Test
	public void testSwitchNull() {
		assertThrows(NullPointerException.class, ()->SwitchExample.switchExample(null));
	}


}
