package com.example.jmh;

import com.example.designpatterns.prototype.MyObj;
import com.example.designpatterns.prototype.PrototyObj;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.SampleTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(time = 2, batchSize = 100)
@Measurement(time = 2, batchSize = 500)
@Fork(2)
@Threads(10)
@State(value = Scope.Benchmark)
public class PrototypeTest {

    private PrototyObj prototyObj;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(PrototypeTest.class.getSimpleName())
                .result("PrototypeTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Setup
    public void init() {
        prototyObj = new PrototyObj();
        prototyObj.intVal=88;
        prototyObj.str="88";
        MyObj myObj = new MyObj();
        myObj.code="zhangsan";
        myObj.title="张三";
        prototyObj.myObj = myObj;
    }

    @Benchmark
    public PrototyObj testClone() throws CloneNotSupportedException {
        return prototyObj.clone();
    }

    @Benchmark
    public PrototyObj testNew() {
        PrototyObj clone = new PrototyObj();
        clone.intVal = prototyObj.intVal;
        clone.str=prototyObj.str;
        MyObj oriMyObj = prototyObj.myObj;
        MyObj myObj = new MyObj();
        myObj.code = oriMyObj.code;
        myObj.title = oriMyObj.title;
        clone.myObj =myObj;
        return clone;
    }
}
