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
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.ChampionDiscount;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;

/**
 * Created by yrazlik on 12/25/15.
 */
public class ChampionDiscountsAdapter extends ArrayAdapter<ChampionDiscount>{

    private static final int TYPE_MAX_COUNT = 4;
    private static final int ROW_UNKNOWN = -1, ROW_CHAMPION = 0, ROW_APP_INSTALL_AD = 1, ROW_CONTENT_AD = 2;

    private Context mContext;
    private ArrayList<ChampionDiscount> championDiscounts;
    private int resourceId;

    public ChampionDiscountsAdapter(Context context, int resource, ArrayList<ChampionDiscount> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.championDiscounts = objects;
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
                    convertView = inflater.inflate(R.layout.list_row_discount_champions, parent, false);
                    holder.discountImage = (CircularImageView) convertView.findViewById(R.id.discountImage);
                    holder.discountName = (RobotoTextView)convertView.findViewById(R.id.name);
                    holder.date = (RobotoTextView)convertView.findViewById(R.id.date);
                    holder.priceBeforeDiscount = (RobotoTextView)convertView.findViewById(R.id.priceBeforeDiscount);
                    holder.priceAfterDiscount = (RobotoTextView)convertView.findViewById(R.id.priceAfterDiscount);
                    holder.rpImage = (ImageView)convertView.findViewById(R.id.rpImage);
                    break;
                case ROW_APP_INSTALL_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.list_row_small_appinstalladview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adHeadline.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adBody.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    break;
                case ROW_CONTENT_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.list_row_small_contentadview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adHeadline.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adBody.setGravity(Gravity.CENTER_HORIZONTAL);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    break;
                default:
                    break;
            }

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ChampionDiscount championDiscount = getItem(position);

        switch (type) {
            case ROW_UNKNOWN:
            case ROW_CHAMPION:
                if (championDiscount != null){
                    if(championDiscount.getImageUrl() != null){
                        LolImageLoader.getInstance().loadImage(championDiscount.getImageUrl(), holder.discountImage);
                    }

                    if(championDiscount.getName() != null){
                        holder.discountName.setText(championDiscount.getName());
                    }

                    if (championDiscount.getStartDate() != null && championDiscount.getEndDate() != null){
                        holder.date.setText(championDiscount.getStartDate() + " - " + championDiscount.getEndDate());
                    }

                    if(championDiscount.getPriceBeforeDiscount() != null && championDiscount.getPriceAfterDiscount() != null){
                        holder.priceBeforeDiscount.setPaintFlags(holder.priceBeforeDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.rpImage.setVisibility(View.VISIBLE);
                        holder.priceBeforeDiscount.setText(championDiscount.getPriceBeforeDiscount());
                        holder.priceAfterDiscount.setText(championDiscount.getPriceAfterDiscount());
                    }else{
                        holder.rpImage.setVisibility(View.GONE);
                    }
                }
                break;
            case ROW_APP_INSTALL_AD:
                handleAppInstallNativeAdView(championDiscount, holder);
                break;
            case ROW_CONTENT_AD:
                handleContentNativeAdView(championDiscount, holder);
                break;
            default:
                break;
        }

        return convertView;
    }

    private void handleAppInstallNativeAdView(ChampionDiscount championDiscount, ViewHolder holder) {
        if(championDiscount.getNativeAd() != null) {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) championDiscount.getNativeAd();
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

            adView.setNativeAd(nativeAppInstallAd);
        }
    }

    private void handleContentNativeAdView(ChampionDiscount championDiscount, ViewHolder holder) {
        if(championDiscount.getNativeAd() != null) {
            NativeContentAd nativeContentAd = (NativeContentAd) championDiscount.getNativeAd();
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

            adView.setNativeAd(nativeContentAd);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChampionDiscount championDiscount = championDiscounts.get(position);
        if(championDiscount.getNativeAd() != null) {
            if(championDiscount.getNativeAd() instanceof NativeAppInstallAd) {
                return ROW_APP_INSTALL_AD;
            } else if(championDiscount.getNativeAd() instanceof NativeContentAd) {
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
        public CircularImageView discountImage;
        public RobotoTextView discountName;
        public RobotoTextView  date;
        public RobotoTextView  priceBeforeDiscount;
        public RobotoTextView  priceAfterDiscount;
        public ImageView rpImage;
        public NativeAdView nativeAdView;
        public RelativeLayout adContainerView;
        public RobotoTextView adHeadline;
        public RobotoTextView adBody;
        public CircularImageView adImage;
    }
}
 