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
import com.yrazlik.loltr.data.Summoner;

import java.util.List;

/**
 * Created by yrazlik on 3/10/15.
 */
public class MatchInfoAdapter extends ArrayAdapter<Summoner>{


    private Context mContext;
    private AQuery aq;

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
            holder.champName = (TextView)convertView.findViewById(R.id.champName);
            holder.userName = (TextView)convertView.findViewById(R.id.userName);
            holder.progress = (ProgressBar)convertView.findViewById(R.id.progress);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Summoner summoner = getItem(position);

        aq = new AQuery(holder.champImage);
        aq.progress(holder.progress).image("http://ddragon.leagueoflegends.com/cdn/5.4.1/img/champion/Tristana.png", true, true);
        holder.champName.setText(mContext.getString(R.string.chosenChampion) + " " + "Tristana");
        holder.userName.setText(mContext.getString(R.string.player) + " " + summoner.getSummonerName());


        return convertView;
    }


    static class ViewHolder {
        public ImageView champImage;
        public TextView champName;
        public TextView userName;
        public ProgressBar progress;
    }
}
