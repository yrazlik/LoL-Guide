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
import com.yrazlik.loltr.BuildConfig;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.GridViewItemsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Items;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.AllItemsResponse;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.utils.LocalizationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AllItemsFragment extends BaseFragment implements ResponseListener, TextWatcher, OnItemClickListener{
	
	private GridView gridViewAllItems;
	private GridViewItemsAdapter adapter;
	private EditText searchBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_allitems, container, false);
		initUI(v);
		
		ArrayList<String> pathParams = new ArrayList<String>();
		pathParams.add("static-data");
		pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
		pathParams.add("v1.2");
		pathParams.add("item");
		HashMap<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("locale", LocalizationUtils.getInstance().getLocale());
        queryParams.put("version", Commons.LATEST_VERSION);
		queryParams.put("itemListData", "groups");
		queryParams.put("api_key", BuildConfig.API_KEY);
		
		ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_ITEMS_REQUEST, pathParams, queryParams, null, this);
		
		return v;
	}
	
	private void initUI(View v){
		gridViewAllItems = (GridView)v.findViewById(R.id.gridviewAllItems);
		gridViewAllItems.setOnItemClickListener(this);
		searchBar = (EditText)v.findViewById(R.id.edittextSearchBar);
		searchBar.addTextChangedListener(this);
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(s.length() >= 2){
			ArrayList<Items> searchResultItems = new ArrayList<Items>();
			for(Items i : Commons.allItems){
				if(containsIgnoreCase(i.getName(), String.valueOf(s))){
					searchResultItems.add(i);
				}
			}
			adapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, searchResultItems);
			gridViewAllItems.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}else{
			adapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, Commons.allItems);
			gridViewAllItems.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		hideKeyboard();
		Items i = (Items)gridViewAllItems.getItemAtPosition(position);
		int itemId = i.getId();
		ItemDetailFragment fragment = new ItemDetailFragment();
		Bundle args = new Bundle();
		args.putInt(ItemDetailFragment.EXTRA_ITEM_ID, itemId);
		args.putString(ItemDetailFragment.EXTRA_ITEM_IMAGE_URL, Commons.ITEM_IMAGES_BASE_URL + String.valueOf(i.getId()) + ".png");
		fragment.setArguments(args);
		FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, fragment).addToBackStack(Commons.ITEM_DETAIL_FRAGMENT).commit();
		
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
		if(response instanceof AllItemsResponse){
			AllItemsResponse resp = (AllItemsResponse)response;
			Map<String, Map<String, String>> data = resp.getData();
			if(Commons.allItems != null){
				Commons.allItems.clear();
			}else{
				Commons.allItems = new ArrayList<Items>();
			}
			for(Entry<String, Map<String, String>> entry : data.entrySet()){
				Items i = new Items();
				i.setId(Integer.valueOf(entry.getValue().get("id")));
				i.setName(entry.getValue().get("name"));
				i.setDescription(entry.getValue().get("description"));
				i.setGroup(entry.getValue().get("group"));
				Commons.allItems.add(i);
			}
			adapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, Commons.allItems);
			gridViewAllItems.setAdapter(adapter);
			adapter.notifyDataSetChanged();
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
        t.setScreenName("AllItemsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
