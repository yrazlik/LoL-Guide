package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Skin;
import com.yrazlik.loltr.utils.Utils;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 2/1/15.
 */
public class ChampionSkinsAdapter extends ArrayAdapter<Skin>{

    private static final int TYPE_MAX_COUNT = 4;
    private static final int ROW_UNKNOWN = -1, ROW_SKIN = 0, ROW_APP_INSTALL_AD = 1, ROW_CONTENT_AD = 2;

    private Context mContext;
    private String key;
    private DisplayImageOptions mOptions;
    private List<Skin> skins;

    public ChampionSkinsAdapter(Context context, int resource, List<Skin> objects, String key) {
        super(context, resource, objects);
        this.mContext = context;
        this.key = key;
        this.skins = objects;
        mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).showImageOnLoading(R.drawable.white_bg).showImageForEmptyUri(R.drawable.white_bg)
                .showImageOnFail(R.drawable.white_bg).cacheOnDisk(true).cacheInMemory(true)
                .build();
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
                case ROW_SKIN:
                    convertView = inflater.inflate(R.layout.listrow_champion_skins, parent, false);
                    holder.skinImage = (ImageView)convertView.findViewById(R.id.championSkin);
                    holder.skinName = (RobotoTextView) convertView.findViewById(R.id.skinName);
                    break;
                case ROW_APP_INSTALL_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.large_nativeinstalladview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.headlineTV = (RobotoTextView) holder.nativeAdView.findViewById(R.id.headlineTV);
                    holder.bodyTV = (RobotoTextView) holder.nativeAdView.findViewById(R.id.bodyTV);
                    holder.callToActionTV = (RobotoTextView) holder.nativeAdView.findViewById(R.id.callToActionTV);
                    holder.adIV = (ImageView) holder.nativeAdView.findViewById(R.id.adIV);
                    break;
                case ROW_CONTENT_AD:
                    holder.nativeAdView = (NativeContentAdView) inflater.inflate(R.layout.large_contentadview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.headlineTV = (RobotoTextView) holder.nativeAdView.findViewById(R.id.headlineTV);
                    holder.bodyTV = (RobotoTextView) holder.nativeAdView.findViewById(R.id.bodyTV);
                    holder.callToActionTV = (RobotoTextView) holder.nativeAdView.findViewById(R.id.callToActionTV);
                    holder.adIV = (ImageView) holder.nativeAdView.findViewById(R.id.adIV);
                    break;
                default:
                    break;
            }

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Skin skin = getItem(position);

        switch (type) {
            case ROW_UNKNOWN:
            case ROW_SKIN:
                LolImageLoader.getInstance().loadImage(getImageUrl(position), holder.skinImage, mOptions);
                holder.skinName.setText(skin.getName());
                break;
            case ROW_APP_INSTALL_AD:
                handleAppInstallNativeAdView(skin, holder);
                break;
            case ROW_CONTENT_AD:
                handleContentNativeAdView(skin, holder);
                break;
            default:
                break;
        }

        return convertView;
    }

    private void handleAppInstallNativeAdView(Skin skin, ViewHolder holder) {
        if(skin.getNativeAd() != null) {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) skin.getNativeAd();
            NativeAppInstallAdView adView = (NativeAppInstallAdView) holder.nativeAdView;

            holder.headlineTV.setText(nativeAppInstallAd.getHeadline());
            adView.setHeadlineView(holder.headlineTV);

            holder.bodyTV.setText(nativeAppInstallAd.getBody());
            adView.setBodyView(holder.bodyTV);

            if(nativeAppInstallAd.getImages() != null && nativeAppInstallAd.getImages().size() > 0 && nativeAppInstallAd.getImages().get(0).getDrawable() != null) {
                holder.adIV.setImageDrawable(nativeAppInstallAd.getImages().get(0).getDrawable());
            } else {
                holder.adIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setImageView(holder.adIV);

            if(nativeAppInstallAd.getCallToAction() != null) {
                holder.callToActionTV.setText(Utils.makeCamelCase(nativeAppInstallAd.getCallToAction().toString()));
            }
            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeAppInstallAd);
        }
    }

    private void handleContentNativeAdView(Skin skin, ViewHolder holder) {
        if(skin.getNativeAd() != null) {
            NativeContentAd nativeContentAd = (NativeContentAd) skin.getNativeAd();
            NativeContentAdView adView = (NativeContentAdView) holder.nativeAdView;

            holder.headlineTV.setText(nativeContentAd.getHeadline());
            adView.setHeadlineView(holder.headlineTV);

            holder.bodyTV.setText(nativeContentAd.getBody());
            adView.setBodyView(holder.bodyTV);

            if(nativeContentAd.getImages() != null && nativeContentAd.getImages().size() > 0 && nativeContentAd.getImages().get(0).getDrawable() != null) {
                holder.adIV.setImageDrawable(nativeContentAd.getImages().get(0).getDrawable());
            } else {
                holder.adIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setImageView(holder.adIV);

            if(nativeContentAd.getCallToAction() != null) {
                holder.callToActionTV.setText(Utils.makeCamelCase(nativeContentAd.getCallToAction().toString()));
            }
            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeContentAd);
        }
    }

    public String getImageUrl(int position) {
        return Commons.URL_CHAMPION_SKIN_BASE + key + "_" + position + ".jpg";
    }

    @Override
    public int getItemViewType(int position) {
        Skin spell = skins.get(position);
        if(spell.getNativeAd() != null) {
            if(spell.getNativeAd() instanceof NativeAppInstallAd) {
                return ROW_APP_INSTALL_AD;
            } else if(spell.getNativeAd() instanceof NativeContentAd) {
                return ROW_CONTENT_AD;
            }
        } else {
            return ROW_SKIN;
        }
        return ROW_UNKNOWN;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    static class ViewHolder {
        public ImageView skinImage;
        public RobotoTextView skinName;
        public NativeAdView nativeAdView;
        public RelativeLayout adContainerView;
        private RobotoTextView headlineTV;
        private ImageView adIV;
        private RobotoTextView bodyTV;
        private RobotoTextView callToActionTV;
    }
}
