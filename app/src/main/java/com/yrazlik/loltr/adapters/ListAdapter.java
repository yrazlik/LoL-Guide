package com.yrazlik.loltr.adapters;

import java.util.List;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.view.RemoteImageView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<Champion> {
	
	private Context mContext;

	public ListAdapter(Context context, int resource, List<Champion> objects) {
		super(context, resource, objects);
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_row, parent, false);
			
			holder = new ViewHolder();
			holder.championImage = (RemoteImageView)convertView.findViewById(R.id.imageViewChampionImage);
			holder.championName = (TextView)convertView.findViewById(R.id.textViewChampionName);
			holder.dateInterval = (TextView)convertView.findViewById(R.id.textViewDateInterval);
			holder.rpIcon = (ImageView)convertView.findViewById(R.id.imageViewRp);
			holder.ipIcon = (ImageView)convertView.findViewById(R.id.imageViewIp);
			holder.rpPrice = (TextView)convertView.findViewById(R.id.textViewRp);
			holder.ipPrice = (TextView)convertView.findViewById(R.id.textViewIp);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Champion champion = getItem(position);
		holder.championName.setText(champion.getChampionName());
		holder.championImage.setLocalURI(null);
		holder.championImage.setRemoteURI(champion.getChampionImageUrl());
		holder.championImage.loadImage(true);
		holder.dateInterval.setText(champion.getDateInterval());
		Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/dinproregular.ttf");
		holder.championName.setTypeface(typeFace);
		holder.dateInterval.setTypeface(typeFace);
		holder.rpPrice.setTypeface(typeFace);
		holder.ipPrice.setTypeface(typeFace);
		if(champion.getChampionRp() != null && champion.getChampionRp().length() > 0){
			holder.rpPrice.setText(champion.getChampionRp());
		}
		if(champion.getChampionIp() != null && champion.getChampionIp().length() > 0){
			holder.ipPrice.setText(champion.getChampionIp());
		}
		
		
		
		return convertView;

	}

	static class ViewHolder {
		public RemoteImageView championImage;
		public TextView championName;
		public TextView dateInterval;
		public ImageView rpIcon;
		public ImageView ipIcon;
		public TextView rpPrice;
		public TextView ipPrice;
	}

}
