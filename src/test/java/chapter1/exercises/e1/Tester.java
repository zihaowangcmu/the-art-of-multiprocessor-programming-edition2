package chapter1.exercises.e1;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tester {

    private Logger logger = LoggerFactory.getLogger("Tester");

    @Test
    public void test() throws InterruptedException {
        logger.info("Test starts");

        // number of philosophies, also is the number of chopsticks
        int n = 5;

        // use this array to mimic the chopsticks on the round table
        Chopstick[] chopsticks = new Chopstick[n];
        for (int i = 0; i < n; i++) {
            // i is the position of the chopstick
            chopsticks[i] = new Chopstick(i);
        }

        // use this array to mimic the philosophers
        Philosopher[] philosophers = new Philosopher[n];
        for (int i = 0; i < n; i++) {
            // i is the position of the philosopher
            philosophers[i] = new Philosopher(i, chopsticks);
            philosophers[i].start();
        }

        Thread.sleep(60 * 60 * 1000);
    }
}
