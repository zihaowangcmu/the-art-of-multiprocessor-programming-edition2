package chapter2.lectures.l2_7;

import chapter2.lectures.l2_5.Filter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class Tester {

    private final Logger logger = LoggerFactory.getLogger(Tester.class);

    @Test
    public void test() throws InterruptedException {
        int n = 4;
        // the map to map the real thread id to ids starting from 0
        ConcurrentHashMap<Integer, Integer> threadIdsMap = new ConcurrentHashMap<>();
        Thread[] threads = new Thread[n];
        Bakery bakery = new Bakery(n, threadIdsMap);
        for (int i = 0; i < n; i++) {
            int id = i;
            threads[i] = new Thread(() -> {
                threadIdsMap.put(currentThreadIdInt(), id);
                logger.info("Start Thread: {}", id);

                logger.info("id: {}, waiting for lock....", id);
                bakery.lock();
                // note this log line should be printed in the increasing order of thread ids (same order of thread started)
                logger.info("id: {}, in Critical Section, will release the lock in 2 seconds.", id);
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                bakery.unlock();
                logger.info("id: {}, finished task, lock released=================================", id);
            });
        }

        for (Thread thread : threads) {
            thread.start();
//            thread.join(100 * 1000);
            try {
                // make the threads start in the order of the id
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
