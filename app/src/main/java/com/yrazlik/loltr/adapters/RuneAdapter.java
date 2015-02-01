package com.yrazlik.loltr.adapters;

import java.util.List;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Rune;
import com.yrazlik.loltr.view.RemoteImageView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RuneAdapter extends ArrayAdapter<Rune> {
	
	private Context mContext;

	public RuneAdapter(Context context, int resource, List<Rune> objects) {
		super(context, resource, objects);
		this.mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.rune_row, parent, false);
			
			holder = new ViewHolder();
			holder.runeImage = (RemoteImageView)convertView.findViewById(R.id.imageViewChampionImage);
			holder.runeName = (TextView)convertView.findViewById(R.id.textViewChampionName);
			holder.runeSanitizedDescription = (TextView)convertView.findViewById(R.id.textViewDateInterval);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Rune rune = getItem(position);
		holder.runeName.setText(rune.getName());
		holder.runeImage.setLocalURI(null);
		holder.runeImage.setRemoteURI(Commons.RUNES_IMAGES_BASE_URL + rune.getImageUrl());
		holder.runeImage.loadImage(true);
		holder.runeSanitizedDescription.setText(rune.getSanitizedDescription());
		Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/dinproregular.ttf");
		holder.runeName.setTypeface(typeFace);
		holder.runeSanitizedDescription.setTypeface(typeFace);
		
		return convertView;
	}

	static class ViewHolder {
		public RemoteImageView runeImage;
		public TextView runeName;
		public TextView runeSanitizedDescription;
	}

}
