package chapter1.exercises.e1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class Philosopher extends Thread {

    int seat;
    Chopstick[] chopsticks;
    private Logger logger = LoggerFactory.getLogger("Philosopher");
    ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public Philosopher(int seat, Chopstick[] chopsticks) {
        this.seat = seat;
        this.chopsticks = chopsticks;
    }

    @Override
    public void run() {
        // from the problem description, the philosopher will always be thinking or eating, so use while (true)
        while (true) {
            tryToEat();
            int waitingTimeSeconds = threadLocalRandom.nextInt(6);
            logger.info("philosopher {} is thinking/waiting to eat, and the time is {} seconds",
                    seat, waitingTimeSeconds);
            try {
                Thread.sleep(waitingTimeSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void tryToEat() {
        // take the left chopstick at position: seat
        // take the left chopstick at position: (seat + 1) % chopsticks.length
        if (chopsticks[seat].tryAcquire()) {
            int rightPosition = (seat + 1) % chopsticks.length;
            if (chopsticks[rightPosition].tryAcquire()) {
                int eatingTimeSeconds = threadLocalRandom.nextInt(6);
                logger.info("philosopher {} is in eating, chopsticks {} and {} are in use, " +
                        "eating time: {} seconds...", seat, seat, rightPosition, eatingTimeSeconds);
                logger.info("======================");
                try {
                    Thread.sleep(eatingTimeSeconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                chopsticks[seat].release();
                chopsticks[rightPosition].release();
                logger.info("philosopher {} finished eating", seat);
            } else {
                // if this guy only gets the left chopstick, release it for others
                chopsticks[seat].release();
            }
        } else {
            logger.info("chopstick {} is in use, waiting...", seat);
        }
    }
}
