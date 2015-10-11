package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.MainActivity;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;

import java.util.Locale;

/**
 * Created by yrazlik on 10/11/15.
 */
public class SettingsFragment extends BaseFragment{


    private String[] regions = {"TR", "EUW", "NA", "EUN", "OC", "BR", "LAN", "LAS", "RU", "KR", "PBE"};
    private Spinner languageSpinner, regionSpinner;
    private ArrayAdapter<String> languageSpinnerAdapter, regionSpinnerAdapter;
    private Button buttonSave;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        languageSpinner = (Spinner)v.findViewById(R.id.languageSpinner);
        regionSpinner = (Spinner)v.findViewById(R.id.regionSpinner);
        buttonSave = (Button)v.findViewById(R.id.buttonSave);

        String[] languages = {getContext().getResources().getString(R.string.turkish), getResources().getString(R.string.english)};

        languageSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, languages);
        regionSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regions);

        languageSpinner.setAdapter(languageSpinnerAdapter);
        regionSpinner.setAdapter(regionSpinnerAdapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = languageSpinner.getSelectedItem().toString();
                String region = regionSpinner.getSelectedItem().toString();

                if(language.equalsIgnoreCase(getResources().getString(R.string.turkish))){
                    Locale myLocale = new Locale("tr");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                    Commons.SELECTED_LANGUAGE = "tr";
                }else if(language.equalsIgnoreCase(getResources().getString(R.string.english))){
                    Locale myLocale = new Locale("en_US");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                    Commons.SELECTED_LANGUAGE = "en_US";
                }

                if(region.equalsIgnoreCase("TR")){
                    Commons.SELECTED_REGION = "tr";
                }else{
                    Commons.SELECTED_REGION = "na";
                }

                Toast.makeText(getContext().getApplicationContext(), getContext().getResources().getString(R.string.preferences_saved), Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).updateDrawer();
                SettingsFragment frg = null;
                frg = (SettingsFragment) getFragmentManager().findFragmentByTag(Commons.TAG_SETTINGS_FRAGMENT);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();
            }
        });

        return v;
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("SettingsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public Context getContext() {
        return getActivity();
    }
}
