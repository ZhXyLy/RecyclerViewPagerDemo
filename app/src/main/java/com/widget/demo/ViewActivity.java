package com.widget.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.widget.lib.viewpager.RecyclerPagerAdapter;
import com.widget.lib.viewpager.RecyclerViewPager;

import java.util.ArrayList;

/**
 * @author zhaoxl
 * @date 19/5/27
 */
public class ViewActivity extends AppCompatActivity {

    private RecyclerViewPager viewPager;
    private boolean isStart = false;
    private boolean isClockwise = true;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        viewPager = findViewById(R.id.view_pager);

        DemoViewAdapter demoViewAdapter = new DemoViewAdapter();
        viewPager.setAdapter(demoViewAdapter);


        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            strings.add("这是item：" + (i + 1));
        }
        demoViewAdapter.setData(strings);

        handler = new Handler(getMainLooper());
        runnable = () -> {
            int currentItem = viewPager.getCurrentItem();
            int itemCount = viewPager.getItemCount();
            if (isClockwise) {
                int position = currentItem + 1;
                if (position < itemCount - 1) {
                    viewPager.setCurrentItem(position, true);
                } else {
                    position = 0;
                    viewPager.setCurrentItem(position);
                }
            } else {
                int position = currentItem - 1;
                if (position > 0) {
                    viewPager.setCurrentItem(position, true);
                } else {
                    position = itemCount - 1;
                    viewPager.setCurrentItem(position);
                }
            }

            handler.postDelayed(runnable, 2000);
        };

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            isStart = !isStart;
            if (isStart) {
                handler.postDelayed(runnable, 500);
                btnStart.setText("停止动画");
            } else {
                handler.removeCallbacks(runnable);
                btnStart.setText("开启动画");
            }
        });

        Button btnOrientation = findViewById(R.id.btn_orientation);
        btnOrientation.setOnClickListener(v -> {
            int orientation = viewPager.getOrientation();
            if (orientation == RecyclerViewPager.HORIZONTAL) {
                orientation = RecyclerViewPager.VERTICAL;
                btnOrientation.setText("水平滚动");
            } else {
                orientation = RecyclerViewPager.HORIZONTAL;
                btnOrientation.setText("垂直滚动");
            }
            viewPager.setOrientation(orientation);
        });

        Button btnDirection = findViewById(R.id.btn_direction);
        btnDirection.setOnClickListener(v -> {
            isClockwise=!isClockwise;
            btnDirection.setText(isClockwise?"逆向":"正向");
        });

        Button btn2 = findViewById(R.id.btn_2);
        btn2.setOnClickListener(v -> {
            viewPager.setScrollVelocity(0.2f);
        });

        Button btn5 = findViewById(R.id.btn_5);
        btn5.setOnClickListener(v -> {
            viewPager.setScrollVelocity(0.5f);
        });

        Button btn10 = findViewById(R.id.btn_10);
        btn10.setOnClickListener(v -> {
            viewPager.setScrollVelocity(1f);
        });

        Button btn15 = findViewById(R.id.btn_15);
        btn15.setOnClickListener(v -> {
            viewPager.setScrollVelocity(1.5f);
        });
    }


    class DemoViewAdapter extends RecyclerPagerAdapter<String> {

        @Override
        public View instantiateItem(@NonNull ViewGroup parent, int viewType) {
            return LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);
        }

        @Override
        public void convert(@NonNull RecyclerPagerAdapter.ViewHolder viewHolder, String item) {
            TextView textView = viewHolder.getView(R.id.textView);
            textView.setText(item);
        }
    }
}
