package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.WeeklyFreeChampionsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.responseclasses.WeeklyFreeChampionsResponse;
import com.yrazlik.loltr.service.ServiceHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class WeeklyFreeChampionsFragment extends BaseFragment implements
		ResponseListener, OnItemClickListener {

	private ListView weeklyFreeChampionsList;

	private WeeklyFreeChampionsAdapter weeklyFreeChampionsAdapter;
	private ArrayList<String> weeklyFreeChampIds;
    private int weeklyFreeChampsTrialCount = 0;
    private int freeToPlayChampsSize;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		initUI(inflater, container);

		if (Commons.weeklyFreeChampions != null && Commons.weeklyFreeChampions.size() > 0) {
            if(weeklyFreeChampionsAdapter != null && weeklyFreeChampionsAdapter.getCount() > 0) {
                notifyDataSetChanged();
            } else {
                populateWeeklyFreeChampionsList();
            }
		} else {
           populateWeeklyFreeChampionsList();
        }
		return rootView;

	}

    private void populateWeeklyFreeChampionsList() {
        Commons.weeklyFreeChampions = new ArrayList<>();
        notifyDataSetChanged();
        ServiceHelper.getInstance(getContext()).makeWeeklyFreeChampsRequest(this);
    }

    private void setWeeklyFreeChampionsAdapter() {
        if(weeklyFreeChampionsAdapter == null) {
            weeklyFreeChampionsAdapter = new WeeklyFreeChampionsAdapter(getActivity(), R.layout.list_row_weeklyfreechampions,
                    Commons.weeklyFreeChampions);
        }
        weeklyFreeChampionsList.setAdapter(weeklyFreeChampionsAdapter);
    }

    private void notifyDataSetChanged() {
        if(weeklyFreeChampionsAdapter != null) {
            weeklyFreeChampionsAdapter.notifyDataSetChanged();
        }
    }
	
	private void initUI(LayoutInflater inflater, ViewGroup container){
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_weekly_free_champions, container, false);
            weeklyFreeChampionsList = (ListView) rootView.findViewById(R.id.listViewWeeklyFreeChampions);
            weeklyFreeChampionsList.setOnItemClickListener(this);
        }
	}

	@Override
	public void onSuccess(Object response) {
		if (response instanceof WeeklyFreeChampionsResponse) {
            WeeklyFreeChampionsResponse resp = (WeeklyFreeChampionsResponse) response;

            if (resp != null && resp.getChampions() != null && resp.getChampions().size() > 0) {
                weeklyFreeChampIds = new ArrayList<>();
                for (Champion c : resp.getChampions()) {
                    try {
                        weeklyFreeChampIds.add(String.valueOf(c.getId()));
                    } catch (Exception ignored) {}
                }
                freeToPlayChampsSize = weeklyFreeChampIds.size();
                ServiceHelper.getInstance(getContext()).makeGetAllChampionsRequest(this);
            }

        } else if (response instanceof AllChampionsResponse) {
			AllChampionsResponse resp = (AllChampionsResponse) response;
			Map<String, Map<String, String>> data = resp.getData();

            if(Commons.weeklyFreeChampions != null){
				Commons.weeklyFreeChampions.clear();
			}else{
				Commons.weeklyFreeChampions = new ArrayList<>();
			}

            notifyDataSetChanged();

			for(Entry<String, Map<String, String>> entry : data.entrySet()){
				String key = entry.getKey();
				String id = entry.getValue().get("id");
				for(String weeklyFreeChampId : weeklyFreeChampIds){
					if(weeklyFreeChampId.equals(id)){
						String imageUrl = Commons.CHAMPION_IMAGE_BASE_URL + key + ".png";
						Champion c = new Champion();
						c.setChampionImageUrl(imageUrl);
						c.setChampionName(entry.getValue().get("name"));
						c.setId(Integer.parseInt(entry.getValue().get("id")));
						c.setKey(entry.getValue().get("key"));
						c.setDateInterval(Commons.getTuesday());
						Commons.weeklyFreeChampions.add(c);
                        notifyDataSetChanged();
					}
				}
			}

			if(LolApplication.firebaseInitialized){
				if (Commons.weeklyFreeChampions.size() == freeToPlayChampsSize) {
					try{
						Firebase firebase = new Firebase(getResources().getString(R.string.lol_firebase));
						firebase.child("championCosts").addListenerForSingleValueEvent(new ValueEventListener() {
							@Override
							public void onDataChange(DataSnapshot dataSnapshot) {
								try {
									ArrayList<Champion> weeklyFreeChampions = new ArrayList<Champion>();
									HashMap<String, HashMap> championCosts = (HashMap<String, HashMap>) dataSnapshot.getValue();
									HashMap<String, String> costs = championCosts.get("costs");
									Iterator it = costs.entrySet().iterator();
									while (it.hasNext()) {
										Map.Entry pairs = (Map.Entry) it.next();
										String key = (String) pairs.getKey();
										for (Champion c : Commons.weeklyFreeChampions) {
											if (String.valueOf(c.getId()).equals(key)) {
												Map<String, String> keyValues = (Map<String, String>) pairs.getValue();
												c.setChampionRp(String.valueOf(keyValues.get("rp_cost")));
												c.setChampionIp(String.valueOf(keyValues.get("ip_cost")));
												weeklyFreeChampions.add(c);
											}
										}
									}
									Commons.weeklyFreeChampions = weeklyFreeChampions;
                                    setWeeklyFreeChampionsAdapter();
                                    notifyDataSetChanged();
								}catch (Exception ignored){}
							}

							@Override
							public void onCancelled(FirebaseError firebaseError) {
                                setWeeklyFreeChampionsAdapter();
                                notifyDataSetChanged();
							}
						});

					}catch (Exception ignored){}
				}
			}else {
                setWeeklyFreeChampionsAdapter();
                notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.listViewWeeklyFreeChampions:
			Champion c = (Champion) weeklyFreeChampionsList.getItemAtPosition(position);
			int champId = c.getId();
			ChampionDetailFragment fragment = new ChampionDetailFragment();
			Bundle args = new Bundle();
			args.putInt(ChampionDetailFragment.EXTRA_CHAMPION_ID, champId);
			args.putString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL, c.getChampionImageUrl());
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

	@Override
	public void onFailure(final Object response) {
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String errorMessage = (String) response;
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    } catch (Exception ignored) {
                    }

                    if (Commons.weeklyFreeChampions != null) {
                        Commons.weeklyFreeChampions.clear();
                        notifyDataSetChanged();
                    }

                    if (weeklyFreeChampsTrialCount < 3) {
                        weeklyFreeChampsTrialCount++;
                        ServiceHelper.getInstance(getContext()).makeWeeklyFreeChampsRequest(WeeklyFreeChampionsFragment.this);
                    } else {
                        Toast.makeText(getContext(), R.string.anErrorOccured, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
