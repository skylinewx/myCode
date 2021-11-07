package com.example.jmh;

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
@Measurement(iterations = 5)
@Threads(4)
@Fork(1)
public class StringTest {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StringTest.class.getSimpleName())
                .result("StringTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Benchmark
    public String testAdd() {
        String a = "";
        for (int i = 0; i < 100; i++) {
            a = a + i;
        }
        return a;
    }

    @Benchmark
    public String testConcat() {
        String a = "";
        for (int i = 0; i < 100; i++) {
            a = a.concat("" + i);
        }
        return a;
    }

    @Benchmark
    public String testBuilder() {
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            a.append(i);
        }
        return a.toString();
    }

    @Benchmark
    public String testBuffer() {
        StringBuffer a = new StringBuffer();
        for (int i = 0; i < 100; i++) {
            a.append(i);
        }
        return a.toString();
    }
}
