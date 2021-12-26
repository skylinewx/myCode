# 什么是JMH

JMH，`Java Microbenchmark Harness`，是专门用于代码微基准测试的工具套件[官方demo](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)

# 我们为什么需要JMH

大家可能会疑问，我就用这样的方式来测试效率不好吗？

```java
long start = System.currentTimeMillis();
doxxx();
Systime.out.println(System.currentTimeMillis()-start);
```

这么说来原因就很多了，比如：

1. 程序预热（线程池是否已经扩容完毕、JIT热点代码）
2. 多次运行
3. 前置逻辑中产生的对象导致gc，结果影响当前方法的测试
4. 多线程

# gradle依赖

```groovy
testImplementation 'org.openjdk.jmh:jmh-generator-annprocess:1.33'
testImplementation 'org.openjdk.jmh:jmh-core:1.33'
testAnnotationProcessor 'org.openjdk.jmh:jmh-generator-annprocess:1.33'
```

# String效率

关于字符串拼接的效率问题也算是老生常谈了，都说如果要循环拼接字符串，那最好使用`StringBuilder`，不要直接用`+`。但是很少人能提供明确的数据支撑。接下来我们先看个demo，感受一下jmh。

```java
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
@Measurement(time = 1, iterations = 5)
@Fork(1)
@State(value = Scope.Benchmark)
public class StringTest {

    @Param({"10", "100", "1000"})
    private int size;

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
        for (int i = 0; i < size; i++) {
            a = a + i;
        }
        return a;
    }

    @Benchmark
    public String testConcat() {
        String a = "";
        for (int i = 0; i < size; i++) {
            a = a.concat("" + i);
        }
        return a;
    }

    @Benchmark
    public String testBuilder() {
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < size; i++) {
            a.append(i);
        }
        return a.toString();
    }

    @Benchmark
    public String testBuffer() {
        StringBuffer a = new StringBuffer();
        for (int i = 0; i < size; i++) {
            a.append(i);
        }
        return a.toString();
    }
}

```

结果：

```java
Benchmark               (size)  Mode  Cnt       Score      Error  Units
StringTest.testAdd          10  avgt    5     127.196 ±    1.965  ns/op
StringTest.testAdd         100  avgt    5    1904.355 ±   61.848  ns/op
StringTest.testAdd        1000  avgt    5  196751.625 ± 7122.108  ns/op
StringTest.testBuffer       10  avgt    5      85.462 ±    2.184  ns/op
StringTest.testBuffer      100  avgt    5    1039.248 ±   22.582  ns/op
StringTest.testBuffer     1000  avgt    5   13067.207 ±  197.001  ns/op
StringTest.testBuilder      10  avgt    5      74.878 ±    3.929  ns/op
StringTest.testBuilder     100  avgt    5     866.980 ±   23.213  ns/op
StringTest.testBuilder    1000  avgt    5   12690.059 ±  417.594  ns/op
StringTest.testConcat       10  avgt    5     300.571 ±    8.185  ns/op
StringTest.testConcat      100  avgt    5    3596.555 ±   98.105  ns/op
StringTest.testConcat     1000  avgt    5  205944.290 ± 8248.218  ns/op
```

从上面的结果中可以看到，随着拼接次数的增加10->100->1000每个方法的效率都会降低。但是使用StringBuilder进行字符串拼接的效率依旧是最高的，其次是StringBuffer，然后是字符串加法，效率最低的concat。

# 基本参数概念

## @BenchmarkMode

标识JMH进行Benchmark时所使用的模式。

1. `Throughput`：吞吐量。比如“1秒内可以执行多少次调用”，单位是`ops/time`
2. `AverageTime`：每次调用的平均耗时。比如“每次调用平均耗时xxx毫秒”，单位是`time/ops`
3. `SampleTime`：随机取样，最后输出取样结果的分布。比如“99%的调用在xxx毫秒内，99.99%的调用在xxx毫秒以内”
4. `SingleShotTime`：只运行一次，往往同时设置warmup=0，一般用于测试冷启动的性能。

上面的这些模式并不是只能使用某一个，这些模式是可以被**组合**使用的，比如

```java
@BenchmarkMode({Mode.AverageTime,Mode.SampleTime})
```

## @State

通过State可以指定一个对象的作用范围，jmh通过scope来来进行实例化和共享操作。@State可以被继承使用，如果父类定义了该注解，子类则无需定义。由于jmh可以进行多线程测试，所以不同的scope的隔离级别如下：

1. `Scope.Benchmark`：全局共享，所有的测试线程共享同一个实例对象。可以用来测试有状态的实例在多线程下的性能。
2. `Scope.Group`：同一个线程组内部的线程共享一个实例对象。
3. `Scope.Thread`：每个线程获取到都是不一样的实例对象。

在上面字符串拼接性能测试的样例中，我们使用的就是`Scope.Benchmark`

## @OutputTimeunit

输出结果的时间单位，咱们上面用的是纳秒，即`TimeUnit.NANOSECONDS`

## @Warmup

程序预热所需要的一些参数，可以用在类或者方法上。由于JVM存在JIT机制，所以一般前几次的效率都可能会不太理想，所以需要让程序先预热一下再跑。这样可以保证测试结果的准确性。参数如下：

1. `iterations`：预热的次数，默认值是`org.openjdk.jmh.runner.Defaults#WARMUP_ITERATIONS`=5
2. `time`：每次预热执行的时长，默认值是`org.openjdk.jmh.runner.Defaults#WARMUP_TIME`=10秒
3. `timeUnit`：上面那个时长对应的单位类型，默认是秒
4. `batchSize`：每个操作的基准方法调用次数，默认值是`org.openjdk.jmh.runner.Defaults#WARMUP_BATCHSIZE`=1。1就代表一次一次的调用，如果是2那就代表2次2次的调用。

## @Measurement

这个参数与`@Warmup`中的参数完全一样，只是`@Warmup`是用在预热上，预热结果不算入最终的结果中。而`@Measurement`是指实际测试时的参数。

## @Fork

默认值是`org.openjdk.jmh.runner.Defaults#MEASUREMENT_FORKS`=5，可以手动指定。`@Fork`中设置是多少，那jmh执行测试时就会创建多少个独立的进程来进行测试。但是需要注意的是，不管有多少个进程进行测试，他们都是串行的。当fork为0时，表示不需要进行fork。官方解释是这样的：

>JVMs are notoriously good at profile-guided optimizations. This is bad for benchmarks, because different tests can mix their profiles together, and then render the "uniformly bad" code for every test. Forking (running in a separate process) each test can help to evade this issue.
>JMH will fork the tests by default.

翻译成人话就是说，首先因为JVM存在**profile-guided optimizations**的特性，但是这样的特性是不利于进行基准测试的。因为不同的测试方法会混在一起，最后会导致结果出现偏差。为了避免这样的偏差才有了@Fork的存在。关于这个偏差的问题可以参考官方的这个例子：[code-tools/jmh: 2be2df7dbaf8 jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_12_Forking.java](http://hg.openjdk.java.net/code-tools/jmh/file/2be2df7dbaf8/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_12_Forking.java)

所以为了避免这样的问题，我们可以设置`@Fork(1)`这样每一个测试的方法都会跑在不同的jvm进程中，也就避免了相互影响。

## @Thread

每一个测试进程（JVM）中的线程数。

## @Param

用来指定某个参数的多种情况，比如上面字符串的例子中的：

```java
@Param({"10", "100", "1000"})
private int size;
```

特别适合用来测试一个函数在不同的参数输入的情况下的性能。只能用在字段上，同时必须使用`@State`注解声明隔离级别。

# 实战

## 一、Random与ThreadLocalRandom

我们都知道获取随机数可以使用`Random`，同时在官方文档中也强调`Random`虽然是**线程安全**的，但是如果在多线程的情况下，最好还是使用`ThreadLocalRandom`。那么，Random与ThreadLocalRandom在效率上相差多少呢？我们在实际使用过程中该如何选择呢？

```java
@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(time = 1, iterations = 3)
@Measurement(time = 1, iterations = 5)
@Fork(1)
@Threads(5)
@State(value = Scope.Benchmark)
public class RandomTest {
    private final Random random = new Random();
    private final ThreadLocal<Random> randomThreadLocalHolder = ThreadLocal.withInitial(Random::new);

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RandomTest.class.getSimpleName())
                .result("RandomTest_result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

    @Benchmark
    public int random() {
        return random.nextInt();
    }

    @Benchmark
    public int randomThreadLocalHolder() {
        return randomThreadLocalHolder.get().nextInt();
    }

    @Benchmark
    public int threadLocalRandom() {
        return ThreadLocalRandom.current().nextInt();
    }
}
```

看下结果：

```java
Benchmark                           Mode  Cnt    Score    Error  Units
RandomTest.random                   avgt    5  423.784 ± 20.159  ns/op
RandomTest.randomThreadLocalHolder  avgt    5   11.369 ±  0.509  ns/op
RandomTest.threadLocalRandom        avgt    5    4.322 ±  0.374  ns/op
```

从结果上看`ThreadLocalRandom.current().nextInt()`完胜，而且效率差别非常大。同时我们也没必要自己搞ThreadLocal来封装Random。因为JDK提供的`ThreadLocalRandom.current()`就已经是天花板了。

##

# 参考链接

[为什么要用JMH？何时应该用？ - 知乎 (zhihu.com)](https://www.zhihu.com/question/276455629/answer/1259967560)

[JMH(Java Micro Benchmark) 简介 - 逝者如斯夫 - BlogJava](http://www.blogjava.net/ideame/archive/2016/08/01/431411.html)

[JMH使用说明 - 简书 (jianshu.com)](https://www.jianshu.com/p/5a501cb6403c)

[JMH使用说明_lxbjkben的博客-CSDN博客_jmh使用](https://blog.csdn.net/lxbjkben/article/details/79410740)

[JAVA 拾遗 — JMH 与 8 个测试陷阱 - 徐靖峰|个人博客 (cnkirito.moe)](https://www.cnkirito.moe/java-jmh/)

[JMH 应用指南 | JAVATECH (dunwu.github.io)](https://dunwu.github.io/javatech/test/jmh.html#teardown)

[在java中使用JMH（Java Microbenchmark Harness）做性能测试 - flydean - 博客园 (cnblogs.com)](https://www.cnblogs.com/flydean/p/12680265.html)

[jmh学习笔记-Forking分叉_m0_37607945的博客-CSDN博客](https://blog.csdn.net/m0_37607945/article/details/111563634)

[^1]:[JMH使用说明 - 简书 (jianshu.com)](https://www.jianshu.com/p/5a501cb6403c)