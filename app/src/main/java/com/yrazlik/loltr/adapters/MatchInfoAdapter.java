package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Summoner;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 3/10/15.
 */
public class MatchInfoAdapter extends ArrayAdapter<Summoner>{


    private Context mContext;

    public MatchInfoAdapter(Context context, int resource, List<Summoner> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.match_info_detail_listrow, parent, false);

            holder = new ViewHolder();
            holder.champImage = (ImageView)convertView.findViewById(R.id.champImage);
            holder.champName = (RobotoTextView)convertView.findViewById(R.id.champName);
            holder.userName = (RobotoTextView)convertView.findViewById(R.id.userName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Summoner summoner = getItem(position);

        LolImageLoader.getInstance().loadImage("http://ddragon.leagueoflegends.com/cdn/" + Commons.LATEST_VERSION + "/img/champion/" + summoner.getKey() + ".png", holder.champImage);
        holder.champName.setText(mContext.getString(R.string.chosenChampion) + " " + summoner.getChampName());
        holder.userName.setText(mContext.getString(R.string.player) + " " + summoner.getSummonerName());


        return convertView;
    }


    static class ViewHolder {
        public ImageView champImage;
        public RobotoTextView champName;
        public RobotoTextView userName;
    }
}
