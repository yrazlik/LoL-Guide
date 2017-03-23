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
import com.yrazlik.loltr.data.Rune;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

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
			holder.runeImage = (ImageView)convertView.findViewById(R.id.imageViewRuneImage);
			holder.runeName = (RobotoTextView)convertView.findViewById(R.id.textViewRuneName);
			holder.runeSanitizedDescription = (RobotoTextView)convertView.findViewById(R.id.textViewDateInterval);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Rune rune = getItem(position);
		holder.runeName.setText(rune.getName());
        LolImageLoader.getInstance().loadImage(Commons.RUNES_IMAGES_BASE_URL + rune.getImageUrl(), holder.runeImage);
		holder.runeSanitizedDescription.setText(rune.getSanitizedDescription());
		
		return convertView;
	}

	static class ViewHolder {
		public ImageView runeImage;
		public RobotoTextView runeName;
		public RobotoTextView runeSanitizedDescription;
	}

}
