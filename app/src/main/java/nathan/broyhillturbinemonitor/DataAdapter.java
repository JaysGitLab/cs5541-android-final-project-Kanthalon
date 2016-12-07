package nathan.broyhillturbinemonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Owner on 12/7/2016.
 */

public class DataAdapter {
    private ArrayList<Double> data;
    private Random mRNG;

    /* Create empty DataAdapter */
    public DataAdapter() {
        mRNG = new Random();
        data = new ArrayList<>();

        for (int i = 0; i < 34; i++) {
            data.add(0.0); // Daily average power outputs
        }
    }

    /* Parse response from server */
    public DataAdapter(String responseData) {
        mRNG = new Random();
        data = new ArrayList<>();

        data.add((double) mRNG.nextInt(360)); // Wind orientation
        data.add((double) mRNG.nextInt(61)); // Wind speed
        data.add((double) mRNG.nextInt(101)); // Power output
        data.add(mRNG.nextDouble()); // Rotation speed

        for (int i = 0; i < 30; i++) {
            data.add((double) mRNG.nextInt(101)); // Daily average power outputs
        }
    }

    public double getWindOrientation() {
        return data.get(0);
    }
    public double getWindSpeed() {
        return data.get(1);
    }
    public double getPowerOutput() {
        return data.get(2);
    }
    public double getRotationSpeed() {
        return data.get(3);
    }
    public List<Double> getMonthData() {
        return data.subList(4, data.size());
    }
}
