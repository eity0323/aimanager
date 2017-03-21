package com.sien.lib.baseapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sien.lib.baseapp.config.CPConfiguration;
import com.sien.lib.baseapp.control.CPAloneBundle;
import com.sien.lib.baseapp.events.IActivityOperater;
import com.sien.lib.baseapp.events.IPluginOrAloneChecker;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.utils.ToastUtil;
import com.sien.lib.databmob.utils.CPLogUtil;

/**
 * @author sien
 * @date 2016/8/25
 * @descript 基类fragment
 *
 * 注：基类管理presenter的生命周期，布局完全由子类管理，提供了统一调用独立开发模块页面功能（使用配置文件配置插件模块，插件模块对宿主透明，仅开发配置文件中提供的接口）
 */
public abstract class CPBaseFragment extends Fragment implements IPluginOrAloneChecker,IActivityOperater {
    protected View rootView;

    protected BasePresenter innerPresenter;

    //system method
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initBase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (innerPresenter != null)  innerPresenter.onViewStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (innerPresenter != null)  innerPresenter.onViewResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (innerPresenter != null)  innerPresenter.onViewPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (innerPresenter != null)  innerPresenter.onViewStop();
    }

    @Override
    public void onDestroy() {
        if (innerPresenter != null){
            innerPresenter.releaseMemory();
            innerPresenter.destory();
        }
        innerPresenter = null;

        //cp.add 2016-12-29
        if (rootView != null){
            rootView.destroyDrawingCache();
            rootView.removeCallbacks(null);
        }
        rootView = null;
        super.onDestroy();
    }


    //base method
    private void initBase(){
        innerPresenter = createPresenter();
    }

    @Override
    public void showToast(String msg){
        ToastUtil.getInstance().showMessage(getActivity(),msg, ToastUtil.LENGTH_SHORT);
    }

    @Override
    public void logDebug(String msg){
        CPLogUtil.logDebug(getClass().getSimpleName(), msg);
    }

    @Override
    public void logError(String msg){
        CPLogUtil.logError(getClass().getSimpleName(), msg);
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     */
    @Override
    public <T extends View> T findView(int viewId) {
        if (rootView == null){
            throw new IllegalArgumentException("请先初始化根接点rootView");
        }
        View view = rootView.findViewById(viewId);
        return (T) view;
    }

    @Override
    public <V extends BasePresenter> V getPresenter() {
        return (V)innerPresenter;
    }

    /**
     * 创建Presenter, 然后通过调用{@link #getPresenter()}来使用生成的Presenter
     * @return Presenter
     */
    @Override
    public abstract <V extends BasePresenter> V createPresenter();

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * 打开插件页面
     * @param uriString 插件名称
     * @param context 应用上下文
     */
    @Override
    public void pluginOrAloneOpenUri(String uriString, Context context) {
        if (CPConfiguration.isUsingPluginFramework()){
//            Small.openUri(uriString,context);
        }else{
            //打开插件配置文件中的页面
            CPAloneBundle.openUri(uriString,context);
        }
    }


    @Override
    public void pluginOrAloneOpenUriForResult(String uriString,int requestCode, Activity context) {
        if (CPConfiguration.isUsingPluginFramework()){
//            Intent it = Small.getIntentOfUri(uriString, context);
//            context.startActivityForResult(it, requestCode);
        }else{
            //打开插件配置文件中的页面
            CPAloneBundle.openUriForResult(uriString,context,requestCode);
        }
    }

    @Override
    public void pluginOrAloneOpenUriForResult(Intent intent, int requestCode, Activity context) {
        if (CPConfiguration.isUsingPluginFramework()){
//            context.startActivityForResult(intent, requestCode);
        }else{
            //打开插件配置文件中的页面
            CPAloneBundle.openUriForResult(intent,context,requestCode);
        }
    }

    /**
     * 获取插件intent
     * @param uriString 插件名称
     * @param context 应用上下文
     */
    @Override
    public Intent pluginOrAloneGetIntentOfUri(String uriString, Context context) {
        Intent tent = null;
        if (CPConfiguration.isUsingPluginFramework()){
//            tent = Small.getIntentOfUri(uriString,context);
        }else{
            //获取插件配置文件中的intent
            tent = CPAloneBundle.getIntentOfUri(uriString,context);
        }
        return tent;
    }

    /**
     * 获取插件传递参数
     * @param activity
     * @return
     */
    @Override
    public Uri pluginOrAloneGetUri(Activity activity) {
        Uri uri = null;
        if (CPConfiguration.isUsingPluginFramework()){
//            uri = Small.getUri(activity);
        }else{
            //获取插件配置文件中的intent参数
            uri = CPAloneBundle.getUri(activity);
        }
        return uri;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (innerPresenter != null){
            innerPresenter.releaseMemory();
        }
    }
}
