package com.sien.aimanager.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sien.aimanager.R;
import com.sien.aimanager.control.ShakeListener;
import com.sien.lib.baseapp.activity.CPBaseActivity;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.utils.RepeatClickUtil;

/**
 * @author sien
 * @date 2016/9/8
 * @descript 关于
 */
public class AboutActivity extends CPBaseActivity {

    private ImageView icon;

    /** 摇动回调监听器 */
    private ShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        mShakeListener = new ShakeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mShakeListener.setOnShakeListener(onShakeListener);
        mShakeListener.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShakeListener.unregister();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mShakeListener.unregister();
    }

    @Override
    protected void onDestroy() {
        mShakeListener.unregister();
        super.onDestroy();

    }

    public void initViews(){
        getSupportActionBar().setTitle(R.string.about_title);

        icon = findView(R.id.about_icon);

        findView(R.id.layout_website).setOnClickListener(clickListener);
        findView(R.id.layout_serve_tel).setOnClickListener(clickListener);
        findView(R.id.layout_serve_email).setOnClickListener(clickListener);
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.layout_website){
                if (!RepeatClickUtil.isRepeatClick()) {
                    gotoWebsite();
                }
            }else if (vid == R.id.layout_serve_tel){
                if (!RepeatClickUtil.isRepeatClick()) {
                    doCall(getString(R.string.about_server_tel));
                }
            }else if (vid == R.id.layout_serve_email){
                if (!RepeatClickUtil.isRepeatClick()) {
                    duplicateEmail();
                }
            }
        }
    };

    /**
     * 进入拨号界面，不需要申请权限
     * @param phoneNum
     */
    public void doCall(String phoneNum){
        Intent intent = new Intent();
//        new Intent(Intent.ACTION_DIAL);
        intent.setAction(Intent.ACTION_VIEW);
        //需要拨打的号码
        intent.setData(Uri.parse("tel:"+phoneNum));
        startActivity(intent);
    }

    /*复制邮箱地址*/
    private void duplicateEmail(){
        ClipboardManager mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", getString(R.string.about_email));
        mClipboard.setPrimaryClip(clip);

        showToast("已复制到剪切板");
    }

    private void gotoWebsite(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://"+getString(R.string.about_website)));
        startActivity(intent);
    }

    //摇一摇监听类
    private ShakeListener.OnShakeListener onShakeListener = new ShakeListener.OnShakeListener()
    {
        @Override
        public void onShakeStart()
        {
            //do nothing
        }

        @Override
        public void onShakeFinish()
        {
            startActivity(new Intent(AboutActivity.this, MenuSettingActivity.class));
            finish();
        }
    };
}
