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
import com.sien.lib.component.pwdlock.LockPatternCache;
import com.sien.lib.component.pwdlock.LockPatternUtil;
import com.sien.lib.component.pwdlock.LockPatternView;

import java.util.List;

/**
 * @author sien
 * @date 2016/9/8
 * @descript 手势锁登录
 */
public class GestureLoginActivity extends CPBaseBoostActivity {

    private static final String TAG = "LoginGestureActivity";

    LockPatternView lockPatternView;
    TextView messageTv;
    Button forgetGestureBtn;

    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;

    private LockPatternCache aCache;

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
                Intent intent = new Intent(GestureLoginActivity.this, CreateGestureActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initial() {
        super.initial();

        //得到当前用户的手势密码
//        try {
//            gesturePassword = CPSharedPreferenceManager.getInstance(this).getData(AppConfig.GESTURE_PASSWORD).getBytes("UTF-8");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        aCache = LockPatternCache.get(GestureLoginActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(AppConfig.GESTURE_PASSWORD);

        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);

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
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
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

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }
}
