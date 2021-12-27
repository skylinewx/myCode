package com.example.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(time = 1, iterations = 3)
@Measurement(time = 1, iterations = 5)
@Fork(1)
@Threads(10)
@State(value = Scope.Benchmark)
public class HotWriteTest {
    private final LongAdder longAdder = new LongAdder();
    private final AtomicLong atomicLong = new AtomicLong();

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HotWriteTest.class.getSimpleName())
                .result("HotWriteTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Benchmark
    public void longAdder() {
        longAdder.increment();
    }

    @Benchmark
    public void atomicLong() {
        atomicLong.incrementAndGet();
    }
}
