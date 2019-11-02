package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.List;

public class Example1 {

	private final static Object lock = new Object();
	public static void main(String[] args) {
		
		/*
		var continuationScope = new ContinuationScope("hello1");
		var continuation = new Continuation(continuationScope, ()->{
			//synchronized(lock) {
			//	Continuation.yield(continuationScope);	// Redonne la main au thread main
				
			//}
			//System.out.println(Continuation.getCurrentContinuation(continuationScope));
			Continuation.yield(continuationScope);
			System.out.println("hello continuation");
		});
		
		continuation.run();
		continuation.run();	// Reprend l'exécution de la continuation après le yield
		*/
		
		// 10.
		
		var scope = new ContinuationScope("scope");
		
		var continuation1 = new Continuation(scope, () -> {
	        System.out.println("start 1");
	        Continuation.yield(scope);
	        System.out.println("middle 1");
	        Continuation.yield(scope);
	        System.out.println("end 1");
	      });
	      var continuation2 = new Continuation(scope, () -> {
	        System.out.println("start 2");
	        Continuation.yield(scope);
	        System.out.println("middle 2");
	        Continuation.yield(scope);
	        System.out.println("end 2");
	      });
	      var contiuation3 = new Continuation(scope, () -> {
		        System.out.println("start 3");
		        Continuation.yield(scope);
		        System.out.println("middle 3");
		        Continuation.yield(scope);
		        System.out.println("end 3");
		      });
	      
	      var list = List.of(continuation1, continuation2, contiuation3);
	      
	      // On veut 1, 2, 1, 2 ... -> Utilise ArrayDeque
	      
	      var deque = new ArrayDeque<Continuation>(list);
	    	
	      while(!deque.isEmpty()) {
	    	  var continuation = deque.poll();	// Retire l'élément
	    	  continuation.run();
	    	  if(!continuation.isDone())
	    		  deque.offer(continuation);	// Remettre la continuation en fin de deque
	      }
	      
	      // Ici : 1 seul thread main qui exécute 3 continuation

	}

}


/*
export JAVA_HOME=/home/ens/forax/java-inside/jdk-14-loom

Une continuation est une sorte de fonction.
Au lieu de sortir avec return, on peut sortir quand on veut et revenir dedans.

4.
Avec yield -> n'affiche plus "hello continuation"
Si on ajoute un appelle à run -> l'affiche de nouveau

5.
Quand on fait un yield : sauvegarde la pile d'exécution 
Quand on fait run : remet le bout de la pile d'exécution

6.
Si on appelle run 2 fois sans appelle à yield -> affiche hello continutation puis IllegalStateException

8.
A l'intérieur d'un runnable on peut demander la continuation courante.
Il y peut y avoir plusieurs continuation les unes dans les autres avec des scopes différents.
Si yield : dans qu'elle scope va-t-il ?
Donc on doit lui donner

9.
Si synchronized autour de yield -> IllegalStateException
Car dans synchronized prend le jeton.
Le jeton renvoyer ne sera pas rendu par le même thread que celui qui a pris le jeton.

11.
Différence thread et continuation :
Dans les 2 cas : exécute runnable 1 seule fois.

Thread :
Un thread "contient" continuation, peut en exécuter plusieurs.
Scheduling : gérer par l'OS.
Concurrence : Non coopérative	/ racy (compétitif) 

Continuation :
Scheduling : c'est du code que l'on écrit dans l'application.
Concurrence : Coopérative car pas d'interruption

La plupart des BDD utilise des continuation

*/