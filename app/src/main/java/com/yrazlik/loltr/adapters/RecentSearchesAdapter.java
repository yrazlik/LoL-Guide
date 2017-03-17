package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.RecentSearchItem;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 1/7/16.
 */
public class RecentSearchesAdapter extends ArrayAdapter<RecentSearchItem>{

    private Context mContext;
    private int resourceId;

    public RecentSearchesAdapter(Context context, int resource, List<RecentSearchItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();
            holder.row = (RelativeLayout) convertView.findViewById(R.id.row);
            holder.summonerProfileIcon = (CircularImageView)convertView.findViewById(R.id.summonerProfileIcon);
            holder.summonerName = (RobotoTextView)convertView.findViewById(R.id.summonerName);
            holder.summonerRegion = (RobotoTextView) convertView.findViewById(R.id.summonerRegion);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        RecentSearchItem item = getItem(position);

        if(item.getName() != null && item.getName().length() > 0){
            holder.summonerName.setText(item.getName());
        }else{
            holder.summonerName.setText("???");
        }

        if(item.getRegion() != null && item.getRegion().length() > 0){
            holder.summonerRegion.setText(mContext.getResources().getString(R.string.region) + " " + item.getRegion().toUpperCase());
        } else {
            holder.summonerRegion.setText(mContext.getResources().getString(R.string.region) + " ???");
        }

        LolImageLoader.getInstance().loadImage(Commons.PROFILE_ICON_BASE_URL + item.getProfileIconId() + ".png", holder.summonerProfileIcon);

        return convertView;

    }


    static class ViewHolder {
        public CircularImageView summonerProfileIcon;
        public RobotoTextView summonerName;
        public RobotoTextView summonerRegion;
        public RelativeLayout row;
    }
}
