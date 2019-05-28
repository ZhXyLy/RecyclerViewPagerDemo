package com.widget.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoxl
 * @date 19/5/22
 */
public class ViewPagerDemoActivity extends AppCompatActivity {
    private static final String TAG = "ViewPagerDemoActivity";
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;

    private boolean isAnimator = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
            }
        });

        TestAdapter testAdapter = new TestAdapter(getSupportFragmentManager());
        testAdapter.addFragment(TestFragment.newInstance("1"));
        testAdapter.addFragment(TestFragment.newInstance("2"));
        testAdapter.addFragment(TestFragment.newInstance("3"));
        viewPager.setAdapter(testAdapter);


        Button btnAnimator = findViewById(R.id.btn_animator);
        btnAnimator.setOnClickListener(v -> {
            isAnimator = !isAnimator;
            btnAnimator.setText(isAnimator ? "禁止动画" : "允许动画");
        });
    }

    class TestAdapter extends FragmentPagerAdapter {

        List<Fragment> fragments;

        public void addFragment(Fragment fragment){
            fragments.add(fragment);
        }

        public TestAdapter(FragmentManager fm) {
            super(fm);
            fragments=new ArrayList<>();
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
