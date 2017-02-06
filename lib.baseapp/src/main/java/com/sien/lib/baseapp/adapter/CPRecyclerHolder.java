package com.sien.lib.baseapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sien.lib.baseapp.R;
import com.sien.lib.baseapp.utils.CPDisplayImageOptionsUtils;
import com.sien.libcomponent.widgets.CircleImageView;

/**
 * @author sien
 * @date 2016/8/29
 * @descript 基类RecyclerHolder
 */
public class CPRecyclerHolder extends RecyclerView.ViewHolder {
    private final SparseArray<View> mViews;

    private DisplayImageOptions displayImageOptions = null;

    public CPRecyclerHolder(View itemView) {
        super(itemView);
        //一般不会超过8个吧
        this.mViews = new SparseArray<View>(8);

        displayImageOptions = CPDisplayImageOptionsUtils.getImageLoaderCachedDisplayOptions();
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     */
    public CPRecyclerHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public CPRecyclerHolder setText(int viewId, int textId) {
        TextView view = getView(viewId);
        view.setText(textId);
        return this;
    }

    public CPRecyclerHolder setTextColor(int viewId, int colorId) {
        TextView view = getView(viewId);
        Context context = view.getContext();
        int color = ContextCompat.getColor(context, colorId);
        view.setTextColor(color);
        return this;
    }

    public CPRecyclerHolder setVisibility(int viewId,int visibility){
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public CPRecyclerHolder setRadioChecked(int viewId,boolean checked){
        RadioButton radioButton = getView(viewId);
        radioButton.setChecked(checked);
        return this;
    }

    /**
     * 为CheckBox设置选中状态
     */
    public CPRecyclerHolder setChecked(int viewId,boolean checked){
        CheckBox view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 为ImageView设置图片
     */
    public CPRecyclerHolder setImageSource( int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public CPRecyclerHolder setImageSource( int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public CPRecyclerHolder setImageSource(int viewId,String resUrl){
        //加载网络图片显示
        ImageLoader.getInstance().displayImage(resUrl, (ImageView) getView(viewId), displayImageOptions);
//        Glide.with(itemView.getContext()).load(resUrl).asBitmap().placeholder(R.drawable.placeholder).into((ImageView) getView(viewId));
        return this;
    }

    public CPRecyclerHolder setImageSource(int viewId,String resUrl,DisplayImageOptions displayImageOptions){
        ImageLoader.getInstance().displayImage(resUrl,(ImageView)getView(viewId),displayImageOptions);
        return this;
    }
    public CPRecyclerHolder setCircleImageSource(int viewId,String resUrl,DisplayImageOptions displayImageOptions){
        ImageLoader.getInstance().displayImage(resUrl,(CircleImageView)getView(viewId),displayImageOptions);
        return this;
    }
    public CPRecyclerHolder setBackgroundResource( int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }
    public CPRecyclerHolder setTextViewDrawalbe(int viewId,int direction,Drawable drawable){
        TextView view = getView(viewId);
        if (direction == 0){
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            view.setCompoundDrawables(drawable, null, null, null);
        }else if (direction == 1){
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            view.setCompoundDrawables(null,drawable, null, null);
        }else if (direction == 2){
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            view.setCompoundDrawables(null, null,drawable, null);
        }else if (direction == 3){
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            view.setCompoundDrawables(null, null, null,drawable);
        }
        return this;
    }

    public CPRecyclerHolder setCheckedChange(int viewId, CompoundButton.OnCheckedChangeListener listener){
        CheckBox view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    public CPRecyclerHolder setRadioCheckedChange(int viewId, CompoundButton.OnCheckedChangeListener listener){
        RadioButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }
}
