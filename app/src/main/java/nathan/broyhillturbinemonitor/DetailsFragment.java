package nathan.broyhillturbinemonitor;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Owner on 11/28/2016.
 */

public class DetailsFragment extends Fragment {

    private TurbineData mTurbineData;
    private BroadcastReceiver mReceiver;

    private WebView mWebView;
    private ImageView mPowerBar;
    private TextView mPowerText;
    private TextView mTextBulbs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        mWebView = (WebView) view.findViewById(R.id.chart_view);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
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
        setChart(mTurbineData.getMonthData());
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
                "chtt=Monthly Output&" +
                "chd=t:");
        builder.append(data.get(0));
        for (int i = 1; i < data.size(); i++) {
            builder.append(',');
            builder.append(data.get(i));
        }
        builder.append("&");
        mWebView.loadUrl(builder.toString());
    }
}