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
import com.yrazlik.loltr.data.App;
import com.yrazlik.loltr.view.RobotoTextView;

import java.util.List;

/**
 * Created by yrazlik on 15/05/17.
 */

public class OtherAppsListAdapter extends ArrayAdapter<App> {

    private Context mContext;
    private List apps;

    public OtherAppsListAdapter(Context context, int resource, List<App> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.apps= objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_row_otherapps, parent, false);

            holder = new ViewHolder();
            holder.appImg = (CircularImageView)  convertView.findViewById(R.id.appImg);
            holder.appName = (RobotoTextView)convertView.findViewById(R.id.appName);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        App app = getItem(position);
        loadImage(holder, app);
        setAppName(holder, app);

        return convertView;
    }

    private void loadImage(ViewHolder holder, App app) {
        LolImageLoader.getInstance().loadImage(app.getImg(), holder.appImg);
    }

    private void setAppName(ViewHolder holder, App app) {
        holder.appName.setText(app.getName());
    }

    static class ViewHolder {
        public CircularImageView appImg;
        public RobotoTextView appName;
    }
}
