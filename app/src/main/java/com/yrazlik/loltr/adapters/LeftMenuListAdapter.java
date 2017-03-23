package com.yrazlik.loltr.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.data.LeftMenuItem;
import com.yrazlik.loltr.view.RobotoTextView;
import java.util.List;

/**
 * Created by yrazlik on 23/03/17.
 */

public class LeftMenuListAdapter extends ArrayAdapter<LeftMenuItem> {

    private Context mContext;
    private int mLayoutId;
    private List mMenuItems;

    private int selectedItemPosition;

    public LeftMenuListAdapter(Context context, int resource, List<LeftMenuItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mLayoutId = resource;
        this.mMenuItems = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutId, parent, false);

            holder = new ViewHolder();
            holder.parentRow = (RelativeLayout) convertView.findViewById(R.id.parentRow);
            holder.rowIcon = (ImageView) convertView.findViewById(R.id.rowIcon);
            holder.rowTitle = (RobotoTextView) convertView.findViewById(R.id.rowTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LeftMenuItem leftMenuItem = getItem(position);

        holder.rowTitle.setText(leftMenuItem.getTitle());
        holder.rowIcon.setBackgroundResource(leftMenuItem.getImage());

        if(position == selectedItemPosition) {
            holder.parentRow.setBackgroundResource(R.drawable.selector_selected_menu_item);
            holder.rowTitle.setTextColor(mContext.getResources().getColor(R.color.tab_color));
        } else {
            holder.parentRow.setBackgroundResource(R.drawable.selector_listview_material);
            holder.rowTitle.setTextColor(mContext.getResources().getColor(R.color.text_black));
        }

        return convertView;
    }

    static class ViewHolder {
        private RelativeLayout parentRow;
        public ImageView rowIcon;
        public RobotoTextView rowTitle;
    }

    public void setSelectedItem(int position) {
        selectedItemPosition = position;
    }
}
