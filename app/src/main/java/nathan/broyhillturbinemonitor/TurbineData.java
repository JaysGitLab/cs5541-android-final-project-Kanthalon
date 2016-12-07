package nathan.broyhillturbinemonitor;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.os.SystemClock.sleep;

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
        mWindOrientation = 0;
        mWindSpeed = 0;
        mPowerOutput = 0;
        mRotationSpeed = 0;
        final Handler dataHandler = new Handler();
        Runnable dataRunnable = new Runnable() {
            @Override
            public void run() {
                new UpdateDataTask().execute();
                dataHandler.postDelayed(this, 500);
            }
        };
        dataRunnable.run();
    }

    public static TurbineData getInstance() {
        if (instance == null) {
            instance = new TurbineData();
        }
        return instance;
    }

    public float getWindOrientation() {
        return (float) mWindOrientation;
    }

    public float getWindSpeed() {
        return (float) mWindSpeed;
    }

    public float getPowerOutput() {
        return (float) mPowerOutput;
    }

    public float getRotationSpeed() {
        return (float) mRotationSpeed;
    }



    private class UpdateDataTask extends AsyncTask<Void, Void, List<Double>> {
        public UpdateDataTask() {
            rng = new Random();
        }

        @Override
        protected List<Double> doInBackground(Void... params) {
            ArrayList<Double> data = new ArrayList<>();
            data.add((double) rng.nextInt(360));
            data.add((double) rng.nextInt(61));
            data.add((double) rng.nextInt(101));
            data.add(rng.nextDouble());
            sleep(500);
            return data;
        }

        @Override
        protected void onPostExecute(List<Double> data) {
            mWindOrientation = data.get(0);
            mWindSpeed = data.get(1);
            mPowerOutput = data.get(2);
            mRotationSpeed = data.get(3);
        }
    }
}
