package nathan.broyhillturbinemonitor;

import java.util.Random;

/**
 * Created by Owner on 12/6/2016.
 */

public class TurbineData {

    private static TurbineData instance;
    private static Random rng;

    private double mWindOrientation;
    private double mWindSpeed;
    private double mPowerOutput;
    private double mRotationSpeed;

    private TurbineData() {
        rng = new Random();
        mWindOrientation = 110;
        mWindSpeed = 20;
        mPowerOutput = 40;
        mRotationSpeed = .5;
    }

    public static TurbineData getInstance() {
        if (instance == null) {
            instance = new TurbineData();
        }
        return instance;
    }

    public float getWindOrientation() {
        return rng.nextInt(360);
    }

    public double getWindSpeed() {
        return mWindSpeed;
    }

    public double getPowerOutput() {
        return mPowerOutput;
    }

    public float getRotationSpeed() {
        return rng.nextFloat();
        //return mRotationSpeed;
    }
}
