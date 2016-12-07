package nathan.broyhillturbinemonitor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Owner on 11/28/2016.
 */

public class ProjectFragment extends Fragment {

    TextView mLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project, container, false);
        mLink = (TextView) view.findViewById(R.id.rei_link);

        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getResources().getString(R.string.asurei_url);
                Uri address = Uri.parse(url);
                Intent i = new Intent(Intent.ACTION_VIEW, address);
                startActivity(i);
            }
        });

        return view;
    }
}
