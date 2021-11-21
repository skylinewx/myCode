package com.example.juc;

import com.example.juc.cache.Main;
import com.example.juc.cache.StudentDO;
import com.example.juc.cache.StudentService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(time = 1, iterations = 1)
@Measurement(iterations = 3)
@Threads(10)
@Fork(1)
@State(Scope.Benchmark)
public class CacheTest {

    StudentService studentService;
    ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CacheTest.class.getSimpleName())
                .result("CacheTest.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Setup
    public void init() {
        applicationContext = SpringApplication.run(Main.class);
        studentService = applicationContext.getBean(StudentService.class);
    }

    @TearDown
    public void down() {
        applicationContext.close();
    }

    public String getRandomId() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(9) + 1);
    }

    @Benchmark
    public StudentDO getByIdFromHashMapCache() {
        return studentService.getByIdFromHashMapCache(getRandomId());
    }

    @Benchmark
    public StudentDO getByIdFromConcurrentHashMapCache() {
        return studentService.getByIdFromConcurrentHashMapCache(getRandomId());
    }

    @Benchmark
    public StudentDO getByIdFromReadWriteLockCache() {
        return studentService.getByIdFromReadWriteLockCache(getRandomId());
    }


}
