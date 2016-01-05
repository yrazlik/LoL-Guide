package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.Gold;
import com.yrazlik.loltr.data.Image;
import com.yrazlik.loltr.data.Item;
import com.yrazlik.loltr.data.Data;

import java.util.ArrayList;

/**
 * Created by yrazlik on 8/6/15.
 */
public class ItemsAdapter extends ArrayAdapter<Item>{

    private Context mContext;
    private int layoutId;
    private ArrayList<Item> items;
    private AQuery aq;

    public ItemsAdapter(Context context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.layoutId = resource;
        this.items = objects;
    }

    @Override
    public int getCount() {
        if(items == null){
            return 0;
        }
        return items.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutId, parent, false);

            holder = new ViewHolder();
            holder.itemImage = (ImageView)convertView.findViewById(R.id.item_image);
            holder.textContainer = (RelativeLayout)convertView.findViewById(R.id.textContainer);
            holder.itemName = (TextView)convertView.findViewById(R.id.itemName);
            holder.shortDescription = (TextView)convertView.findViewById(R.id.shortDescription);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = getItem(position);
        Data itemData = item.getData();
        if(itemData != null) {
            String itemName = itemData.getName();
            if(itemName != null){
                holder.itemName.setText(itemName);
            }
            Gold gold = itemData.getGold();
            if(gold != null){
                int total = gold.getTotal();
                holder.shortDescription.setText(total + " " + mContext.getString(R.string.gold));
            }

            Image itemImage = itemData.getImage();
            if(itemImage != null){
                String imageUrl = itemImage.getFull();
                if(imageUrl != null){
                    imageUrl = Commons.ITEM_IMAGES_BASE_URL + imageUrl;
                  //  LolApplication.imageLoader.loadImage(imageUrl, null);
                    aq = new AQuery(holder.itemImage);
                    aq.image(imageUrl, true, true, 0, 0, null, android.R.anim.fade_in);
                }
            }


        }



        return convertView;
    }


    static class ViewHolder{
        public ImageView itemImage;
        public RelativeLayout textContainer;
        public TextView itemName;
        public TextView shortDescription;
    }
}
