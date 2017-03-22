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

import com.android.volley.toolbox.NetworkImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.News;
import com.yrazlik.loltr.service.ServiceRequest;
import com.yrazlik.loltr.view.RobotoTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yrazlik on 12/28/15.
 */
public class NewsAdapter extends ArrayAdapter<News>{

    private Context mContext;
    private int resourceId;

    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();
            holder.relativeLayoutTextContainer = (RelativeLayout)convertView.findViewById(R.id.relativeLayoutTextContainer);
            holder.smallImage = (ImageView) convertView.findViewById(R.id.newsIV);
            holder.rightArrow = (ImageView) convertView.findViewById(R.id.rightArrow);
            holder.title = (RobotoTextView) convertView.findViewById(R.id.newsTitleTV);
            holder.date = (RobotoTextView) convertView.findViewById(R.id.dateTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News news = getItem(position);
        String title = news.getTitle();
        String titleEnglish = news.getTitleEnglish();
        String smallImageUrl = news.getSmallImage();
        Date createdAt = news.getCreatedAt();

        if(Commons.SELECTED_LANGUAGE != null) {
            if (Commons.SELECTED_LANGUAGE.equalsIgnoreCase("tr")) {
                if(title != null && title.length() > 0) {
                    holder.title.setText(title);
                }else{
                    holder.title.setText("");
                }
            } else if (Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                if(titleEnglish != null && titleEnglish.length() > 0) {
                    holder.title.setText(titleEnglish);
                }else{
                    holder.title.setText("");
                }
            }
        }else {
            if(titleEnglish != null && titleEnglish.length() > 0) {
                holder.title.setText(titleEnglish);
            }else{
                holder.title.setText("");
            }
        }

        String dateCreated = null;
        if(createdAt != null){
            try {
                String DATE_FORMAT_NOW = "yyyy-MM-dd";
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                dateCreated = sdf.format(date);
            }catch (Exception e){
                dateCreated = null;
            }
        }

        if(dateCreated != null){
            if(Commons.SELECTED_LANGUAGE != null) {
                if (Commons.SELECTED_LANGUAGE.equalsIgnoreCase("tr")) {
                    holder.date.setText(dateCreated + " " + mContext.getResources().getString(R.string.createdAt));
                } else if (Commons.SELECTED_LANGUAGE.equalsIgnoreCase("en_us")) {
                    holder.date.setText(mContext.getResources().getString(R.string.createdAt) + " " + dateCreated);
                } else{
                    holder.date.setText(mContext.getResources().getString(R.string.createdAt) + " " + dateCreated);
                }
            }else{
                holder.date.setText("");
            }
        }else {
            holder.date.setText("");
        }

        if(smallImageUrl != null){
            LolImageLoader.getInstance().loadImage(smallImageUrl, holder.smallImage);
        }

        return convertView;

    }

    static class ViewHolder {
        public RelativeLayout relativeLayoutTextContainer;
        public ImageView smallImage;
        public ImageView rightArrow;
        public RobotoTextView title;
        public RobotoTextView date;
    }
}
