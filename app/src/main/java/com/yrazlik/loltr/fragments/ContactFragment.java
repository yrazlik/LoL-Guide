package com.yrazlik.loltr.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.utils.AdUtils;
import com.yrazlik.loltr.utils.Utils;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.view.RobotoButton;
import com.yrazlik.loltr.view.RobotoEditText;

/**
 * Created by yrazlik on 1/6/15.
 */
public class ContactFragment extends BaseFragment implements View.OnClickListener{

    private LinearLayout parentView;
    private RobotoButton send;
    private RobotoEditText message;

    private LinearLayout facebookButton, twitterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAGGG", "ContactFragmentOnCreateView");
        View v = inflater.inflate(R.layout.fragment_contact, container, false);

        parentView = (LinearLayout) v.findViewById(R.id.parentView);
        send = (RobotoButton) v.findViewById(R.id.buttonContact);
        message = (RobotoEditText) v.findViewById(R.id.edittextContactBox);
        facebookButton = (LinearLayout) v.findViewById(R.id.facebookButton);
        twitterButton = (LinearLayout) v.findViewById(R.id.twitterButton);

        facebookButton.setOnClickListener(this);
        twitterButton.setOnClickListener(this);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = message.getText().toString();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { "yrazlik@gmail.com", "loltrdestek@gmail.com" });
                i.putExtra(Intent.EXTRA_TEXT, s);
                startActivity(i);
            }
        });

        Utils.addView(parentView, AdUtils.getInstance().createLargeAdView());

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
        t.setScreenName("ContactFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onClick(View v) {
        if(v == facebookButton) {
            Commons.openFacebookPage(getContext());
        } else if(v == twitterButton) {
            Commons.openTwitterPage(getContext());
        }
    }
}
