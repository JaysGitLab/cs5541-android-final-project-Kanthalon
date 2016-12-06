package nathan.broyhillturbinemonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * Created by Owner on 11/28/2016.
 */

public class DetailsFragment extends Fragment {

    private static final String CHART_URL = "https://chart.googleapis.com/chart?" +
            "cht=lc&" +
            "chs=600x300&" +
            "chtt=Monthly Output&" +
            "chd=t:70,30,50,20,70,30,50,20,70,30,50,20&";

    private WebView mWebView;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        mWebView = (WebView) view.findViewById(R.id.chart_view);
        mWebView.loadUrl(CHART_URL);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        return view;
    }
}