package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.List;

public class WeeklyFreeChampionsAdapter extends ArrayAdapter<ChampionDto> {

    private static final int TYPE_MAX_COUNT = 4;
    private static final int ROW_UNKNOWN = -1, ROW_CHAMPION = 0, ROW_APP_INSTALL_AD = 1, ROW_CONTENT_AD = 2;

    private Context mContext;
    private List<ChampionDto> mObjects;

	public WeeklyFreeChampionsAdapter(Context context, int resource, List<ChampionDto> objects) {
		super(context, resource, objects);
		this.mContext = context;
        this.mObjects = objects;
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
                    convertView = inflater.inflate(R.layout.list_row_weeklyfreechampions, parent, false);
                    holder.championImage = (ImageView)convertView.findViewById(R.id.imageViewChampionImage);
                    holder.championName = (RobotoTextView)convertView.findViewById(R.id.textViewChampionName);
                    holder.dateInterval = (RobotoTextView)convertView.findViewById(R.id.textViewDateInterval);
                    holder.rpIcon = (ImageView)convertView.findViewById(R.id.imageViewRp);
                    holder.ipIcon = (ImageView)convertView.findViewById(R.id.imageViewIp);
                    holder.rpPrice = (RobotoTextView)convertView.findViewById(R.id.textViewRp);
                    holder.ipPrice = (RobotoTextView)convertView.findViewById(R.id.textViewIp);
                    break;
                case ROW_APP_INSTALL_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.list_row_weekly_free_appinstalladview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    break;
                case ROW_CONTENT_AD:
                    holder.nativeAdView = (NativeContentAdView) inflater.inflate(R.layout.list_row_weekly_free_contentadview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    break;
                default:
                    break;
            }

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

        ChampionDto champion = getItem(position);

        switch (type) {
            case ROW_UNKNOWN:
            case ROW_CHAMPION:
                setChampionName(champion, holder);
                LolImageLoader.getInstance().loadImage(champion.getImage().getFull(), holder.championImage);
                setFreeDateInterval(champion, holder);
                setIpPrices(champion, holder);
                setRpPrices(champion, holder);
                break;
            case ROW_APP_INSTALL_AD:
                handleAppInstallNativeAdView(champion, holder);
                break;
            case ROW_CONTENT_AD:
                handleAContentNativeAdView(champion, holder);
                break;
            default:
                break;
        }

		return convertView;
	}

    private void setChampionName(ChampionDto champion, ViewHolder holder) {
        holder.championName.setText(champion.getName());
    }

    private void setFreeDateInterval(ChampionDto champion, ViewHolder holder) {
        holder.dateInterval.setText(champion.getDateInterval());
    }

    private void setIpPrices(ChampionDto champion, ViewHolder holder) {
        holder.ipPrice.setVisibility(View.VISIBLE);
        holder.ipIcon.setVisibility(View.VISIBLE);
        if(champion.getChampionRp() != null && champion.getChampionRp().length() > 0){
            holder.rpPrice.setText(champion.getChampionRp());
        } else {
            holder.rpPrice.setText("???");
        }
    }

    private void setRpPrices(ChampionDto champion, ViewHolder holder) {
        holder.rpPrice.setVisibility(View.VISIBLE);
        holder.rpIcon.setVisibility(View.VISIBLE);
        if(champion.getChampionIp() != null && champion.getChampionIp().length() > 0){
            holder.ipPrice.setText(champion.getChampionIp());
        } else {
            holder.ipPrice.setText("???");
        }
    }

    private void handleAppInstallNativeAdView(ChampionDto champion, ViewHolder holder) {
        if(champion.getNativeAd() != null) {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) champion.getNativeAd();
            NativeAppInstallAdView adView = (NativeAppInstallAdView) holder.nativeAdView;

            holder.adHeadline.setText(nativeAppInstallAd.getHeadline());
            adView.setHeadlineView(holder.adHeadline);

            holder.adBody.setText(nativeAppInstallAd.getBody());
            adView.setBodyView(holder.adBody);

            holder.adImage.setImageDrawable(nativeAppInstallAd.getIcon().getDrawable());
            adView.setIconView(holder.adImage);

            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeAppInstallAd);
        }
    }

    private void handleAContentNativeAdView(ChampionDto champion, ViewHolder holder) {
        if(champion.getNativeAd() != null) {
            NativeContentAd nativeContentAd = (NativeContentAd) champion.getNativeAd();
            NativeContentAdView adView = (NativeContentAdView) holder.nativeAdView;

            holder.adHeadline.setText(nativeContentAd.getHeadline());
            adView.setHeadlineView(holder.adHeadline);

            holder.adBody.setText(nativeContentAd.getBody());
            adView.setBodyView(holder.adBody);

            if(nativeContentAd.getImages() != null && nativeContentAd.getImages().size() > 0) {
                holder.adImage.setImageDrawable(nativeContentAd.getImages().get(0).getDrawable());
            } else {
                holder.adImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.placeholder));
            }
            adView.setImageView(holder.adImage);

            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeContentAd);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChampionDto championDto = mObjects.get(position);
        if(championDto.getNativeAd() != null) {
            if(championDto.getNativeAd() instanceof NativeAppInstallAd) {
                return ROW_APP_INSTALL_AD;
            } else if(championDto.getNativeAd() instanceof NativeContentAd) {
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
		public ImageView championImage;
		public RobotoTextView championName;
		public RobotoTextView dateInterval;
		public ImageView rpIcon;
		public ImageView ipIcon;
		public RobotoTextView rpPrice;
		public RobotoTextView ipPrice;
        public NativeAdView nativeAdView;
        public RelativeLayout adContainerView;
        public RobotoTextView adHeadline;
        public RobotoTextView adBody;
        public CircularImageView adImage;
	}
}
