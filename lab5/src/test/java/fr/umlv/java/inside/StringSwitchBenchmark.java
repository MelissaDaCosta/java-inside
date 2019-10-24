package fr.umlv.java.inside;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)

public class StringSwitchBenchmark {
	
	private static ArrayList<String> list;

	static {
		final int ONE_MILLION = 1000000;
		
		String[] stringValues= {"foo", "bar", "bazz", "chaine", "autre", "test"};
		
		list = new ArrayList<String>(ONE_MILLION);
		
		Random r=new Random();
        int randomValue=r.nextInt(stringValues.length);
		
		for(int i=0; i<ONE_MILLION; i++) {
			list.add(stringValues[randomValue]);
		}
			
	}

	@Benchmark
	  public void stringSwitch1Foo() {
		list.forEach(l->StringSwitchExample.stringSwitch(l));
	    
	  }
	  
}
