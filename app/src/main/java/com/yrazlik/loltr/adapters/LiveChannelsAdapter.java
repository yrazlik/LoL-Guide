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
import com.yrazlik.loltr.data.Streams;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 1/10/15.
 */
public class LiveChannelsAdapter extends ArrayAdapter<Streams>{

    private static final int TYPE_MAX_COUNT = 4;
    private static final int ROW_UNKNOWN = -1, ROW_STREAM = 0, ROW_APP_INSTALL_AD = 1, ROW_CONTENT_AD = 2;

    private Context mContext;
    private List<Streams> streams;

    public LiveChannelsAdapter(Context context, int resource, List<Streams> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.streams = objects;
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
                case ROW_STREAM:
                    convertView = inflater.inflate(R.layout.list_row_livechannel, parent, false);
                    holder.channelImage = (CircularImageView) convertView.findViewById(R.id.imageViewChannelLogo);
                    holder.channelTitle = (RobotoTextView) convertView.findViewById(R.id.textViewChannelName);
                    holder.textViewChannelStatus = (RobotoTextView) convertView.findViewById(R.id.textViewChannelStatus);
                    break;
                case ROW_APP_INSTALL_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.list_row_small_appinstalladview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    holder.adRightArrow = (ImageView) holder.nativeAdView.findViewById(R.id.rightArrow);
                    holder.adRightArrow.setVisibility(View.VISIBLE);
                    break;
                case ROW_CONTENT_AD:
                    holder.nativeAdView = (NativeContentAdView) inflater.inflate(R.layout.list_row_small_contentadview, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    holder.adRightArrow = (ImageView) holder.nativeAdView.findViewById(R.id.rightArrow);
                    holder.adRightArrow.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Streams stream = getItem(position);

        switch (type) {
            case ROW_UNKNOWN:
            case ROW_STREAM:
                holder.channelTitle.setText(stream.getChannel().getDisplay_name());
                holder.textViewChannelStatus.setText(stream.getChannel().getStatus() == null ? ". . ." : stream.getChannel().getStatus());
                LolImageLoader.getInstance().loadImage(stream.getChannel().getLogo(), holder.channelImage);
                break;
            case ROW_APP_INSTALL_AD:
                handleAppInstallNativeAdView(stream, holder);
                break;
            case ROW_CONTENT_AD:
                handleContentNativeAdView(stream, holder);
                break;
            default:
                break;
        }

        return convertView;
    }

    private void handleAppInstallNativeAdView(Streams stream, ViewHolder holder) {
        if(stream.getNativeAd() != null) {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) stream.getNativeAd();
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

    private void handleContentNativeAdView(Streams stream, ViewHolder holder) {
        if(stream.getNativeAd() != null) {
            NativeContentAd nativeContentAd = (NativeContentAd) stream.getNativeAd();
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
        Streams stream = streams.get(position);
        if(stream.getNativeAd() != null) {
            if(stream.getNativeAd() instanceof NativeAppInstallAd) {
                return ROW_APP_INSTALL_AD;
            } else if(stream.getNativeAd() instanceof NativeContentAd) {
                return ROW_CONTENT_AD;
            }
        } else {
            return ROW_STREAM;
        }
        return ROW_UNKNOWN;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    static class ViewHolder {
        public CircularImageView channelImage;
        public RobotoTextView channelTitle;
        public RobotoTextView textViewChannelStatus;
        public NativeAdView nativeAdView;
        public RelativeLayout adContainerView;
        public RobotoTextView adHeadline;
        public RobotoTextView adBody;
        public CircularImageView adImage;
        private ImageView adRightArrow;
    }
}
