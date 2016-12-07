package nathan.broyhillturbinemonitor;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ClipDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Owner on 11/28/2016.
 */

public class DetailsFragment extends Fragment {

    private static final String TAG = "DetailsFragment";
    private static final String CHART_BYTES = "CHART_BYTES";

    private TurbineData mTurbineData;
    private BroadcastReceiver mReceiver;
    private byte[] mChartBytes;

    private ImageView mChartView;
    private ImageView mPowerBar;
    private TextView mPowerText;
    private TextView mTextBulbs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTurbineData = TurbineData.getInstance(getContext());
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateValues();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mPowerBar = (ImageView) view.findViewById(R.id.power_bar);
        mPowerText = (TextView) view.findViewById(R.id.power_text);
        mTextBulbs = (TextView) view.findViewById(R.id.light_bulbs);
        mChartView = (ImageView) view.findViewById(R.id.chart_view);
        if (mChartBytes != null) {
            Log.d(TAG, "Reloading saved chart");
            //byte[] bytes = (byte[]) savedInstanceState.getSerializable(CHART_BYTES);
            mChartView.setImageBitmap(BitmapFactory.decodeByteArray(mChartBytes, 0, mChartBytes.length));
        }
        updateValues();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mReceiver, new IntentFilter("update"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mReceiver);
    }

    private void updateValues() {
        double power = mTurbineData.getPowerOutput();
        setPower(power);
        setLightBulbs(power);
        if (mChartView.getDrawable() == null) {
            Log.d(TAG, "Setting Chart");
            setChart(mTurbineData.getMonthData());
        }
    }

    private void setPower(double power) {
        mPowerText.setText( String.format(
                getResources().getString(R.string.power_format), power));
        ClipDrawable pbar = (ClipDrawable) mPowerBar.getDrawable();
        ObjectAnimator animator = ObjectAnimator.ofInt(pbar,
                "level",
                pbar.getLevel(),
                (int) (power * 100))
                .setDuration(1000);
        animator.start();
    }

    private void setLightBulbs(double power) {
        int bulbs = (int) (power * 100 / 6);
        mTextBulbs.setText( String.format(
                getResources().getString(R.string.number_of), bulbs));
    }

    private void setChart(List<Double> data) {
        StringBuilder builder = new StringBuilder(
                "https://chart.googleapis.com/chart?" +
                "cht=lc&" +
                "chs=600x300&" +
                "chtt=Monthly_Output&" +
                "chxt=y&" +
                "chxl=1:|kW&" +
                "chd=t:");
        builder.append(data.get(0).intValue());
        for (int i = 1; i < data.size(); i++) {
            builder.append(',');
            builder.append(data.get(i).intValue());
        }
        builder.append("&");
        Log.d(TAG, "Target URL: " + builder.toString());
        new ChartFetcherTask(builder.toString()).execute();
    }

    private class ChartFetcherTask extends AsyncTask<Void, Void, byte[]> {
        private String mURL;

        public  ChartFetcherTask(String url) {
            mURL = url;
        }

        @Override
        protected byte[] doInBackground(Void... params) {
            Log.d(TAG, "Getting Chart");
            try {
                return new URLFetcher().fetchURL(mURL);
            } catch (IOException ioe) {
                Log.e(TAG, "Error loading bytes: " + ioe);
                return new byte[0];
            }
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            mChartBytes = bytes;
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mChartView.setImageBitmap(bmp);
        }
    }
}