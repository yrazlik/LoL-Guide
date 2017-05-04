package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Spell;
import com.yrazlik.loltr.fragments.ChampionAbilitiesVideosFragment;
import com.yrazlik.loltr.utils.Utils;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

public class ChampionSpellsListAdapter extends ArrayAdapter<Spell> implements
		OnClickListener {

    private static final int TYPE_MAX_COUNT = 4;
    private static final int ROW_UNKNOWN = -1, ROW_SPELLS = 0, ROW_APP_INSTALL_AD = 1, ROW_CONTENT_AD = 2;

	private Context mContext;
    private int champId;
    private List<Spell> mObjects;

	public ChampionSpellsListAdapter(Context context, int resource, List<Spell> objects, int champId) {
		super(context, resource, objects);
		this.mContext = context;
        this.champId = champId;
        this.mObjects = objects;
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
                case ROW_SPELLS:
                    convertView = inflater.inflate(R.layout.list_row_abilities, parent, false);

                    holder.spellImage = (CircularImageView) convertView
                            .findViewById(R.id.imageViewSpellImage);
                    holder.spellTitle = (RobotoTextView) convertView
                            .findViewById(R.id.textViewSpellTitle);
                    holder.spellBody = (RobotoTextView) convertView
                            .findViewById(R.id.textViewSpellBody);

                    holder.textViewVideo = (RobotoTextView) convertView.findViewById(R.id.textViewVideo);
                    holder.progress = (ProgressBar) convertView.findViewById(R.id.imageProgress);
                    break;
                case ROW_APP_INSTALL_AD:
                    holder.nativeAdView = (NativeAppInstallAdView) inflater.inflate(R.layout.large_appinstalladview_small_icon, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.contentActionTV = (RobotoTextView) holder.nativeAdView.findViewById(R.id.contentActionTV);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    break;
                case ROW_CONTENT_AD:
                    holder.nativeAdView = (NativeContentAdView) inflater.inflate(R.layout.large_contentadview_small_icon, parent, false);
                    convertView = holder.nativeAdView;
                    holder.adContainerView = (RelativeLayout) holder.nativeAdView.findViewById(R.id.adContainerView);
                    holder.adHeadline = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adHeadline);
                    holder.adBody = (RobotoTextView) holder.nativeAdView.findViewById(R.id.adBody);
                    holder.contentActionTV = (RobotoTextView) holder.nativeAdView.findViewById(R.id.contentActionTV);
                    holder.adImage = (CircularImageView) holder.nativeAdView.findViewById(R.id.adImage);
                    break;
                default:
                    break;
            }

            convertView.setTag(holder);
        } else {
			holder = (ViewHolder) convertView.getTag();
		}

        Spell championSpell = getItem(position);

        switch (type) {
            case ROW_UNKNOWN:
            case ROW_SPELLS:
                String spellKey = championSpell.getSpellKey();
                if(spellKey != null && spellKey.trim().toString().length() > 0) {
                    spellKey = spellKey + " : ";
                } else {
                    spellKey = "";
                }
                holder.spellTitle.setText(spellKey + championSpell.getName());
                holder.spellBody.setText(championSpell.getSanitizedDescription());

                if (championSpell.getName().contains(getContext().getResources().getString(R.string.passive))) {
                    LolImageLoader.getInstance().loadImage(Commons.CHAMPION_PASSIVE_IMAGE_BASE_URL
                            + championSpell.getImage().getFull(), holder.spellImage);
                } else {
                    LolImageLoader.getInstance().loadImage(Commons.CHAMPION_SPELL_IMAGE_BASE_URL
                            + championSpell.getImage().getFull(), holder.spellImage);
                }

                holder.textViewVideo.setTag(position);
                holder.textViewVideo.setOnClickListener(this);

                break;
            case ROW_APP_INSTALL_AD:
                handleAppInstallNativeAdView(championSpell, holder);
                break;
            case ROW_CONTENT_AD:
                handleContentNativeAdView(championSpell, holder);
                break;
            default:
                break;
        }

		return convertView;

	}

    private void handleAppInstallNativeAdView(Spell spell, ViewHolder holder) {
        if(spell.getNativeAd() != null) {
            NativeAppInstallAd nativeAppInstallAd = (NativeAppInstallAd) spell.getNativeAd();
            NativeAppInstallAdView adView = (NativeAppInstallAdView) holder.nativeAdView;

            holder.adHeadline.setText("Sponsored: " + nativeAppInstallAd.getHeadline());
            adView.setHeadlineView(holder.adHeadline);

            holder.adBody.setText(nativeAppInstallAd.getBody());
            adView.setBodyView(holder.adBody);

            if(nativeAppInstallAd.getIcon() != null && nativeAppInstallAd.getIcon().getDrawable() != null) {
                holder.adImage.setImageDrawable(nativeAppInstallAd.getIcon().getDrawable());
            } else {
                holder.adImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setIconView(holder.adImage);

            if(nativeAppInstallAd.getCallToAction() != null) {
                holder.contentActionTV.setText(Utils.makeCamelCase(nativeAppInstallAd.getCallToAction().toString()));
            }
            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeAppInstallAd);
        }
    }

    private void handleContentNativeAdView(Spell spell, ViewHolder holder) {
        if(spell.getNativeAd() != null) {
            NativeContentAd nativeContentAd = (NativeContentAd) spell.getNativeAd();
            NativeContentAdView adView = (NativeContentAdView) holder.nativeAdView;

            holder.adHeadline.setText("Sponsored: " + nativeContentAd.getHeadline());
            adView.setHeadlineView(holder.adHeadline);

            holder.adBody.setText(nativeContentAd.getBody());
            adView.setBodyView(holder.adBody);

            if(nativeContentAd.getLogo() != null && nativeContentAd.getLogo().getDrawable() != null) {
                holder.adImage.setImageDrawable(nativeContentAd.getLogo().getDrawable());
            } else {
                holder.adImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.white_bg));
            }
            adView.setImageView(holder.adImage);

            if(nativeContentAd.getCallToAction() != null) {
                holder.contentActionTV.setText(Utils.makeCamelCase(nativeContentAd.getCallToAction().toString()));
            }
            adView.setCallToActionView(holder.adContainerView);

            adView.setNativeAd(nativeContentAd);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Spell spell = mObjects.get(position);
        if(spell.getNativeAd() != null) {
            if(spell.getNativeAd() instanceof NativeAppInstallAd) {
                return ROW_APP_INSTALL_AD;
            } else if(spell.getNativeAd() instanceof NativeContentAd) {
                return ROW_CONTENT_AD;
            }
        } else {
            return ROW_SPELLS;
        }
        return ROW_UNKNOWN;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

	static class ViewHolder {
		public CircularImageView spellImage;
		public RobotoTextView spellTitle;
		public RobotoTextView spellBody;
        public RobotoTextView textViewVideo;
        public ProgressBar progress;
        public NativeAdView nativeAdView;
        public RelativeLayout adContainerView;
        private RobotoTextView adHeadline;
        private CircularImageView adImage;
        private RobotoTextView adBody;
        private RobotoTextView contentActionTV;
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.textViewVideo) {
            try {
                int key = (Integer) v.getTag();
                if(!mObjects.get(key).isAd()) {
                    ChampionAbilitiesVideosFragment fragment = new ChampionAbilitiesVideosFragment();
                    Bundle args = new Bundle();
                    args.putInt(ChampionAbilitiesVideosFragment.EXTRA_CHAMP_ID, champId);
                    args.putInt(ChampionAbilitiesVideosFragment.EXTRA_KEY_POSITION, key);
                    fragment.setArguments(args);
                    fragment.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "");
                }
            } catch (Exception ignored) {}
		}
	}
}