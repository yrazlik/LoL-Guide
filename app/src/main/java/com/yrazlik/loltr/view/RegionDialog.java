package com.yrazlik.loltr.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;

import java.util.Locale;

/**
 * Created by yrazlik on 1/13/16.
 */
public class RegionDialog extends Dialog{

    private Context mContext;

    public RegionDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    private ListView regionList;
    private ArrayAdapter<String> adapter;
    private String[] regions = {"Turkey", "Europe West", "North America", "Europe North/East", "Oceania", "Brazil", "Latin America North", "Latin America South", "Russia", "Korea"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_region);
        regionList = (ListView) findViewById(R.id.regionList);
        adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, regions);
        regionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String region = regions[position];

                if(region.equalsIgnoreCase("Turkey")){
                    Commons.SELECTED_REGION = "tr";
                }else if(region.equalsIgnoreCase("Europe West")){
                    Commons.SELECTED_REGION = "euw";
                }else if(region.equalsIgnoreCase("North America")){
                    Commons.SELECTED_REGION = "na";
                }else if(region.equalsIgnoreCase("Europe North/East")){
                    Commons.SELECTED_REGION = "eune";
                }else if(region.equalsIgnoreCase("Oceania")){
                    Commons.SELECTED_REGION = "oce";
                }else if(region.equalsIgnoreCase("Latin America South")){
                    Commons.SELECTED_REGION = "las";
                }else if(region.equalsIgnoreCase("Latin America North")){
                    Commons.SELECTED_REGION = "lan";
                }else if(region.equalsIgnoreCase("Brazil")){
                    Commons.SELECTED_REGION = "br";
                }else if(region.equalsIgnoreCase("Russia")){
                    Commons.SELECTED_REGION = "ru";
                }else if(region.equalsIgnoreCase("Korea")){
                    Commons.SELECTED_REGION = "kr";
                }else{
                    Commons.SELECTED_REGION = "tr";
                }

                Locale locale = mContext.getApplicationContext().getResources().getConfiguration().locale;
                if (locale.getISO3Country().equalsIgnoreCase("tur") || locale.getISO3Language().equalsIgnoreCase("tur")) {
                    Commons.SELECTED_LANGUAGE = "tr";
                    Locale myLocale = new Locale("tr");
                    Resources res = mContext.getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                } else {
                    Commons.SELECTED_LANGUAGE = "en_us";
                    Locale myLocale = new Locale("en_us");
                    Resources res = mContext.getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                }
                saveToSharedPrefs();
                try{
                    if(Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                        Toast.makeText(mContext.getApplicationContext(), "Region was set to " + Commons.SELECTED_REGION.toUpperCase(), Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext.getApplicationContext(), "Bolge, " + Commons.SELECTED_REGION.toUpperCase() + " olarak secildi.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ignored){}
                dismiss();
            }
        });
        regionList.setAdapter(adapter);
    }

    private void saveToSharedPrefs(){
        try{
            SharedPreferences prefs = mContext.getApplicationContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
            prefs.edit().putString(Commons.LOL_TR_SHARED_PREF_LANGUAGE, Commons.SELECTED_LANGUAGE).commit();
            prefs.edit().putString(Commons.LOL_TR_SHARED_PREF_REGION, Commons.SELECTED_REGION).commit();
        }catch (Exception ignored){}
    }
}
