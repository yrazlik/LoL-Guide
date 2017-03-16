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
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.List;

public class WeeklyFreeChampionsAdapter extends ArrayAdapter<Champion> {
	
	private Context mContext;

	public WeeklyFreeChampionsAdapter(Context context, int resource, List<Champion> objects) {
		super(context, resource, objects);
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_row_weeklyfreechampions, parent, false);
			
			holder = new ViewHolder();
			holder.championImage = (ImageView)convertView.findViewById(R.id.imageViewChampionImage);
			holder.championName = (RobotoTextView)convertView.findViewById(R.id.textViewChampionName);
			holder.dateInterval = (RobotoTextView)convertView.findViewById(R.id.textViewDateInterval);
			holder.rpIcon = (ImageView)convertView.findViewById(R.id.imageViewRp);
			holder.ipIcon = (ImageView)convertView.findViewById(R.id.imageViewIp);
			holder.rpPrice = (RobotoTextView)convertView.findViewById(R.id.textViewRp);
			holder.ipPrice = (RobotoTextView)convertView.findViewById(R.id.textViewIp);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Champion champion = getItem(position);

        setChampionName(champion, holder);
        LolImageLoader.getInstance().loadImage(champion.getChampionImageUrl(), holder.championImage);
        setFreeDateInterval(champion, holder);
        setIpPrices(champion, holder);
        setRpPrices(champion, holder);

		return convertView;
	}

    private void setChampionName(Champion champion, ViewHolder holder) {
        holder.championName.setText(champion.getChampionName());
    }

    private void setFreeDateInterval(Champion champion, ViewHolder holder) {
        holder.dateInterval.setText(champion.getDateInterval());
    }

    private void setIpPrices(Champion champion, ViewHolder holder) {
        if(champion.getChampionRp() != null && champion.getChampionRp().length() > 0){
            holder.rpPrice.setText(champion.getChampionRp());
        } else {
            holder.rpPrice.setText("");
        }
    }

    private void setRpPrices(Champion champion, ViewHolder holder) {
        if(champion.getChampionIp() != null && champion.getChampionIp().length() > 0){
            holder.ipPrice.setText(champion.getChampionIp());
        } else {
            holder.ipPrice.setText("");
        }
    }

	static class ViewHolder {
		public ImageView championImage;
		public RobotoTextView championName;
		public RobotoTextView dateInterval;
		public ImageView rpIcon;
		public ImageView ipIcon;
		public RobotoTextView rpPrice;
		public RobotoTextView ipPrice;
	}
}
