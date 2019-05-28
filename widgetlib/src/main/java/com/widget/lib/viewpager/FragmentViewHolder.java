package com.widget.lib.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author zhaoxl
 * @date 19/5/17
 */
class FragmentViewHolder extends RecyclerView.ViewHolder {
    Fragment currentFragment;

    static FragmentViewHolder createFragmentViewHolder(@NonNull ViewGroup parent) {
        //空的FrameLayout
        FrameLayout frameLayout = new FrameLayout(parent.getContext());
        frameLayout.setLayoutParams(
                new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.MATCH_PARENT));
        return new FragmentViewHolder(frameLayout);
    }

    private FragmentViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}