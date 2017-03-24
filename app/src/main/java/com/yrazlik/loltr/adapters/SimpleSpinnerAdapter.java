package com.yrazlik.loltr.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yrazlik on 26/09/16.
 */
public class SimpleSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> items;
    private int textColor = R.color.text_black;

    public SimpleSpinnerAdapter(Context context, ArrayList<String> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_spinner_row, null);
            holder = new ViewHolder();
            holder.txtRow = (RobotoTextView) convertView.findViewById(R.id.txtRow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = items.get(position);
        holder.txtRow.setText(item);

        holder.txtRow.setTextColor(mContext.getResources().getColor(textColor));

        return convertView;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        RobotoTextView txtRow;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}