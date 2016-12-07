package nathan.broyhillturbinemonitor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;

import java.util.List;
import java.util.Random;

/**
 * Created by Owner on 12/6/2016.
 */

public class TurbineData {

    private static TurbineData sInstance;
    private static Random sRNG;

    private Context mContext;

    private DataAdapter mData;

    private TurbineData(Context context) {
        mData = new DataAdapter();
        sRNG = new Random();
        mContext = context;
        final Handler dataHandler = new Handler();
        Runnable dataRunnable = new Runnable() {
            @Override
            public void run() {
                new UpdateDataTask().execute();
                dataHandler.postDelayed(this, 5000);
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
        return (float) mData.getWindOrientation();
    }

    public float getWindSpeed() {
        return (float) mData.getWindSpeed();
    }

    public float getPowerOutput() {
        return (float) mData.getPowerOutput();
    }

    public float getRotationSpeed() {
        return (float) mData.getRotationSpeed();
    }

    public List<Double> getMonthData() {
        return mData.getMonthData();
    }

    private class UpdateDataTask extends AsyncTask<Void, Void, DataAdapter> {

        @Override
        protected DataAdapter doInBackground(Void... params) {
            // Connect to server and get data
            DataAdapter data = new DataAdapter("insert HTTP response here");
            return data;
        }

        @Override
        protected void onPostExecute(DataAdapter data) {
            // Update local data
            mData = data;

            // Tell active fragments that new data is available
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("update"));
        }
    }
}
