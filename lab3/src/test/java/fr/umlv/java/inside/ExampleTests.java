package fr.umlv.java.inside;

import static java.lang.invoke.MethodType.methodType;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.print.attribute.IntegerSyntax;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


public class ExampleTests {

	@Tag("Q3")
	@Test
	public void getAStaticHello() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		var method = Example.class.getDeclaredMethod("aStaticHello", int.class);	// recupérer methode prive
		method.setAccessible(true);	// sinon on ne peut pas l'appelée
		assertEquals("question 1", method.invoke(null, 1));
	}
	
	@Tag("Q3")
	@Test
	public void getAnInstanceHello() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		var method = Example.class.getDeclaredMethod("anInstanceHello", int.class);
		method.setAccessible(true);
		assertEquals("question 2", method.invoke(new Example(), 2));

	}
	
	@Tag("Q4")
	@Test
	public void callAStaticHello() throws Throwable {
		var lookup = MethodHandles.lookup();	// donne le lookup courant
		var lookupPrivate = MethodHandles.privateLookupIn(Example.class, lookup);	// Accède aux méthodes privée de la class Example
		var methodHandle = lookupPrivate.findStatic(Example.class, "aStaticHello", methodType(String.class, int.class));	// donne un pointeur de fonction
		// Oblige de donner le type de retour d'invoke : regarde qu'il est exactement la même signature
		assertEquals("question 4", (String) methodHandle.invokeExact(4));
		
	}
	
	@Tag("Q5")
	@Test
	public void callAnInstanceHello() throws Throwable  {
		var lookup = MethodHandles.lookup();	// donne le lookup courant
		var lookupPrivate = MethodHandles.privateLookupIn(Example.class, lookup);	// Accède aux méthodes privée de la class Example
		var methodHandle = lookupPrivate.findVirtual(Example.class, "anInstanceHello", methodType(String.class, int.class));
		assertEquals("question 5", (String)methodHandle.invokeExact(new Example(), 5));
		
	}
	
	@Tag("Q6")
	@Test
	public void anInstanceHelloInsertArgs() throws Throwable {
		var lookup = MethodHandles.lookup();	// donne le lookup courant
		var lookupPrivate = MethodHandles.privateLookupIn(Example.class, lookup);	// Accède aux méthodes privée de la class Example
		var methodHandle = lookupPrivate.findVirtual(Example.class, "anInstanceHello", methodType(String.class, int.class));
		// Insert l'argument 6 à la position 1 car la position 0 est occupé pas le nom de la classe
		var methodHandleInsert = MethodHandles.insertArguments(methodHandle, 1, 6);
		assertEquals("question 6", (String) methodHandleInsert.invokeExact(new Example()));
	}
	
	@Tag("Q7")
	@Test
	public void anInstanceHelloDropArguments() throws Throwable {
		var lookup = MethodHandles.lookup();	// donne le lookup courant
		var lookupPrivate = MethodHandles.privateLookupIn(Example.class, lookup);	// Accède aux méthodes privée de la class Example
		var methodHandle = lookupPrivate.findVirtual(Example.class, "anInstanceHello", methodType(String.class, int.class));
		
		// Retire l'argument à la position 0 qui est le nom de la class
		var methodeHandleDrop = MethodHandles.dropArguments(methodHandle, 0, Example.class);
		// appelle la méthode en ajoutant le nom de la classe (car il a été supprimé avant) et 7
		assertEquals("question 7", (String) methodeHandleDrop.invokeExact(new Example(), new Example(), 7));
		
	}

	@Tag("Q8")
	@Test
	public void anInstanceHelloAsType() throws Throwable{
		var lookup = MethodHandles.lookup();	// donne le lookup courant
		var lookupPrivate = MethodHandles.privateLookupIn(Example.class, lookup);	// Accède aux méthodes privée de la class Example
		var methodHandle = lookupPrivate.findVirtual(Example.class, "anInstanceHello", methodType(String.class, int.class));
		
		// nouveaux arguments de la méthode appelée
		// il va faire la diff avec le methodHandle de base et va convertir l'Integer reçu en int
		var methodHandleAsType = methodHandle.asType(methodType(String.class, Example.class, Integer.class));
		
		assertEquals("question 8", (String) methodHandleAsType.invokeExact(new Example(), Integer.valueOf((8))));
		
	}
	
	@Tag("Q9")
	@Test
	public void callConstant() throws Throwable{
		// crée une nouvelle méthode qui ne prend rien en argument et qui renvoie une constante int qui vaut 9
		var methodHandleConstant = MethodHandles.constant(int.class, 9);
		assertEquals(9, (int)methodHandleConstant.invokeExact());
	}
	
	@Tag("Q10")
	@Test
	public void callGuardWithTest() throws Throwable{
		// Renvoie 1
		var methodHandleTarget = MethodHandles.constant(int.class, 1);
		
		// Renvoie -1
		var methodHandleFallback = MethodHandles.constant(int.class, -1);
		
		var lookup = MethodHandles.lookup();	
		// Appelle la méthode equals de la classe String
		// Retoune un boolean et prend un object
		var methodHandleTest = lookup.findVirtual(String.class, "equals", methodType(boolean.class, Object.class));

		// Supprime les arguments des constantes renvoyés car nous n'avons pas besoin 
		// des arguments String et Object rajoutés par la méthodeHandleTest lors du guardWithTest
		// car quand on fait un equals : String.equals(Object)
		var dropForTarget = MethodHandles.dropArguments(methodHandleTarget, 0, String.class, Object.class);
		var dropForFallback = MethodHandles.dropArguments(methodHandleFallback, 0, String.class, Object.class);
		
		// Effectue le guardTest
		var guardTest = MethodHandles.guardWithTest(methodHandleTest, dropForTarget, dropForFallback);
		
		// Donne comme argument la string sur laquelle la méthode equals va être effectué
		var execution = MethodHandles.insertArguments(guardTest, 1, "foo");
		
		assertEquals(1, (int) execution.invokeExact("foo"));
		
		assertEquals(-1, (int) execution.invokeExact("pasfoo"));
		
	}
		
	@Test
	public void revisions() throws Throwable {
		/*
		var lookup = MethodHandles.lookup();
		var privatelookup = MethodHandles.privateLookupIn(Example.class, lookup);
		//var met1 = privatelookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
		var met1 = privatelookup.findStatic(Example.class, "aStaticHello", MethodType.methodType(String.class, int.class));
		var expected = "question 42";
		var met = met1.asType(MethodType.methodType(String.class, Integer.class));
		var met2 = MethodHandles.insertArguments(met, 0, Integer.valueOf(42));
		*/
		
		var lookup = MethodHandles.lookup();
		var met = lookup.findVirtual(String.class, "equals", MethodType.methodType(boolean.class, Object.class));
	//	var test1 = met.asType(methodType(boolean.class, Object.class, Object.class));
		//var test = MethodHandles.insertArguments(test1, 1, "foo");
		
		var fallback1 = MethodHandles.constant(int.class, -1);
		var target1 = MethodHandles.constant(int.class, 1);
		var fallback2 = MethodHandles.dropArguments(fallback1, 0, String.class);
		var target2 = MethodHandles.dropArguments(target1, 0, String.class);
		var fallback = MethodHandles.dropArguments(fallback2, 1, Object.class);
		var target = MethodHandles.dropArguments(target2, 1, Object.class);
		var guard = MethodHandles.guardWithTest(met, target, fallback);
		var exec = MethodHandles.insertArguments(guard, 1, "foo");
		assertEquals(1, (int) exec.invokeExact("foo"));
		
	}
}

/*
 * 4.
 * La classe lookup sert à connaître les visibilité 
 * privateLokkupIn est équivalent à setAccessible
 * 
 * methode virtuelle = methode d'instance ou d'interface, polymorphisme
 */

