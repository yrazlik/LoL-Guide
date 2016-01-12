package com.yrazlik.loltr.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.AggregatedStatsDto;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.data.ChampionStatsDto;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.FadeInNetworkImageView;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by yrazlik on 1/12/16.
 */
public class SummonerChampionsAdapter extends ArrayAdapter<ChampionStatsDto> implements StickyListHeadersAdapter {

    private List<ChampionStatsDto> champions;
    private LayoutInflater inflater;
    private Context mContext;
    private int layoutResourceId;


    public SummonerChampionsAdapter(Context context, int resource, List<ChampionStatsDto> champions) {
        super(context, resource, champions);
        inflater = LayoutInflater.from(context);
        this.champions = champions;
        this.mContext = context;
        this.layoutResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = ((AppCompatActivity) mContext).getLayoutInflater().inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.champIV = (FadeInNetworkImageView) convertView.findViewById(R.id.champIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ChampionStatsDto champion = getItem(position);

        if (champion != null) {

        }
        return convertView;
    }


    @Override
    public int getCount() {
        if (champions == null) {
            return 0;
        }
        return champions.size();
    }

    @Override
    public ChampionStatsDto getItem(int position) {
        return champions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header_summoner_champions, parent, false);
            holder.champIV = (FadeInNetworkImageView) convertView.findViewById(R.id.champIV);
            holder.kdaTV = (TextView) convertView.findViewById(R.id.kdaTV);
            holder.goldTV = (TextView) convertView.findViewById(R.id.goldTV);
            holder.gamesPlayedTV = (TextView) convertView.findViewById(R.id.gamesPlayedTV);
            holder.winCountTV = (TextView) convertView.findViewById(R.id.winCountTV);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //set header text as first char in name
        ChampionStatsDto champion = getItem(position);
        if (champion != null) {
            final int champId = champion.getId();
            String champImageUrl = "";
            if (Commons.allChampions != null && Commons.allChampions.size() > 0) {
                for (Champion champ : Commons.allChampions) {
                    if (champ != null && champ.getId() == champId) {
                        champImageUrl = Commons.CHAMPION_IMAGE_BASE_URL + champ.getKey() + ".png";
                        break;
                    }
                }
            }

            if (champImageUrl != null && champImageUrl.length() > 0) {
                holder.champIV.setImageUrl(champImageUrl, ServiceRequest.getInstance(mContext).getImageLoader());
            } else {
                holder.champIV.setBackgroundResource(R.drawable.question_mark);
            }

            AggregatedStatsDto stats = champion.getStats();
            if(stats != null){
                int totalSessionsPlayed = stats.getTotalSessionsPlayed();
                int totalWin = stats.getTotalSessionsWon();
                int totalLose = stats.getTotalSessionsLost();
                int totalKill = stats.getTotalChampionKills();
                int totalDeath = stats.getTotalDeathsPerSession();
                int totalAssist = stats.getTotalAssists();

                holder.kdaTV.setText((int)(Math.floor((double)(totalKill)/((double)(totalSessionsPlayed)))) +
                "/" + (int)(Math.floor((double)(totalDeath)/((double)(totalSessionsPlayed)))) +
                "/" + (int)(Math.floor((double)(totalAssist)/((double)(totalSessionsPlayed)))));

                holder.goldTV.setText(format((long)stats.getTotalGoldEarned()/(long)stats.getTotalSessionsPlayed()));
                holder.gamesPlayedTV.setText(mContext.getResources().getString(R.string.games_played) + ": " + stats.getTotalSessionsPlayed());
                holder.winCountTV.setText(mContext.getResources().getString(R.string.games_won) + ": " + stats.getTotalSessionsWon());

            }
        } else {
        }

        return convertView;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = ((truncated / 10d) != (truncated / 10));
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    @Override
    public long getHeaderId(int position) {
        if (champions != null) {
            ChampionStatsDto champion = champions.get(position);
            if (champion != null) {
                int id = champion.getId();
                return Long.valueOf(id);
            }
        }
        return 0;
    }

    class HeaderViewHolder {
        FadeInNetworkImageView champIV;
        TextView kdaTV, goldTV, gamesPlayedTV, winCountTV;
    }


    static class ViewHolder {
        FadeInNetworkImageView champIV;
    }
}
