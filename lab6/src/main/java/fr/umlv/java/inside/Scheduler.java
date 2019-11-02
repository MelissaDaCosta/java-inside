package fr.umlv.java.inside;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class Scheduler {

	private final Collection<Continuation> queue;
	// public static final ContinuationScope SCOPE = new ContinuationScope("scope");
	public final SCHEDULER_MODE mode;

	public static enum SCHEDULER_MODE {
		STACK, // exécute la dernière continuation qui s'est enregistrée
		FIFO, // exécute la première continuation qui s'est enregistrée
		RANDOM // exécute une continuation au hazard (Il existe une classe
				// java.util.ThreadLocalRandom)
	};

	public Scheduler(SCHEDULER_MODE mode) {
		if(mode == SCHEDULER_MODE.RANDOM)
			this.queue = new ArrayList<Continuation>();
		else	// STACK ou FIFO
			this.queue = new ArrayDeque<Continuation>();
		this.mode = mode;
	}

	public void enqueue(ContinuationScope scope) {
		if (Continuation.getCurrentContinuation(scope) == null) { // Il n'y a pas de continuation courante
			throw new IllegalStateException();
		}
		this.queue.add(Continuation.getCurrentContinuation(scope));
		Continuation.yield(scope);
	}

	public void runLoop() {
		while (!queue.isEmpty()) {
			Continuation continuation = null;
			switch (this.mode) {
				case STACK:	// ArrayDeqe
					continuation = ((ArrayDeque<Continuation>) queue).pollLast(); // Retire le dernier élément
					break;
				case FIFO: // ArrayDeqe
					continuation = ((ArrayDeque<Continuation>) queue).pollFirst();
					break;
				case RANDOM:	// ArrayList
					int randomValue = ThreadLocalRandom.current().nextInt(0, queue.size());
					continuation = ((ArrayList<Continuation>) queue).get(randomValue);
					if (!continuation.isDone())
				        continuation.run();
					break;
			}
			continuation.run();
		}
	}
}

/*
 * Il faut une structure de donnée différente en fonction de SCHEDULER_MODE
 */
