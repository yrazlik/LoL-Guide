package com.yrazlik.loltr.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yrazlik on 27/04/16.
 */
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private final int mVerticalSpaceHeight;

    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = mVerticalSpaceHeight;
        }
    }

}
