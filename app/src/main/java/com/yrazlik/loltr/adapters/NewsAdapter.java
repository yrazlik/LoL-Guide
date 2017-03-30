package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.commons.Commons;
import com.yrazlik.loltr.data.News;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.List;

/**
 * Created by yrazlik on 12/28/15.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private Context mContext;
    private int resourceId;
    private DisplayImageOptions mOptions;

    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resourceId = resource;
        mOptions = new DisplayImageOptions.Builder().resetViewBeforeLoading(true).showImageOnLoading(R.drawable.white_bg).showImageForEmptyUri(R.drawable.white_bg)
                .showImageOnFail(R.drawable.white_bg).cacheOnDisk(true).cacheInMemory(true)
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);

            holder = new ViewHolder();
            holder.smallImage = (ImageView) convertView.findViewById(R.id.newsIV);
            holder.rightArrow = (ImageView) convertView.findViewById(R.id.rightArrow);
            holder.title = (RobotoTextView) convertView.findViewById(R.id.newsTitleTV);
            holder.shortDescTV = (RobotoTextView) convertView.findViewById(R.id.shortDescTV);
            holder.txtSeeMore = (RobotoTextView) convertView.findViewById(R.id.txtSeeMore);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        News news = getItem(position);

        LolImageLoader.getInstance().loadImage(news.getImg(), holder.smallImage, mOptions);
        setTitle(holder, news);
        setShortDesc(holder, news);

        return convertView;

    }

    private void setTitle(ViewHolder holder, News news) {
        String title = news.getTitle();
        if (title != null && title.length() > 0) {
            holder.title.setText(title);
        } else {
            holder.title.setText("???");
        }
    }

    private void setShortDesc(ViewHolder holder, News news) {
        String shortDesc = news.getShortDesc();
        if (shortDesc != null && shortDesc.length() > 0) {
            holder.shortDescTV.setText(shortDesc);
        } else {
            holder.shortDescTV.setText("...");
        }
    }

    static class ViewHolder {
        public ImageView smallImage;
        public ImageView rightArrow;
        public RobotoTextView title;
        private RobotoTextView shortDescTV;
        private RobotoTextView txtSeeMore;
    }
}
