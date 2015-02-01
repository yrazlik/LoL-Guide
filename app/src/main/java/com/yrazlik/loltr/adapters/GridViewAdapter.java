package com.yrazlik.loltr.adapters;

import java.util.ArrayList;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.view.RemoteImageView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter<Champion> {
	private Context context;
	private int layoutResourceId;
	private ArrayList<Champion> data = new ArrayList<Champion>();
	ViewHolder holder = null;

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
			holder.championImage = (RemoteImageView) row.findViewById(R.id.championImage);
			holder.championName = (TextView) row.findViewById(R.id.championName);
			
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		Champion champion = data.get(position);
		holder.championName.setText(champion.getChampionName());
		holder.championImage.setLocalURI(null);
		holder.championImage.setRemoteURI(champion.getChampionImageUrl());
		holder.championImage.loadImage();
		return row;
	}

	static class ViewHolder {
		RemoteImageView championImage;
		TextView championName;
	}
}