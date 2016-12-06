package nathan.broyhillturbinemonitor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

/**
 * Created by Owner on 12/3/2016.
 */

public class FragmentTabActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_tabs);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("history").setIndicator(getString(R.string.history)),
                HistoryFragment.class, null
        );

        mTabHost.addTab(
                mTabHost.newTabSpec("turbine").setIndicator(getString(R.string.turbine)),
                TurbineFragment.class, null
        );

        mTabHost.addTab(
                mTabHost.newTabSpec("details").setIndicator(getString(R.string.details)),
                DetailsFragment.class, null
        );

        mTabHost.addTab(
                mTabHost.newTabSpec("project").setIndicator(getString(R.string.project)),
                ProjectFragment.class, null
        );

        mTabHost.setCurrentTabByTag("turbine");
    }
}
