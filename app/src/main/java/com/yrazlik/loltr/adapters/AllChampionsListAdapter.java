package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Champion;

import java.util.List;

/**
 * Created by yrazlik on 8/5/15.
 */
public class AllChampionsListAdapter extends ArrayAdapter<Champion> {

    private Context mContext;
    private AQuery listAq;
    private Bitmap placeholder;

    public AllChampionsListAdapter(Context context, int resource, List<Champion> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listAq = new AQuery(this.mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_allchampions, parent, false);

            holder = new ViewHolder();
            holder.relativeLayoutTextContainer = (RelativeLayout)convertView.findViewById(R.id.relativeLayoutTextContainer);
            holder.championImage = (ImageView) convertView.findViewById(R.id.imageViewChampionImage);
            holder.rightArrow = (ImageView) convertView.findViewById(R.id.rightArrow);
            holder.championName = (TextView) convertView.findViewById(R.id.textViewChampionName);
            holder.champInfo = (TextView) convertView.findViewById(R.id.textViewChampInfo);
            convertView.setTag(holder);
            /*Animation animZoom = new ScaleAnimation(0, 1, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animZoom.setDuration(400);
            convertView.startAnimation(animZoom);*/
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Champion champion = getItem(position);
        holder.championName.setText(champion.getChampionName());


     //   placeholder = listAq.getCachedImage(R.drawable.contact);
        listAq.id(holder.championImage).image(champion.getChampionImageUrl(), false, true, 0, 0, null, android.R.anim.fade_in);
        //   holder.aq.recycle(holder.championImage).image(champion.getChampionImageUrl(), true, true, 0, 0, null, android.R.anim.fade_in);
      //  LolApplication.imageLoader.displayImage(champion.getChampionImageUrl(), holder.championImage);
        holder.champInfo.setText(champion.getTitle());
        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/dinproregular.ttf");
        holder.championName.setTypeface(typeFace);
        holder.champInfo.setTypeface(typeFace);


        return convertView;

    }

    static class ViewHolder {
        public RelativeLayout relativeLayoutTextContainer;
        public ImageView championImage;
        public ImageView rightArrow;
        public TextView championName;
        public TextView champInfo;
    }
}