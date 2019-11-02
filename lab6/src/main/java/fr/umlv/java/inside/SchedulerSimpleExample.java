package fr.umlv.java.inside;

import java.util.List;

public class SchedulerSimpleExample {
	public static void main(String[] args) {

		var scope = new ContinuationScope("scope");
		
		var scheduler = new SchedulerSimple();
		var continuation1 = new Continuation(scope, () -> {
			System.out.println("start 1");
			scheduler.enqueue(scope);
			System.out.println("middle 1");
			scheduler.enqueue(scope);
			System.out.println("end 1");
		});
		var continuation2 = new Continuation(scope, () -> {
			System.out.println("start 2");
			scheduler.enqueue(scope);
			System.out.println("middle 2");
			scheduler.enqueue(scope);
			System.out.println("end 2");
		});
		var list = List.of(continuation1, continuation2);
		/*
		 * exécute 1 fois chaque continuation : start 1 et start 2
		 * pile : cont1 -> cont2
		 */
		list.forEach(Continuation::run);
		
		scheduler.runLoop();
		/* 
		 * relance cont2 : middle2 puis remet cont2 dans la pile : cont1 -> cont2
		 * relance cont2 : end2, pile : cont1
		 * relance cont1 : middle1 puis remet cont1 dans la pile, pile : cont1
		 * relance cont1 : end1
		 * FIN
		 */
	}
}
