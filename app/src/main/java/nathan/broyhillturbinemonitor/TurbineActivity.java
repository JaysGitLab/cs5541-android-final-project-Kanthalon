package nathan.broyhillturbinemonitor;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TurbineActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return TurbineFragment.newInstance();
    }
}
