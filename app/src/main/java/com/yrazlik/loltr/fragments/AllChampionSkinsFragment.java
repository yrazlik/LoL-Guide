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
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.GridViewAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.AllChampionsResponse;
import com.yrazlik.loltr.service.ServiceHelper;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AllChampionSkinsFragment extends BaseFragment implements ResponseListener, OnItemClickListener, TextWatcher{
	
	private GridView gridView;
	private GridViewAdapter adapter;
	private EditText searchBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_champions, container, false);
            initUI(rootView);
        }


		if(Commons.allChampions == null || Commons.allChampions.size() == 0) {
            showProgressWithWhiteBG();
            ServiceHelper.getInstance(getContext()).makeGetAllChampionsRequest(this);
        }else{
            setAdapter();
		}
		
		return rootView;
		
	}
	
	private void initUI(View v){
		gridView = (GridView)v.findViewById(R.id.gridview_champions);
		searchBar = (EditText)v.findViewById(R.id.edittextSearchBar);
		searchBar.addTextChangedListener(this);
		gridView.setOnItemClickListener(this);
	}

    private void setAdapter() {
        dismissProgress();
        if (adapter == null || adapter.getCount() == 0) {
            adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
            gridView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		if(s.length() >= 2){
			ArrayList<Champion> searchResultChampions = new ArrayList<Champion>();
			for(Champion c : Commons.allChampions){
				if(containsIgnoreCase(c.getChampionName(), String.valueOf(s))){
					searchResultChampions.add(c);
				}
			}
			adapter = new GridViewAdapter(getContext(), R.layout.row_grid, searchResultChampions);
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}else{
			adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
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
        ChampionSkinsFragment fragment = new ChampionSkinsFragment();
		Bundle args = new Bundle();
		args.putInt(ChampionDetailFragment.EXTRA_CHAMPION_ID, champId);
		fragment.setArguments(args);
		FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
		ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.CHAMPION_SKINS_FRAGMENT).commit();
		showInterstitial();
		
	}

	private void showInterstitial(){
		try {
			if (((LolApplication) (getActivity().getApplication())).shouldShowInterstitial()) {
				((LolApplication) (getActivity().getApplication())).showInterstitial();
			}
		}catch (Exception ignored){}
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
            dismissProgress();
			if(response instanceof AllChampionsResponse){
				AllChampionsResponse resp = (AllChampionsResponse) response;
				Map<String, Map<String, String>> data = resp.getData();
				Commons.setAllChampions(resp);
				adapter = new GridViewAdapter(getContext(), R.layout.row_grid, Commons.allChampions);
				gridView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void onFailure(final Object response) {
        dismissProgress();
        if(getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String errorMessage = (String) response;
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
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
        t.setScreenName("AllChampionSkinsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
