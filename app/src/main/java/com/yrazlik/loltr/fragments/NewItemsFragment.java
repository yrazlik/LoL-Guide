package com.yrazlik.loltr.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.activities.FilterActivity;
import com.yrazlik.loltr.activities.ItemDetailActivity;
import com.yrazlik.loltr.adapters.ItemsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Item;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.data.Data;
import com.yrazlik.loltr.responseclasses.ItemsResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yrazlik on 8/6/15.
 */
public class NewItemsFragment extends BaseFragment implements ResponseListener, TextWatcher{

    private ListView itemsLV;
    private ArrayList<Item> allItems;
    private ItemsAdapter itemsLVAdapter;
    private EditText searchBar;
    private ImageView filterIcon;
    private final String EXTRA_ITEM_DETAIL = "EXTRA_ITEM_DETAIL";
    private final String EXTRA_ALL_ITEMS = "EXTRA_ALL_ITEMS";
    private final int FILTER_REQUEST_CODE = 1;
    private final String EXTRA_FILTER = "EXTRA_FILTER";
    private final String EXTRA_FIRST_ITEM_STACK = "EXTRA_FIRST_ITEM_STACK";
    private ArrayList<String> filters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items_new, container, false);
        initUI(v);

        if(Commons.allItemsNew == null || Commons.allItemsNew.size() <= 0) {
            ArrayList<String> pathParams = new ArrayList<String>();
            pathParams.add("static-data");
            pathParams.add(Commons.getInstance(getContext().getApplicationContext()).getRegion());
            pathParams.add("v1.2");
            pathParams.add("item");
            HashMap<String, String> queryParams = new HashMap<String, String>();
            queryParams.put("locale", Commons.getInstance(getContext().getApplicationContext()).getLocale());
            queryParams.put("version", Commons.LATEST_VERSION);
            queryParams.put("itemListData", "all");
            queryParams.put("api_key", Commons.API_KEY);

            ServiceRequest.getInstance(getContext()).makeGetRequest(Commons.ALL_ITEMS_REQUEST, pathParams, queryParams, null, this);
        }else {
            try {
                Collections.sort(Commons.allItemsNew, new Comparator<Item>() {
                    @Override
                    public int compare(Item i1, Item i2) {
                        return i1.getData().getName().compareTo(i2.getData().getName());
                    }
                });
            }catch (Exception ignored){}
            allItems = Commons.allItemsNew;
            itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, Commons.allItemsNew);
            itemsLV.setAdapter(itemsLVAdapter);
        }

        return v;
    }

    private void initUI(View v){

        filters = new ArrayList<String>();
        itemsLV = (ListView) v.findViewById(R.id.itemsLV);
        searchBar = (EditText) v.findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(this);
        filterIcon = (ImageView) v.findViewById(R.id.filterIcon);
        allItems = new ArrayList<Item>();
        itemsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(itemsLVAdapter != null) {
                    Item item = itemsLVAdapter.getItem(position);
                    if(item != null) {
                        Gson gson = new Gson();
                        Gson gson2 = new Gson();
                        String extraItemDataJSON = gson.toJson(item);
                        Intent i = new Intent(getContext(), ItemDetailActivity.class);
                        i.putExtra(EXTRA_ITEM_DETAIL, extraItemDataJSON);
                        i.putExtra(EXTRA_FIRST_ITEM_STACK, true);
                        showInterstitial();
                        startActivity(i);
                    }
                }
            }
        });

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FilterActivity.class);
                i.putExtra(EXTRA_FILTER, filters);
                startActivityForResult(i, FILTER_REQUEST_CODE);
            }
        });

    }

    private void showInterstitial(){
        try {
            if (((LolApplication) (getActivity().getApplication())).shouldShowInterstitial()) {
                ((LolApplication) (getActivity().getApplication())).showInterstitial();
            }
        }catch (Exception ignored){}
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("NewAllItemsFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void onSuccess(Object response) {
        if(response instanceof ItemsResponse){
            ItemsResponse resp = (ItemsResponse)response;
            if(allItems == null){
                allItems = new ArrayList<Item>();
            }
            if(Commons.allItemsNew == null){
                Commons.allItemsNew = new ArrayList<Item>();
            }
            allItems.clear();
            Map<String, Data> data = resp.getData();
            for(Map.Entry<String, Data> entry : data.entrySet()) {
                Item item = new Item();
                item.setData(entry.getValue());
                allItems.add(item);
                Commons.allItemsNew.add(item);
            }

            try {
                Collections.sort(allItems, new Comparator<Item>() {
                    @Override
                    public int compare(Item i1, Item i2) {
                        return i1.getData().getName().compareTo(i2.getData().getName());
                    }
                });
            }catch (Exception ignored){}

            try {
                if(getContext() != null) {
                    itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, allItems);
                    itemsLV.setAdapter(itemsLVAdapter);
                }
            }catch (Exception ignored){}


        }
    }

    @Override
    public void onFailure(Object response) {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public static boolean containsIgnoreCase(String src, String what) {
        if (src != null) {
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
        }

        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length() >= 2){
            if(filters != null && filters.size() > 0){
                filters.clear();
            }
            ArrayList<Item> searchResultItems = new ArrayList<Item>();
            for(Item i : allItems){
                if(containsIgnoreCase(i.getData().getName(), String.valueOf(s))){
                    searchResultItems.add(i);
                }
            }
            itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, searchResultItems);
            itemsLV.setAdapter(itemsLVAdapter);
            itemsLVAdapter.notifyDataSetChanged();
        }else{
            itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, allItems);
            itemsLV.setAdapter(itemsLVAdapter);
            itemsLVAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FILTER_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    ArrayList<Item> filteredItems = new ArrayList<Item>();
                    filters = data.getStringArrayListExtra(EXTRA_FILTER);
                    if(filters != null && filters.size() > 0 && allItems != null && allItems.size() > 0){
                        for (String filter : filters){
                            for(Item i : allItems){
                                Data itemData = i.getData();
                                if(itemData != null){
                                    ArrayList<String> tags = itemData.getTags();
                                    if(tags != null && tags.size() > 0){
                                        if(tags.contains(filter)){
                                            filteredItems.add(i);
                                        }
                                    }
                                }
                            }
                        }

                        itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, filteredItems);
                        itemsLV.setAdapter(itemsLVAdapter);
                    }else {
                        itemsLVAdapter = new ItemsAdapter(getContext(), R.layout.list_row_items, Commons.allItemsNew);
                        itemsLV.setAdapter(itemsLVAdapter);
                    }
                }
            }
        }
    }
}
