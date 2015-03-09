package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
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
import com.yrazlik.loltr.data.Rune;

import java.util.List;

public class RuneAdapter extends ArrayAdapter<Rune> {
	
	private Context mContext;
    private AQuery aq;

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
			holder.runeImage = (ImageView)convertView.findViewById(R.id.imageViewChampionImage);
			holder.runeName = (TextView)convertView.findViewById(R.id.textViewChampionName);
			holder.runeSanitizedDescription = (TextView)convertView.findViewById(R.id.textViewDateInterval);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.imageProgress);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Rune rune = getItem(position);
		holder.runeName.setText(rune.getName());
        aq = new AQuery(holder.runeImage);
        aq.progress(holder.progress).image(Commons.RUNES_IMAGES_BASE_URL + rune.getImageUrl(), true, true);
		holder.runeSanitizedDescription.setText(rune.getSanitizedDescription());
		Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/dinproregular.ttf");
		holder.runeName.setTypeface(typeFace);
		holder.runeSanitizedDescription.setTypeface(typeFace);
		
		return convertView;
	}

	static class ViewHolder {
		public ImageView runeImage;
		public TextView runeName;
		public TextView runeSanitizedDescription;
        public ProgressBar progress;
	}

}
