package com.yrazlik.loltr.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.GridViewItemsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Blocks;
import com.yrazlik.loltr.data.Items;
import com.yrazlik.loltr.data.Recommended;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.ChampionOverviewResponse;
import com.yrazlik.loltr.responseclasses.RecommendedItemsResponse;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi") public class ChampionOverviewFragment extends BaseFragment implements ResponseListener, OnItemClickListener{
	
	private static final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
	
	private int champId;
	private String champLogoImageUrl;
	private String extraChampName;
	
	private ImageView champLogo;
	private ImageView splashImage;
	private TextView champName;
	private TextView champTitle;
	private Typeface typeFace;
	private RelativeLayout barAttack;
	private RelativeLayout barDefense;
	private RelativeLayout barMagic;
	private RelativeLayout barDifficulty;
	private TextView tags;
	private TextView tagsTitle;
	public static int lastSelectedChampionId;
    private AQuery aq;
	private GridView gridviewStartingItems, gridviewEssentialItems, gridviewOffensiveItems, gridviewDeffensiveItems;
	private GridViewItemsAdapter startingItemsAdapter, essentialItemsAdapter, offensiveItemsAdapter, deffensiveItemsAdapter;
    private ProgressBar progress, progressStartingItems, progressEssentialItems, progressOffensiveItems, progressDeffensiveItems;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_champion_overview, container,
				false);
		getExtras();
		initUI(v);
		
		ArrayList<String> pathParams = new ArrayList<String>();
		pathParams.add("static-data");
		pathParams.add("tr");
		pathParams.add("v1.2");
		pathParams.add("champion");
		pathParams.add(String.valueOf(champId));
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("version", Commons.LATEST_VERSION);
		queryParams.put("champData", "info,tags");
		queryParams.put("api_key", Commons.API_KEY);
		
		ServiceRequest.getInstance().makeGetRequest(Commons.CHAMPION_OVERVIEW_REQUEST, pathParams, queryParams, null, this);
		
		ArrayList<String> pathParams2 = new ArrayList<String>();
		pathParams2.add("static-data");
		pathParams2.add("tr");
		pathParams2.add("v1.2");
		pathParams2.add("champion");
		pathParams2.add(String.valueOf(champId));
		HashMap<String, String> queryParams2 = new HashMap<String, String>();
		queryParams2.put("version", Commons.LATEST_VERSION);
		queryParams2.put("champData", "recommended");
		queryParams2.put("api_key", Commons.API_KEY);
		
		ServiceRequest.getInstance().makeGetRequest(Commons.RECOMMENDED_ITEMS_REQUEST, pathParams2, queryParams2, null, this);
	
		return v;	
	}
	
	private void getExtras(){
		Bundle args = getArguments();
		if(args != null){
			champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
			lastSelectedChampionId = champId;
			champLogoImageUrl = args.getString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL);
			extraChampName = args.getString(ChampionDetailFragment.EXTRA_CHAMPION_NAME);
		}
	}
	
	private void initUI(View v){
		gridviewStartingItems = (GridView)v.findViewById(R.id.gridviewStartingItems);
		gridviewEssentialItems = (GridView)v.findViewById(R.id.gridviewEssentialItems);
		gridviewOffensiveItems = (GridView)v.findViewById(R.id.gridviewOffensiveItems);
		gridviewDeffensiveItems = (GridView)v.findViewById(R.id.gridviewDeffensiveItems);
		gridviewStartingItems.setOnItemClickListener(this);
		gridviewEssentialItems.setOnItemClickListener(this);
		gridviewOffensiveItems.setOnItemClickListener(this);
		gridviewDeffensiveItems.setOnItemClickListener(this);
		typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/dinproregular.ttf");
		champLogo = (ImageView)v.findViewById(R.id.imageViewChampionImage);
		aq = new AQuery(champLogo);
        progress = (ProgressBar)v.findViewById(R.id.imageProgress);
        progressStartingItems = (ProgressBar)v.findViewById(R.id.progressStartingItems);
        progressEssentialItems = (ProgressBar)v.findViewById(R.id.progressEssentialItems);
        progressOffensiveItems = (ProgressBar)v.findViewById(R.id.progressOffensiveItems);
        progressDeffensiveItems = (ProgressBar)v.findViewById(R.id.progressDeffensiveItems);
        aq.progress(progress).image(champLogoImageUrl, true, true);
		champName = (TextView)v.findViewById(R.id.textViewChampName);
		champTitle = (TextView)v.findViewById(R.id.textViewChampTitle);
		tags = (TextView)v.findViewById(R.id.textviewTags);
		tagsTitle = (TextView) v.findViewById(R.id.textviewTagsTitle);
		tagsTitle.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		tagsTitle.setTypeface(typeFace);
		tags.setTypeface(typeFace);
		champName.setTypeface(typeFace);
		champTitle.setTypeface(typeFace);
		barAttack = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarAttack);
		barDefense = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarDefense);
		barMagic = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarMagic);
		barDifficulty = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarDifficulty);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		hideKeyboard();
		Items i = null;
		int itemId = 0;
		if(parent.getId() == R.id.gridviewStartingItems){
			i = (Items)gridviewStartingItems.getItemAtPosition(position);
			itemId = i.getId();
		}else if(parent.getId() == R.id.gridviewEssentialItems){
			i = (Items)gridviewEssentialItems.getItemAtPosition(position);
			itemId = i.getId();
		}else if(parent.getId() == R.id.gridviewOffensiveItems){
			i = (Items)gridviewOffensiveItems.getItemAtPosition(position);
			itemId = i.getId();
		}else if(parent.getId() == R.id.gridviewDeffensiveItems){
			i = (Items)gridviewDeffensiveItems.getItemAtPosition(position);
			itemId = i.getId();
		}
		if(i != null){
			ItemDetailFragment fragment = new ItemDetailFragment();
			Bundle args = new Bundle();
			args.putInt(ItemDetailFragment.EXTRA_ITEM_ID, itemId);
			args.putString(ItemDetailFragment.EXTRA_ITEM_IMAGE_URL, Commons.ITEM_IMAGES_BASE_URL + String.valueOf(i.getId()) + ".png");
			fragment.setArguments(args);
			FragmentManager fm = getParentFragment().getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Commons.setAnimation(ft, Commons.ANIM_OPEN_FROM_RIGHT_WITH_POPSTACK);
			ft.addToBackStack(Commons.ITEM_DETAIL_FRAGMENT).replace(R.id.content_frame, fragment).commit();
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

	
	private void setBarLength(View v, int length){
        int pixels = 0;
        try {
            try {
                pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        length, getContext().getResources().getDisplayMetrics()) * 25;
            } catch (Exception e) {
                pixels = 300;
            }
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.width = pixels;
            v.setLayoutParams(params);
        }catch (Exception e){

        }
	}
	
	private void setAbilityBars(ChampionOverviewResponse resp) {
		
		setBarLength(barAttack, resp.getInfo().getAttack());
		setBarLength(barDefense, resp.getInfo().getDefense());
		setBarLength(barMagic, resp.getInfo().getMagic());
		setBarLength(barDifficulty, resp.getInfo().getDifficulty());
		stretchBar(barAttack);
		stretchBar(barDefense);
		stretchBar(barMagic);
		stretchBar(barDifficulty);
		
	}
	
	private void setTags(ChampionOverviewResponse resp) {
		String tagsString="";
		
		ArrayList<String> tagsResponse = resp.getTags();
		for(String tag : tagsResponse){
			tagsString = tagsString+ "- " + Commons.getTurkishTag(tag) + "\n";
		}
		tags.setText(tagsString);
		
	}
	
	private void stretchBar(View view){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			long animationDuration = (long) (1000 * 1);
			view.setPivotX(view.getWidth());
			
			PropertyValuesHolder pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, .1f, 1f);
			pvhSX = PropertyValuesHolder.ofFloat(View.SCALE_X, .1f, 1f);
			ObjectAnimator stretchAnim =
			ObjectAnimator.ofPropertyValuesHolder(view, pvhSX);
			stretchAnim.setInterpolator(sDecelerator);
			stretchAnim.setDuration(animationDuration);
			
	
			AnimatorSet set = new AnimatorSet();
			set.playSequentially(stretchAnim);
			set.start();
		}
	}

	@Override
	public void onSuccess(Object response) {
		if(response instanceof ChampionOverviewResponse){
			ChampionOverviewResponse resp = (ChampionOverviewResponse)response;
			champName.setText(resp.getName());
			champTitle.setText(resp.getTitle());
			setAbilityBars(resp);
			setTags(resp);
           /* Animation animZoom = new ScaleAnimation(0, 1, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animZoom.setDuration(400);
            champLogo.startAnimation(animZoom);*/
		}else if(response instanceof RecommendedItemsResponse){
            try {
                RecommendedItemsResponse resp = (RecommendedItemsResponse) response;
                if (resp.getRecommended() != null) {
                    for (Recommended r : resp.getRecommended()) {
                        if (r.getMode().equals("CLASSIC")) {
                            if (r.getBlocks() != null && r.getBlocks().size() > 0) {
                                for (Blocks b : r.getBlocks()) {
                                    ArrayList<Items> items = b.getItems();
                                    if (items != null && items.size() > 0) {
                                        if (b.getType().equals("starting")) {
                                            startingItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewStartingItems.setAdapter(startingItemsAdapter);
                                            startingItemsAdapter.notifyDataSetChanged();
                                            progressStartingItems.setVisibility(View.GONE);
                                        } else if (b.getType().equals("essential")) {
                                            essentialItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewEssentialItems.setAdapter(essentialItemsAdapter);
                                            essentialItemsAdapter.notifyDataSetChanged();
                                            progressEssentialItems.setVisibility(View.GONE);
                                        } else if (b.getType().equals("offensive")) {
                                            offensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewOffensiveItems.setAdapter(offensiveItemsAdapter);
                                            offensiveItemsAdapter.notifyDataSetChanged();
                                            progressOffensiveItems.setVisibility(View.GONE);
                                        } else if (b.getType().equals("defensive")) {
                                            deffensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewDeffensiveItems.setAdapter(deffensiveItemsAdapter);
                                            deffensiveItemsAdapter.notifyDataSetChanged();
                                            progressDeffensiveItems.setVisibility(View.GONE);
                                        } else if (b.getType().equals("ability_scaling")) {
                                            offensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                            gridviewOffensiveItems.setAdapter(offensiveItemsAdapter);
                                            offensiveItemsAdapter.notifyDataSetChanged();
                                            progressOffensiveItems.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }catch (Exception e){}
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
    public void onResume() {
        super.onResume();
        reportGoogleAnalytics();
    }

    @Override
    public void reportGoogleAnalytics() {
        Tracker t = ((LolApplication) getActivity().getApplication()).getTracker();
        t.setScreenName("ChampionOverViewFragment");
        t.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
