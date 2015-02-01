package com.yrazlik.loltr.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.listener.ResponseListener;
import com.yrazlik.loltr.responseclasses.ItemDetailResponse;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.RemoteImageView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailFragment extends Fragment implements ResponseListener{

	public static final String EXTRA_ITEM_ID = "com.yrazlik.leagueoflegends.fragments.ItemDetailFragment.extraitemid";
	public static final String EXTRA_ITEM_IMAGE_URL = "com.yrazlik.leagueoflegends.fragments.ItemDetailFragment.extraitemimageurl";
	private int itemId;
	private String itemImageUrl;
	private RemoteImageView itemImage;
	private TextView itemName, itemGold, itemDescription, descriptionTitle;
	private Typeface typeface;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_item_detail, container, false);	
		initUI(v);
		getExtras();
		
		ArrayList<String> pathParams = new ArrayList<String>();
		pathParams.add("static-data");
		pathParams.add("tr");
		pathParams.add("v1.2");
		pathParams.add("item");
		pathParams.add(String.valueOf(itemId));
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("version", Commons.LATEST_VERSION);
		queryParams.put("itemData", "gold,sanitizedDescription");
		queryParams.put("api_key", Commons.API_KEY);
		
		ServiceRequest.getInstance().makeGetRequest(Commons.ITEM_DETAIL_REQUEST, pathParams, queryParams, null, this);
	
		return v;
	}
	
	private void getExtras(){
		Bundle args = getArguments();
		itemId = args.getInt(EXTRA_ITEM_ID);
		itemImageUrl = args.getString(EXTRA_ITEM_IMAGE_URL);
	}
	
	private void initUI(View v){
		itemImage = (RemoteImageView)v.findViewById(R.id.imageViewItemImage);
		itemGold = (TextView)v.findViewById(R.id.textViewItemGold);
		itemDescription = (TextView)v.findViewById(R.id.textViewDetailedDescription);
		itemName = (TextView)v.findViewById(R.id.textViewItemName);
		descriptionTitle = (TextView)v.findViewById(R.id.textViewDescription);
		typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/dinproregular.ttf");
		itemName.setTypeface(typeface);
		itemGold.setTypeface(typeface);
		itemDescription.setTypeface(typeface);
		descriptionTitle.setTypeface(typeface);
	}

	@Override
	public void onSuccess(Object response) {
		if(response instanceof ItemDetailResponse){
			itemImage.setLocalURI(null);
			itemImage.setRemoteURI(itemImageUrl);
			itemImage.loadImage();
			ItemDetailResponse resp = (ItemDetailResponse)response;
			String detailedDescription = "";
			if(resp.getSanitizedDescription() != null){
				detailedDescription = detailedDescription + resp.getSanitizedDescription();
			}
			if(resp.getPlaintext() != null){
				detailedDescription = detailedDescription + "\n\n" + resp.getPlaintext();
			}
			itemDescription.setText(detailedDescription);
			if(resp.getGold() != null){
				itemGold.setText(String.valueOf(resp.getGold().getTotal()) + " " + getResources().getString(R.string.gold));
			}
			if(resp.getName() != null){
				itemName.setText(resp.getName());
			}
			
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
	
}
