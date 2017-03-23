package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.pkmmte.view.CircularImageView;
import com.yrazlik.loltr.LolImageLoader;
import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Streams;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 1/10/15.
 */
public class LiveChannelsAdapter extends ArrayAdapter<Streams>{

    private Context mContext;

    public LiveChannelsAdapter(Context context, int resource, List<Streams> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_livechannel, parent,
                    false);

            holder = new ViewHolder();
            holder.channelImage = (CircularImageView) convertView.findViewById(R.id.imageViewChannelLogo);
            holder.channelTitle = (RobotoTextView) convertView.findViewById(R.id.textViewChannelName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Streams stream = getItem(position);
        holder.channelTitle.setText(stream.getChannel().getDisplay_name());
        LolImageLoader.getInstance().loadImage(stream.getChannel().getLogo(), holder.channelImage);

        return convertView;
    }

    static class ViewHolder {
        public CircularImageView channelImage;
        public RobotoTextView channelTitle;
    }
}
