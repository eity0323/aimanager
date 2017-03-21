package com.sien.lib.baseapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sien.lib.baseapp.R;
import com.sien.lib.baseapp.config.CPConfiguration;
import com.sien.lib.baseapp.control.CPAloneBundle;
import com.sien.lib.baseapp.control.CPCircleLoadingHelper;
import com.sien.lib.baseapp.events.IActivityOperater;
import com.sien.lib.baseapp.events.ICPLoadingBehaviour;
import com.sien.lib.baseapp.events.IPluginOrAloneChecker;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.baseapp.utils.ToastUtil;
import com.sien.lib.databmob.utils.CPLogUtil;
import com.sien.lib.databmob.utils.CPNetworkUtil;

/**
 * @author sien
 * @date 2016/8/25
 * @descript 基类fragment(提供全局加载效果、无网络状态效果 -----可与CPBaseBoostActivity、CPBaseBoostFragment共同使用，不管理toolbar，仅控制布局的各种状态 eg:网络异常、空数据、加载中、内容布局)
 *
 * 注：子类不能覆盖onCreateView方法，需要通过setContentView方法设置布局文件，此外子类不需要主动调用initViews，initial方法
 */
public abstract class CPBaseCoBoostFragment extends Fragment implements IPluginOrAloneChecker,IActivityOperater,ICPBaseBoostViewModel {
    public final int LAYOUT_STATUS_LOADING = 1;
    public final int LAYOUT_STATUS_NETWORK = 2;
    public final int LAYOUT_STATUS_CONTENT = 3;
    public final int LAYOUT_STATUS_EMPTY = 4;

    protected BusBaseBoostPresenter innerPresenter;

    private int contentResId = View.NO_ID;//内容资源文件

    private FrameLayout loadingView;
    private FrameLayout networkView;
    private FrameLayout contentView;
    private FrameLayout emptyView;
    private View rootView;

    private boolean needLoadingStatus = true;//是否需要加载状态
    private boolean needNetworkStatus = true;//是否需要无网络状态
    private boolean needEmptyStatus = true;//是否需要空数据状态（可子类设置）
    private int layoutStatus = LAYOUT_STATUS_LOADING;//当前布局状态

    private Context mcontext;

    private ICPLoadingBehaviour loadingBehaviour;//加载布局行为管理

    //system method
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_base_coboost,container,false);

        setContentView();
        return rootView;
    }

    private void setContentView(){
        contentResId = getContentLayoutResId();

        mcontext = getActivity();

        initBase();
        // cp.add 子类无需主动调用
        initViews();
        initial();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*设置内容布局文件*/
    public abstract @LayoutRes
    int getContentLayoutResId();

    /*设置空数据布局文件,可子类设置*/
    public int getBoostEmptyResId(){return R.layout.layout_boost_empty_normal;}

    /*设置加载布局文件，可子类设置*/
    public int getLoadingResId(){ return R.layout.layout_boost_loading_normal;}

    /*设置无网络布局文件，可子类设置*/
    public int getNetworkResId(){ return R.layout.layout_boost_network_normal;}

    public int getLayoutStatus() {
        return layoutStatus;
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

        loadingView = null;
        networkView = null;
        super.onDestroy();
    }


    //base method
    private void initBase(){
        //加载ing布局
        loadingView = findView(R.id.fragment_boost_loading_layout);
        if (getLoadingResId() != View.NO_ID){
            View v = LayoutInflater.from(mcontext).inflate(getLoadingResId(),null);
            loadingView.addView(v);
        }

        //无网络布局
        networkView = findView(R.id.fragment_boost_network_layout);
        if (getNetworkResId() != View.NO_ID) {
            View v = LayoutInflater.from(mcontext).inflate(getNetworkResId(),null);
            networkView.addView(v);

            networkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (CPNetworkUtil.isNetworkAvailable(mcontext)) {
                        networkValidRefresh();
//                    }
                }
            });
        }
        //内容布局
        contentView = findView(R.id.fragment_boost_content_layout);
        if (contentResId != View.NO_ID) {
            View v = LayoutInflater.from(mcontext).inflate(contentResId, null);
            contentView.addView(v);
        }

        //空数据布局
        emptyView = findView(R.id.fragment_boost_empty_layout);
        if (getBoostEmptyResId() != View.NO_ID) {
            View eptView = LayoutInflater.from(mcontext).inflate(getBoostEmptyResId(), null);
            emptyView.addView(eptView);
        }

        //初始化布局
        if (needLoadingStatus){
            boostLayoutStatus(LAYOUT_STATUS_LOADING);
        }else {
            boostLayoutStatus(LAYOUT_STATUS_CONTENT);
        }

        innerPresenter = createPresenter();
    }

    /*布局状态设置*/
    private void boostLayoutStatus(int layoutStatus){
        if (layoutStatus == LAYOUT_STATUS_LOADING){
            //初始化加载布局行为管理
            if (loadingBehaviour == null){
                loadingBehaviour = new CPCircleLoadingHelper(mcontext,loadingView);
            }

            //加载ing布局
            if (needNetworkStatus) {
                if (CPNetworkUtil.isNetworkAvailable(mcontext)){
                    loadingView.setVisibility(View.VISIBLE);
                    layoutStatus = LAYOUT_STATUS_LOADING;

                    if (loadingBehaviour != null){
                        loadingBehaviour.show();
                    }
                    networkView.setVisibility(View.GONE);
                    contentView.setVisibility(View.GONE);
                }else {
                    networkView.setVisibility(View.VISIBLE);
                    layoutStatus = LAYOUT_STATUS_NETWORK;

                    contentView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    if (loadingBehaviour != null){
                        loadingBehaviour.hide();
                    }
                }
            }else {
                loadingView.setVisibility(View.VISIBLE);
                layoutStatus = LAYOUT_STATUS_LOADING;

                if (loadingBehaviour != null){
                    loadingBehaviour.show();
                }
                networkView.setVisibility(View.GONE);
                contentView.setVisibility(View.GONE);
            }
        }else{
            //内容布局
            if (needNetworkStatus) {
                if (CPNetworkUtil.isNetworkAvailable(mcontext)){
                    contentView.setVisibility(View.VISIBLE);
                    layoutStatus = LAYOUT_STATUS_CONTENT;

                    networkView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    if (loadingBehaviour != null){
                        loadingBehaviour.hide();
                    }
                }else {
                    networkView.setVisibility(View.VISIBLE);
                    layoutStatus = LAYOUT_STATUS_NETWORK;

                    contentView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    if (loadingBehaviour != null){
                        loadingBehaviour.hide();
                    }
                }
            }else {
                contentView.setVisibility(View.VISIBLE);
                layoutStatus = LAYOUT_STATUS_CONTENT;

                loadingView.setVisibility(View.GONE);
                if (loadingBehaviour != null){
                    loadingBehaviour.hide();
                }
                networkView.setVisibility(View.GONE);
            }
        }
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

    public void setNeedLoadingStatus(boolean needLoadingStatus) {
        this.needLoadingStatus = needLoadingStatus;
    }

    public void setNeedNetworkStatus(boolean needNetworkStatus) {
        this.needNetworkStatus = needNetworkStatus;
    }

    /**
     * 获取loading布局，可用于自定义loading布局
     * （不推荐子类直接使用）
     * @return
     */
    protected FrameLayout getLoadingView() {
        return loadingView;
    }

    /**
     * 获取无网络布局，可用于自定义无网络布局
     * （不推荐子类直接使用）
     * @return
     */
    protected FrameLayout getNetworkView() {
        return networkView;
    }

    /**
     * 获取内容布局，可用于修改内容布局
     * （不推荐子类直接使用）
     * @return
     */
    protected FrameLayout getContentView() {
        return contentView;
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
//        if (needLoadingStatus){
//            boostLayoutStatus(LAYOUT_STATUS_LOADING);
//        }
    }

    @Override
    public void hideLoading() {
//        boostLayoutStatus(LAYOUT_STATUS_CONTENT);
    }

    /**
     * 显示加载布局
     */
    public void showLoadingLayout() {
        if (needLoadingStatus){
            boostLayoutStatus(LAYOUT_STATUS_LOADING);
        }
    }
    /**
     * 显示内容布局
     */
    public void showContentLayout(){
        boostLayoutStatus(LAYOUT_STATUS_CONTENT);
    }

    /**
     * 强制显示网络异常布局（有可能是服务器异常，非本地网络异常）
     */
    public void showNetoworkLayout(){
        if (needNetworkStatus){
            loadingView.setVisibility(View.GONE);
            if (loadingBehaviour != null){
                loadingBehaviour.hide();
            }
            networkView.setVisibility(View.VISIBLE);
            layoutStatus = LAYOUT_STATUS_NETWORK;

            contentView.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
        }
    }

    /*强制显示空数据布局*/
    public void showEmptyLayout(){
        if (needEmptyStatus) {
            loadingView.setVisibility(View.GONE);
            if (loadingBehaviour != null){
                loadingBehaviour.hide();
            }
            networkView.setVisibility(View.GONE);
            contentView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            layoutStatus = LAYOUT_STATUS_EMPTY;
        }
    }

    @Override
    public void innerNetworkRefresh(boolean success){
        //网络变化，显示无网络状态 (可使用缓存数据，不根据网络情况改变状态)
        if (success){
            //TODO 如果需要有网络时自动刷新界面无网络状态，则取消此注释
//            networkValidRefresh();
        }
    }

    /**
     * 无网络布局点击通知子类刷新界面
     */
    public void networkValidRefresh(){
        //子类处理，刷新显示界面
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
