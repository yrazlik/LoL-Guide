package com.yrazlik.loltr.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.AllChampionsListAdapter;
import com.yrazlik.loltr.adapters.GridViewAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AllChampionsFragment extends BaseFragment implements ResponseListener, OnItemClickListener, TextWatcher{
	
	private GridView gridView;
    private ListView listViewChampions;
	private GridViewAdapter adapter;
    private AllChampionsListAdapter listAdapter;
	private EditText searchBar;
    private TextView noChampsFoundTV;
    private ImageView imageviewListGrid;
    private ArrayList<Champion> searchResultChampions;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	
		View v = inflater.inflate(R.layout.fragment_champions, container, false);
		initUI(v);
		if(Commons.allChampions == null || Commons.allChampions.size() == 0){
			ArrayList<String> pathParams = new ArrayList<String>();
			pathParams.add("static-data");
			pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
			pathParams.add("v1.2");
			pathParams.add("champion");
			HashMap<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
            queryParams.put("version", Commons.LATEST_VERSION);
			queryParams.put("champData", "altimages");
			queryParams.put("api_key", Commons.API_KEY);
			ServiceRequest.getInstance().makeGetRequest(Commons.ALL_CHAMPIONS_REQUEST, pathParams, queryParams, null, this);
		}else{
			adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
			gridView.setAdapter(adapter);
		}
		
		return v;
		
	}
	
	private void initUI(View v){
        noChampsFoundTV = (TextView)v.findViewById(R.id.noChampsFoundTV);
        imageviewListGrid = (ImageView)v.findViewById(R.id.imageviewListGrid);
		gridView = (GridView)v.findViewById(R.id.gridview_champions);
        listViewChampions = (ListView)v.findViewById(R.id.listview_champions);
		searchBar = (EditText)v.findViewById(R.id.edittextSearchBar);
		searchBar.addTextChangedListener(this);
		gridView.setOnItemClickListener(this);
     /*   imageviewListGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listViewChampions.getVisibility() == View.GONE){
                    if(adapter != null) {
                        ArrayList<Champion> currentChampsInAdapter = new ArrayList<Champion>();
                        for(int i = 0; i< adapter.getCount(); i++){
                            Champion c = (Champion)adapter.getItem(i);
                            currentChampsInAdapter.add(c);
                        }
                        listAdapter = new AllChampionsListAdapter(getContext(), R.layout.list_row_allchampions, currentChampsInAdapter);
                        listViewChampions.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);
                        imageviewListGrid.setBackgroundResource(R.drawable.listview);
                        listViewChampions.setAdapter(listAdapter);
                    }
                }else {
                    if(listAdapter != null) {
                        ArrayList<Champion> currentChampsInAdapter = new ArrayList<Champion>();
                        for(int i = 0; i< listAdapter.getCount(); i++){
                            Champion c = (Champion)listAdapter.getItem(i);
                            currentChampsInAdapter.add(c);
                        }

                        adapter = new GridViewAdapter(getContext(), R.layout.row_grid, currentChampsInAdapter);

                        listViewChampions.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);
                        imageviewListGrid.setBackgroundResource(R.drawable.gridview);
                        gridView.setAdapter(adapter);
                    }
                }
            }
        });*/
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
        noChampsFoundTV.setVisibility(View.GONE);
		if(s.length() >= 2){
			searchResultChampions = new ArrayList<Champion>();
			for(Champion c : Commons.allChampions){
				if(containsIgnoreCase(c.getChampionName(), String.valueOf(s))){
					searchResultChampions.add(c);
				}
			}
			adapter = new GridViewAdapter(getContext(), R.layout.row_grid, searchResultChampions);
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
            if(searchResultChampions != null && searchResultChampions.size() <= 0){
                noChampsFoundTV.setVisibility(View.VISIBLE);
            }else if(searchResultChampions != null && searchResultChampions.size() > 0){
                noChampsFoundTV.setVisibility(View.GONE);
            }
		}else{
            if(Commons.allChampions != null && Commons.allChampions.size() > 0) {
                adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
                if(adapter != null && gridView != null) {
                    try {
                        gridView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e){}
                }
            }
		}
	}
	
	private void hideKeyboard() {   
		try{
		    View view = getActivity().getCurrentFocus();
		    if (view != null) {
		        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		    }
		}catch(Exception e){
			
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		hideKeyboard();
		Champion c = (Champion)gridView.getItemAtPosition(position);
		int champId = c.getId();
		ChampionDetailFragment fragment = new ChampionDetailFragment();
		Bundle args = new Bundle();
		args.putInt(ChampionDetailFragment.EXTRA_CHAMPION_ID, champId);
		args.putString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL, c.getChampionImageUrl());
		args.putString(ChampionDetailFragment.EXTRA_CHAMPION_NAME, c.getKey());
		fragment.setArguments(args);
		FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
		ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.CHAMPION_DETAILS_FRAGMENT).commit();
		
	}
	
	public static boolean containsIgnoreCase(String src, String what) {
	    final int length = what.length();
	    if (length == 0)
	        return true; // Empty string is contained

	    final char firstLo = Character.toLowerCase(what.charAt(0));
	    final char firstUp = Character.toUpperCase(what.charAt(0));

	    for (int i = src.length() - length; i >= 0; i--) {
	        // Quick check before calling the more expensive regionMatches() method:
	        final char ch = src.charAt(i);
	        if (ch != firstLo && ch != firstUp)
	            continue;

	        if (src.regionMatches(true, i, what, 0, length))
	            return true;
	    }

	    return false;
	}

	@Override
	public void onSuccess(Object response) {
		try{
			if(response instanceof AllChampionsResponse){
				AllChampionsResponse resp = (AllChampionsResponse) response;
				Map<String, Map<String, String>> data = resp.getData();
				if(Commons.allChampions != null){
					Commons.allChampions.clear();
				}else{
					Commons.allChampions = new ArrayList<Champion>();
				}
				for(Entry<String, Map<String, String>> entry : data.entrySet()){
					String key = entry.getKey();
					String imageUrl = Commons.CHAMPION_IMAGE_BASE_URL + key + ".png";
					Champion c = new Champion();
					c.setChampionImageUrl(imageUrl);
					c.setChampionName(entry.getValue().get("name"));
					c.setId(Integer.parseInt(entry.getValue().get("id")));
					c.setKey(entry.getValue().get("key"));
                    c.setTitle("\"" + entry.getValue().get("title") + "\"");
					Commons.allChampions.add(c);
				}
                if(Commons.allChampions != null) {
                    Collections.sort(Commons.allChampions, new Comparator<Champion>() {
                        @Override
                        public int compare(Champion c1, Champion c2) {
                            return c1.getChampionName().compareTo(c2.getChampionName());
                        }
                    });
                }
				adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
				gridView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void onFailure(Object response) {
		String errorMessage = (String)response;
		Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("AllChampionsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
