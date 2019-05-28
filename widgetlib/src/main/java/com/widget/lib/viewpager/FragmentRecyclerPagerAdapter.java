package com.widget.lib.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;

/**
 * Fragment的adapter
 *
 * @author zhaoxl
 * @date 19/5/22
 */
public abstract class FragmentRecyclerPagerAdapter extends PagerAdapter<FragmentViewHolder> {

    private static final String TAG = FragmentRecyclerPagerAdapter.class.getSimpleName();

    private final FragmentManager mFm;
    private final int mContainerId;
    private int mItemCount;

    public FragmentRecyclerPagerAdapter(FragmentManager fm) {
        this.mFm = fm;
        mContainerId = ViewCompat.generateViewId();
    }

    /**
     * 设置fragment的数量
     *
     * @param itemCount 数量
     */
    public void setItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    @NonNull
    @Override
    public FragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return FragmentViewHolder.createFragmentViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentViewHolder viewHolder, int position) {
        viewHolder.itemView.setId(mContainerId + position);
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull FragmentViewHolder holder) {
        String tag = createFragmentTag(holder.itemView.getId(), holder.getAdapterPosition());
        Fragment fragment = mFm.findFragmentByTag(tag);

        if (fragment == null || fragment != holder.currentFragment) {
            FragmentTransaction fragmentTransaction = mFm.beginTransaction();

            if (fragment != null) {
                fragmentTransaction.show(fragment);
            } else {
                fragment = createFragment(holder.getAdapterPosition());
                fragmentTransaction.add(holder.itemView.getId(), fragment, tag);
            }
            fragmentTransaction.commitNowAllowingStateLoss();
            //隐藏当前的
            if (holder.currentFragment != null && holder.currentFragment.getUserVisibleHint()) {
                //用到menu的话很有用
                holder.currentFragment.setMenuVisibility(false);
                holder.currentFragment.setUserVisibleHint(false);
            }
            //赋值给currentFragment
            holder.currentFragment = fragment;
            if (!fragment.getUserVisibleHint()) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull FragmentViewHolder holder) {
        if (holder.currentFragment != null) {
            if (holder.currentFragment.getUserVisibleHint()) {
                holder.currentFragment.setMenuVisibility(false);
                holder.currentFragment.setUserVisibleHint(false);
            }
            FragmentTransaction fragmentTransaction = mFm.beginTransaction();
            fragmentTransaction.hide(holder.currentFragment);
            fragmentTransaction.commitNowAllowingStateLoss();
        }
        holder.currentFragment = null;
    }

    @Override
    public void onViewRecycled(@NonNull FragmentViewHolder holder) {
        if (holder.currentFragment != null) {
            if (holder.currentFragment.getUserVisibleHint()) {
                holder.currentFragment.setMenuVisibility(false);
                holder.currentFragment.setUserVisibleHint(false);
            }
            FragmentTransaction fragmentTransaction = mFm.beginTransaction();
            fragmentTransaction.detach(holder.currentFragment);
            fragmentTransaction.commitNowAllowingStateLoss();
        }
        holder.currentFragment = null;
    }

    /**
     * Fragment的tag，可以通过重写自定义tag
     *
     * @param viewId   itemView的id
     * @param position 位置
     * @return tag
     */
    protected String createFragmentTag(int viewId, int position) {
        return "FragmentRecyclerPagerAdapter:" + viewId + ":" + position;
    }

    /**
     * 创建{{@link android.support.v4.app.Fragment}}
     *
     * @param position 第几个位置
     * @return android.support.v4.app.Fragment
     */
    public abstract Fragment createFragment(int position);

}
