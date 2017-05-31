package com.example.gtk.platform.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.gtk.platform.R;
import com.example.gtk.platform.adapter.NormalPagerAdapter;
import com.example.gtk.platform.transformer.DepthPageTransformer;
import com.example.gtk.platform.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {
    private String Tag = "gtk.test";
    private ViewPager mViewPager;
    private GestureDetector gestureDetector;
    private int currentItem;
    private  int flaggingWidth;
    private int[] mImgIds = new int[] {R.drawable.hpw1,
            R.drawable.hpw2, R.drawable.hpw3, R.drawable.hpw4};
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        initData();
        flaggingWidth  = DisplayUtils.dip2px(WelcomeActivity.this, 20);
        gestureDetector = new GestureDetector(this, new GuideViewTouch());


        mViewPager = (ViewPager)findViewById(R.id.normalviewpager);
        mViewPager.setAdapter(new NormalPagerAdapter(WelcomeActivity.this, mImageViews));
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem= position;
                Log.i(Tag, currentItem + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gestureDetector.onTouchEvent(ev)) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initData() {
        for (int imgId : mImgIds) {
            ImageView imageView = new ImageView(WelcomeActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            mImageViews.add(imageView);
        }
    }

    private class GuideViewTouch extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX,float velocityY) {
            Log.i(Tag,"entered");
            if (currentItem == mImageViews.size() - 1) {
                if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
                        - e2.getY()) && (e1.getX() - e2.getX() <= (-flaggingWidth)
                        || e1.getX() - e2.getX() >= flaggingWidth)) {
                    if (e1.getX() - e2.getX() >= flaggingWidth) {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
