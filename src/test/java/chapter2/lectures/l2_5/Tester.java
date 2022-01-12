package chapter2.lectures.l2_5;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Tester {

    private final Logger logger = LoggerFactory.getLogger(Tester.class);

    @Test
    public void test() {
        int n = 4;
        // the map to map the real thread id to ids starting from 0
        ConcurrentHashMap<Integer, Integer> threadIdsMap = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[n];
        Filter filter = new Filter(n, threadIdsMap);
        for (int i = 0; i < n; i++) {
            int id = i;
            threads[i] = new Thread(() -> {
                threadIdsMap.put(currentThreadIdInt(), id);
                logger.info("Start Thread: {}", id);
                while (true) {
                    logger.info("id: {}, waiting for lock....", id);
                    filter.lock();
                    logger.info("id: {}, in Critical Section, will release the lock in 2 seconds.", id);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    filter.unlock();
                    logger.info("id: {}, finished task, lock released=================================", id);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            Thread.sleep(100 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // this id does not return 0 1 2, instead sth like 13 14 15. So be careful.
    public static int currentThreadIdInt() {
        return (int) Thread.currentThread().getId();
    }
}
