package com.devil.guide.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 上下文，可用于多线程中传递数据
 *
 * @author Devil
 * @since 2021/8/10
 */
public class ContextTest {

    public static Scheduler custom_Scheduler() {
        Executor executor = new ThreadPoolExecutor(
                10,
                10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1000),
                Executors.defaultThreadFactory()
        );
        return Schedulers.fromExecutor(executor);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void flux_generate4() {
        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            System.out.println("所发射元素产生的线程: " + Thread.currentThread().getName());
            sink.next(value);
            sleep(1000);
            if (list.size() == 20) {
                sink.complete();
            }
            return list;
        }).publishOn(custom_Scheduler(), 1)
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribe(System.out::println);

        sleep(20000);
    }

    @Test
    public void contextSimple1() {
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap(new Function<String, Mono<String>>() {
                    @Override
                    public Mono<String> apply(String s) {
                        return Mono.deferContextual(
                                new Function<ContextView, Mono<String>>() {
                                    @Override
                                    public Mono<String> apply(ContextView contextView) {
                                        return Mono.just(s + " " + contextView.get(key));
                                    }
                                }
                        );
                    }
                })
                .contextWrite(new Function<Context, Context>() {
                    @Override
                    public Context apply(Context context) {
                        String value = "World";
                        System.out.println(value);
                        return context.put(key, value);
                    }
                })
                // 代码上越靠后的 contextWrite 越先调用
                .contextWrite(new Function<Context, Context>() {
                    @Override
                    public Context apply(Context context) {
                        String value = "test";
                        System.out.println(value);
                        return context.put(key, value);
                    }
                });

        r.subscribe(System.out::println);
    }

    /**
     * 旧版api使用方式 已经废弃
     */
    @Test
    public void contextOld() {
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap(new Function<String, Mono<String>>() {
                    @Override
                    public Mono<String> apply(String s) {
                        return Mono.subscriberContext().map(new Function<Context, String>() {
                            @Override
                            public String apply(Context context) {
                                return s + " " + context.get(key);
                            }
                        });
                    }
                })
                .subscriberContext(new Function<Context, Context>() {
                    @Override
                    public Context apply(Context context) {
                        String value = "World";
                        System.out.println(value);
                        return context.put(key, value);
                    }
                })
                // 代码上越靠后的 contextWrite 越先调用
                .subscriberContext(new Function<Context, Context>() {
                    @Override
                    public Context apply(Context context) {
                        String value = "test";
                        System.out.println(value);
                        return context.put(key, value);
                    }
                });

        r.subscribe(System.out::println);
    }

}
