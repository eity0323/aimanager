package com.sien.lib.baseapp.presenters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.sien.lib.datapp.utils.CPLogUtil;

import java.lang.ref.WeakReference;

/**
 * @author sien
 * @date 2017/1/22
 * @descript UI逻辑管理基类presenter
 */
public class BasePresenter {
    protected Context mcontext = null;

    public BasePresenter() {

    }

    public void destory(){
        mcontext = null;
        if (updateMessageHander != null){
            updateMessageHander.removeCallbacksAndMessages(null);
        }
        updateMessageHander = null;
    }

    /**
     * 界面初始化完毕时调用, 此时界面控件等已初始化完毕
     */
    protected void onStart(){};

    public void onViewStart() {}

    public void onViewResume() {}

    public void onViewPause() {}

    public void onViewStop() {}

    public void logError(String msg){
        CPLogUtil.logError("BasePresenter",msg);
    }

    public void logDebug(String msg){
        CPLogUtil.logDebug("BasePresenter",msg);
    }

    public void releaseMemory(){
        //子类处理
    }

    /** 用于当前页面处于激活状态时，及时更新界面 */
    protected void handleMessageFunc(BasePresenter helper, Message msg) {

    }

    /*将数据传回UI线程*/
    protected void postMessage2UI(Object data,int what){
        if (updateMessageHander != null) {
            Message msg = new Message();
            msg.what = what;
            msg.obj = data;
            updateMessageHander.sendMessage(msg);
        }
    }

    /** 用于当前页面处于激活状态时，及时更新界面 */
    protected static class InnerHandler extends Handler {
        private final WeakReference<BasePresenter> helper;

        public InnerHandler(BasePresenter activity) {
            helper = new WeakReference<BasePresenter>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BasePresenter theActivity = helper.get();
            if (theActivity == null)
                return;

            theActivity.handleMessageFunc(theActivity, msg);
        }
    }

    protected InnerHandler updateMessageHander = null;
}
