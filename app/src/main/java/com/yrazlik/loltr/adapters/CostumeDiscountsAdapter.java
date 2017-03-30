package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.ChampionDiscount;
import com.yrazlik.loltr.data.CostumeDiscount;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;

/**
 * Created by yrazlik on 12/25/15.
 */
public class CostumeDiscountsAdapter extends ArrayAdapter<CostumeDiscount>{

    private Context mContext;
    private ArrayList<CostumeDiscount> costumeDiscounts;
    private int resourceId;

    public CostumeDiscountsAdapter(Context context, int resource, ArrayList<CostumeDiscount> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.costumeDiscounts = objects;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_discount_costumes, parent, false);

            holder = new ViewHolder();
            holder.discountImage = (ImageView) convertView.findViewById(R.id.discountImage);
            holder.discountName = (RobotoTextView)convertView.findViewById(R.id.name);
            holder.date = (RobotoTextView)convertView.findViewById(R.id.date);
            holder.priceBeforeDiscount = (RobotoTextView)convertView.findViewById(R.id.priceBeforeDiscount);
            holder.priceAfterDiscount = (RobotoTextView)convertView.findViewById(R.id.priceAfterDiscount);
            holder.rpImage = (ImageView)convertView.findViewById(R.id.rpImage);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CostumeDiscount costumeDiscount = getItem(position);

        if (costumeDiscount != null){
            if(costumeDiscount.getImageUrl() != null){
                LolImageLoader.getInstance().loadImage(costumeDiscount.getImageUrl(),  holder.discountImage);
            }

            if(costumeDiscount.getName() != null){
                holder.discountName.setText(costumeDiscount.getName());
            }

            if (costumeDiscount.getStartDate() != null && costumeDiscount.getEndDate() != null){
                holder.date.setText(costumeDiscount.getStartDate() + " - " + costumeDiscount.getEndDate());
            }

            if(costumeDiscount.getPriceBeforeDiscount() != null && costumeDiscount.getPriceAfterDiscount() != null){
                holder.priceBeforeDiscount.setPaintFlags(holder.priceBeforeDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.rpImage.setVisibility(View.VISIBLE);
                holder.priceBeforeDiscount.setText(costumeDiscount.getPriceBeforeDiscount());
                holder.priceAfterDiscount.setText(costumeDiscount.getPriceAfterDiscount());
            }else{
                holder.rpImage.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        public ImageView discountImage;
        public RobotoTextView discountName;
        public RobotoTextView  date;
        public RobotoTextView  priceBeforeDiscount;
        public RobotoTextView  priceAfterDiscount;
        public ImageView rpImage;
    }
}
