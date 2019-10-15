package fr.umlv.javainside.lab4;

/*
 java --enable-preview -jar target/benchmarks.jar
 */
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
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class LoggerBenchMark {
	
	 private static class Foo {
		private static final Logger LOGGER = Logger.of(Foo.class, __->{});
	}
	 
	 private static class Bar {
			private static final Logger LOGGER = Logger.fastOf(Bar.class, __->{});
		}
	 
  @Benchmark
  public void no_op() {
    // empty : cout d'un appel de méthode
  }
  
  @Benchmark
  public void simple_logger() {
    Foo.LOGGER.log("test");
  }
  
  @Benchmark
  public void fast_logger() {
    Bar.LOGGER.log("test");	// même cout que no op, remplace tout le code par rien
    // Dans le cas ou la lambda est considéré comme une constance alors toutes les variables sont considérer comme des constantes
  }
}