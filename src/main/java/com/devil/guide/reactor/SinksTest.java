package com.devil.guide.reactor;

import org.junit.After;
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

    @After
    public void after() throws InterruptedException {
        Thread.sleep(1000000);
    }

    /**
     * unicast只能被订阅一次 第二次订阅会报错
     * 如果没有订阅者，会保存接收到的消息直到第一个订阅者订阅
     */
    @Test
    public void unicast() {
        Sinks.Many<Integer> sinks = Sinks.many().unicast().onBackpressureBuffer();
        number1(sinks);
        number2(sinks);
        // 1. 一个消费
//        subscribe(sinks.asFlux());
        // 2. 两个消费
        subscribe(sinks.asFlux());
        subscribe(sinks.asFlux());
        // 3. publishOn一个
//        subscribe(sinks.asFlux().publishOn(Schedulers.newParallel("P")));
        // 4. publishOn两个
//        subscribe(sinks.asFlux().publishOn(Schedulers.newParallel("P1")));
//        subscribe(sinks.asFlux().publishOn(Schedulers.newParallel("P2")));
    }

    /**
     * multicast支持多订阅者，如果没有订阅者，那么接收的消息直接丢弃
     */
    @Test
    public void multicast() throws InterruptedException {
        Sinks.Many<Integer> sinks = Sinks.many().multicast().directAllOrNothing();
//        Sinks.Many<Integer> sinks = Sinks.many().multicast().onBackpressureBuffer();
        subscribe(sinks.asFlux());
        Flux<Integer> p1 = sinks.asFlux().publishOn(Schedulers.newSingle("P"));
        subscribe(p1);
        subscribe(p1);
        number1(sinks);
        number2(sinks);
        Thread.sleep(2000);
//        subscribe(sinks.asFlux());
        System.out.println("end");
    }

    /**
     * replay不管多少订阅者都保存消息
     * 它将向新订阅者重播推送数据的指定历史
     * all limit(100) latest
     */
    @Test
    public void replay() throws InterruptedException {
        Sinks.Many<Integer> sinks = Sinks.many().replay().all();
        number1(sinks);
        number2(sinks);
        Thread.sleep(2000);
        subscribe(sinks.asFlux().publishOn(Schedulers.newParallel("P1")));
        subscribe(sinks.asFlux().publishOn(Schedulers.newParallel("P2")));
        System.out.println("end");
    }


    private void number1(Sinks.Many<Integer> sinks) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 0; i < 10; i = i + 2) {
                    try {
                        Thread.sleep(random.nextInt(100));
                        System.out.println(Thread.currentThread().getName() + " publish " + i);
                        sinks.emitNext(i, Sinks.EmitFailureHandler.FAIL_FAST);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("end 1");
            }
        }, "t1").start();
    }

    private void number2(Sinks.Many<Integer> sinks) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 1; i < 10; i = i + 2) {
                    try {
                        Thread.sleep(random.nextInt(100));
                        System.out.println(Thread.currentThread().getName() + " publish " + i);
                        sinks.emitNext(i, Sinks.EmitFailureHandler.FAIL_FAST);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("end 2");
            }
        }, "t2").start();
    }

    private void subscribe(Flux<Integer> flux) {
        flux.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(Thread.currentThread().getName() + " consume " + integer);
            }
        });
    }

}
