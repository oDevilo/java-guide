package com.devil.guide.reactor;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

/**
 * 创建 Flux 或者 Mono 并订阅
 *
 * @author Devil
 * @date 2021/7/19 11:40 上午
 */
public class Subscribe {

    public static void main(String[] args) {
        // 普通情况
        Flux.range(1, 3).subscribe(System.out::println);
        System.out.println("==========");

        // 当执行中出现异常
        Flux.range(1, 4).map(i -> {
            if (i <= 3) return i;
            throw new RuntimeException("Got to 4");
        }).subscribe(System.out::println,
                error -> System.err.println("Error: " + error));
        System.out.println("==========");

        // finally 的场景
        Flux.range(1, 4).subscribe(System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"));
        System.out.println("==========");

        // 这个还不清楚，只知道消费了 3 个
        Flux.range(1, 4).subscribe(System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                sub -> sub.request(3)
        );
        System.out.println("==========");

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
        System.out.println("==========");
    }
}
