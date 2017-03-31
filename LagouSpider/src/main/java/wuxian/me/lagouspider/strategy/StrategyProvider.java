package wuxian.me.lagouspider.strategy;

import java.util.Random;

/**
 * Created by wuxian on 31/3/2017.
 * <p>
 * usage:
 * 1 IStrategy strategy = StrategyProvider.getStrategy();
 * 2 strategy.setRunnable(real-runnable);
 * 3 strategy.run();
 */
public class StrategyProvider {
    private StrategyProvider() {
    }

    private static Random random = new Random();

    public static IStrategy getStrategy() {

        if (random.nextDouble() * 100 > 30) {
            return new DelayStrategy();
        }

        return new ImmediateStrategy();
    }
}
