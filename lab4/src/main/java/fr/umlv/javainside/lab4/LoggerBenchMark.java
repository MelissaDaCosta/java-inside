package fr.umlv.javainside.lab4;

/* Si laisser les --enable-preview dans pom.xml : 
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
	  /*
	  
	  Result "fr.umlv.javainside.lab4.LoggerBenchMark.no_op":
		  0,375 ±(99.9%) 0,004 ns/op [Average]
		  (min, avg, max) = (0,371, 0,375, 0,381), stdev = 0,003
		  CI (99.9%): [0,371, 0,378] (assumes normal distribution)
		  
		  Result "fr.umlv.javainside.lab4.LoggerBenchMark.simple_logger":
  0,379 ±(99.9%) 0,005 ns/op [Average]
  (min, avg, max) = (0,372, 0,379, 0,393), stdev = 0,005
  CI (99.9%): [0,373, 0,384] (assumes normal distribution)
		  */
  }
  
  @Benchmark
  public void simple_logger() {
    Foo.LOGGER.log("test");
  }
  
  @Benchmark
  public void fast_logger() {
    Bar.LOGGER.log("test");	// même cout que no op, remplace tout le code par rien
    // Dans le cas ou la lambda est considéré comme une constance alors toutes les variables sont considérer comme des constantes
    /*
    Result "fr.umlv.javainside.lab4.LoggerBenchMark.fast_logger":
    	  0,381 ±(99.9%) 0,002 ns/op [Average]
    	  (min, avg, max) = (0,378, 0,381, 0,385), stdev = 0,002
    	  CI (99.9%): [0,379, 0,383] (assumes normal distribution)
    	  */
  }
  
  @Benchmark
  public void create_and_disable_logger() {
    Foo.LOGGER.log("test");
    Logger.enable(Foo.class, false);
    
    /*
    Result "fr.umlv.javainside.lab4.LoggerBenchMark.create_and_disable_logger":
    	  157,773 ±(99.9%) 28,997 ns/op [Average]
    	  (min, avg, max) = (135,455, 157,773, 212,947), stdev = 27,124
    	  CI (99.9%): [128,776, 186,770] (assumes normal distribution)
    	  
    	  énorme cout
     */
  }
  
}