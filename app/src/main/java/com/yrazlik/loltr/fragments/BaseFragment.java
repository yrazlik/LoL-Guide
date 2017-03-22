package com.yrazlik.loltr.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yrazlik.loltr.R;
import com.yrazlik.loltr.view.ConnectionProgressController;
import com.yrazlik.loltr.view.RobotoTextView;

/**
 * Created by yrazlik on 8/4/15.
 */
public abstract class BaseFragment extends Fragment{

 //   private ConnectionProgressController connectionProgressController;

    protected View rootView;

    public abstract void reportGoogleAnalytics();

   /* protected void showProgressFragmentBoundsWithWhiteBG(){
        if(connectionProgressController != null)
            connectionProgressController.hideProgress(false);
        connectionProgressController = new ConnectionProgressController(getActivity(), rootView, true, null);
        connectionProgressController.showProgress();
    }*/

    protected void showProgressWithWhiteBG() {
        if(rootView != null) {
            CardView loadingView = (CardView) rootView.findViewById(R.id.loadingView);
            if (loadingView != null) {
                loadingView.findViewById(R.id.retryContainer).setOnClickListener(null);
                loadingView.findViewById(R.id.imgRetry).setVisibility(View.GONE);
                loadingView.findViewById(R.id.progress).setVisibility(View.VISIBLE);
                ((RobotoTextView) loadingView.findViewById(R.id.loadingText)).setText(getString(R.string.loading));
                loadingView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void showRetryView() {
        if(rootView != null) {
            CardView loadingView = (CardView) rootView.findViewById(R.id.loadingView);
            if (loadingView != null) {
                loadingView.findViewById(R.id.imgRetry).setVisibility(View.VISIBLE);
                loadingView.findViewById(R.id.progress).setVisibility(View.GONE);
                ((RobotoTextView) loadingView.findViewById(R.id.loadingText)).setText(getString(R.string.retry));
                loadingView.findViewById(R.id.retryContainer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        retry();
                    }
                });
                loadingView.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void retry() {

    }

    protected void dismissProgress() {
        CardView loadingView = (CardView) rootView.findViewById(R.id.loadingView);
        if(loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }
}
