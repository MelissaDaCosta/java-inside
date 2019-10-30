package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.Deque;

public class SchedulerSimple {
	
	private Deque<Continuation> deque;
	//public static final ContinuationScope SCOPE = new ContinuationScope("scope");
	
	public SchedulerSimple() {
		this.deque = new ArrayDeque<Continuation>();
	}
	
	public void enqueue(ContinuationScope scope) {
		if(Continuation.getCurrentContinuation(scope) == null) {	// Il n'y a pas de continuation courante
			throw new IllegalStateException();
		}
		this.deque.offer(Continuation.getCurrentContinuation(scope));	// Le met en dernier dans la deque
		Continuation.yield(scope);
	}
	
	public void runLoop() {
		while(!deque.isEmpty()) {
			Continuation continuation = null;
			continuation = deque.pollLast();	// Retire le dernier élément
			continuation.run();
		}
	}
}

/*
 * Il faut une structure de donnée différente en fonction de SCHEDULER_MODE
 */
