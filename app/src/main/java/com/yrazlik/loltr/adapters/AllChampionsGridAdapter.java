package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Champion;
import com.yrazlik.loltr.model.ChampionDto;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.ArrayList;
import java.util.List;

public class AllChampionsGridAdapter extends ArrayAdapter<ChampionDto> {

    private Context context;
	private int layoutResourceId;
	private List<ChampionDto> data = new ArrayList<ChampionDto>();
	private ViewHolder holder = null;

	public AllChampionsGridAdapter(Context context, int layoutResourceId, List<ChampionDto> data) {
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
			holder.championImage = (CircularImageView) row.findViewById(R.id.championImage);
			holder.championName = (RobotoTextView) row.findViewById(R.id.championName);
			row.setTag(holder);
            Animation animZoom = new ScaleAnimation(0, 1, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animZoom.setDuration(400);
            row.startAnimation(animZoom);
		} else {
			holder = (ViewHolder) row.getTag();
		}

        ChampionDto champion = data.get(position);
		holder.championName.setText(champion.getName());
        LolImageLoader.getInstance().loadImage(champion.getImage().getFull(), holder.championImage);

		return row;
	}

	static class ViewHolder {
		CircularImageView championImage;
		RobotoTextView championName;
	}
}