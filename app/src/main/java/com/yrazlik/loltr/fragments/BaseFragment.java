package com.yrazlik.loltr.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by yrazlik on 8/4/15.
 */
public abstract class BaseFragment extends Fragment{


    protected View rootView;

    public abstract void reportGoogleAnalytics();

}
