package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.view.RobotoTextView;

/**
 * Created by yrazlik on 31/03/17.
 */

public class RegionListAdapter extends ArrayAdapter<String>{

    private Context mContext;
    private int mLayoutResId;
    private String [] mRegions;

    public RegionListAdapter(Context context, int resource, String[] regions) {
        super(context, resource, regions);
        this.mContext= context;
        this.mRegions = regions;
        this.mLayoutResId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_text, parent, false);

            holder = new ViewHolder();
            holder.text = (RobotoTextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String regionText = getItem(position);
        holder.text.setText(regionText);

        return convertView;
    }

    static class ViewHolder {
        public RobotoTextView text;
    }
}
