package fr.umlv.java.inside.lab2;

import java.util.Objects;

public class Person {
	  private final String firstName;
	  private final String lastName;

	  public Person(String firstName, String lastName) {
	    this.firstName = Objects.requireNonNull(firstName);
	    this.lastName = Objects.requireNonNull(lastName);
	  }
	  
	  public String getFirstName() {
	    return firstName;
	  }
	  public String getLastName() {
	    return lastName;
	  }
	}
	   