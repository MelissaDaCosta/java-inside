package fr.umlv.java.inside.lab2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.Test;

public class TestJSON {
	public static class Alien {
		private final String planet;
		private final int age;

		public Alien(String planet, int age) {
			if (age <= 0) {
				throw new IllegalArgumentException("Too young...");
			}

			this.planet = Objects.requireNonNull(planet);
			this.age = age;
		}

		@JSONProperty("planet")	// Valeur de l'annotation = planet
		public String getPlanet() {
			return planet;
		}

		
		@JSONProperty("age")
		public int getAge() {
			return age;
		}
	}

	public static class Person {
		private final String firstName;
		private final String lastName;

		public Person(String firstName, String lastName) {
			this.firstName = Objects.requireNonNull(firstName);
			this.lastName = Objects.requireNonNull(lastName);
		}

		@JSONProperty("firstName")
		public String getFirstName() {
			return firstName;
		}

		@JSONProperty("lastName")
		public String getLastName() {
			return lastName;
		}
	}

	@Test
	public void testReturnPerson() {
		var person = new Person("John", "Doe");
		//System.out.println(MainJSON.toJSON(person));
		var expected = "{\"firstName\" : \"John\", \"lastName\" : \"Doe\"}";
		assertEquals(expected, MainJSON.toJSON(person));

	}
	
	@Test
	public void testReturnAlien() {
		var alien = new Alien("Mars", 21);
		//System.out.println(MainJSON.toJSON(alien));
		var expected = "{\"age\" : \"21\", \"planet\" : \"Mars\"}";
		assertEquals(expected, MainJSON.toJSON(alien));

	}
	
	
	public void testIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, ()->MainJSON.toJSON(new Alien("mars", -2)));

	}
	
	
	

}
