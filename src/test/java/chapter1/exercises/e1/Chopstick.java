package chapter1.exercises.e1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;

public class Chopstick {

    int position;
    Semaphore semaphore = new Semaphore(1);
    private Logger logger = LoggerFactory.getLogger("Chopstick");

    public Chopstick(int position) {
        this.position = position;
    }

    public boolean tryAcquire() {
        boolean tryAcquire = semaphore.tryAcquire(1);
        if (tryAcquire) {
            logger.info("chopstick {} acquired", position);
        } else {
            logger.info("chopstick {} is in use, fail to acquire", position);
        }
        return tryAcquire;
    }

    public void release() {
        semaphore.release(1);
        logger.info("chopstick {} released", position);

    }
}
