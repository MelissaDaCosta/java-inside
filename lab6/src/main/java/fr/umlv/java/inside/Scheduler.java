package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.Deque;

public class Scheduler {

	private Deque<Continuation> deque;
	// public static final ContinuationScope SCOPE = new ContinuationScope("scope");
	public SCHEDULER_MODE mode;

	public static enum SCHEDULER_MODE {
		STACK, // exécute la dernière continuation qui s'est enregistrée
		FIFO, // exécute la première continuation qui s'est enregistrée
		RANDOM // exécute une continuation au hazard (Il existe une classe
				// java.util.ThreadLocalRandom)
	};

	public Scheduler(SCHEDULER_MODE mode) {
		this.deque = new ArrayDeque<Continuation>();
		this.mode = mode;
	}

	public void enqueue(ContinuationScope scope) {
		if (Continuation.getCurrentContinuation(scope) == null) { // Il n'y a pas de continuation courante
			throw new IllegalStateException();
		}
		this.deque.offer(Continuation.getCurrentContinuation(scope)); // Le met en dernier dans la deque
		Continuation.yield(scope);
	}

	public void runLoop() {
		while (!deque.isEmpty()) {
			Continuation continuation = null;
			switch (this.mode) {
				case STACK:	// ArrayDeqe
					continuation = deque.pollLast(); // Retire le dernier élément
					break;
				case FIFO: // ArrayDeqe
					continuation = deque.pollFirst();
					break;
				case RANDOM:	// ArrayList
					break;
			}
			continuation.run();
		}
	}
}

/*
 * Il faut une structure de donnée différente en fonction de SCHEDULER_MODE
 */
