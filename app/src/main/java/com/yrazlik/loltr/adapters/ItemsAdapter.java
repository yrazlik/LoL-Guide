package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Gold;
import com.yrazlik.loltr.data.Image;
import com.yrazlik.loltr.data.Item;
import com.yrazlik.loltr.data.Data;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yrazlik on 8/6/15.
 */
public class ItemsAdapter extends ArrayAdapter<Item>{

    private static final int TYPE_MAX_COUNT = 4;
    private static final int ROW_UNKNOWN = -1, ROW_ITEM = 0, ROW_APP_INSTALL_AD = 1, ROW_CONTENT_AD = 2;

    private Context mContext;
    private int layoutId;
    private List<Item> items;

    public ItemsAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutId = resource;
        this.items = objects;
    }

    @Override
    public int getCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        int type = getItemViewType(position);
        if(convertView == null){
            holder = new ViewHolder();

            switch (type) {
                case ROW_UNKNOWN:
                case ROW_ITEM:
                    convertView = inflater.inflate(layoutId, parent, false);
                    holder.itemImage = (CircularImageView)convertView.findViewById(R.id.item_image);
                    holder.textContainer = (RelativeLayout)convertView.findViewById(R.id.textContainer);
                    holder.itemName = (RobotoTextView)convertView.findViewById(R.id.itemName);
                    holder.shortDescription = (RobotoTextView)convertView.findViewById(R.id.shortDescription);
                    break;
                case ROW_APP_INSTALL_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.list_row_small_appinstalladview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    holder.rightArrow = (ImageView) holder.nativeAdView.findViewById(R.id.rightArrow);
                    holder.rightArrow.setVisibility(View.VISIBLE);
                    break;
                case ROW_CONTENT_AD:
                    holder.nativeAdView = (NativeContentAdView) inflater.inflate(R.layout.list_row_small_contentadview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    holder.rightArrow = (ImageView) holder.nativeAdView.findViewById(R.id.rightArrow);
                    holder.rightArrow.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);

        switch (type) {
            case ROW_UNKNOWN:
            case ROW_ITEM:
                Data itemData = item.getData();
                if(itemData != null) {
                    String itemName = itemData.getName();
                    if(itemName != null){
                        holder.itemName.setText(itemName);
                    }
                    Gold gold = itemData.getGold();
                    if(gold != null){
                        int total = gold.getTotal();
                        holder.shortDescription.setText(total + " " + mContext.getString(R.string.gold));
                    }

                    Image itemImage = itemData.getImage();
                    if(itemImage != null){
                        String imageUrl = itemImage.getFull();
                        if(imageUrl != null){
                            imageUrl = Commons.ITEM_IMAGES_BASE_URL + imageUrl;
                            LolImageLoader.getInstance().loadImage(imageUrl, holder.itemImage);
                        }
                    }
                }
                break;
            case ROW_APP_INSTALL_AD:
                handleAppInstallNativeAdView(item, holder);
                break;
            case ROW_CONTENT_AD:
                handleContentNativeAdView(item, holder);
                break;
            default:
                break;
        }

        return convertView;
    }

    private void handleAppInstallNativeAdView(Item item, ViewHolder holder) {
        if(item.getNativeAd() != null) {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) item.getNativeAd();
            NativeAppInstallAdView adView = (NativeAppInstallAdView) holder.nativeAdView;

            holder.adHeadline.setText(nativeAppInstallAd.getHeadline());
            adView.setHeadlineView(holder.adHeadline);

            holder.adBody.setText(nativeAppInstallAd.getBody());
            adView.setBodyView(holder.adBody);

            if(nativeAppInstallAd.getIcon() != null && nativeAppInstallAd.getIcon().getDrawable() != null) {
                holder.adImage.setImageDrawable(nativeAppInstallAd.getIcon().getDrawable());
            } else {
                holder.adImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setIconView(holder.adImage);

            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeAppInstallAd);
        }
    }

    private void handleContentNativeAdView(Item item, ViewHolder holder) {
        if(item.getNativeAd() != null) {
            NativeContentAd nativeContentAd = (NativeContentAd) item.getNativeAd();
            NativeContentAdView adView = (NativeContentAdView) holder.nativeAdView;

            holder.adHeadline.setText(nativeContentAd.getHeadline());
            adView.setHeadlineView(holder.adHeadline);

            holder.adBody.setText(nativeContentAd.getBody());
            adView.setBodyView(holder.adBody);

            if(nativeContentAd.getLogo() != null && nativeContentAd.getLogo().getDrawable() != null) {
                holder.adImage.setImageDrawable(nativeContentAd.getLogo().getDrawable());
            } else {
                holder.adImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setImageView(holder.adImage);

            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeContentAd);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Item item = items.get(position);
        if(item.getNativeAd() != null) {
            if(item.getNativeAd() instanceof NativeAppInstallAd) {
                return ROW_APP_INSTALL_AD;
            } else if(item.getNativeAd() instanceof NativeContentAd) {
                return ROW_CONTENT_AD;
            }
        } else {
            return ROW_ITEM;
        }
        return ROW_UNKNOWN;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }


    static class ViewHolder{
        public CircularImageView itemImage;
        public RelativeLayout textContainer;
        public RobotoTextView itemName;
        public RobotoTextView shortDescription;
        public NativeAdView nativeAdView;
        public RelativeLayout adContainerView;
        public RobotoTextView adHeadline;
        public RobotoTextView adBody;
        public CircularImageView adImage;
        public ImageView rightArrow;
    }
}
