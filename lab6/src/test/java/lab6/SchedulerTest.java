package lab6;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import fr.umlv.java.inside.Scheduler;
import fr.umlv.java.inside.Scheduler.SCHEDULER_MODE;

class SchedulerTest {

	@Test
	public void fifoSchedulerTest() {
		var fifoScheduler = new Scheduler(SCHEDULER_MODE.FIFO);
		
		var scope = new ContinuationScope("scope");
		
		var sb= new StringBuilder();
		var continuation1 = new Continuation(scope, () -> {
			sb.append("start 1\n");
			fifoScheduler.enqueue(scope);
			sb.append("middle 1\n");
			fifoScheduler.enqueue(scope);
			sb.append("end 1\n");
		});
		var continuation2 = new Continuation(scope, () -> {
			sb.append("start 2\n");
			fifoScheduler.enqueue(scope);
			sb.append("middle 2\n");
			fifoScheduler.enqueue(scope);
			sb.append("end 2\n");
		});
		
		var expected = new StringBuilder();
		expected.append("start 1\n").append("start 2\n").append("middle 1\n").append("middle 2\n").append("end 1\n").append("end 2\n");
		
		var list = List.of(continuation1, continuation2);
	    list.forEach(Continuation::run);

	    fifoScheduler.runLoop();

	    assertEquals(expected.toString(), sb.toString());
	}
	
	@Test
	public void stackSchedulerTest() {
		var stackScheduler = new Scheduler(SCHEDULER_MODE.STACK);
		
		var scope = new ContinuationScope("scope");
		
		var sb= new StringBuilder();
		var continuation1 = new Continuation(scope, () -> {
			sb.append("start 1\n");
			stackScheduler.enqueue(scope);
			sb.append("middle 1\n");
			stackScheduler.enqueue(scope);
			sb.append("end 1\n");
		});
		var continuation2 = new Continuation(scope, () -> {
			sb.append("start 2\n");
			stackScheduler.enqueue(scope);
			sb.append("middle 2\n");
			stackScheduler.enqueue(scope);
			sb.append("end 2\n");
		});
		
		var expected = new StringBuilder();
		expected.append("start 1\n").append("start 2\n").append("middle 2\n").append("end 2\n").append("middle 1\n").append("end 1\n");
		
		var list = List.of(continuation1, continuation2);
	    list.forEach(Continuation::run);

	    stackScheduler.runLoop();

	    assertEquals(expected.toString(), sb.toString());
	}

}
