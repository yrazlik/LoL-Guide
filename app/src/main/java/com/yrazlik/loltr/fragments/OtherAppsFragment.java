package com.yrazlik.loltr.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.OtherAppsListAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.App;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yrazlik on 15/05/17.
 */

public class OtherAppsFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private ArrayList<App> otherApps;

    private ListView otherAppsList;
    private OtherAppsListAdapter otherAppsListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otherApps = new ArrayList<>();
        createDefaultApps();
        sortAppsById();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other_apps, container, false);
        if(otherAppsList == null) {
            otherAppsList = (ListView) v.findViewById(R.id.otherAppsList);
            otherAppsList.setOnItemClickListener(this);
            setAdapter();
            updateAppsFromFirebase();
        } else {
            notifyDataSetChanged();
        }
        return v;
    }

    private void setAdapter() {
        otherAppsListAdapter = new OtherAppsListAdapter(getContext(), R.layout.list_row_otherapps, otherApps);
        otherAppsList.setAdapter(otherAppsListAdapter);
    }

    private void notifyDataSetChanged() {
        if(otherAppsListAdapter != null) {
            otherAppsListAdapter.notifyDataSetChanged();;
        }
    }

    private void createDefaultApps() {
        App blackjack = new App("1", "Blackjack", "https://play.google.com/store/apps/details?id=com.randomapps.blackjack", "com.randomapps.blackjack", "https://lh3.googleusercontent.com/UAjndK9gpXwu3jeLylemM1gN8OnVoEhp3mcJW8QeTphp8ngz6yP1_4lpinp3tz3fCw=w300");
        App tvSeries = new App("0", "TV Series Tracker", "https://play.google.com/store/apps/details?id=com.yrazlik.tvshowtracker", "com.yrazlik.tvshowtracker", "https://lh3.googleusercontent.com/1qnIaHd7EperIKfWsM6bYEXCO3jYJPNFNcZ4YOmn976qhnMs5MbxS5yCk5q5tcqBimcl=w300");
        App listify = new App("2", "Listify", "https://play.google.com/store/apps/details?id=com.yrazlik.listify", "com.yrazlik.listify", "https://lh3.googleusercontent.com/OBsAvQb_XnOoa2GaW_Iz2mbzXt84XLznc-MIIIg2tgwhqfSYyKGRJXwaByIVpqyxLw=w300");
        App jumpAndRun = new App("3", "Jump and Run", "https://play.google.com/store/apps/details?id=com.yrazlik.jumpandrun", "com.yrazlik.jumpandrun", "https://lh3.googleusercontent.com/zmwHQ-aXgQIsbRaDd2qqARmi0jg-s-qcEAeA_b1fBRTTxaBviIXkjT3ywMZek8_kO4E=w300");
        App whatColor = new App("4", "What Color?", "https://play.google.com/store/apps/details?id=com.randomapps.whatcolor", "com.randomapps.whatcolor", "https://lh3.googleusercontent.com/WikV5OOe8rlRAOrKl2pYvExL0n_kyYFD9xk3lPtYQzQ7LSj9813gntEN-6Gl6TfR3Jg=w300");
        otherApps.add(blackjack);
        otherApps.add(tvSeries);
        otherApps.add(listify);
        otherApps.add(jumpAndRun);
        otherApps.add(whatColor);
    }

    private void updateAppsFromFirebase() {
        Firebase firebase = new Firebase(getResources().getString(R.string.lol_firebase));
        firebase.child("other-apps").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                otherApps.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> keyValues = (HashMap<String, Object>) postSnapshot.getValue();
                    if (keyValues != null && keyValues.size() > 0) {
                        String id = (String) keyValues.get("id");
                        String name = (String) keyValues.get("name");
                        String url = (String) keyValues.get("url");
                        String packageName = (String) keyValues.get("packageName");
                        String img = (String) keyValues.get("img");
                        otherApps.add(new App(id, name, url, packageName,img));
                    }
                }
                sortAppsById();
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("OtherAppsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object clickedItem = parent.getAdapter().getItem(position);
        if(clickedItem instanceof App) {
            openInMarket((App) clickedItem);
        }
    }

    private void openInMarket(App app) {
        try {
            if(Commons.isValidString(app.getPackageName())) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app.getPackageName())));
            } else {
                Commons.openInBrowser(getContext(), app.getUrl());
            }
        } catch (Exception e) {
            Commons.openInBrowser(getContext(), app.getUrl());
        }
    }

    private void sortAppsById() {
        try {
            Collections.sort(otherApps, new Comparator<App>() {
                @Override
                public int compare(App a1, App a2) {
                    return a1.getId().compareTo(a2.getId());
                }
            });
        } catch (Exception ignored) {}
    }
}
