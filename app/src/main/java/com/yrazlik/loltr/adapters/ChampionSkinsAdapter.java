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
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Skin;

import java.util.List;

/**
 * Created by yrazlik on 2/1/15.
 */
public class ChampionSkinsAdapter extends ArrayAdapter<Skin>{

    private Context mContext;
    private AQuery aQuery;
    private String key;

    public ChampionSkinsAdapter(Context context, int resource, List<Skin> objects, String key) {
        super(context, resource, objects);
        this.mContext = context;
        this.key = key;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listrow_champion_skins, parent, false);

            holder = new ViewHolder();
            holder.skinImage = (ImageView)convertView.findViewById(R.id.championSkin);
            holder.skinName = (TextView)convertView.findViewById(R.id.skinName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Skin skin = getItem(position);
        aQuery = new AQuery(holder.skinImage);
        ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.imageProgress);
        aQuery.id(R.id.championSkin).progress(progress).image(Commons.URL_CHAMPION_SKIN_BASE + key + "_" + position + ".jpg", true, true);

        holder.skinName.setText(skin.getName());

        return convertView;
    }


    static class ViewHolder {
        public ImageView skinImage;
        public TextView skinName;
    }
}
