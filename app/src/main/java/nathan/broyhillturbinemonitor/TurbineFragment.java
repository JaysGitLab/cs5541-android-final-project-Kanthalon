package nathan.broyhillturbinemonitor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Owner on 11/17/2016.
 */

public class TurbineFragment extends Fragment{

    private static final String TAG = "TurbineFragment";
    private static final String COMPASS_ROTATION = "compass rotation";
    private static final String TURBINE_ROTATION = "turbine rotation";

    private TurbineData mTurbineData;
    private BroadcastReceiver mReceiver;
    private ObjectAnimator mTurbineAnimator;

    private View mTurbineView;
    private ImageView mCompassView;
    private TextView mWindOrientView;
    private TextView mWindSpeedView;
    private TextView mPowerOutputView;

    private float compassRotation;
    private float turbineRotation;

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
        View view = inflater.inflate(R.layout.fragment_turbine, container, false);
        mTurbineView = view.findViewById(R.id.turbine_blades_image);
        mCompassView = (ImageView) view.findViewById(R.id.compass);
        mWindOrientView = (TextView) view.findViewById(R.id.wind_orient);
        mWindSpeedView = (TextView) view.findViewById(R.id.wind_speed);
        mPowerOutputView = (TextView) view.findViewById(R.id.power_output);

        if (savedInstanceState != null) {
            mCompassView.setRotation(savedInstanceState.getFloat(COMPASS_ROTATION));
            mTurbineView.setRotation(savedInstanceState.getFloat(TURBINE_ROTATION));
        }

        compassRotation = mCompassView.getRotation();
        turbineRotation = mTurbineView.getRotation();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateValues();
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mReceiver, new IntentFilter("update"));
    }

    @Override
    public void onPause() {
        super.onPause();
        mTurbineAnimator = null;
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mReceiver);
        compassRotation = mCompassView.getRotation();
        turbineRotation = mTurbineView.getRotation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat(COMPASS_ROTATION, compassRotation);
        outState.putFloat(TURBINE_ROTATION, turbineRotation);
    }

    private void updateValues() {
        setWindOrientation(mTurbineData.getWindOrientation());
        setWindSpeed(mTurbineData.getWindSpeed());
        setPowerOutput(mTurbineData.getPowerOutput());
        animateTurbine(mTurbineData.getRotationSpeed());
    }

    private void setWindOrientation(float orientation) {
        float start = mCompassView.getRotation();
        if (start - orientation > 180)
        {
            start -= 360;
        }
        if (orientation - start > 180)
        {
            start += 360;
        }
        String text = String.format(getResources().getString(R.string.direction_format), orientation);
        mWindOrientView.setText(text);
        ObjectAnimator compassAnimator = ObjectAnimator.ofFloat(mCompassView,
                "rotation",
                start,
                orientation)
                .setDuration(1000);
        compassAnimator.start();
    }

    private void setWindSpeed(double speed) {
        String text = String.format(getResources().getString(R.string.speed_format), speed);
        mWindSpeedView.setText(text);

    }

    private void setPowerOutput(double output) {
        String text = String.format(getResources().getString(R.string.power_format), output);
        mPowerOutputView.setText(text);
    }

    private void animateTurbine(float speed) {
        float start = mTurbineView.getRotation() % 360;
        float end = mTurbineView.getRotation() + 360;
        if (mTurbineAnimator == null) {
            Log.d(TAG, "Animator is null");
            mTurbineAnimator = ObjectAnimator.ofFloat(mTurbineView, "rotation", start, end)
                    .setDuration(1000);
            mTurbineAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mTurbineAnimator.setInterpolator(new LinearInterpolator());
        } else {
            Log.d(TAG, "Animator is not null");
            mTurbineAnimator.cancel();
            mTurbineAnimator.setFloatValues(start, end);
        }
        if (speed != 0) {
            Log.d(TAG, "Speed is not 0");
            mTurbineAnimator.setDuration((long) (1000 / speed));
            mTurbineAnimator.start();
        }
    }
}
