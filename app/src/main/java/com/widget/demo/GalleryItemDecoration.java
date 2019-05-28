package com.widget.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * @author zhaoxl
 * @date 19/3/7
 */
public class GalleryItemDecoration extends RecyclerView.ItemDecoration {

    private int mPageMargin = 0;//自定义默认item边距
    private int mTopMargin = 0;
    private int mBottomMargin = 0;
    private int mLeftPageVisibleWidth;//第一张图片的左边距
    private int mWidth;

    public GalleryItemDecoration(Context context, int pageMargin) {
        this.mPageMargin = pageMargin;
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mWidth = outMetrics.widthPixels - mPageMargin * 4;
        mLeftPageVisibleWidth = mPageMargin * 2;
    }

    public GalleryItemDecoration(Context context, int pageMargin, int topMargin, int bottomMargin) {
        this.mPageMargin = pageMargin;
        this.mTopMargin = topMargin;
        this.mBottomMargin = bottomMargin;
        WindowManager manager = ((Activity) context).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        mWidth = outMetrics.widthPixels - mPageMargin * 4;
        mLeftPageVisibleWidth = mPageMargin * 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int positon = parent.getChildAdapterPosition(view); //获得当前item的position
        int itemCount = parent.getAdapter().getItemCount(); //获得item的数量
        int leftMargin;
        if (positon == 0) {
            leftMargin = mLeftPageVisibleWidth;
        } else {
            leftMargin = mPageMargin / 2;
        }
        int rightMargin;
        if (positon == itemCount - 1) {
            rightMargin = mLeftPageVisibleWidth;
        } else {
            rightMargin = mPageMargin / 2;
        }
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.setMargins(leftMargin, mTopMargin, rightMargin, mBottomMargin);
        lp.width = mWidth;
        view.setLayoutParams(lp);
        super.getItemOffsets(outRect, view, parent, state);
    }

}
