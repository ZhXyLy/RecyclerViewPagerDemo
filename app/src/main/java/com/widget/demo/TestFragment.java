package com.widget.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * @author zhaoxl
 * @date 19/5/17
 */
public class TestFragment extends Fragment {

    private static final String TAG = "TestFragment";
    private int[] colors = {
            Color.RED, Color.BLUE, Color.CYAN,
            Color.YELLOW, Color.MAGENTA, Color.GREEN
    };

    public static TestFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView textView = new TextView(getContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
        Random random = new Random();
        textView.setBackgroundColor(colors[random.nextInt(colors.length)]);
        if (getArguments() != null) {
            textView.setText(getArguments().getString("name"));
        } else {
            textView.setText("空页面");
        }
        Log.d(TAG, "onCreateView: " + textView.getText());
        return textView;
    }
}
