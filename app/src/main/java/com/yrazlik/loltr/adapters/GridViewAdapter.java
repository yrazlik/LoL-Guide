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
import com.yrazlik.loltr.data.Champion;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<Champion> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<Champion> data = new ArrayList<Champion>();
	private ViewHolder holder = null;
    private AQuery aq;

	public GridViewAdapter(Context context, int layoutResourceId,
			ArrayList<Champion> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.championImage = (ImageView) row.findViewById(R.id.championImage);
			holder.championName = (TextView) row.findViewById(R.id.championName);
            holder.progress = (ProgressBar) row.findViewById(R.id.imageProgress);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		Champion champion = data.get(position);
		holder.championName.setText(champion.getChampionName());
        aq = new AQuery(holder.championImage);
        aq.progress(holder.progress).image(champion.getChampionImageUrl(), true, true);
		return row;
	}

	static class ViewHolder {
		ImageView championImage;
		TextView championName;
        ProgressBar progress;
	}
}