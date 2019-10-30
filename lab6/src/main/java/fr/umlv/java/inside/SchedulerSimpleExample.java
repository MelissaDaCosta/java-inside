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
		list.forEach(Continuation::run);
		scheduler.runLoop();
	}
}
