package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.annotations.NotNull;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.WeeklyFreeChampionsAdapter;
import com.yrazlik.loltr.api.ApiHelper;
import com.yrazlik.loltr.api.error.ApiResponseListener;
import com.yrazlik.loltr.api.error.RetrofitResponseHandler;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.db.DbHelper;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.model.ChampionListDto;
import com.yrazlik.loltr.model.ImageDto;
import com.yrazlik.loltr.model.WeeklyFreeResponseDto;
import com.yrazlik.loltr.utils.CacheUtils;
import com.yrazlik.loltr.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import retrofit2.Call;
import retrofit2.Response;

public class WeeklyFreeChampionsFragment extends BaseFragment implements OnItemClickListener {

    private interface IpRpPricesListener {
        void onIpRpPricesReceived();
    }

    private ListView weeklyFreeChampionsList;

    private List<ChampionDto> weeklyFreeChampions;
    private WeeklyFreeChampionsAdapter weeklyFreeChampionsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weeklyFreeChampions = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initUI(inflater, container);
        populateWeeklyFreeChampionsList();
        return rootView;
    }

    private void initUI(LayoutInflater inflater, ViewGroup container) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_weekly_free_champions, container, false);
            showProgressWithWhiteBG();
            weeklyFreeChampionsList = (ListView) rootView.findViewById(R.id.listViewWeeklyFreeChampions);
            weeklyFreeChampionsList.setOnItemClickListener(this);
        }
    }

    private void populateWeeklyFreeChampionsList() {
        if (weeklyFreeChampions == null || weeklyFreeChampions.size() == 0
                || weeklyFreeChampionsAdapter == null || weeklyFreeChampionsAdapter.getCount() == 0) {

            showProgressWithWhiteBG();

            ApiHelper.getInstance(getContext()).getWeeklyFreeChampions(new RetrofitResponseHandler(new ApiResponseListener() {

                @Override
                public void onResponseFromCache(Object response) {
                    dismissProgress(); //TODO: 1
                    List<ChampionDto> resp = (List<ChampionDto>) response;
                    weeklyFreeChampions.clear();

                    for (int i = 0; i < resp.size(); i++) {
                        weeklyFreeChampions.add(resp.get(i));
                    }
                    setWeeklyFreeChampionsAdapter();
                }

                @Override
                public void onResponse(Call call, Response response) {//TODO: 2
                    WeeklyFreeResponseDto resp = (WeeklyFreeResponseDto) response.body();

                    if (resp != null && resp.getChampions() != null && resp.getChampions().size() > 0) {
                        for (ChampionDto c : resp.getChampions()) {
                            weeklyFreeChampions.add(c);
                        }
                        getAllChampions();
                    }
                }

                @Override
                public void onUnknownError() {
                    handleWeeklyFreeRequestFailure(null);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    handleWeeklyFreeRequestFailure(null);
                }

                @Override
                public void onFailure(String errorMessage) {
                    handleWeeklyFreeRequestFailure(errorMessage);
                }

                @Override
                public void onNetworkError() {
                    handleWeeklyFreeRequestFailure(null);
                }
            }));
        } else {
            notifyDataSetChanged();
        }
    }

    private void getAllChampions() {
        ApiHelper.getInstance(getContext()).getAllChampions(new RetrofitResponseHandler(new ApiResponseListener() {
            @Override
            public void onResponseFromCache(Object response) {
                dismissProgress();//TODO: 3
                List<ChampionDto> allChampionsData = (List<ChampionDto>) response;
                updateWeeklyFreeArray(allChampionsData);
                setIpRpPrices(new IpRpPricesListener() {
                    @Override
                    public void onIpRpPricesReceived() {
                        setWeeklyFreeChampionsAdapter();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                dismissProgress();//TODO: 4
                ChampionListDto championListDto = (ChampionListDto) response.body();
                List<ChampionDto> allChampsArr = convertAllChampionsDataIntoList(championListDto);
                CacheUtils.getInstance().saveAllChampionsData(championListDto);
                updateWeeklyFreeArray(allChampsArr);
                setIpRpPrices(new IpRpPricesListener() {
                    @Override
                    public void onIpRpPricesReceived() {
                        setWeeklyFreeChampionsAdapter();
                    }
                });
            }

            @Override
            public void onUnknownError() {
                handleWeeklyFreeRequestFailure(null);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                handleWeeklyFreeRequestFailure(null);
            }

            @Override
            public void onFailure(String errorMessage) {
                handleWeeklyFreeRequestFailure(errorMessage);
            }

            @Override
            public void onNetworkError() {
                handleWeeklyFreeRequestFailure(null);
            }
        }));
    }

    private List<ChampionDto> convertAllChampionsDataIntoList (ChampionListDto championListDto){
        Map<String, ChampionDto> champsData = championListDto.getData();
        if(champsData != null && champsData.size() > 0) {
            try {
                List<ChampionDto> allChampions = new ArrayList<>();

                for (Map.Entry<String, ChampionDto> entry : champsData.entrySet()) {
                    ChampionDto c = new ChampionDto(entry.getValue().getId(),
                            entry.getValue().getKey(),
                            entry.getValue().getName(),
                            Commons.CHAMPION_IMAGE_BASE_URL + entry.getKey() + ".png",
                            "\"" + entry.getValue().getTitle());
                    allChampions.add(c);
                }

                return allChampions;
            } catch (Exception e) {
                Log.d("DB", "Error parsing all champions list");
            }
        }
        return null;
    }

    private void setIpRpPrices(@NotNull final IpRpPricesListener ipRpPricesListener) {
        Map<String, HashMap> championCosts = CacheUtils.getInstance().getRpIpCostsData();
        if (championCosts != null) {
            populateChampionCosts(championCosts);//TODO: 5
            ipRpPricesListener.onIpRpPricesReceived();
        } else {
            if (LolApplication.firebaseInitialized) {
                try {
                    Firebase firebase = new Firebase(getResources().getString(R.string.lol_firebase));
                    firebase.child("championCosts").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {//TODO: 6
                                Map<String, HashMap> championCosts = (HashMap<String, HashMap>) dataSnapshot.getValue();
                                CacheUtils.getInstance().saveRpIpCostsData(championCosts);
                                populateChampionCosts(championCosts);
                                ipRpPricesListener.onIpRpPricesReceived();
                            } catch (Exception ignored) {
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {}
                    });

                } catch (Exception e) {}

            }
        }
    }

    private void populateChampionCosts(Map<String, HashMap> championCosts) {
        HashMap<String, String> costs = championCosts.get("costs");
        Iterator it = costs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String key = (String) pairs.getKey();
            for (ChampionDto c : weeklyFreeChampions) {
                if (String.valueOf(c.getId()).equals(key)) {
                    Map<String, String> keyValues = (Map<String, String>) pairs.getValue();
                    c.setChampionRp(String.valueOf(keyValues.get("rp_cost")));
                    c.setChampionIp(String.valueOf(keyValues.get("ip_cost")));
                }
            }
        }

        for(int i = 0; i < weeklyFreeChampions.size(); i++) {
            if(!Utils.isValidString(weeklyFreeChampions.get(i).getChampionIp())) {
                weeklyFreeChampions.get(i).setChampionIp("???");
            }
            if(!Utils.isValidString(weeklyFreeChampions.get(i).getChampionRp())) {
                weeklyFreeChampions.get(i).setChampionRp("???");
            }
        }
        CacheUtils.getInstance().saveWeeklyFreeChampionsData(weeklyFreeChampions);
    }

    private void updateWeeklyFreeArray(List<ChampionDto> allChampionsData) {

        List<Integer> weeklyFreeChampIds = new ArrayList<>();
        for (int i = 0; i < weeklyFreeChampions.size(); i++) {
            weeklyFreeChampIds.add(weeklyFreeChampions.get(i).getId());
        }

        weeklyFreeChampions.clear();
        for (int i = 0; i < allChampionsData.size(); i++) {
            ChampionDto c = allChampionsData.get(i);
            int id = c.getId();
            for (int weeklyFreeChampId : weeklyFreeChampIds) {
                if (id == weeklyFreeChampId) {
                    c.setDateInterval(Commons.getTuesday());
                    weeklyFreeChampions.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void setWeeklyFreeChampionsAdapter() {
        dismissProgress();
        if (weeklyFreeChampionsAdapter == null) {
            weeklyFreeChampionsAdapter = new WeeklyFreeChampionsAdapter(getActivity(), R.layout.list_row_weeklyfreechampions, weeklyFreeChampions);
            weeklyFreeChampionsList.setAdapter(weeklyFreeChampionsAdapter);
        } else {
            weeklyFreeChampionsAdapter.notifyDataSetChanged();
        }
    }

    private void notifyDataSetChanged() {
        if (weeklyFreeChampionsAdapter != null) {
            weeklyFreeChampionsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.listViewWeeklyFreeChampions:
                ChampionDto c = (ChampionDto) weeklyFreeChampionsList.getItemAtPosition(position);
                int champId = c.getId();
                ChampionDetailFragment fragment = new ChampionDetailFragment();
                Bundle args = new Bundle();
                args.putInt(ChampionDetailFragment.EXTRA_CHAMPION_ID, champId);
                args.putString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL, c.getImage().getFull());
                args.putString(ChampionDetailFragment.EXTRA_CHAMPION_NAME, c.getKey());
                fragment.setArguments(args);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out, R.anim.slide_right_in, R.anim.slide_right_out);
                ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.CHAMPION_DETAILS_FRAGMENT).commit();

                break;

            default:
                break;
        }
    }

    private void handleWeeklyFreeRequestFailure(String errorMessage) {
        showRetryView();

        if (Utils.isValidString(errorMessage)) {
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        }

        if (weeklyFreeChampions != null) {
            weeklyFreeChampions.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    protected void retry() {
        super.retry();
        populateWeeklyFreeChampionsList();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("WeeklyFreeChampionsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
