package com.example.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(time = 1, iterations = 3)
@Measurement(time = 1, iterations = 5)
@Fork(1)
@Threads(50)
@State(value = Scope.Benchmark)
public class IdTest {
    private final IdWorker idWorker = new IdWorker(0, 0);

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(IdTest.class.getSimpleName())
                .result("IdTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Benchmark
    public long idWorker() {
        return idWorker.nextId();
    }

    @Benchmark
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Benchmark
    public UUID randomThreadLocalHolder() {
        return UUID.randomUUID();
    }
}
