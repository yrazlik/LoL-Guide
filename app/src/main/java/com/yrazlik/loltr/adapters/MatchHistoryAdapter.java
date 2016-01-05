package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Game;
import com.yrazlik.loltr.data.Stats;

import java.util.List;

/**
 * Created by yrazlik on 1/5/16.
 */
public class MatchHistoryAdapter extends ArrayAdapter<Game>{


    private long summonerId;
    private List<Game> games;
    private int resourceId;
    private Context mContext;


    public MatchHistoryAdapter(Context context, int resource, List<Game> objects, long summonerId) {
        super(context, resource, objects);
        this.mContext = context;
        this.games = objects;
        this.summonerId = summonerId;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();

            holder.winLoseLabel = (RelativeLayout) convertView.findViewById(R.id.winLoseLabel);
            holder.champIV = (NetworkImageView) convertView.findViewById(R.id.champIV);
            holder.textContainer = (RelativeLayout) convertView.findViewById(R.id.textContainer);
            holder.matchInfoContainer = (RelativeLayout) convertView.findViewById(R.id.matchInfoContainer);
            holder.matchTypeTV = (TextView) convertView.findViewById(R.id.matchTypeTV);
            holder.kdaTV = (TextView) convertView.findViewById(R.id.kdaTV);
            holder.goldContainer = (RelativeLayout) convertView.findViewById(R.id.goldContainer);
            holder.goldIV = (ImageView) convertView.findViewById(R.id.goldIV);
            holder.goldTV = (TextView) convertView.findViewById(R.id.goldTV);
            holder.timeContainer = (RelativeLayout) convertView.findViewById(R.id.timeContainer);
            holder.matchTimeTV = (TextView) convertView.findViewById(R.id.matchTimeTV);
            holder.matchDateTV = (TextView) convertView.findViewById(R.id.matchDateTV);
            holder.separator = (View) convertView.findViewById(R.id.separator);
            holder.spellsContainer = (RelativeLayout) convertView.findViewById(R.id.spellsContainer);
            holder.spell1 = (NetworkImageView) convertView.findViewById(R.id.spell1);
            holder.spell2 = (NetworkImageView) convertView.findViewById(R.id.spell2);
            holder.itemsContainer = (RelativeLayout) convertView.findViewById(R.id.itemsContainer);
            holder.item0 = (NetworkImageView) convertView.findViewById(R.id.item0);
            holder.item1 = (NetworkImageView) convertView.findViewById(R.id.item1);
            holder.item2 = (NetworkImageView) convertView.findViewById(R.id.item2);
            holder.item3 = (NetworkImageView) convertView.findViewById(R.id.item3);
            holder.item4 = (NetworkImageView) convertView.findViewById(R.id.item4);
            holder.item5 = (NetworkImageView) convertView.findViewById(R.id.item5);
            holder.item6 = (NetworkImageView) convertView.findViewById(R.id.item6);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Game game = getItem(position);
        if(game != null){

            String gameMode = game.getGameMode();
            if(gameMode != null){
                holder.matchTypeTV.setText(gameMode);
            }

            Stats stats = game.getStats();
            if(stats != null){
                if(stats.isWin()){
                    holder.winLoseLabel.setBackgroundColor(mContext.getResources().getColor(R.color.discount_green));
                }else{
                    holder.winLoseLabel.setBackgroundColor(mContext.getResources().getColor(R.color.gg_red));
                }
            }



        }


        return convertView;
    }

    static class ViewHolder {
        private RelativeLayout winLoseLabel;
        private NetworkImageView champIV;
        private RelativeLayout textContainer;
        private RelativeLayout matchInfoContainer;
        private TextView matchTypeTV;
        private TextView kdaTV;
        private RelativeLayout goldContainer;
        private ImageView goldIV;
        private TextView goldTV;
        private RelativeLayout timeContainer;
        private TextView matchTimeTV;
        private TextView matchDateTV;
        private View separator;
        private RelativeLayout spellsContainer;
        private NetworkImageView spell1;
        private NetworkImageView spell2;
        private RelativeLayout itemsContainer;
        private NetworkImageView item0;
        private NetworkImageView item1;
        private NetworkImageView item2;
        private NetworkImageView item3;
        private NetworkImageView item4;
        private NetworkImageView item5;
        private NetworkImageView item6;

    }
}
