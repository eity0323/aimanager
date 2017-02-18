package com.sien.aimanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.GuideAdapter;
import com.sien.lib.baseapp.activity.CPBaseActivity;
import com.sien.lib.baseapp.config.CPConfiguration;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.datapp.config.DatappConfig;
import com.sien.lib.datapp.control.CPSharedPreferenceManager;
import com.sien.lib.datapp.utils.CPDeviceUtil;

/**
 * @author sien
 * @date 2016/8/25
 * @descript 引导页
 */
public class GuideActivity extends CPBaseActivity {
    private static final int NUM_PAGES = 4; //引导页面数

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private LinearLayout circles; //索引指示
    private Button skipBtn;
    private boolean isOpaque = true;//透明主题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void onDestroy() {
        setFullScreen();
        super.onDestroy();
        if (pager != null){
            pager.clearOnPageChangeListeners();
        }
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        }else {
            pager.setCurrentItem(pager.getCurrentItem() -1);
        }
    }

    public void initViews(){
        skipBtn = findView(R.id.skipBtn);
        skipBtn.setOnClickListener(clickListener);

        pager = findView(R.id.pager);
        pagerAdapter = new GuideAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(pageChangeListener);

        //生成指示索引
        buildCircles();
    }

    @Override
    public <V extends BasePresenter> V createPresenter() {
        return null;
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if((position == 1 && positionOffset>0.6f) || position>1){//避免第二页划向第三页的时候，点与按钮重合
                circles.setVisibility(View.GONE);
            }else{
                circles.setVisibility(View.VISIBLE);
            }
            if (position == NUM_PAGES - 2 && positionOffset > 0){
                if (isOpaque){
                    pager.setBackgroundColor(Color.TRANSPARENT);
                    isOpaque = false;
                }
            }else {
                if (!isOpaque){
                    pager.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isOpaque = true;
                }
            }

        }

        @Override
        public void onPageSelected(int position) {
            setIndicator(position);
            if (position == NUM_PAGES -2){
                skipBtn.setVisibility(View.GONE);
            }else if (position < NUM_PAGES -2){
                skipBtn.setVisibility(View.VISIBLE);
            }else if (position == NUM_PAGES -1){
                endTutorial();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int vid = view.getId();
            if (vid == R.id.skipBtn){
                endTutorial();
            }
        }
    };

    /*结束引导*/
    public void endTutorial(){
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CPSharedPreferenceManager.getInstance(GuideActivity.this).saveData(CPSharedPreferenceManager.SETTINGS_SHOW_GUIDE+ CPDeviceUtil.getVersionName(GuideActivity.this),CPDeviceUtil.getVersionName(GuideActivity.this));

                go2MainActivity();

                finish();
            }
        },300);
    }

    private void go2MainActivity(){
        String needPwdLock = CPSharedPreferenceManager.getInstance(this).getData(DatappConfig.PWDLOCK_KEY);
        if (String.valueOf(CPConfiguration.USING_PASSWORD).equals(needPwdLock) && CPConfiguration.USING_PASSWORD){
            startActivity(new Intent(this, GestureLoginActivity.class));
        }else {
            startActivity(new Intent(this, MainAimObjectActivity.class));
        }
    }

    /*创建指示索引*/
    private void buildCircles(){
        circles = findView(R.id.circles);

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for (int i = 0;i < NUM_PAGES -1 ;i++){
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.shape_indicator_normal);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding,0,padding,0);
            circles.addView(circle);
        }

        setIndicator(0);
    }

    /*指定当前索引*/
    private void setIndicator(int index){
        if (index < NUM_PAGES){
            for (int i = 0;i<NUM_PAGES -1 ;i++){
                ImageView circle = (ImageView)circles.getChildAt(i);
                if(i == index){
                    circle.setImageResource(R.drawable.shape_indicator_selected);
                }else {
                    circle.setImageResource(R.drawable.shape_indicator_normal);
                }
            }
        }
    }

}
