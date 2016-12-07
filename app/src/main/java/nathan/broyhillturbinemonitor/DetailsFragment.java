package nathan.broyhillturbinemonitor;

import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Owner on 11/28/2016.
 */

public class DetailsFragment extends Fragment {

    private static final String CHART_URL = "https://chart.googleapis.com/chart?" +
            "cht=lc&" +
            "chs=600x300&" +
            "chtt=Monthly Output&" +
            "chd=t:70,30,50,20,70,30,50,20,70,30,50,20&";

    private TurbineData mTurbineData;
    private Handler mHandler;
    private Runnable mUpdateRunnable;

    private WebView mWebView;
    private ImageView mPowerBar;
    private TextView mPowerText;
    private TextView mTextBulbs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mTurbineData = TurbineData.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mPowerBar = (ImageView) view.findViewById(R.id.power_bar);
        mPowerText = (TextView) view.findViewById(R.id.power_text);
        mTextBulbs = (TextView) view.findViewById(R.id.light_bulbs);
        mWebView = (WebView) view.findViewById(R.id.chart_view);
        mWebView.loadUrl(CHART_URL);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);


        mHandler = new Handler();
        mUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                double power = mTurbineData.getPowerOutput();
                setPower(power);
                setLightBulbs(power);
                mHandler.postDelayed(this, 2000);
            }
        };
        mUpdateRunnable.run();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mUpdateRunnable);
    }

    private void setPower(double power) {
        mPowerText.setText( String.format(
                getResources().getString(R.string.power_format), power));
        ClipDrawable pbar = (ClipDrawable) mPowerBar.getDrawable();
        pbar.setLevel((int) (power * 100));
    }

    private void setLightBulbs(double power) {
        int bulbs = (int) (power * 100 / 6);
        mTextBulbs.setText( String.format(
                getResources().getString(R.string.number_of), bulbs));
    }
}