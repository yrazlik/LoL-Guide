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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.News;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.utils.Utils;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.List;

/**
 * Created by yrazlik on 12/28/15.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private static final int TYPE_MAX_COUNT = 4;
    private static final int ROW_UNKNOWN = -1, ROW_NEWS = 0, ROW_APP_INSTALL_AD = 1, ROW_CONTENT_AD = 2;

    private Context mContext;
    private int resourceId;
    private DisplayImageOptions mOptions;
    private List<News> mObjects;

    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceId = resource;
        this.mObjects = objects;
        mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).showImageOnLoading(R.drawable.white_bg).showImageForEmptyUri(R.drawable.white_bg)
                .showImageOnFail(R.drawable.white_bg).cacheOnDisk(true).cacheInMemory(true)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        int type = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case ROW_UNKNOWN:
                case ROW_NEWS:
                    convertView = inflater.inflate(resourceId, parent, false);
                    break;
                case ROW_APP_INSTALL_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.list_row_news_nativeinstallad, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    break;
                case ROW_CONTENT_AD:
                    holder.nativeAdView = (NativeContentAdView) inflater.inflate(R.layout.list_row_news_nativecontentad, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    break;
                default:
                    break;
            }

            holder.smallImage = (ImageView) convertView.findViewById(R.id.newsIV);
            holder.rightArrow = (ImageView) convertView.findViewById(R.id.rightArrow);
            holder.title = (RobotoTextView) convertView.findViewById(R.id.newsTitleTV);
            holder.shortDescTV = (RobotoTextView) convertView.findViewById(R.id.shortDescTV);
            holder.txtSeeMore = (RobotoTextView) convertView.findViewById(R.id.txtSeeMore);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News news = getItem(position);

        switch (type) {
            case ROW_UNKNOWN:
            case ROW_NEWS:
                LolImageLoader.getInstance().loadImage(news.getImg(), holder.smallImage, mOptions);
                setTitle(holder, news);
                setShortDesc(holder, news);
                break;
            case ROW_APP_INSTALL_AD:
                handleAppInstallNativeAdView(news, holder);
                break;
            case ROW_CONTENT_AD:
                handleContentNativeAdView(news, holder);
                break;
            default:
                break;
        }

        return convertView;

    }

    private void setTitle(ViewHolder holder, News news) {
        String title = news.getTitle();
        if (title != null && title.length() > 0) {
            holder.title.setText(title);
        } else {
            holder.title.setText("???");
        }
    }

    private void setShortDesc(ViewHolder holder, News news) {
        String shortDesc = news.getShortDesc();
        if (shortDesc != null && shortDesc.length() > 0) {
            holder.shortDescTV.setText(shortDesc);
        } else {
            holder.shortDescTV.setText("...");
        }
    }

    private void handleAppInstallNativeAdView(News news, ViewHolder holder) {
        if(news.getNativeAd() != null) {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) news.getNativeAd();
            NativeAppInstallAdView adView = (NativeAppInstallAdView) holder.nativeAdView;

            holder.title.setText(nativeAppInstallAd.getHeadline());
            adView.setHeadlineView(holder.title);

            holder.shortDescTV.setText(nativeAppInstallAd.getBody());
            adView.setBodyView(holder.shortDescTV);

            if(nativeAppInstallAd.getImages() != null && nativeAppInstallAd.getImages().size() > 0) {
                holder.smallImage.setImageDrawable(nativeAppInstallAd.getImages().get(0).getDrawable());
            } else {
                holder.smallImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setIconView(holder.smallImage);

            holder.txtSeeMore.setText(Utils.makeCamelCase(nativeAppInstallAd.getCallToAction().toString()));
            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeAppInstallAd);
        }
    }

    private void handleContentNativeAdView(News news, ViewHolder holder) {
        if(news.getNativeAd() != null) {
            NativeContentAd nativeContentAd = (NativeContentAd) news.getNativeAd();
            NativeContentAdView adView = (NativeContentAdView) holder.nativeAdView;

            holder.title.setText(nativeContentAd.getHeadline());
            adView.setHeadlineView(holder.title);

            holder.shortDescTV.setText(nativeContentAd.getBody());
            adView.setBodyView(holder.shortDescTV);

            if(nativeContentAd.getImages() != null && nativeContentAd.getImages().size() > 0) {
                holder.smallImage.setImageDrawable(nativeContentAd.getImages().get(0).getDrawable());
            } else {
                holder.smallImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setImageView(holder.smallImage);

            holder.txtSeeMore.setText(Utils.makeCamelCase(nativeContentAd.getCallToAction().toString()));
            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeContentAd);
        }
    }

    @Override
    public int getItemViewType(int position) {
        News news = mObjects.get(position);
        if(news.getNativeAd() != null) {
            if(news.getNativeAd() instanceof NativeAppInstallAd) {
                return ROW_APP_INSTALL_AD;
            } else if(news.getNativeAd() instanceof NativeContentAd) {
                return ROW_CONTENT_AD;
            }
        } else {
            return ROW_NEWS;
        }
        return ROW_UNKNOWN;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    static class ViewHolder {
        public ImageView smallImage;
        public ImageView rightArrow;
        public RobotoTextView title;
        private RobotoTextView shortDescTV;
        private RobotoTextView txtSeeMore;
        public NativeAdView nativeAdView;
        public RelativeLayout adContainerView;
    }
}
