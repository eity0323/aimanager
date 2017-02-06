package com.sien.lib.baseapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sien.lib.baseapp.R;

/**
 * @author sien
 * @date 2017/1/16
 * @descript 通用未找到插件页面
 */
public class CPNoFoundActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_no_page_found);
    }
}
