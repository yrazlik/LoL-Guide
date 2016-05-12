package com.yrazlik.loltr.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 1/6/15.
 */
public class ContactFragment extends BaseFragment{

    private Button send;
    private EditText message;
    private ImageView parentBG;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAGGG", "ContactFragmentOnCreateView");
        View v = inflater.inflate(R.layout.fragment_contact, container, false);
        parentBG = (ImageView) v.findViewById(R.id.parentBG);
        parentBG.setAlpha(0.3f);
        parentBG.setBackgroundResource(R.drawable.annie);
        send = (Button)v.findViewById(R.id.buttonContact);
        message = (EditText)v.findViewById(R.id.edittextContactBox);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = message.getText().toString();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { "yrazlik@gmail.com" });
                i.putExtra(Intent.EXTRA_TEXT, s);
                startActivity(i);
            }
        });

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
}
