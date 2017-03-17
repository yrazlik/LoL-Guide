package com.yrazlik.loltr.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yrazlik.loltr.R;

/**
 * Created by yrazlik on 30/06/16.
 */
public class ConnectionProgressController {

    private Context context;
    private RelativeLayout parentView;
    private View progressConnectionView;
    private RelativeLayout progressBackgroundPanel;
    private ConnectionProgressListener connectionProgressListener;
    private View loadingView1, loadingView2;

    private boolean isServiceFail = false;
    private boolean shouldProgressWrapView = false;
    private boolean isCancelable = true;

    public ConnectionProgressController(Context context, RelativeLayout parentView,boolean shouldProgressWrapView, ConnectionProgressListener connectionProgressListener){
        this.context = context;
        this.parentView = parentView;
        this.connectionProgressListener = connectionProgressListener;
        this.shouldProgressWrapView = shouldProgressWrapView;
    }

    public void setCancelable(boolean cancelable) {
        isCancelable = cancelable;
    }

    public void showProgress(){

        if(parentView != null) {
            if (progressConnectionView == null)
                initializeView();

            hideProgress(false);
            changeVisibilityOfProgressPanel(true);

            parentView.addView(progressConnectionView);

        }

    }

    public void hideProgress(boolean isServiceFail){

        this.isServiceFail = isServiceFail;

        if(isServiceFail && shouldProgressWrapView){
            changeVisibilityOfProgressPanel(false);
        }else {
            if (parentView != null && progressConnectionView != null) {
                parentView.removeView(progressConnectionView);
            }
        }

    }

    private void initializeView(){

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        progressConnectionView = layoutInflater.inflate(R.layout.progress_connection_view, null);
        loadingView1 = progressConnectionView.findViewById(R.id.loadingView1);
        loadingView2 = progressConnectionView.findViewById(R.id.loadingView2);
        progressBackgroundPanel = (RelativeLayout)progressConnectionView.findViewById(R.id.progressBackgroundPanel);

        progressConnectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!shouldProgressWrapView){
                    if(isCancelable) {
                        hideProgress(false);
                        if (connectionProgressListener != null)
                            connectionProgressListener.onCancelProgressClicked();
                    }
                }
            }
        });

        changeVisibilityOfProgressPanel(true);

    }

    private void changeVisibilityOfProgressPanel(boolean visible){

        if(visible){

            if(shouldProgressWrapView) {
                progressBackgroundPanel.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                loadingView1.setVisibility(View.VISIBLE);
                loadingView2.setVisibility(View.GONE);
            }else {
                progressBackgroundPanel.setBackgroundColor(0);
                loadingView1.setVisibility(View.GONE);
                loadingView2.setVisibility(View.VISIBLE);
            }

        }else{

            loadingView1.setVisibility(View.GONE);
            loadingView2.setVisibility(View.GONE);

        }

    }

}
