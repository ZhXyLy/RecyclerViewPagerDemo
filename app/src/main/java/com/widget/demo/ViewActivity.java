package com.widget.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private RecyclerViewPager viewPager, viewPager2;
    private boolean isStart = false;
    private boolean isClockwise = true;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        viewPager = findViewById(R.id.view_pager);
        viewPager2 = findViewById(R.id.view_pager2);

        DemoViewAdapter demoViewAdapter = new DemoViewAdapter();
        viewPager.setAdapter(demoViewAdapter);
        viewPager2.setAdapter(demoViewAdapter);
        int offset = getResources().getDimensionPixelSize(R.dimen.dp8);
        viewPager2.addItemDecoration(new GalleryItemDecoration(this, offset, offset, offset * 2));

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            strings.add("这是item：" + (i + 1));
        }
        //为了无限循环给开始加个最后的元素，给最后加个开始的元素
        String s = strings.get(0);
        String s1 = strings.get(strings.size() - 1);
        strings.add(0, s1);
        strings.add(s);
        demoViewAdapter.setData(strings);
        viewPager.setCurrentItem(1);
        viewPager.setOnPageChangeListener(new RecyclerViewPager.SimplePageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                //滚动开头的地方，直接切换到倒数第二个位置
                //滚动的结尾，直接切换到第二个位置
                if (position==0) {
                    viewPager.setCurrentItem(viewPager.getItemCount()-2);
                }else if (position == viewPager.getItemCount()-1){
                    viewPager.setCurrentItem(1);
                }
            }
        });
        handler = new Handler(Looper.getMainLooper());
        runnable = () -> {
            int currentItem = viewPager.getCurrentItem();
            int itemCount = viewPager.getItemCount();
            if (isClockwise) {
                int position = currentItem + 1;
                if (position < itemCount - 2) {
                    viewPager.setCurrentItem(position, true);
                } else {
                    viewPager.setCurrentItem(position, true);
                }
            } else {
                int position = currentItem - 1;
                if (position > 1) {
                    viewPager.setCurrentItem(position, true);
                } else {
                    viewPager.setCurrentItem(position, true);
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
            isClockwise = !isClockwise;
            btnDirection.setText(isClockwise ? "逆向" : "正向");
        });

        Button btn1 = findViewById(R.id.btn_150);
        btn1.setOnClickListener(v -> {
            viewPager.setDuration(150);
        });

        Button btn2 = findViewById(R.id.btn_500);
        btn2.setOnClickListener(v -> {
            viewPager.setDuration(500);
        });

        Button btn3 = findViewById(R.id.btn_800);
        btn3.setOnClickListener(v -> {
            viewPager.setDuration(800);
        });

        Button btn4 = findViewById(R.id.btn_1000);
        btn4.setOnClickListener(v -> {
            viewPager.setDuration(1000);
        });

        Button btn5 = findViewById(R.id.btn_1500);
        btn5.setOnClickListener(v -> {
            viewPager.setDuration(1500);
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
