package com.yrazlik.loltr.view;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.RegionListAdapter;
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
    private RobotoTextView title;
    private RegionListAdapter adapter;
    private String[] regions = {"Turkey", "Europe West", "North America", "Europe North/East", "Japan", "Oceania", "Brazil", "Latin America North", "Latin America South", "Russia", "Korea"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_region);
        regionList = (ListView) findViewById(R.id.regionList);
        title = (RobotoTextView) findViewById(R.id.title);
        Commons.underline(title);
        adapter = new RegionListAdapter(mContext, R.layout.list_row_text, regions);
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
                } else if(region.equalsIgnoreCase("Japan")){
                    Commons.SELECTED_REGION = "jp";
                } else if(region.equalsIgnoreCase("Japan")){
                    Commons.SELECTED_REGION = "jp";
                } else{
                    Commons.SELECTED_REGION = "tr";
                }

                Commons.saveToSharedPrefs(Commons.LOL_TR_SHARED_PREF_REGION, Commons.SELECTED_REGION);
                dismiss();
            }
        });
        regionList.setAdapter(adapter);
    }
}
