package fr.umlv.java.inside.lab2;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)	// C'est les méthodes qu'on annote
@Retention(RetentionPolicy.RUNTIME)	// visible a l'exécution

public @interface JSONProperty {
	String value() default "";
}
