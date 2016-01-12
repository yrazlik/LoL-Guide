package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.RecentSearchItem;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.FadeInNetworkImageView;

import java.util.List;

/**
 * Created by yrazlik on 1/7/16.
 */
public class RecentSearchesAdapter extends ArrayAdapter<RecentSearchItem>{

    private Context mContext;
    private int resourceId;
    private List<RecentSearchItem> items;

    public RecentSearchesAdapter(Context context, int resource, List<RecentSearchItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceId = resource;
        this.items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();
            holder.row = (RelativeLayout) convertView.findViewById(R.id.row);
            holder.summonerProfileIcon = (FadeInNetworkImageView)convertView.findViewById(R.id.summonerProfileIcon);
            holder.summonerName = (TextView)convertView.findViewById(R.id.summonerName);
            holder.summonerRegion = (TextView) convertView.findViewById(R.id.summonerRegion);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

     /*   if(position%2 == 0){
            holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.material_green));
        }else{
            holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.material_dark_green));
        }*/

        RecentSearchItem item = getItem(position);

        if(item.getName() != null && item.getName().length() > 0){
            holder.summonerName.setText(item.getName());
        }else{
            holder.summonerName.setText("???");
        }

        if(item.getRegion() != null && item.getRegion().length() > 0){
            holder.summonerRegion.setText(mContext.getResources().getString(R.string.region) + " " + item.getRegion().toUpperCase());
        }
        holder.summonerProfileIcon.setImageUrl(Commons.PROFILE_ICON_BASE_URL + item.getProfileIconId() + ".png", ServiceRequest.getInstance(mContext).getImageLoader());

        return convertView;

    }


    static class ViewHolder {
        public FadeInNetworkImageView summonerProfileIcon;
        public TextView summonerName;
        public TextView summonerRegion;
        public RelativeLayout row;
    }
}
