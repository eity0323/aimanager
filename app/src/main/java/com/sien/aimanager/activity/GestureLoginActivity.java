package com.sien.aimanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sien.aimanager.R;
import com.sien.aimanager.config.AppConfig;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.component.pwdlock.LockPatternUtil;
import com.sien.lib.component.pwdlock.LockPatternView;
import com.sien.lib.component.pwdlock.LockStatus;
import com.sien.lib.datapp.cache.disk.DiskLruCacheManager;

import java.util.List;

/**
 * @author sien
 * @date 2016/9/8
 * @descript 手势锁登录
 */
public class GestureLoginActivity extends CPBaseBoostActivity {

    private LockPatternView lockPatternView;
    private TextView messageTv;
    private Button forgetGestureBtn;

    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_login);
    }

    @Override
    public void initViews() {
        super.initViews();

        lockPatternView = findView(R.id.lockPatternView);
        messageTv = findView(R.id.messageTv);
        forgetGestureBtn = findView(R.id.forgetGestureBtn);

        forgetGestureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureLoginActivity.this, SettingGestureActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initial() {
        super.initial();

        //得到当前用户的手势密码
        try {
            DiskLruCacheManager diskLruCacheManager = new DiskLruCacheManager(this);
            gesturePassword = diskLruCacheManager.getAsBytes(AppConfig.GESTURE_PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
        }

        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(LockStatus.DEFAULT);

        showContentLayout();
    }

    @Override
    public <V extends BasePresenter> V createPresenter() {
        return null;
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(LockStatus.CORRECT);
                } else {
                    updateStatus(LockStatus.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(LockStatus status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        Toast.makeText(GestureLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainAimObjectActivity.class));
    }

}
