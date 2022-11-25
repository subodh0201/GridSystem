package util;

import java.awt.*;

public class RandomUtil {

    private RandomUtil() {}


    /**
     * Returns a uniformly random double in [low, high)
     * @param low lower bound
     * @param high upper bound
     * @return a uniformly random double in [low, high)
     */
    public static double uniform(double low, double high) {
        return Math.random() * (high - low) + low;
    }


    /**
     * Returns a uniformly random int in [low, high)
     * @param low lower bound
     * @param high upper bound
     * @return a uniformly random int in [low, high)
     */
    public static int uniform(int low, int high) {
        return (int)uniform(low, (double)high);
    }


    /**
     * Returns a Point with uniformly random coordinates in [low, high)
     * @param low lower bound
     * @param high upper bound
     * @return a Point with uniformly random coordinates in [low, high)
     */
    public static Point uniformPoint(int low, int high) {
        return new Point(
                RandomUtil.uniform(low, high),
                RandomUtil.uniform(low, high)
        );
    }
}
