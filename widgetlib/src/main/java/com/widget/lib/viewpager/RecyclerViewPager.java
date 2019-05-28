package com.widget.lib.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * RecyclerView实现ViewPager效果
 *
 * @author zhaoxl
 * @date 19/5/22
 */
public class RecyclerViewPager extends RecyclerView {
    private static final String TAG = "RecyclerViewPager";
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private PagerSnapHelper mPagerSnapHelper;
    private LinearLayoutManager mLinearLayoutManager;
    private PagerAdapter mPagerAdapter;
    private OnPageChangeListener mOnPageChangeListener;
    private PageTransformer mPageTransformer;
    private int mCurPosition;
    private int mOrientation = HORIZONTAL;
    private int mOffset;
    private boolean mScrollEnable = true;
    /**
     * 速率，默认是1，<1：表示慢速，>1：表示速度比正常的快
     */
    private float scrollVelocity = 1;

    public RecyclerViewPager(Context context) {
        this(context, null);
    }

    public RecyclerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mLinearLayoutManager = new LinearLayoutManager(context) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
                LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        //重写此方法，改变smooth滚动的速度，返回值越大越慢
                        return 25.0F / scrollVelocity / (float) displayMetrics.densityDpi;
                    }
                };
                linearSmoothScroller.setTargetPosition(position);
                super.startSmoothScroll(linearSmoothScroller);
            }
        };
        mLinearLayoutManager.setOrientation(HORIZONTAL);
        setLayoutManager(mLinearLayoutManager);

        mPagerSnapHelper = new PagerSnapHelper();
        mPagerSnapHelper.attachToRecyclerView(this);

    }

    @Override
    public void onScrollStateChanged(int state) {

        onPageScrollStateChanged(state);

        //滚动停止后才判断选中哪个，由于fling的原因，可能会慢一点。
        if (state == SCROLL_STATE_IDLE) {
            int targetPosition = getTargetPosition();
            //如果滚动后，position有变化，更新并通知监听器选中位置
            if (mCurPosition != targetPosition) {
                mCurPosition = targetPosition;
                notifyPageSelected(mCurPosition);
            }

            mOffset = 0;
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            //scrollToPosition的偏移量是0，通知偏移因子是1，这样做动画可以恢复
            View view = mLinearLayoutManager.findViewByPosition(mCurPosition);
            if (view != null) {
                notifyTransformPage(view, 1);
            }
            return;
        }

        int baseWidth;
        if (mOrientation == HORIZONTAL) {
            mOffset += dx;
            baseWidth = getWidth();
        } else {
            mOffset += dy;
            baseWidth = getHeight();
        }

        float offsetRate = 1.0f * mOffset / baseWidth;

        if (Math.abs(offsetRate) > 1) {
            //超过一屏，做截取处理
            Log.d(TAG, "onScrolled超过一屏: offsetRate" + offsetRate + " = mOffset(" + mOffset + ") / (" + baseWidth + ")");
            if (mOffset > 0) {
                mCurPosition += 1;
                mOffset -= baseWidth;
            } else if (mOffset < 0) {
                mCurPosition -= 1;
                mOffset += baseWidth;
            }
            offsetRate = 1.0f * mOffset / baseWidth;
        }

        int scrollPosition = getScrollPosition(mOffset);

        notifyPageScrolled(scrollPosition, offsetRate, mOffset);

        View view = mLinearLayoutManager.findViewByPosition(mCurPosition);
        View nextView = mLinearLayoutManager.findViewByPosition(scrollPosition);

        //通知当前view的变化，和下个view的变化
        if (view != null) {
            notifyTransformPage(view, 1 - Math.abs(offsetRate));
        }

        if (mCurPosition == scrollPosition) {
            //如果又滚回来了就给他恢复
            if (nextView != null) {
                notifyTransformPage(nextView, 1);
            }
            return;
        }
        if (nextView != null) {
            notifyTransformPage(nextView, Math.abs(offsetRate));
        }

    }

    private void notifyTransformPage(View itemView, float position) {
        if (mPageTransformer != null) {
            mPageTransformer.transformPage(itemView, position);
        }
    }

    private void notifyPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    private void notifyPageSelected(int position) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }
    }

    private void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    private int getTargetPosition() {
        View snapView = mPagerSnapHelper.findSnapView(mLinearLayoutManager);
        if (snapView != null) {
            return mLinearLayoutManager.getPosition(snapView);
        }
        return getScrollPosition(0);
    }

    private int getScrollPosition(int offset) {
        int position;
        if (offset >= 0) {
            position = mLinearLayoutManager.findLastVisibleItemPosition();
        } else {
            position = mLinearLayoutManager.findFirstVisibleItemPosition();
        }
        position = Math.max(0, Math.min(mLinearLayoutManager.getItemCount() - 1, position));
        return position;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mScrollEnable && super.onInterceptTouchEvent(ev);
    }

    private float downX, downY;
    private boolean isScroll;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downX = x;
            downY = y;
            isScroll = false;
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (isScroll) {
                return true;
            }
            float offsetX = Math.abs(x - downX);
            float offsetY = Math.abs(y - downY);
            if (mOrientation == HORIZONTAL && offsetX < offsetY) {
                isScroll = true;
                return true;
            }
            if (mOrientation == VERTICAL && offsetX > offsetY) {
                isScroll = true;
                return true;
            }
        }
        return mScrollEnable && super.onTouchEvent(ev);
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.mScrollEnable = scrollEnable;
    }

    public boolean getScrollEnable() {
        return mScrollEnable;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
        mLinearLayoutManager.setOrientation(orientation);
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setAdapter(@Nullable PagerAdapter adapter) {
        super.setAdapter(adapter);
        this.mPagerAdapter = adapter;
    }

    /**
     * @deprecated 请使用{{@link #setAdapter(PagerAdapter)}
     */
    @Override
    @Deprecated
    public void setAdapter(@Nullable Adapter adapter) {
        if (!(adapter instanceof PagerAdapter)) {
            throw new IllegalArgumentException("需要PagerAdapter");
        }
        setAdapter(((PagerAdapter) adapter));
    }

    /**
     * @param velocity 速率 1表示正常速度，越大越快
     */
    public void setScrollVelocity(float velocity) {
        this.scrollVelocity = velocity;
    }

    public void setCurrentItem(int position) {
        setCurrentItem(position, false);
    }

    public void setCurrentItem(int position, boolean animate) {
        position = regulatePosition(position);

        if (animate) {
            smoothScrollToPosition(position);
        } else {
            if (mCurPosition != position) {
                mCurPosition = position;
            }
            scrollToPosition(position);
        }
    }

    private int regulatePosition(int position) {
        int itemCount = mPagerAdapter.getItemCount();
        if (itemCount > 0) {
            if (position < 0) {
                position = 0;
            } else if (position > itemCount - 1) {
                position = itemCount - 1;
            }
        }
        return position;
    }

    public int getCurrentItem() {
        return mCurPosition;
    }

    public int getItemCount() {
        return mPagerAdapter.getItemCount();
    }

    public void setOnPageChangeListener(@NonNull OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setPageTransformer(PageTransformer transformer) {
        this.mPageTransformer = transformer;
    }

    public interface OnPageChangeListener {

        /**
         * 页面滚动
         *
         * @param position             当前所在位置
         * @param positionOffset       偏移的比例
         * @param positionOffsetPixels 偏移的距离
         */
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        /**
         * 选中新页面
         *
         * @param position 新选择页面的位置索引
         */
        void onPageSelected(int position);

        /**
         * 滚动状态更改时调用
         *
         * @param state 新的状态
         * @see ViewPager#SCROLL_STATE_IDLE
         * @see ViewPager#SCROLL_STATE_DRAGGING
         * @see ViewPager#SCROLL_STATE_SETTLING
         */
        void onPageScrollStateChanged(int state);
    }

    public interface PageTransformer {
        /**
         * 仿ViewPager的{@link android.support.v4.view.ViewPager.PageTransformer#transformPage(View, float)}
         *
         * @param itemView 需要改变的view
         * @param position [0-1]的值
         */
        void transformPage(@NonNull View itemView, float position);
    }
}
