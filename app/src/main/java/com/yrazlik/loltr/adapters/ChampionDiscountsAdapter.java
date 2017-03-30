package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.ChampionDiscount;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;

/**
 * Created by yrazlik on 12/25/15.
 */
public class ChampionDiscountsAdapter extends ArrayAdapter<ChampionDiscount>{

    private Context mContext;
    private ArrayList<ChampionDiscount> championDiscounts;
    private int resourceId;

    public ChampionDiscountsAdapter(Context context, int resource, ArrayList<ChampionDiscount> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.championDiscounts = objects;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_discount_champions, parent, false);

            holder = new ViewHolder();
            holder.discountImage = (CircularImageView) convertView.findViewById(R.id.discountImage);
            holder.discountName = (RobotoTextView)convertView.findViewById(R.id.name);
            holder.date = (RobotoTextView)convertView.findViewById(R.id.date);
            holder.priceBeforeDiscount = (RobotoTextView)convertView.findViewById(R.id.priceBeforeDiscount);
            holder.priceAfterDiscount = (RobotoTextView)convertView.findViewById(R.id.priceAfterDiscount);
            holder.rpImage = (ImageView)convertView.findViewById(R.id.rpImage);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ChampionDiscount championDiscount = getItem(position);

        if (championDiscount != null){
            if(championDiscount.getImageUrl() != null){
                LolImageLoader.getInstance().loadImage(championDiscount.getImageUrl(), holder.discountImage);
            }

            if(championDiscount.getName() != null){
                holder.discountName.setText(championDiscount.getName());
            }

            if (championDiscount.getStartDate() != null && championDiscount.getEndDate() != null){
                holder.date.setText(championDiscount.getStartDate() + " - " + championDiscount.getEndDate());
            }

            if(championDiscount.getPriceBeforeDiscount() != null && championDiscount.getPriceAfterDiscount() != null){
                holder.priceBeforeDiscount.setPaintFlags(holder.priceBeforeDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.rpImage.setVisibility(View.VISIBLE);
                holder.priceBeforeDiscount.setText(championDiscount.getPriceBeforeDiscount());
                holder.priceAfterDiscount.setText(championDiscount.getPriceAfterDiscount());
            }else{
                holder.rpImage.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        public CircularImageView discountImage;
        public RobotoTextView discountName;
        public RobotoTextView  date;
        public RobotoTextView  priceBeforeDiscount;
        public RobotoTextView  priceAfterDiscount;
        public ImageView rpImage;
    }
}
 