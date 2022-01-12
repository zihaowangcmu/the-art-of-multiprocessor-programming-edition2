package chapter2.lectures.l2_7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Bakery implements Lock {
    /* Due to Java Memory Model, int[] not used for level and victim variables.
     Java programming language does not guarantee linearizability, or even sequential consistency,
     when reading or writing fields of shared objects
     [The Art of Multiprocessor Programming. Maurice Herlihy, Nir Shavit, 2008, pp.61.]
     */
    private Logger logger = LoggerFactory.getLogger(Bakery.class);

    private AtomicBoolean[] flag;
    private AtomicInteger[] label;
    private ConcurrentHashMap<Integer, Integer> threadIdsMap;

    private int n;

    /**
     * Constructor for Bakery lock
     *
     * @param n thread count
     */
    public Bakery(int n, ConcurrentHashMap<Integer, Integer> threadIdsMap) {
        this.n = n;
        this.threadIdsMap = threadIdsMap;
        flag = new AtomicBoolean[n];
        label = new AtomicInteger[n];
        for (int i = 0; i < n; i++) {
            flag[i] = new AtomicBoolean();
            label[i] = new AtomicInteger();
        }
    }

    /**
     * Acquires the lock.
     */
    @Override
    public void lock() {
        int i = myId();
        flag[i].set(true);
        label[i].set(findMaximumElement(label) + 1);
        logger.info("id: {}, acquiring lock...", i);
        for (int k = 0; k < n; k++) {
            while ((k != i) && flag[k].get() && ((label[k].get() < label[i].get()) || ((label[k].get() == label[i].get()) && k < i))) {
                //spin wait
            }
        }
        logger.info("id: {}, acquired lock", i);
    }

    /**
     * Releases the lock.
     */
    @Override
    public void unlock() {
        int myId = myId();
        flag[myId].set(false);
        logger.info("id: {}, released lock", myId);
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

    /**
     * Finds maximum element within and {@link java.util.concurrent.atomic.AtomicInteger} array
     *
     * @param elementArray element array
     * @return maximum element
     */
    private int findMaximumElement(AtomicInteger[] elementArray) {
        int maxValue = Integer.MIN_VALUE;
        for (AtomicInteger element : elementArray) {
            if (element.get() > maxValue) {
                maxValue = element.get();
            }
        }
        return maxValue;
    }
}