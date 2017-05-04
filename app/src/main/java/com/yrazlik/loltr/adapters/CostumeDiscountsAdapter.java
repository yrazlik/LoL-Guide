package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.ChampionDiscount;
import com.yrazlik.loltr.data.CostumeDiscount;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;

/**
 * Created by yrazlik on 12/25/15.
 */
public class CostumeDiscountsAdapter extends ArrayAdapter<CostumeDiscount>{

    private static final int TYPE_MAX_COUNT = 4;
    private static final int ROW_UNKNOWN = -1, ROW_CHAMPION = 0, ROW_APP_INSTALL_AD = 1, ROW_CONTENT_AD = 2;

    private Context mContext;
    private ArrayList<CostumeDiscount> costumeDiscounts;
    private int resourceId;

    public CostumeDiscountsAdapter(Context context, int resource, ArrayList<CostumeDiscount> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.costumeDiscounts = objects;
        this.resourceId = resource;
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
                case ROW_CHAMPION:
                    convertView = inflater.inflate(R.layout.list_row_discount_costumes, parent, false);
                    holder.discountImage = (ImageView) convertView.findViewById(R.id.discountImage);
                    holder.discountName = (RobotoTextView)convertView.findViewById(R.id.name);
                    holder.date = (RobotoTextView)convertView.findViewById(R.id.date);
                    holder.priceBeforeDiscount = (RobotoTextView)convertView.findViewById(R.id.priceBeforeDiscount);
                    holder.priceAfterDiscount = (RobotoTextView)convertView.findViewById(R.id.priceAfterDiscount);
                    holder.rpImage = (ImageView)convertView.findViewById(R.id.rpImage);
                    break;
                case ROW_APP_INSTALL_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.list_row_small_appinstalladview_rectangular, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adHeadline.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adBody.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.adImage = (ImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    break;
                case ROW_CONTENT_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.list_row_small_contentadview_rectangular, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adHeadline.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adBody.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.adImage = (ImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    break;
                default:
                    break;
            }

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CostumeDiscount costumeDiscount = getItem(position);

        switch (type) {
            case ROW_UNKNOWN:
            case ROW_CHAMPION:
                if (costumeDiscount != null){
                    if(costumeDiscount.getImageUrl() != null){
                        LolImageLoader.getInstance().loadImage(costumeDiscount.getImageUrl(),  holder.discountImage);
                    }

                    if(costumeDiscount.getName() != null){
                        holder.discountName.setText(costumeDiscount.getName());
                    }

                    if (costumeDiscount.getStartDate() != null && costumeDiscount.getEndDate() != null){
                        holder.date.setText(costumeDiscount.getStartDate() + " - " + costumeDiscount.getEndDate());
                    }

                    if(costumeDiscount.getPriceBeforeDiscount() != null && costumeDiscount.getPriceAfterDiscount() != null){
                        holder.priceBeforeDiscount.setPaintFlags(holder.priceBeforeDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.rpImage.setVisibility(View.VISIBLE);
                        holder.priceBeforeDiscount.setText(costumeDiscount.getPriceBeforeDiscount());
                        holder.priceAfterDiscount.setText(costumeDiscount.getPriceAfterDiscount());
                    }else{
                        holder.rpImage.setVisibility(View.GONE);
                    }
                }
                break;
            case ROW_APP_INSTALL_AD:
                handleAppInstallNativeAdView(costumeDiscount, holder);
                break;
            case ROW_CONTENT_AD:
                handleContentNativeAdView(costumeDiscount, holder);
                break;
            default:
                break;
        }

        return convertView;
    }

    private void handleAppInstallNativeAdView(CostumeDiscount costumeDiscount, ViewHolder holder) {
        if(costumeDiscount.getNativeAd() != null) {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) costumeDiscount.getNativeAd();
            NativeAppInstallAdView adView = (NativeAppInstallAdView) holder.nativeAdView;

            holder.adHeadline.setText(nativeAppInstallAd.getHeadline());
            adView.setHeadlineView(holder.adHeadline);

            holder.adBody.setText(nativeAppInstallAd.getBody());
            adView.setBodyView(holder.adBody);

            if(nativeAppInstallAd.getImages() != null && nativeAppInstallAd.getImages().size() > 0 && nativeAppInstallAd.getImages().get(0).getDrawable() != null) {
                holder.adImage.setImageDrawable(nativeAppInstallAd.getImages().get(0).getDrawable());
            } else {
                holder.adImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setIconView(holder.adImage);

            adView.setNativeAd(nativeAppInstallAd);
        }
    }

    private void handleContentNativeAdView(CostumeDiscount costumeDiscount, ViewHolder holder) {
        if(costumeDiscount.getNativeAd() != null) {
            NativeContentAd nativeContentAd = (NativeContentAd) costumeDiscount.getNativeAd();
            NativeContentAdView adView = (NativeContentAdView) holder.nativeAdView;

            holder.adHeadline.setText(nativeContentAd.getHeadline());
            adView.setHeadlineView(holder.adHeadline);

            holder.adBody.setText(nativeContentAd.getBody());
            adView.setBodyView(holder.adBody);

            if(nativeContentAd.getImages() != null && nativeContentAd.getImages().size() > 0 && nativeContentAd.getImages().get(0).getDrawable() != null) {
                holder.adImage.setImageDrawable(nativeContentAd.getImages().get(0).getDrawable());
            } else {
                holder.adImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setImageView(holder.adImage);

            adView.setNativeAd(nativeContentAd);
        }
    }

    @Override
    public int getItemViewType(int position) {
        CostumeDiscount costumeDiscount = costumeDiscounts.get(position);
        if(costumeDiscount.getNativeAd() != null) {
            if(costumeDiscount.getNativeAd() instanceof NativeAppInstallAd) {
                return ROW_APP_INSTALL_AD;
            } else if(costumeDiscount.getNativeAd() instanceof NativeContentAd) {
                return ROW_CONTENT_AD;
            }
        } else {
            return ROW_CHAMPION;
        }
        return ROW_UNKNOWN;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    static class ViewHolder {
        public ImageView discountImage;
        public RobotoTextView discountName;
        public RobotoTextView  date;
        public RobotoTextView  priceBeforeDiscount;
        public RobotoTextView  priceAfterDiscount;
        public ImageView rpImage;
        public NativeAdView nativeAdView;
        public RelativeLayout adContainerView;
        public RobotoTextView adHeadline;
        public RobotoTextView adBody;
        public ImageView adImage;
    }
}
