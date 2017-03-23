package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Skin;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 2/1/15.
 */
public class ChampionSkinsAdapter extends ArrayAdapter<Skin>{

    private Context mContext;
    private String key;
    private DisplayImageOptions mOptions;

    public ChampionSkinsAdapter(Context context, int resource, List<Skin> objects, String key) {
        super(context, resource, objects);
        this.mContext = context;
        this.key = key;
        mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).showImageOnLoading(R.drawable.white_bg).showImageForEmptyUri(R.drawable.white_bg)
                .showImageOnFail(R.drawable.white_bg).cacheOnDisk(true).cacheInMemory(true)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listrow_champion_skins, parent, false);

            holder = new ViewHolder();
            holder.skinImage = (ImageView)convertView.findViewById(R.id.championSkin);
            holder.skinName = (RobotoTextView) convertView.findViewById(R.id.skinName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Skin skin = getItem(position);
        LolImageLoader.getInstance().loadImage(getImageUrl(position), holder.skinImage, mOptions);

        holder.skinName.setText(skin.getName());

        return convertView;
    }

    public String getImageUrl(int position) {
        return Commons.URL_CHAMPION_SKIN_BASE + key + "_" + position + ".jpg";
    }


    static class ViewHolder {
        public ImageView skinImage;
        public RobotoTextView skinName;
    }
}
