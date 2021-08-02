package com.devil.guide.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.Random;
import java.util.function.Consumer;

/**
 * @author Devil
 * @since 2021/7/30
 */
public class SinksTest {

    // 只会生产一次
    @Test
    public void test() throws InterruptedException {
        Sinks.Many<Integer> sinks = Sinks.many().multicast().directAllOrNothing();
//        Sinks.Many<Integer> sinks = Sinks.many().multicast().onBackpressureBuffer();
//        Sinks.Many<Integer> sinks = Sinks.many().unicast().onBackpressureBuffer();
//        Sinks.Many<Integer> sinks = Sinks.many().unicast().onBackpressureBuffer(new ArrayBlockingQueue<>(100));
        // 双数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 0; i < 10; i = i + 2) {
                    try {
                        Thread.sleep(random.nextInt(100));
                        System.out.println(i + " publish by " + Thread.currentThread().getName());
                        sinks.emitNext(i, Sinks.EmitFailureHandler.FAIL_FAST);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("end 1");
            }
        }, "t1").start();
        // 单数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 1; i < 10; i = i + 2) {
                    try {
                        Thread.sleep(random.nextInt(100));
                        System.out.println(i + " publish by " + Thread.currentThread().getName());
                        sinks.emitNext(i, Sinks.EmitFailureHandler.FAIL_FAST);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("end 2");
            }
        }, "t2").start();
        // 多个消费者消费
        sinks.asFlux().publishOn(Schedulers.newParallel("P")).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(Thread.currentThread().getName() + " consume " + integer);
            }
        });
        sinks.asFlux().publishOn(Schedulers.newParallel("P")).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(Thread.currentThread().getName() + " consume " + integer);
            }
        });

        Thread.sleep(1000000);
    }

}
