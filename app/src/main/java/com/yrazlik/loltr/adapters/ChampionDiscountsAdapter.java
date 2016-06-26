package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Discount;
import com.yrazlik.loltr.service.ServiceRequest;

import java.util.ArrayList;

/**
 * Created by yrazlik on 12/25/15.
 */
public class ChampionDiscountsAdapter extends ArrayAdapter<Discount>{

    private Context mContext;
    private ArrayList<Discount> discounts;
    private int resourceId;

    public ChampionDiscountsAdapter(Context context, int resource, ArrayList<Discount> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.discounts = objects;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_discount_champions, parent, false);

            holder = new ViewHolder();
            holder.discountImage = (NetworkImageView)convertView.findViewById(R.id.discountImage);
            holder.discountName = (TextView)convertView.findViewById(R.id.name);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.priceBeforeDiscount = (TextView)convertView.findViewById(R.id.priceBeforeDiscount);
            holder.priceAfterDiscount = (TextView)convertView.findViewById(R.id.priceAfterDiscount);
            holder.rpImage = (ImageView)convertView.findViewById(R.id.rpImage);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Discount discount = getItem(position);

        if (discount != null){
            if(discount.getImageUrl() != null){
                holder.discountImage.setImageUrl(discount.getImageUrl(), ServiceRequest.getInstance(mContext).getImageLoader());
            }

            if(Commons.SELECTED_LANGUAGE != null) {
                if (Commons.SELECTED_LANGUAGE.equalsIgnoreCase("tr")) {
                    if(discount.getName() != null){
                        holder.discountName.setText(discount.getName());
                    }
                } else if (Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                    if(discount.getNameEnglish() != null){
                        holder.discountName.setText(discount.getNameEnglish());
                    }
                }
            }else {
                if(discount.getNameEnglish() != null){
                    holder.discountName.setText(discount.getNameEnglish());
                }
            }

            if (discount.getStartDate() != null && discount.getEndDate() != null){
                holder.date.setText(discount.getStartDate() + " - " + discount.getEndDate());
            }

            if(discount.getPriceBeforeDiscount() != null && discount.getPriceAfterDiscount() != null){
                holder.priceBeforeDiscount.setPaintFlags(holder.priceBeforeDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.rpImage.setVisibility(View.VISIBLE);
                holder.priceBeforeDiscount.setText(discount.getPriceBeforeDiscount());
                holder.priceAfterDiscount.setText(discount.getPriceAfterDiscount());
            }else{
                holder.rpImage.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        public NetworkImageView discountImage;
        public TextView  discountName;
        public TextView  date;
        public TextView  priceBeforeDiscount;
        public TextView  priceAfterDiscount;
        public ImageView rpImage;
    }
}
 