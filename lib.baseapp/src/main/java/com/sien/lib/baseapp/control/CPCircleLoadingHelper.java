package com.sien.lib.baseapp.control;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.sien.lib.baseapp.R;
import com.sien.lib.baseapp.events.ICPLoadingBehaviour;

/**
 * @author sien
 * @date 2017/1/16
 * @descript 圆形加载行为管理（与layout_boost_loading_normal配套使用，若自定义加载布局样式，需要自定义加载行为管理）
 */
public class CPCircleLoadingHelper implements ICPLoadingBehaviour {
    private Context mcontext;
    private View mImageView;

    public CPCircleLoadingHelper(Context context, View loadingView){
        mcontext = context;
        mImageView = loadingView.findViewById(R.id.boost_loading_iv);
    }

    @Override
    public void show() {
        if (mImageView != null){
            Animation loadAnimation = AnimationUtils.loadAnimation(mcontext, R.anim.common_loading);
            mImageView.startAnimation(loadAnimation);
        }
    }

    @Override
    public void hide() {
        if (mImageView != null){
            mImageView.clearAnimation();
        }
    }
}
