package com.widget.lib.viewpager;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * View的adapter
 *
 * @author zhaoxl
 * @date 19/5/22
 */
public abstract class RecyclerPagerAdapter<T> extends PagerAdapter<RecyclerPagerAdapter.ViewHolder> {
    private static final String TAG = "RecyclerPagerAdapter";

    private List<T> mData;

    public void setData(List<T> data) {
        mData = data == null ? new ArrayList<>() : data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = instantiateItem(parent, viewType);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        convert(viewHolder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 初始化itemView
     *
     * @param parent   {@link ViewGroup}
     * @param viewType item类型
     * @return itemView
     */
    public abstract View instantiateItem(@NonNull ViewGroup parent, int viewType);

    /**
     * 设置对应数据到view
     *
     * @param viewHolder {@link ViewHolder}
     * @param item       当前position对应的数据
     */
    public abstract void convert(@NonNull ViewHolder viewHolder, T item);

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final SparseArray<View> views;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.views = new SparseArray<>();
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(@IdRes int viewId) {
            View view = views.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) view;
        }
    }
}
