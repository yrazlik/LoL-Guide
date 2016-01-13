package com.yrazlik.loltr.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.yrazlik.loltr.MainActivity;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.view.RegionDialog;

import java.util.Locale;

/**
 * Created by yrazlik on 1/13/16.
 */
public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Commons.SELECTED_REGION == null || Commons.SELECTED_REGION.length() <= 0 || Commons.SELECTED_LANGUAGE == null || Commons.SELECTED_LANGUAGE.length() <= 0){
            setContentView(R.layout.activity_splash);
            Dialog d  = new RegionDialog(this);
            if(d != null){
                d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Commons.SELECTED_REGION == null || Commons.SELECTED_REGION.length() <= 0 || Commons.SELECTED_LANGUAGE == null || Commons.SELECTED_LANGUAGE.length() <= 0) {
                            setRegionAuomatically();
                            if(Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                                Toast.makeText(getApplicationContext(), "Region was set to " + Commons.SELECTED_REGION.toUpperCase(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Bolge, " + Commons.SELECTED_REGION.toUpperCase() + " olarak secildi.", Toast.LENGTH_SHORT).show();
                            }
                            startMainActivity();
                        } else {
                            saveToSharedPrefs();
                            if(Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                                Toast.makeText(getApplicationContext(), "Region was set to " + Commons.SELECTED_REGION.toUpperCase(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Bolge, " + Commons.SELECTED_REGION.toUpperCase() + " olarak secildi.", Toast.LENGTH_SHORT).show();
                            }
                            startMainActivity();
                        }
                    }

                });
                d.show();
            }else{
                setRegionAuomatically();
                startMainActivity();
            }
        }else{
            startMainActivity();
        }
    }

    private void startMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        finish();
        startActivity(i);
    }

    private void setRegionAuomatically(){
        Locale locale = getApplicationContext().getResources().getConfiguration().locale;
        if(locale.getISO3Country().equalsIgnoreCase("tur") || locale.getISO3Language().equalsIgnoreCase("tur")){
            Commons.SELECTED_LANGUAGE = "tr";
            Commons.SELECTED_REGION = "tr";
            Locale myLocale = new Locale("tr");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }else{
            Commons.SELECTED_LANGUAGE = "en_us";
            Commons.SELECTED_REGION = "na";
            Locale myLocale = new Locale("en_us");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        saveToSharedPrefs();
    }

    private void saveToSharedPrefs(){
        try{
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
            prefs.edit().putString(Commons.LOL_TR_SHARED_PREF_LANGUAGE, Commons.SELECTED_LANGUAGE).commit();
            prefs.edit().putString(Commons.LOL_TR_SHARED_PREF_REGION, Commons.SELECTED_REGION).commit();
        }catch (Exception ignored){}
    }
}
