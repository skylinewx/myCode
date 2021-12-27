package com.example.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(time = 1, iterations = 2)
@Measurement(time = 1, iterations = 3)
@Fork(1)
@State(Scope.Group)
public class SpringBootTest {

    private ConfigurableApplicationContext applicationContext;
    private IQueue arrayQueue;
    private IQueue linkedQueue;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SpringBootTest.class.getSimpleName())
                .result("SpringBootTest.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Setup
    public void init() {
        applicationContext = SpringApplication.run(SpringBootApp.class);
        arrayQueue = applicationContext.getBean("arrayQueue", IQueue.class);
        linkedQueue = applicationContext.getBean("linkedQueue", IQueue.class);
    }

    @TearDown
    public void down() {
        applicationContext.close();
    }

    @Group("arrayQueue")
    @GroupThreads(8)
    @Benchmark
    public void arrayQueuePut() throws InterruptedException {
        arrayQueue.put(new Object());
    }

    @Group("arrayQueue")
    @GroupThreads(10)
    @Benchmark
    public Object arrayQueueGet() throws InterruptedException {
        return arrayQueue.take();
    }

    @Group("linkedQueue")
    @GroupThreads(8)
    @Benchmark
    public void linkedQueuePut() throws InterruptedException {
        linkedQueue.put(new Object());
    }

    @Group("linkedQueue")
    @GroupThreads(10)
    @Benchmark
    public Object linkedQueueGet() throws InterruptedException {
        return linkedQueue.take();
    }
}
