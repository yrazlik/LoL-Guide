package com.yrazlik.loltr.adapters;

import java.util.ArrayList;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Items;
import com.yrazlik.loltr.view.RemoteImageView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GridViewItemsAdapter extends ArrayAdapter<Items>{

	private Context context;
	private int layoutResourceId;
	private ArrayList<Items> data = new ArrayList<Items>();
	private ViewHolder holder;

	public GridViewItemsAdapter(Context context, int layoutResourceId,
			ArrayList<Items> data) {
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
			holder.itemImage = (RemoteImageView) row.findViewById(R.id.itemImage);
			
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		Items item = data.get(position);
		holder.itemImage.setLocalURI(null);
		holder.itemImage.setRemoteURI(Commons.ITEM_IMAGES_BASE_URL + String.valueOf(item.getId()) + ".png");
		holder.itemImage.loadImage();
		return row;
	}
	
	static class ViewHolder {
		RemoteImageView itemImage;
	}

}
