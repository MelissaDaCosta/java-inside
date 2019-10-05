package fr.umlv.java.inside.lab2;

import java.lang.annotation.*;

@Documented	// Spécifie l'annotation dans la javadoc
@Target(ElementType.METHOD)	// C'est les méthodes qu'on annote
@Retention(RetentionPolicy.RUNTIME)	// Visible a l'exécution

public @interface JSONProperty {
	String value() default "";
}
