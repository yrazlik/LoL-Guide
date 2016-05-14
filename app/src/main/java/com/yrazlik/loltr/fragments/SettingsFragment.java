package com.yrazlik.loltr.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.MainActivity;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.billing.PaymentSevice;
import com.yrazlik.loltr.commons.Commons;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by yrazlik on 10/11/15.
 */
public class SettingsFragment extends BaseFragment{


    private String[] regions = {"TR", "EUW", "NA", "EUNE", "OCE", "BR", "LAN", "LAS", "RU", "KR"};
    String[] languages;
    private Spinner languageSpinner, regionSpinner;
    private ArrayAdapter<String> languageSpinnerAdapter, regionSpinnerAdapter;
    private Button buttonSave;
    private TextView selectLanguageText, selectRegionText;
    private Button removeAdsButton;
    private ImageView parentBG;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        selectLanguageText = (TextView) v.findViewById(R.id.selectLanguageText);
        selectRegionText = (TextView) v.findViewById(R.id.selectRegionText);
        parentBG = (ImageView) v.findViewById(R.id.parentBG);
        parentBG.setAlpha(0.3f);
        parentBG.setBackgroundResource(R.drawable.riven);

        SharedPreferences prefs = getContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
        String region = prefs.getString(Commons.LOL_TR_SHARED_PREF_REGION, null);
        String language = prefs.getString(Commons.LOL_TR_SHARED_PREF_LANGUAGE, null);

        if(region != null){
            String[] newRegions = new String[10];
            String[] restOfRegions = new String[9];
            int i = 0;
            for(String r : regions){
                if(r.equalsIgnoreCase(region)){
                    newRegions[0] = r;
                }
                else{
                    restOfRegions[i] = r;
                    i++;
                }
            }

            int j = 1;
            for(String r : restOfRegions){
                newRegions[j] = r;
                j++;
            }
            regions = newRegions;
        }

        languages = new String[]{getContext().getResources().getString(R.string.turkish), getResources().getString(R.string.english)};
        if(language != null){
            if(language.equalsIgnoreCase("tr")){
                languages = new String[]{getContext().getResources().getString(R.string.turkish), getResources().getString(R.string.english)};
            }else{
                languages = new String[]{getContext().getResources().getString(R.string.english), getResources().getString(R.string.turkish)};
            }
        }

        languageSpinner = (Spinner)v.findViewById(R.id.languageSpinner);
        regionSpinner = (Spinner)v.findViewById(R.id.regionSpinner);
        buttonSave = (Button)v.findViewById(R.id.buttonSave);


        languageSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, languages);
        regionSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, regions);

        languageSpinner.setAdapter(languageSpinnerAdapter);
        languageSpinnerAdapter.notifyDataSetChanged();
        regionSpinner.setAdapter(regionSpinnerAdapter);
        regionSpinnerAdapter.notifyDataSetChanged();

        regionSpinner.setSelection(0);
        languageSpinner.setSelection(0);

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
                    Locale myLocale = new Locale("en_us");
                    Resources res = getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    Configuration conf = res.getConfiguration();
                    conf.locale = myLocale;
                    res.updateConfiguration(conf, dm);
                    Commons.SELECTED_LANGUAGE = "en_us";
                }

                if(region.equalsIgnoreCase("TR")){
                    Commons.SELECTED_REGION = "tr";
                }else if(region.equalsIgnoreCase("EUW")){
                    Commons.SELECTED_REGION = "euw";
                }else if(region.equalsIgnoreCase("NA")){
                    Commons.SELECTED_REGION = "na";
                }else if(region.equalsIgnoreCase("EUNE")){
                    Commons.SELECTED_REGION = "eune";
                }else if(region.equalsIgnoreCase("OCE")){
                    Commons.SELECTED_REGION = "oce";
                }else if(region.equalsIgnoreCase("LAS")){
                    Commons.SELECTED_REGION = "las";
                }else if(region.equalsIgnoreCase("LAN")){
                    Commons.SELECTED_REGION = "lan";
                }else if(region.equalsIgnoreCase("BR")){
                    Commons.SELECTED_REGION = "br";
                }else if(region.equalsIgnoreCase("RU")){
                    Commons.SELECTED_REGION = "ru";
                }else if(region.equalsIgnoreCase("KR")){
                    Commons.SELECTED_REGION = "kr";
                }

                try{
                    SharedPreferences prefs = getContext().getApplicationContext().getSharedPreferences(Commons.LOL_TR_SHARED_PREFS, Context.MODE_PRIVATE);
                    prefs.edit().putString(Commons.LOL_TR_SHARED_PREF_LANGUAGE, Commons.SELECTED_LANGUAGE).commit();
                    prefs.edit().putString(Commons.LOL_TR_SHARED_PREF_REGION, Commons.SELECTED_REGION).commit();
                }catch (Exception ignored){}

                Toast.makeText(getContext().getApplicationContext(), getContext().getResources().getString(R.string.preferences_saved), Toast.LENGTH_SHORT).show();
                ((MainActivity)getActivity()).updateDrawer();

                if(Commons.SELECTED_LANGUAGE.equalsIgnoreCase("tr")){
                    selectLanguageText.setText(R.string.languageTurkish);
                    selectRegionText.setText(R.string.regionTurkish);
                    languages = new String[]{getContext().getResources().getString(R.string.turkishTurkish), getResources().getString(R.string.englishTurkish)};
                    languageSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, languages);
                    languageSpinner.setAdapter(languageSpinnerAdapter);
                    languageSpinnerAdapter.notifyDataSetChanged();
                }else{
                    selectLanguageText.setText(R.string.languageEnglish);
                    selectRegionText.setText(R.string.regionEnglish);
                    languages = new String[]{getContext().getResources().getString(R.string.englishEnglish), getResources().getString(R.string.turkishEnglish)};
                    languageSpinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, languages);
                    languageSpinner.setAdapter(languageSpinnerAdapter);
                    languageSpinnerAdapter.notifyDataSetChanged();
                }
                Commons.allItemsNew = null;
              /*  SettingsFragment frg = null;
                frg = (SettingsFragment) getFragmentManager().findFragmentByTag(Commons.TAG_SETTINGS_FRAGMENT);
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();*/

            }
        });

        removeAdsButton = (Button) v.findViewById(R.id.removeAdsButton);
        removeAdsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> skuList = new ArrayList<String>();
                skuList.add("remove_ads");
                final Bundle querySkus = new Bundle();
                querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
                final Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bundle skuDetails = PaymentSevice.getInstance(getActivity()).getService().getSkuDetails(3, getActivity().getPackageName(), "inapp", querySkus);
                            int response = skuDetails.getInt("RESPONSE_CODE");
                            if (response == 0) {
                                ArrayList<String> responseList
                                        = skuDetails.getStringArrayList("DETAILS_LIST");

                                for (String thisResponse : responseList) {
                                    JSONObject object = new JSONObject(thisResponse);
                                    String sku = object.getString("productId");
                                    if(sku.equalsIgnoreCase(Commons.REMOVE_ADS_ID)){
                                        Bundle buyIntentBundle = PaymentSevice.getInstance(getActivity()).buyRemoveAdsItem(sku);
                                        if(buyIntentBundle != null) {
                                            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                                            try {
                                                getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
                                                        Commons.REMOVE_ADS_REQUEST_CODE, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                                                        Integer.valueOf(0));
                                            } catch (IntentSender.SendIntentException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (RemoteException e) {

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
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
