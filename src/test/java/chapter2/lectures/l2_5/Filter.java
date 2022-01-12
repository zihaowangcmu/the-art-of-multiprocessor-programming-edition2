package chapter2.lectures.l2_5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Filter implements Lock {
    /* Due to Java Memory Model, int[] not used for level and victim variables.
     Java programming language does not guarantee linearizability, or even sequential consistency,
     when reading or writing fields of shared objects
     [The Art of Multiprocessor Programming. Maurice Herlihy, Nir Shavit, 2008, pp.61.]
     */
    private AtomicInteger[] level;
    private AtomicInteger[] victim;
    private int n;
    private ConcurrentHashMap<Integer, Integer> threadIdsMap;

    private Logger logger = LoggerFactory.getLogger(Filter.class);

    /**
     * Constructor for Filter lock
     *
     * @param n thread count
     */
    public Filter(int n, ConcurrentHashMap<Integer, Integer> threadIdsMap) {
        this.n = n;
        this.threadIdsMap = threadIdsMap;
        level = new AtomicInteger[n];
        victim = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            level[i] = new AtomicInteger();
            victim[i] = new AtomicInteger();
        }
    }

    /**
     * Acquires the lock.
     */
    @Override
    public void lock() {
        int me = myId();
        for (int i = 1; i < n; i++) {
            level[me].set(i);
            victim[i].set(me);
            for (int k = 0; k < n; k++) {
                while ((k != me) && (level[k].get() >= i && victim[i].get() == me)) {
                    // spin wait
                    // commented since too many logs
//                    logger.info("id:{}, waiting for lock...", me);
                }
            }
        }
        logger.info("id:{}, lock acquired", me);
    }

    /**
     * Releases the lock.
     */
    @Override
    public void unlock() {
        int me = myId();
        level[me].set(0);
        logger.info("id:{}, lock released", me);
    }


    @Override
    public void lockInterruptibly() throws InterruptedException {
        logger.error("Not implemented");
    }

    @Override
    public boolean tryLock() {
        logger.error("Not implemented");
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        logger.error("Not implemented");
        return false;
    }

    @Override
    public Condition newCondition() {
        logger.error("Not implemented");
        return null;
    }

    private int myId() {
        return threadIdsMap.get((int) Thread.currentThread().getId());
    }
}