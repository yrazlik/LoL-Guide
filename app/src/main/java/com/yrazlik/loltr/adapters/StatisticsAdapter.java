package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.Statistics;
import com.yrazlik.loltr.view.RobotoTextView;

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
            holder.row = (LinearLayout) convertView.findViewById(R.id.row);
            holder.name = (RobotoTextView) convertView.findViewById(R.id.name);
            holder.value = (RobotoTextView) convertView.findViewById(R.id.value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position%2 != 0){
            holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.statistics_row_dark_color));
        }else{
            holder.row.setBackgroundColor(mContext.getResources().getColor(R.color.statistics_row_light_color));
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
            if(name.equalsIgnoreCase(mContext.getResources().getString(R.string.kill_death))
                    || name.equalsIgnoreCase(mContext.getResources().getString(R.string.earnings))
                    || name.equalsIgnoreCase(mContext.getResources().getString(R.string.damagePart))) {
                holder.name.setTextColor(mContext.getResources().getColor(R.color.material_yellow));
            } else {
                holder.name.setTextColor(mContext.getResources().getColor(R.color.white));
            }
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
        public RobotoTextView name;
        public RobotoTextView value;
        public LinearLayout row;
    }
}
