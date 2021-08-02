package com.devil.guide.reactor;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Devil
 * @since 2021/8/1
 */
public class Threaded {

    /**
     * 可以多线程发射元素
     */
    @Test
    public void create() {
        Flux<String> bridge = Flux.create(new Consumer<FluxSink<String>>() {
            @Override
            public void accept(FluxSink<String> stringFluxSink) {
                // see create/push
            }
        });
    }

    /**
     * 下发前处理
     */
    @Test
    public void handler() {
        Flux.just(-1, 30, 13, 9, 20).handle((i, sink) -> {
            String letter = null;
            if (i >= 1 && i <= 26) {
                int letterIndexAscii = 'A' + i - 1;
                letter = "" + (char) letterIndexAscii;
            }
            if (letter != null)
                sink.next(letter);
        }).subscribe(System.out::println);
    }

    /**
     * publishOn 将 publishOn 之后的 onNext, onComplete and onError 交由 Scheduler 操作
     */
    @Test
    public void publishOn() throws InterruptedException {
//        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
        Scheduler s = Schedulers.newSingle("Scheduler");
        final Flux<String> flux = Flux
                .range(1, 2)
                .map(i -> {
                    System.out.println("map before " + Thread.currentThread().getName());
                    return "10" + i;
                })
                .publishOn(s)
                .map(i -> {
                    System.out.println("map after " + Thread.currentThread().getName());
                    return "value " + i;
                });

        Thread t = new Thread(() -> {
            System.out.println("t " + Thread.currentThread().getName());
            flux.subscribe(s1 -> {
                System.out.println(s1);
                System.out.println("subscribe " + Thread.currentThread().getName());
            });
        });

        t.start();
        t.join();
    }

    /**
     * 从注释和结果来看 subscribeOn 和 publishOn 的区别在于
     *
     * publishOn 在调用此方法之前的所有操作是由调用 subscribe 的线程执行的
     * 之后的方法会由 publishOn 的 Scheduler 线程调用
     *
     * subscribeOn 的 Scheduler 会执行所有相关操作 直到遇到 publishOn
     */
    @Test
    public void subscribeOn() throws InterruptedException {
        final Flux<String> flux = Flux
                .range(1, 2)
                .map(i -> {
                    System.out.println("map before " + Thread.currentThread().getName());
                    return "10" + i;
                })
                .subscribeOn(Schedulers.newSingle("Scheduler"))
//                .publishOn(Schedulers.newSingle("Scheduler P"))
                .map(i -> {
                    System.out.println("map after " + Thread.currentThread().getName());
                    return "value " + i;
                });

        Thread t = new Thread(() -> {
            flux.subscribe(s1 -> {
                System.out.println(s1);
                System.out.println("subscribe " + Thread.currentThread().getName());
            });
        });

        t.start();
        t.join();
    }

    interface MyEventListener<T> {
        // 生成
        void onDataChunk(List<T> chunk);

        // 生成结束
        void processComplete();

        // 处理异常
        void processError(Throwable e);
    }
}
