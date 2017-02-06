package com.sien.lib.baseapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sien.lib.baseapp.R;
import com.sien.lib.baseapp.widgets.ToastTextView;

/**
 * @author 蒋宇超
 * @date 2016/12/15
 * @descript com.sien.lib.basecomponent.utils
 */
public class ToastUtil {
    private static ToastUtil mToastUtil;
    private Toast mToast;
    private TextView textView;
    private ImageView tiptextImage;

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    public ToastUtil() {
    }

    // 唯一实例
    public static ToastUtil getInstance() {
        if (mToastUtil == null) {
            synchronized (ToastUtil.class) {
                if (mToastUtil == null) {
                    mToastUtil = new ToastUtil();
                }
            }
        }
        return mToastUtil;
    }

    /**
     * 系统的toast
     */
    public void showMessage(Context context, String message, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, duration);
        mToast.show();
    }

    public void showShortMessage(Context context,String message){
        showMessage(context,message,Toast.LENGTH_SHORT);
    }

    public void showLongMessage(Context context,String message){
        showMessage(context,message,Toast.LENGTH_LONG);
    }

    /**
     * 系统的toast
     */
    public void showMessage(Context context, int resId, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, resId, duration);
        mToast.show();
    }

    public void showShortMessage(Context context,int resId){
        showMessage(context,resId,Toast.LENGTH_SHORT);
    }

    public void showLongMessage(Context context,int resId){
        showMessage(context,resId,Toast.LENGTH_LONG);
    }

    /**
     * 带图片的toast
     */
//    public void showImageMessage(Context context, String message) {
//        View viwe = ((LinearLayout) View.inflate(context, R.layout.view_tiptext, null));
//        tiptextImage = (ImageView) viwe.findViewById(R.id.tiptext_image);
//        textView = (TextView) viwe.findViewById(R.id.tiptext_text);
//        textView.setText(message);
//
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        mToast = new Toast(context);
//        mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0,
//                context.getResources().getDimensionPixelSize(R.dimen.tiptext_bottom_margin));
//        mToast.setView(viwe);
//
//        if (message.equals(context.getResources().getString(R.string.save_lose_str))) {
//            tiptextImage.setImageResource(R.drawable.save_lose);
//        } else if (message.equals(context.getResources().getString(R.string.save_success_str))) {
//            tiptextImage.setImageResource(R.drawable.save_success);
//        }
//
//        mToast.setDuration(500);
//        mToast.show();
//    }

    public void showTopNavMessage(Context context, String message) {
        float topHeight = context.getResources().getDimension(R.dimen.toast_margin_top);
        showTopMessage(context, message, (int)(topHeight + CPScreenUtil.dip2px(context, 10)));
    }

    public void showMessage(Context context, String message) {
        showTopMessage(context, message, CPScreenUtil.dip2px(context, 10));
    }

    public void showCenterMessage(Context context, String message) {
        showTopMessage(context, message, context.getResources().getDimensionPixelSize(R.dimen.toast_margin_bottom));
    }

    /**
     * 自定义的toast
     */
    private void showTopMessage(Context context, String message, int top) {
        if (context instanceof Activity && ((Activity)context).isFinishing()) {
            return;
        }
        View viwe = View.inflate(context, R.layout.layout_toast, null);
        textView = (ToastTextView) viwe.findViewById(R.id.message);
        textView.setText(message);

        if (mToast != null) {
            mToast.cancel();
        }
        mToast = new Toast(context);
        mToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, top);
        mToast.setView(viwe);

        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

}
