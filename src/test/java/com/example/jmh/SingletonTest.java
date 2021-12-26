package com.example.jmh;

import com.example.designpatterns.singleton.Singleton1;
import com.example.designpatterns.singleton.Singleton2;
import com.example.designpatterns.singleton.Singleton3;
import com.example.designpatterns.singleton.Singleton4;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(time = 1, iterations = 5)
@Measurement(time = 1, iterations = 5)
@Threads(4)
@Fork(1)
public class SingletonTest {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SingletonTest.class.getSimpleName())
                .result("SingletonTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Benchmark
    public Singleton1 getInstance1() {
        return Singleton1.getInstance();
    }

    @Benchmark
    public Singleton2 getInstance2() {
        return Singleton2.getInstance();
    }

    @Benchmark
    public Singleton3 getInstance3() {
        return Singleton3.getInstance();
    }

    @Benchmark
    public Singleton4 getInstance4() {
        return Singleton4.INTANCE;
    }
}
