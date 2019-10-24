package fr.umlv.java.inside;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class StringSwitchExampleTests {

	// Exercice 1 - Switch on strings
	@Test
	public void testFoo() {
		assertAll(() -> assertEquals(0, StringSwitchExample.stringSwitch("foo")),
				() -> assertEquals(1, StringSwitchExample.stringSwitch("bar")),
				() -> assertEquals(2, StringSwitchExample.stringSwitch("bazz")),
				() -> assertEquals(-1, StringSwitchExample.stringSwitch("autre")));
	}

	// Tests Paramétrés :

	@ParameterizedTest
	@MethodSource("testWithMultipleMethods") // Méthode à appeler
	/*
	 * Cette méthode va effectuer le assertAll pour chaque fonction stocker dans le
	 * Stream renvoyer par testWithMultipleMethods.
	 */
	public void multipleMethod(ToIntFunction<String> function) {
		assertAll(() -> assertEquals(0, function.applyAsInt("foo")), () -> assertEquals(1, function.applyAsInt("bar")),
				() -> assertEquals(2, function.applyAsInt("bazz")),
				() -> assertEquals(-1, function.applyAsInt("autre")));

	}

	/*
	 * Renvoie un Stream contenant plusieurs interfaces fonctionnelles L'interface
	 * fonctionnelle choisie est ToIntFunction car les méthodes a tester renvoies un
	 * int
	 */
	static Stream<ToIntFunction<String>> testWithMultipleMethods() {
		return Stream.of(StringSwitchExample::stringSwitch, StringSwitchExample::stringSwitch2,
				StringSwitchExample::stringSwitch3); // Test plusieurs méthodes
	}
}
