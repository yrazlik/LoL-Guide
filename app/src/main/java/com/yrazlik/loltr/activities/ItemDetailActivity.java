package com.yrazlik.loltr.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.DataContainer;
import com.yrazlik.loltr.data.Gold;
import com.yrazlik.loltr.data.Image;
import com.yrazlik.loltr.data.Item;
import com.yrazlik.loltr.data.Data;

import java.util.ArrayList;

/**
 * Created by yrazlik on 8/6/15.
 */
public class ItemDetailActivity extends ActionBarActivity {

    private final String EXTRA_ITEM_DETAIL = "EXTRA_ITEM_DETAIL";
    private final String EXTRA_FIRST_ITEM_STACK = "EXTRA_FIRST_ITEM_STACK";

    private ImageView imageViewItemImage;
    private RelativeLayout relativeLayoutTitleContainer;
    private TextView textViewItemName, textViewItemGold, textViewDetailedDescription, noFromTV, noToTV;
    private LinearLayout buildFromLL, buildToLL;
    private AQuery aq;
    boolean firstItemOnStack = false;

    private DataContainer itemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        overridePendingTransition(R.anim.slide_left_in, android.R.anim.fade_out);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getString(R.string.itemDetail) + "</font>"));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D9B40617")));
        getSupportActionBar().show();
        Intent i = getIntent();
        if (i != null) {
            String extraJsonString = i.getStringExtra(EXTRA_ITEM_DETAIL);
            if(extraJsonString != null){
                Gson gson = new Gson();
                itemData = gson.fromJson(extraJsonString, DataContainer.class);
            }

            firstItemOnStack = i.getBooleanExtra(EXTRA_FIRST_ITEM_STACK, false);
        }
        if(itemData != null && itemData.getData() != null) {
            initUI();
        }else {
            Toast.makeText(getApplicationContext(), R.string.anErrorOccured, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initUI(){
        imageViewItemImage = (ImageView) findViewById(R.id.imageViewItemImage);
        buildFromLL = (LinearLayout) findViewById(R.id.buildFromLL);
        buildToLL = (LinearLayout) findViewById(R.id.buildToLL);
        textViewItemName = (TextView) findViewById(R.id.textViewItemName);
        textViewItemGold = (TextView) findViewById(R.id.textViewItemGold);
        textViewDetailedDescription = (TextView) findViewById(R.id.textViewDetailedDescription);
        noFromTV = (TextView) findViewById(R.id.noFromTV);
        noToTV = (TextView) findViewById(R.id.noToTV);
        aq = new AQuery(imageViewItemImage);
        Image itemImage = itemData.getData().getImage();
        if(itemImage != null){
            String full = itemImage.getFull();
            if(full != null) {
                aq.image(Commons.ITEM_IMAGES_BASE_URL + full, false, true, 0, 0, null, android.R.anim.fade_in);
            }
        }
        String itemName = itemData.getData().getName();
        Gold itemGold = itemData.getData().getGold();
        String detailedDescription = itemData.getData().getSanitizedDescription();
        if(itemName != null){
            textViewItemName.setText(itemName);
        }
        if(detailedDescription != null){
            textViewDetailedDescription.setText(detailedDescription);
        }
        if(itemGold != null){
            int totalGold = itemGold.getTotal();
            textViewItemGold.setText(totalGold + " " + getString(R.string.gold));
        }

        ArrayList<String> from = itemData.getData().getFrom();
        if(from != null && from.size() > 0) {
            noFromTV.setVisibility(View.GONE);
            for (String id : from) {
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.build_hierarchy_view, null);
                ImageView buildFromItemImage = (ImageView) v.findViewById(R.id.itemImage);
                TextView buildFromItemName = (TextView)v.findViewById(R.id.textViewItemName);
                TextView buildFromItemGold = (TextView)v.findViewById(R.id.textViewItemGold);
                String buildFromItemNameString = "";
                String buildFromItemGoldString = "";

                if(Commons.allItemsNew != null && Commons.allItemsNew.size() > 0){
                    for(Item i : Commons.allItemsNew){
                        Data buildFromData = i.getData();
                        if(buildFromData != null){
                            if(id.equals(String.valueOf(buildFromData.getId()))){
                                buildFromItemNameString = buildFromData.getName();
                                if(buildFromData.getGold() != null) {
                                    buildFromItemGoldString = String.valueOf(buildFromData.getGold().getTotal() + " " + getString(R.string.gold));
                                }
                                if(buildFromItemNameString != null && buildFromItemNameString.length() > 0) {
                                    AQuery aq = new AQuery(buildFromItemImage);
                                    aq.image(Commons.ITEM_IMAGES_BASE_URL + id + ".png");
                                    buildFromItemName.setText(buildFromItemNameString);
                                    if(buildFromItemGoldString != null) {
                                        buildFromItemGold.setText(buildFromItemGoldString);
                                    }
                                    Gson gson = new Gson();
                                    String json = gson.toJson(i);
                                    v.setTag(json);
                                    v.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(ItemDetailActivity.this, ItemDetailActivity.class);
                                            i.putExtra(EXTRA_ITEM_DETAIL, (String)v.getTag());
                                            startActivity(i);
                                        }
                                    });
                                    buildFromLL.addView(v);
                                }
                                break;
                            }
                        }
                    }
                }


            }
        }else {
            noFromTV.setVisibility(View.VISIBLE);
        }

        String itemId = String.valueOf(itemData.getData().getId());
        boolean itemCanBeBuiltToMoreItems = false;
        if(Commons.allItemsNew != null && Commons.allItemsNew.size() > 0) {
            for (Item i : Commons.allItemsNew) {
                Data itemData = i.getData();
                if(itemData != null){
                    ArrayList<String> fromItem = itemData.getFrom();
                    if(fromItem != null && fromItem.size() > 0){
                        if(fromItem.contains(itemId)){
                            itemCanBeBuiltToMoreItems = true;
                            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View v = inflater.inflate(R.layout.build_hierarchy_view, null);
                            ImageView buildFromItemImage = (ImageView) v.findViewById(R.id.itemImage);
                            TextView buildFromItemName = (TextView)v.findViewById(R.id.textViewItemName);
                            TextView buildFromItemGold = (TextView)v.findViewById(R.id.textViewItemGold);
                            String buildFromItemNameString = itemData.getName();
                            String buildFromItemGoldString = "";
                            if(itemData.getGold() != null) {
                                buildFromItemGoldString = String.valueOf(itemData.getGold().getTotal() + " " + getString(R.string.gold));
                            }

                            if(buildFromItemNameString != null){
                                buildFromItemName.setText(buildFromItemNameString);
                            }
                            if(buildFromItemGoldString != null){
                                buildFromItemGold.setText(buildFromItemGoldString);
                            }
                            AQuery aq = new AQuery(buildFromItemImage);
                            aq.image(Commons.ITEM_IMAGES_BASE_URL + itemData.getId() + ".png");

                            Gson gson = new Gson();
                            String json = gson.toJson(i);
                            v.setTag(json);
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i = new Intent(ItemDetailActivity.this, ItemDetailActivity.class);
                                    i.putExtra(EXTRA_ITEM_DETAIL, (String) v.getTag());
                                    startActivity(i);
                                }
                            });

                            buildToLL.addView(v);
                        }
                    }
                }
            }
        }

        if(itemCanBeBuiltToMoreItems){
            noToTV.setVisibility(View.GONE);
        }else {
            noToTV.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.slide_right_out);
    }

}
