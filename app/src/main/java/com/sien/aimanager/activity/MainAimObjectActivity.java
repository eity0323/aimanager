package com.sien.aimanager.activity;

import android.os.Bundle;

import com.sien.aimanager.R;
import com.sien.aimanager.model.IMainAimObjectViewModel;
import com.sien.aimanager.presenter.MainAimObjectPresenter;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 主页（aimobject版）
 */
public class MainAimObjectActivity extends CPBaseBoostActivity implements IMainAimObjectViewModel {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_aimobject);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void initial() {
        super.initial();
    }

    @Override
    public MainAimObjectPresenter createPresenter() {
        return new MainAimObjectPresenter(this);
    }


}
