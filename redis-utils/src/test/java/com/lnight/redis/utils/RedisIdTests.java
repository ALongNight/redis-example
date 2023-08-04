package com.lnight.redis.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class RedisIdTests {

    @Autowired
    private RedisIdWorker idWorker;

    private final ExecutorService es = Executors.newFixedThreadPool(500);

    @Test
    public void idGeneratorTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(300);
        Runnable runnable = () -> {
            for (int i = 0; i < 100; i++) {
                long id = idWorker.nextId("stock");
                System.out.println(id);
            }
            latch.countDown();
        };
        for (int i = 0; i < 300; i++) {
            es.submit(runnable);
        }
        long start = System.currentTimeMillis();

        latch.await();

        long end = System.currentTimeMillis();

        System.out.println("time=" + (start - end));
    }
}
