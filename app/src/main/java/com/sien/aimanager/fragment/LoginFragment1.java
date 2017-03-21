package com.sien.aimanager.fragment;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.Button;

import com.sien.aimanager.R;
import com.sien.aimanager.activity.LoginActivity;
import com.sien.aimanager.control.WeLoginHelper;
import com.sien.lib.baseapp.beans.APPOperateEventBundle;
import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.baseapp.utils.CPScreenUtil;
import com.sien.lib.baseapp.utils.RepeatClickUtil;
import com.sien.lib.baseapp.utils.ToastUtil;
import com.sien.lib.databmob.utils.CPDeviceUtil;
import com.sien.lib.databmob.utils.EventPostUtil;
//import com.umeng.analytics.MobclickAgent;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 登录fragment第一步
 */
public class LoginFragment1 {
    public static final String TAG = "LoginFragment1";
    private View loginBtn;
    private Button wxloginBtn;

    private WeLoginHelper helper;

    private LoginActivity context;
    private View rootView;
    private View icon_phone;

    public LoginFragment1(LoginActivity context) {
        this.context = context;
        onCreateView();
        onViewCreated();
    }

    public void onCreateView() {
        rootView = context.findView(R.id.login1);
        icon_phone = context.findView(R.id.iv_login1_phone_icon);
    }

    public void onViewCreated() {
        initViews();
        initial();
    }

    public void show(boolean transition) {
        rootView.setVisibility(View.VISIBLE);
        if (!transition) {
            return;
        }
        long duration = 300l;
        ObjectAnimator.ofFloat(icon_phone, "translationX", (float) (CPScreenUtil.dip2px(context, 7 - 76)), 0).start();
        ObjectAnimator.ofFloat(loginBtn, "alpha", 0f, 1.0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(wxloginBtn, "alpha", 0f, 1.0f).setDuration(duration).start();
    }

    public void hide() {
        rootView.setVisibility(View.GONE);
    }

    public boolean isShowing() {
        return context.findViewById(R.id.login1).getVisibility() == View.VISIBLE;
    }

    public void onDestroy() {
        helper = null;
        context = null;
    }

    public void initViews() {
        loginBtn = context.findView(R.id.login_btn);
        wxloginBtn = context.findView(R.id.login_wx);
        context.findView(R.id.btn_login_close).setOnClickListener(clickListener);
    }

    public void initial() {
        loginBtn.setOnClickListener(clickListener);
        wxloginBtn.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.login_btn) {
//                if (!RepeatClickUtil.isRepeatClick()) {
//                }
                doFragmentChange(1);
            } else if (vid == R.id.login_wx) {
                //调用微信登录
                if (!RepeatClickUtil.isRepeatClick()) {
                    callWechatLogin();
                }
            } else if (vid == R.id.btn_login_close) {
                if (!RepeatClickUtil.isRepeatClick()) {

                    //统计“X”关闭按钮的点击
//                    MobclickAgent.onEvent(context,"062");

                    context.finish();
                }
            }
        }
    };

    /*调用微信登录*/
    private void callWechatLogin() {
        //检测是否安装微信应用
        if (!CPDeviceUtil.isWeixinAvilible(context)){
            ToastUtil.getInstance().showMessage(context,"抱歉，您未安装微信", ToastUtil.LENGTH_SHORT);
            return;
        }

        helper = new WeLoginHelper(context);
        helper.authLogin();
    }

    //fragment跳转
    private void doFragmentChange(int index) {
        APPOperateEventBundle bundle = new APPOperateEventBundle();
        bundle.putString(APPOperateEventBundle.EVENTACTION, "fragmentChange");
        bundle.putInt("fragmentIndex", index);
        EventPostUtil.post(new BaseAppEvents.APPOperateEvent(BaseAppEvents.STATUS_SUCCESS, bundle));
    }
}
