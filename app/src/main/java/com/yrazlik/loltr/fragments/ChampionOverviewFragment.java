package com.yrazlik.loltr.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolApplication;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.adapters.GridViewItemsAdapter;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Blocks;
import com.yrazlik.loltr.data.Items;
import com.yrazlik.loltr.data.Recommended;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.ChampionOverviewResponse;
import com.yrazlik.loltr.responseclasses.RecommendedItemsResponse;
import com.yrazlik.loltr.service.ServiceHelper;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.ArrayList;

@SuppressLint("NewApi") public class ChampionOverviewFragment extends BaseFragment implements ResponseListener, OnItemClickListener{
	
	private static final DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
	
	private int champId;
	private String champLogoImageUrl;

	private CircularImageView champLogo;
	private RobotoTextView champName;
	private RobotoTextView champTitle;
	private RelativeLayout barAttack;
	private RelativeLayout barDefense;
	private RelativeLayout barMagic;
	private RelativeLayout barDifficulty;
	private RobotoTextView tags;
	private RobotoTextView tagsTitle;
	public static int lastSelectedChampionId;

	private GridView gridviewStartingItems, gridviewEssentialItems, gridviewOffensiveItems, gridviewDeffensiveItems;
	private GridViewItemsAdapter startingItemsAdapter, essentialItemsAdapter, offensiveItemsAdapter, deffensiveItemsAdapter;
    private RobotoTextView textViewStartingItems, textViewEssentialItems, textViewOffensiveItems, textViewDeffensiveItems;

    private ChampionOverviewResponse championOverviewResponse;
    private RecommendedItemsResponse recommendedItemsResponse;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_champion_overview, container, false);
            showProgressWithWhiteBG();
            getExtras();
            initUI(rootView);
            getFragmentData();
            showInterstitial();
        }
		return rootView;
	}

    private void getExtras(){
        Bundle args = getArguments();
        if(args != null){
            champId = args.getInt(ChampionDetailFragment.EXTRA_CHAMPION_ID);
            lastSelectedChampionId = champId;
            champLogoImageUrl = args.getString(ChampionDetailFragment.EXTRA_CHAMPION_IMAGE_URL);
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
        champLogo = (CircularImageView)v.findViewById(R.id.imageViewChampionImage);

        textViewDeffensiveItems = (RobotoTextView) v.findViewById(R.id.textViewDeffensiveItems);
        textViewEssentialItems = (RobotoTextView)v.findViewById(R.id.textViewEssentialItems);
        textViewOffensiveItems = (RobotoTextView)v.findViewById(R.id.textViewOffensiveItems);
        textViewStartingItems = (RobotoTextView)v.findViewById(R.id.textViewStartingItems);
        Commons.underline(textViewDeffensiveItems);
        Commons.underline(textViewEssentialItems);
        Commons.underline(textViewOffensiveItems);
        Commons.underline(textViewStartingItems);

        LolImageLoader.getInstance().loadImage(champLogoImageUrl, champLogo);
        champName = (RobotoTextView) v.findViewById(R.id.textViewChampName);
        champTitle = (RobotoTextView)v.findViewById(R.id.textViewChampTitle);
        tags = (RobotoTextView)v.findViewById(R.id.textviewTags);
        tagsTitle = (RobotoTextView) v.findViewById(R.id.textviewTagsTitle);
        Commons.underline(tagsTitle);
        barAttack = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarAttack);
        barDefense = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarDefense);
        barMagic = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarMagic);
        barDifficulty = (RelativeLayout)v.findViewById(R.id.relativeLayoutBarDifficulty);
    }

    private void getFragmentData() {
        if(championOverviewResponse == null) {
            ServiceHelper.getInstance(getContext()).makeChampionOverviewRequest(champId, this);
        } else {
            handleChampionOverviewResponse();
        }
    }

    private void showInterstitial(){
		try {
			if (((LolApplication) (getActivity().getApplication())).shouldShowInterstitial()) {
				((LolApplication) (getActivity().getApplication())).showInterstitial();
			}
		}catch (Exception ignored){}
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
		}catch(Exception e){}
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
        }catch (Exception e){}
	}
	
	private void setAbilityBars(final ChampionOverviewResponse resp) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setBarLength(barAttack, resp.getInfo().getAttack());
                setBarLength(barDefense, resp.getInfo().getDefense());
                setBarLength(barMagic, resp.getInfo().getMagic());
                setBarLength(barDifficulty, resp.getInfo().getDifficulty());
                stretchBar(barAttack);
                stretchBar(barDefense);
                stretchBar(barMagic);
                stretchBar(barDifficulty);
            }
        }, 620);
		
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

    private void handleChampionOverviewResponse() {
        if(championOverviewResponse != null) {
            champName.setText(championOverviewResponse.getName());
            champTitle.setText(championOverviewResponse.getTitle());
            setAbilityBars(championOverviewResponse);
            setTags(championOverviewResponse);
        }

        if(recommendedItemsResponse == null) {
            ServiceHelper.getInstance(getContext()).makeRecommendedItemsRequest(champId, this);
        } else {
            handleRecommendedItemsResponse();
        }
    }

    private void handleRecommendedItemsResponse() {
        if(recommendedItemsResponse != null) {
            boolean startingOK = false, essentialOK = false, offensiveOK = false, defensiveOK = false;
            if (recommendedItemsResponse.getRecommended() != null) {
                for (Recommended r : recommendedItemsResponse.getRecommended()) {
                    if (r.getMode().equals("CLASSIC")) {
                        if (r.getBlocks() != null && r.getBlocks().size() > 0) {
                            for (Blocks b : r.getBlocks()) {
                                ArrayList<Items> items = b.getItems();
                                if (items != null && items.size() > 0) {
                                    if (b.getType().equals("starting")) {
                                        startingOK = true;
                                        startingItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                        gridviewStartingItems.setAdapter(startingItemsAdapter);
                                        startingItemsAdapter.notifyDataSetChanged();
                                    } else if (b.getType().equals("essential")) {
                                        essentialOK = true;
                                        essentialItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                        gridviewEssentialItems.setAdapter(essentialItemsAdapter);
                                        essentialItemsAdapter.notifyDataSetChanged();
                                    } else if (b.getType().equals("offensive")) {
                                        offensiveOK = true;
                                        offensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                        gridviewOffensiveItems.setAdapter(offensiveItemsAdapter);
                                        offensiveItemsAdapter.notifyDataSetChanged();
                                    } else if (b.getType().equals("defensive")) {
                                        defensiveOK = true;
                                        deffensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                        gridviewDeffensiveItems.setAdapter(deffensiveItemsAdapter);
                                        deffensiveItemsAdapter.notifyDataSetChanged();
                                    } else if (b.getType().equals("ability_scaling")) {
                                        offensiveOK = true;
                                        offensiveItemsAdapter = new GridViewItemsAdapter(getContext(), R.layout.row_grid_items, items);
                                        gridviewOffensiveItems.setAdapter(offensiveItemsAdapter);
                                        offensiveItemsAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }

                if(!startingOK){
                    textViewStartingItems.setVisibility(View.GONE);
                    gridviewStartingItems.setVisibility(View.GONE);
                }

                if(!essentialOK){
                    textViewEssentialItems.setVisibility(View.GONE);
                    gridviewEssentialItems.setVisibility(View.GONE);
                }

                if(!defensiveOK){
                    textViewDeffensiveItems.setVisibility(View.GONE);
                    gridviewDeffensiveItems.setVisibility(View.GONE);
                }

                if(!offensiveOK){
                    textViewOffensiveItems.setVisibility(View.GONE);
                    gridviewOffensiveItems.setVisibility(View.GONE);
                }

            }
        }
    }

	@Override
	public void onSuccess(Object response) {
		if(response instanceof ChampionOverviewResponse){
			championOverviewResponse = (ChampionOverviewResponse)response;
            handleChampionOverviewResponse();
		}else if(response instanceof RecommendedItemsResponse){;
            try {
                dismissProgress();
                recommendedItemsResponse = (RecommendedItemsResponse) response;
                handleRecommendedItemsResponse();
            }catch (Exception e){
                dismissProgress();
            }
		}
	}

	@Override
	public void onFailure(Object response) {
        dismissProgress();
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
