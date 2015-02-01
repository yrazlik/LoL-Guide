package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Streams;
import com.yrazlik.loltr.view.RemoteImageView;

import java.util.List;

/**
 * Created by yrazlik on 1/10/15.
 */
public class LiveChannelsAdapter extends ArrayAdapter<Streams>{

    private Context mContext;
    private Typeface typeFace;

    public LiveChannelsAdapter(Context context, int resource,
                                     List<Streams> objects) {
        super(context, resource, objects);
        this.mContext = context;
        typeFace = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/dinproregular.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_livechannel, parent,
                    false);

            holder = new ViewHolder();
            holder.channelImage = (RemoteImageView) convertView
                    .findViewById(R.id.imageViewChannelLogo);
            holder.channelTitle = (TextView) convertView
                    .findViewById(R.id.textViewChannelName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Streams stream = getItem(position);
        holder.channelTitle.setText(stream.getChannel().getDisplay_name());
        holder.channelImage.setLocalURI(null);
        holder.channelImage.setRemoteURI(stream.getChannel().getLogo());
        holder.channelImage.loadImage();

        return convertView;

    }


    static class ViewHolder {
        public RemoteImageView channelImage;
        public TextView channelTitle;
    }


}
