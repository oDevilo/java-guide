package com.devil.guide.reactor;

import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * 创建 Flux 或者 Mono 并订阅
 *
 * @author Devil
 * @date 2021/7/19 11:40 上午
 */
public class FluxTest {

    /**
     * 普通情况
     */
    @Test
    public void test1() {
        Flux.range(1, 3).subscribe(System.out::println);
    }

    @Test
    public void create() {
        Flux.create(sink -> {
            //向下游发布元素
            sink.next("helloword");
            sink.next("helloword2");
            //结束发布元素
            sink.complete();
        }).subscribe(System.out::println);//subscribe发布消息，System.out.println为消费者，消费消息;
    }

    /**
     * 只获取前面 n 个/阻塞
     */
    @Test
    public void take() {
        Flux.range(1, 5)
                .map(v -> {
                    System.out.println(v);
                    return v;
                })
                .take(3).subscribe(System.out::println);
    }

    @Test
    public void retry() throws InterruptedException {
        // 不用retry 失败后直接跳出
        Flux<String> flux = Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) return "tick " + input;
                    throw new RuntimeException("boom");
                })
                .onErrorReturn("Uh oh");

        flux.subscribe(System.out::println);
        Thread.sleep(2100);

        // 使用 retry 在失败的时候 重试一次
        Flux.interval(Duration.ofMillis(250))
                .map(input -> {
                    if (input < 3) return "tick " + input;
                    throw new RuntimeException("boom");
                })
                .retry(1)
                // elapsed : Map this Flux into Tuple2<Long, T> of timemillis and source data.
                // The timemillis corresponds to the elapsed time between each signal as measured by the parallel scheduler.
                // First duration is measured between the subscription and the first element
                // 将 值与 时间绑定 成为一个Tuple2 此时间为每个信号之间的时间
                .elapsed()
                .subscribe(System.out::println, System.err::println);

        Thread.sleep(2100);
    }

    /**
     * 乘法表
     */
    @Test
    public void generate() {
        Flux.generate(new Callable<Integer>() {
            // called for each incoming Subscriber to provide the initial state for the generator bifunction
            // 为每个进入的订阅服务器提供生成器双函数的初始状态
            @Override
            public Integer call() throws Exception {
                return 0;
            }
        }, new BiFunction<Integer, SynchronousSink<String>, Integer>() {
            @Override
            public Integer apply(Integer state, SynchronousSink<String> sink) {
                sink.next("3 x " + state + " = " + 3 * state);
                if (state == 10) sink.complete(); // 合适停止
                return state + 1;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
    }

    /**
     * 当执行中出现异常
     */
    @Test
    public void onError() {
        Flux.range(1, 4).map(i -> {
            if (i <= 3) return i;
            throw new RuntimeException("Got to 4");
        }).subscribe(System.out::println,
                error -> System.err.println("Error: " + error));
    }

    /**
     * finally 的场景
     */
    @Test
    public void whenFinally() {
        Flux.range(1, 4).subscribe(System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"));
    }

    /**
     * subscription 决定订阅方式
     */
    @Test
    public void subscription() {
        Flux.range(1, 4).subscribe(System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) {
                        subscription.request(1);
                        System.out.println("===");
                        subscription.request(2);
                        System.out.println("===");
                    }
                }
        );
    }

    @Test
    public void baseSubscriber() {
        Flux.range(1, 4).subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("Subscribed");
                request(1);
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println(value);
                request(2);
            }
        });
    }

    /**
     * 不间断的发出递增数
     */
    @Test
    public void interval() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1)).subscribe(System.out::println);
        Thread.sleep(3000);
    }

    /**
     * doOnNext中的 Consumer 最终才执行 onNext 的
     */
    @Test
    public void doOnNext() {
        Flux.just(new User(1))
                .doOnNext(user -> {
                    user.age = 100;
                    System.out.println(1);
                })
                .doOnNext(user -> user.age = 200)
                .subscribe(System.out::println);
    }

    @Test
    public void block() {
        Flux<Integer> range = Flux.range(1, 3);
        System.out.println(range.blockFirst());
        System.out.println(range.blockFirst());
        System.out.println(range.blockLast());
    }

    @Test
    public void multiFlux() {
        Flux<Integer> map = Flux.range(1, 3).map(v -> {
            System.out.println(v + " t");
            return v + 1;
        });

        map.publishOn(Schedulers.newParallel("P")).subscribe(System.out::println);
        map.publishOn(Schedulers.newParallel("P")).subscribe(System.out::println);
    }

    @Test
    public void buffer() {
        Flux<List<Integer>> map = Flux.range(1, 3).map(v -> {
            System.out.println(v + " t");
            return v + 1;
        }).buffer();

//        Flux<List<Integer>> map = Flux.range(1, 3).map(v -> {
//            System.out.println(v + " t");
//            return v + 1;
//        }).buffer(10);

//        Flux<Integer> map = Flux.range(1, 3).map(v -> {
//            System.out.println(v + " t");
//            return v + 1;
//        }).onBackpressureBuffer();

        map.publishOn(Schedulers.newParallel("P")).subscribe(System.out::println);
        map.publishOn(Schedulers.newParallel("P")).subscribe(System.out::println);
    }

    @Test
    public void cache() {
        Flux<Integer> map = Flux.range(1, 3).map(v -> {
            System.out.println(v + " t");
            return v + 1;
        }).cache();

        map.publishOn(Schedulers.newParallel("P")).subscribe(System.out::println);
        map.publishOn(Schedulers.newParallel("P")).subscribe(System.out::println);
    }

    static class User {
        int age;

        public User(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "age=" + age +
                    '}';
        }
    }
}
