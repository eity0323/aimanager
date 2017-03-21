package com.sien.aimanager.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.sien.aimanager.R;
import com.sien.aimanager.activity.LoginActivity;
import com.sien.aimanager.control.WeLoginHelper;
import com.sien.aimanager.model.ICheckCodeViewModel;
import com.sien.aimanager.presenter.CheckCodePresenter;
import com.sien.lib.baseapp.beans.APPOperateEventBundle;
import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.baseapp.utils.CPScreenUtil;
import com.sien.lib.baseapp.utils.RepeatClickUtil;
import com.sien.lib.component.edittext.ClearEditText;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.utils.EventPostUtil;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 登录fragment第二步
 */
public class LoginFragment2 implements ICheckCodeViewModel {
    public static final String TAG = "LoginFragment2";
    private CheckCodePresenter presenter;

    private ClearEditText mobileET;
    private Button checkCodeBtn;
    private View btn_back;
    private LoginActivity context;
    private ImageView iv_blur;
    private ImageView icon_phone;
    private View inputBox;
    private View rootView;

    public LoginFragment2(LoginActivity context) {
        this.context = context;
        onCreateView();
        onViewCreated();
    }
    public void onCreateView() {
        rootView = context.findView(R.id.login2);
    }

    public void onViewCreated() {

        initViews();
        initial();
    }

    public void onDestory(){
        if (presenter != null){
            presenter.releaseMemory();
            presenter.destory();
        }
        presenter = null;
    }

    public void initViews() {
        mobileET = context.findView(R.id.login_mobile);
        mobileET.addTextChangedListener(textWatcher);
        mobileET.setPhoneNumberMode(true);
        inputBox = context.findView(R.id.ll_login2_input);
        btn_back = context.findView(R.id.login2_back);
        icon_phone = context.findView(R.id.iv_login2_phone_icon);

        checkCodeBtn = context.findView(R.id.login_next_step);
        checkCodeBtn.setOnClickListener(clickListener);
        checkCodeBtn.setEnabled(false);

        btn_back.setOnClickListener(clickListener);

        context.findViewById(R.id.btn_login2_close).setOnClickListener(clickListener);
        iv_blur = context.findView(R.id.iv_login2_blur);
    }
    public void show(boolean transition){
        rootView.setVisibility(View.VISIBLE);

        mobileET.requestFocus();
        mobileET.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(mobileET, 0);
            }
        });
        if(!transition){
            return;
        }

        long duration = 300l;
        ObjectAnimator.ofFloat(icon_phone,"translationX",(float)(CPScreenUtil.dip2px(context,76-7)),0).start();
        ObjectAnimator.ofFloat(iv_blur,"alpha",0f,1.0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(checkCodeBtn,"alpha",0f,1.0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(btn_back,"alpha",0f,1.0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(inputBox,"alpha",0f,1.0f).setDuration(duration).start();

    }
    public void hide(boolean transition){
        if(!transition){
            rootView.setVisibility(View.GONE);
            return;
        }
        long duration = 300l;
        ObjectAnimator.ofFloat(icon_phone,"translationX",0,(float)(CPScreenUtil.dip2px(context,76-7))).start();
        ObjectAnimator.ofFloat(iv_blur,"alpha",1.0f,0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(checkCodeBtn,"alpha",1.0f,0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(btn_back,"alpha",1.0f,0f).setDuration(duration).start();
        ObjectAnimator.ofFloat(inputBox,"alpha",1.0f,0f).setDuration(duration).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rootView.setVisibility(View.GONE);
            }
        },duration);

    }
    public boolean isShowing(){
        return context.findViewById(R.id.login2).getVisibility() == View.VISIBLE;
    }
    public void initial() {
        presenter = createPresenter();
    }

    /*手机号输入监听*/
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > 12) {
                checkCodeBtn.setEnabled(true);
            } else {
                checkCodeBtn.setEnabled(false);
            }
        }
    };


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int vid = view.getId();
            if (vid == R.id.login_next_step){
                if (!RepeatClickUtil.isRepeatClick()) {
                    String mobile = mobileET.getText().toString().replaceAll(" ","").trim();

//                BaseApplication.setSharePerfence("loginMobile",mobile);
                    WeLoginHelper.mobile = mobile;
                    //获取验证码
                    presenter.requestCheckCode(mobile);

                    checkCodeBtn.setEnabled(false);

                    //统计获取验证码的点击
//                    MobclickAgent.onEvent(context,"062");
                }

            }else if (vid == R.id.login2_back){
                //返回上一页
                if (!RepeatClickUtil.isRepeatClick()) {
                    doFragmentChange(0);
                }
                InputMethodManager keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(mobileET.getWindowToken(), 0);
            }else if (vid == R.id.btn_login2_close){
                //关闭登录页
                if (!RepeatClickUtil.isRepeatClick()) {
                    doFragmentChange(-1);
                }

            }
        }
    };

    //fragment跳转
    private void doFragmentChange(int index){
        APPOperateEventBundle bundle = new APPOperateEventBundle();
        bundle.putString(APPOperateEventBundle.EVENTACTION,"fragmentChange");
        bundle.putInt("fragmentIndex",index);
        EventPostUtil.post(new BaseAppEvents.APPOperateEvent(BaseAppEvents.STATUS_SUCCESS,bundle));
    }


    public CheckCodePresenter createPresenter() {
        return new CheckCodePresenter(context,this);
    }

    @Override
    public void refreshMsgCode(RequestFreshStatus status) {
        if (checkCodeBtn != null) {
            checkCodeBtn.setEnabled(true);
        }

        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            //跳转至下一页
            doFragmentChange(2);
        }else {
            context.showToast("发送验证码失败");
        }
    }
}
