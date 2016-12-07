package nathan.broyhillturbinemonitor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.os.SystemClock.sleep;

/**
 * Created by Owner on 12/6/2016.
 */

public class TurbineData {

    private static TurbineData sInstance;
    private static Random sRNG;

    private Context mContext;

    private double mWindOrientation;
    private double mWindSpeed;
    private double mPowerOutput;
    private double mRotationSpeed;

    private TurbineData() {}

    private TurbineData(Context context) {
        mWindOrientation = 0;
        mWindSpeed = 0;
        mPowerOutput = 0;
        mRotationSpeed = 0;
        sRNG = new Random();
        mContext = context;
        final Handler dataHandler = new Handler();
        Runnable dataRunnable = new Runnable() {
            @Override
            public void run() {
                new UpdateDataTask().execute();
                dataHandler.postDelayed(this, 2000);
            }
        };
        dataRunnable.run();
    }

    public static TurbineData getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TurbineData(context);
        }
        return sInstance;
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

        @Override
        protected List<Double> doInBackground(Void... params) {
            // Connect to server and get data
            ArrayList<Double> data = new ArrayList<>();
            data.add((double) sRNG.nextInt(360));
            data.add((double) sRNG.nextInt(61));
            data.add((double) sRNG.nextInt(101));
            data.add(sRNG.nextDouble());
            sleep(500);
            return data;
        }

        @Override
        protected void onPostExecute(List<Double> data) {
            // Update local data
            mWindOrientation = data.get(0);
            mWindSpeed = data.get(1);
            mPowerOutput = data.get(2);
            mRotationSpeed = data.get(3);

            // Tell active fragments that new data is available
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("update"));
        }
    }
}
