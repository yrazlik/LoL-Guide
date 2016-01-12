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
import com.yrazlik.loltr.data.Statistics;

import java.util.List;

/**
 * Created by yrazlik on 1/6/16.
 */
public class StatisticsAdapter extends ArrayAdapter<Statistics>{


    private Context mContext;
    private int resourceId;

    public StatisticsAdapter(Context context, int resource, List<Statistics> objects) {
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
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.value = (TextView) convertView.findViewById(R.id.value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Statistics statistics = getItem(position);
        String name = statistics.getName();
        String value = statistics.getValue();
        boolean isHeader = statistics.isHeader();

        if(isHeader){
            holder.name.setTypeface(null, Typeface.BOLD);
            holder.name.setPadding(0, 5, 0, 5);
        }else{
            holder.name.setTypeface(null, Typeface.NORMAL);
            holder.name.setPadding(0, 0, 0, 0);
        }

        if(name != null){
            holder.name.setText(name);
        }else {
            holder.name.setText("?????");
        }

        if(value != null){
            holder.value.setText(value);
        }else {
            holder.value.setText("?????");
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView name;
        public TextView value;
    }
}
