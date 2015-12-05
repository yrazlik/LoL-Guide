package com.yrazlik.loltr.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;

/**
 * Created by yrazlik on 1/6/15.
 */
public class AboutFragment extends BaseFragment {



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        TextView currentVersionTV = (TextView) v.findViewById(R.id.currentVersionTV);
        if(Commons.LATEST_VERSION != null) {
            currentVersionTV.setText("Version " + Commons.LATEST_VERSION + ".");
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("AboutFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
