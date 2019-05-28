package com.widget.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.widget.lib.viewpager.FragmentRecyclerPagerAdapter;
import com.widget.lib.viewpager.RecyclerViewPager;

/**
 * @author zhaoxl
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private RecyclerViewPager viewPager;
    private static final float MIN_SCALE = 0.85f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            switch (itemId) {
                case R.id.nav_home:
                    viewPager.setCurrentItem(0, isAnimator);
                    break;
                case R.id.nav_function:
                    viewPager.setCurrentItem(1, isAnimator);
                    break;
                case R.id.nav_personal:
                    viewPager.setCurrentItem(2, isAnimator);
                    break;
                default:
                    break;
            }
            return true;
        });


        viewPager = findViewById(R.id.view_pager);
        viewPager.setOnPageChangeListener(new RecyclerViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: " + position + "===" + positionOffset + "==" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
                bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(position).getItemId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "onPageScrollStateChanged: " + state);
            }
        });
        DemoFragmentRecyclerPagerAdapter adapter = new DemoFragmentRecyclerPagerAdapter(getSupportFragmentManager());
        adapter.setItemCount(3);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer((view, position) -> {
            Log.d(TAG, "transformPage: " + position + "===" + view);
            if (isTransformer) {
                float scaleFactor = Math.max(MIN_SCALE, position);
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setScaleX(1);
                view.setScaleY(1);
            }
        });

        Button btnOrientation = findViewById(R.id.btn_orientation);
        btnOrientation.setOnClickListener(v -> {
            if (viewPager.getOrientation() == RecyclerViewPager.HORIZONTAL) {
                viewPager.setOrientation(RecyclerViewPager.VERTICAL);
                btnOrientation.setText("水平滚动");
            } else {
                viewPager.setOrientation(RecyclerViewPager.HORIZONTAL);
                btnOrientation.setText("垂直滚动");
            }
        });
        Button btnAnimator = findViewById(R.id.btn_animator);
        btnAnimator.setOnClickListener(v -> {
            isAnimator = !isAnimator;
            btnAnimator.setText(isAnimator ? "禁止动画" : "允许动画");
        });
        Button btnScroll = findViewById(R.id.btn_scroll);
        btnScroll.setOnClickListener(v -> {
            viewPager.setScrollEnable(!viewPager.getScrollEnable());
            btnScroll.setText(viewPager.getScrollEnable() ? "禁止滚动" : "允许滚动");
        });
        Button btnTransformer = findViewById(R.id.btn_transformer);
        btnTransformer.setOnClickListener(v -> {
            isTransformer = !isTransformer;
            btnTransformer.setText(isTransformer ? "默认效果" : "缩放效果");
        });
        Button btnView = findViewById(R.id.btn_view);
        btnView.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewActivity.class));
        });
        Button btnVp = findViewById(R.id.btn_vp);
        btnVp.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewPagerDemoActivity.class));
        });
    }

    private boolean isAnimator = true;
    private boolean isTransformer = false;

    static class DemoFragmentRecyclerPagerAdapter extends FragmentRecyclerPagerAdapter {

        public DemoFragmentRecyclerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment createFragment(int position) {
            //创建position位置的Fragment
            return TestFragment.newInstance("这是fragment: " + position);
        }

        @Override
        protected String createFragmentTag(int viewId, int position) {
            return super.createFragmentTag(viewId, position);
        }
    }
}
