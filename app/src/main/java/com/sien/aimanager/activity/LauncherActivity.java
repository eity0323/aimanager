package com.sien.aimanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.sien.aimanager.R;
import com.sien.lib.datapp.control.CPSharedPreferenceManager;
import com.sien.lib.datapp.network.action.MainDatabaseAction;
import com.sien.lib.datapp.utils.CPDeviceUtil;

/**
 * @author sien
 * @date 2016/8/25
 * @descript 启动页
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        setTranslucentStatusBar();

        initDefaultData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                go2MainActivity();
            }
        }, 300);

    }

    private void initDefaultData(){
        String generateDefaultData = CPSharedPreferenceManager.getInstance(this).getData(CPSharedPreferenceManager.SETTINGS_DEFAULT_DATA);

        //初始创建系统默认数据
        if (TextUtils.isEmpty(generateDefaultData)) {
            MainDatabaseAction.createInitialAimType(this);
        }
    }

    protected void setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

            setFullScreen();
        }
    }

    protected void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void go2MainActivity() {
//        Small.openUri("main/home",LauncherActivity.this);

        String showGuide = CPSharedPreferenceManager.getInstance(this).getData(CPSharedPreferenceManager.SETTINGS_SHOW_GUIDE + CPDeviceUtil.getVersionName(this));

        if (TextUtils.isEmpty(showGuide)) {
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            startActivity(new Intent(this, AimTypeListActivity.class));
        }
        finish();
    }

}
