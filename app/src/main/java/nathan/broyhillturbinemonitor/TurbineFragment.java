package nathan.broyhillturbinemonitor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

    private TurbineData mTurbineData;

    private View mTurbineView;
    private ImageView mCompassView;
    private TextView mWindOrientView;
    private TextView mWindSpeedView;
    private TextView mPowerOutputView;

    public static TurbineFragment newInstance() {
        return new TurbineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTurbineData = TurbineData.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_turbine, container, false);
        mTurbineView = view.findViewById(R.id.turbine_blades_image);
        mCompassView = (ImageView) view.findViewById(R.id.compass);
        mWindOrientView = (TextView) view.findViewById(R.id.wind_orient);
        mWindSpeedView = (TextView) view.findViewById(R.id.wind_speed);
        mPowerOutputView = (TextView) view.findViewById(R.id.power_output);

        //Log.d(TAG, "Wind Orientation: " + mTurbineData.getWindOrientation());

        setWindOrientation(mTurbineData.getWindOrientation());
        setWindSpeed(mTurbineData.getWindSpeed());
        setPowerOutput(mTurbineData.getPowerOutput());
        animateTurbine();

        mCompassView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWindOrientation(mTurbineData.getWindOrientation());
            }
        });

        return view;
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
        String text = orientation + " Degrees";
        mWindOrientView.setText(text);
        ObjectAnimator compassAnimator = ObjectAnimator.ofFloat(mCompassView,
                "rotation",
                start,
                orientation)
                .setDuration(1000);
        compassAnimator.start();
    }

    private void setWindSpeed(double speed) {
        String text = speed + " Mph";
        mWindSpeedView.setText(text);

    }

    private void setPowerOutput(double output) {
        String text = output + " kW";
        mPowerOutputView.setText(text);
    }

    private void animateTurbine() {
        float start = mTurbineView.getRotation() % 360;
        float end = (start + (mTurbineData.getRotationSpeed() * 360));
        ObjectAnimator turbineAnimator = ObjectAnimator.ofFloat(mTurbineView, "rotation", start, end)
                .setDuration(1000);
        turbineAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                float start = mTurbineView.getRotation() % 360;
                float end = (start + (mTurbineData.getRotationSpeed() * 360));
                ((ObjectAnimator) animation).setFloatValues(start, end);
            }
        });
        turbineAnimator.setRepeatCount(ValueAnimator.INFINITE);
        turbineAnimator.setInterpolator(new LinearInterpolator());
        turbineAnimator.start();
    }
}
