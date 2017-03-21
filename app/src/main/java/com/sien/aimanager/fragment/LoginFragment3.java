package com.sien.aimanager.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.activity.LoginActivity;
import com.sien.aimanager.control.WeLoginHelper;
import com.sien.aimanager.model.ICheckUserViewModel;
import com.sien.aimanager.presenter.CheckUserPresenter;
import com.sien.lib.baseapp.beans.APPOperateEventBundle;
import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.baseapp.utils.CPScreenUtil;
import com.sien.lib.baseapp.utils.RepeatClickUtil;
import com.sien.lib.component.countdown.DownTimer;
import com.sien.lib.component.countdown.DownTimerListener;
import com.sien.lib.component.edittext.CPPwdEditText;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.utils.EventPostUtil;
//import com.umeng.analytics.MobclickAgent;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 登录fragment第三步
 */
public class LoginFragment3 implements ICheckUserViewModel {
    public static final String TAG = "LoginFragment3";
    private CheckUserPresenter presenter;
    private DownTimer downTimer;

    private Button checkCodeBtn;
    private CPPwdEditText codeET;
    private TextView mobileTV;

    private String mobile = "";

    private LoginActivity context;
    private View rootView;
    private ImageView iv_blur;
    private TextView tv_tips;
    private View ll_change;
    private View iv_logo;

    public LoginFragment3(LoginActivity context) {
        this.context = context;
        onCreateView();
        onViewCreated();
    }

    public void onCreateView() {
        rootView = context.findView(R.id.login3);
    }

    public void onViewCreated() {

        initViews();
        initial();
        mobile = WeLoginHelper.mobile;//BaseApplication.getSharePerfence("loginMobile");
        if (mobileTV != null) {
            mobileTV.setText("发送至 " + mobile);
        }
    }

    public boolean isShowing() {
        return context.findViewById(R.id.login3).getVisibility() == View.VISIBLE;
    }

    public void onDestroy() {
        if (downTimer != null) {
            downTimer.stopDown();
            downTimer.setListener(null);
        }
        downTimer = null;

        if (presenter != null){
            presenter.releaseMemory();
            presenter.destory();
        }
        presenter = null;
    }

    public void initViews() {
        checkCodeBtn = context.findView(R.id.login_resend);
        iv_logo = context.findViewById(R.id.iv_login3_logo);
        tv_tips = context.findView(R.id.tv_login3_tips);
        ll_change = context.findView(R.id.ll_login3_change);

        mobile = WeLoginHelper.mobile;//BaseApplication.getSharePerfence("loginMobile");
        mobileTV = context.findView(R.id.login_sendto);
        mobileTV.setText("发送至 " + mobile);

        context.findView(R.id.login_chgmobile).setOnClickListener(clickListener);
        checkCodeBtn.setOnClickListener(clickListener);

        codeET = context.findView(R.id.login_checkcode);
        codeET.initStyle(R.color.tab_text_color, 6, 15, android.R.color.transparent, android.R.color.black, 30, android.R.color.transparent, android.R.color.white);
        codeET.setShowPwd(false);
        codeET.setOnTextFinishListener(
                new CPPwdEditText.OnTextFinishListener() {
                    @Override
                    public void onFinish(String str) {
                        doLoginAction(str);
                    }
                });

        context.findViewById(R.id.btn_login3_close).setOnClickListener(clickListener);

        iv_blur = context.findView(R.id.iv_login2_blur);
    }

    public void initial() {
        presenter = createPresenter();

        downTimer = new DownTimer();
        downTimer.setListener(downTimerListener);

        //验证码倒计时
        downTimer.startDown(90 * 1000);

        codeET.clearText();
    }

    public void show() {
        rootView.setVisibility(View.VISIBLE);
        final long duration = 300l;
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(iv_logo, "scaleX", 100f / 55f, 1.0f),
                ObjectAnimator.ofFloat(iv_logo, "scaleY", 100f / 55f, 1.0f),
                ObjectAnimator.ofFloat(iv_logo, "translationY", CPScreenUtil.dip2px(context, 72 - 7), 0));
        set.setDuration(duration);
        set.start();
        ObjectAnimator.ofFloat(checkCodeBtn, "alpha", 0f, 1.0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(codeET, "alpha", 0f, 1.0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(ll_change, "alpha", 0f, 1.0f).setDuration(duration).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_tips.setVisibility(View.VISIBLE);
                ObjectAnimator.ofFloat(tv_tips, "alpha", 0f, 1.0f).setDuration(duration).start();
            }
        }, duration);
        codeET.getEditText().requestFocus();
        codeET.getEditText().post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(codeET.getEditText(), 0);
            }
        });

        //cp.add 每次显示更新最新手机号
        mobile = WeLoginHelper.mobile;//BaseApplication.getSharePerfence("loginMobile");
        if (mobileTV != null) {
            mobileTV.setText("发送至 " + mobile);
        }
    }

    public void hide() {
        rootView.setVisibility(View.GONE);
        tv_tips.setVisibility(View.INVISIBLE);
        codeET.clearText();
    }

    /**
     * 重置登录帐号
     */
    private void resetMobile() {
        //停止倒计时
        stopTimeCount();

        codeET.clearText();

        doFragmentChange(1);
    }

    //fragment跳转
    private void doFragmentChange(int index) {
        APPOperateEventBundle bundle = new APPOperateEventBundle();
        bundle.putString(APPOperateEventBundle.EVENTACTION, "fragmentChange");
        bundle.putInt("fragmentIndex", index);
        EventPostUtil.post(new BaseAppEvents.APPOperateEvent(BaseAppEvents.STATUS_SUCCESS, bundle));
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.login_chgmobile) {
                if (!RepeatClickUtil.isRepeatClick()) {
                    resetMobile();
                }
            } else if (vid == R.id.btn_login3_close) {
                if (!RepeatClickUtil.isRepeatClick()) {
                    stopTimeCount();

                    //关闭登录页
                    doFragmentChange(-1);
                }

            } else if (vid == R.id.login_resend) {
                if (!RepeatClickUtil.isRepeatClick()) {
                    presenter.requestCheckCode(mobile);
                }
            }
        }
    };

    /*倒计时监听*/
    private DownTimerListener downTimerListener = new DownTimerListener() {
        @Override
        public void onTick(long millisUntilFinished) {
            if (checkCodeBtn != null) {
                checkCodeBtn.setText("重新获取验证码 （" + String.valueOf(millisUntilFinished / 1000) + "s）");
                checkCodeBtn.setEnabled(false);
            }
        }

        @Override
        public void onFinish() {
            resetSmsCodeRequestStatus(true);
        }
    };

    public CheckUserPresenter createPresenter() {
        return new CheckUserPresenter(context, this);
    }

    //重置验证码请求状态
    private void resetSmsCodeRequestStatus(boolean needStopCount) {
        if (checkCodeBtn != null) {
            checkCodeBtn.setText(R.string.personal_request_again);
            checkCodeBtn.setEnabled(true);
        }

        //停止计时
        if (needStopCount) {
            if (downTimer != null) {
                stopTimeCount();
            }
        }
    }

    /*停止倒计时*/
    private void stopTimeCount(){
        if (downTimer != null) {
            downTimer.stopDown();
        }
    }

    /*错误提示*/
    private void showTipPanel() {
        AlertDialog cancelDialog = new AlertDialog.Builder(context).setCancelable(false).setMessage(R.string.personal_checkcode_avalible).setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //清空验证码
                codeET.clearText();
            }
        }).create();
        cancelDialog.show();
    }

    /*登录*/
    private void doLoginAction(String value) {
        if (checkCodeBtn != null) {
            checkCodeBtn.setEnabled(true);
        }
        if (presenter != null) {
            presenter.requestLogin(mobile, value);
        }
    }

    @Override
    public void refreshLogin(RequestFreshStatus status) {
        //登录成功,保存登录状态，关闭登录页
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)) {
//            context.showToast("登录成功");

            //停止倒计时
            stopTimeCount();

            //统计登录类型为手机号登录
//            MobclickAgent.onProfileSignIn(CPConfiguration.userAccount);
        } else {
            showTipPanel();

            //登录不成功，停止计时，重新发送
//            resetSmsCodeRequestStatus();
        }
    }

    @Override
    public void refreshFillSMS(RequestFreshStatus status, String value) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)) {
            doLoginAction(value);
        }
    }

    @Override
    public void refreshMsgCode(RequestFreshStatus status) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)) {
            if (downTimer != null) {
                downTimer.stopDown();
                downTimer.startDown(90 * 1000);
            }
        }
    }
}
