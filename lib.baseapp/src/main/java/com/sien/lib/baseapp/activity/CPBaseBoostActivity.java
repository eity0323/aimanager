package com.sien.lib.baseapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sien.lib.baseapp.R;
import com.sien.lib.baseapp.config.CPConfiguration;
import com.sien.lib.baseapp.control.CPActivityManager;
import com.sien.lib.baseapp.control.CPAloneBundle;
import com.sien.lib.baseapp.control.CPCircleLoadingHelper;
import com.sien.lib.baseapp.events.IActivityOperater;
import com.sien.lib.baseapp.events.ICPLoadingBehaviour;
import com.sien.lib.baseapp.events.IPluginOrAloneChecker;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.baseapp.utils.ToastUtil;
import com.sien.lib.databmob.utils.CPDeviceUtil;
import com.sien.lib.databmob.utils.CPLogUtil;
import com.sien.lib.databmob.utils.CPNetworkUtil;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author sien
 * @date 2017/1/7
 * @descript 基类Activity(提供全局加载效果、无网络状态效果)
 *
 * 基类管理presenter的生命周期，布局完全由子类管理，提供了统一调用独立开发模块页面功能（使用配置文件配置插件模块，插件模块对宿主透明，仅开发配置文件中提供的接口）
 */
public abstract class CPBaseBoostActivity extends AppCompatActivity implements IPluginOrAloneChecker,IActivityOperater,ICPBaseBoostViewModel,EasyPermissions.PermissionCallbacks {
    public final int LAYOUT_STATUS_LOADING = 1;
    public final int LAYOUT_STATUS_NETWORK = 2;
    public final int LAYOUT_STATUS_CONTENT = 3;
    public final int LAYOUT_STATUS_EMPTY = 4;

    protected BusBaseBoostPresenter innerPresenter;
    private boolean isActive = true;//当前页面是否处于激活状态

    private int contentResId = View.NO_ID;//内容资源文件
    private int toolbarResId = View.NO_ID;//toolbar资源文件  R.layout.layout_boost_toolbar_normal

    private FrameLayout loadingView;
    private FrameLayout networkView;
    private FrameLayout contentView;
    private FrameLayout emptyView;
    private TextView noNetworkBar;
    private Toolbar toolbar;

    private boolean needLoadingStatus = true;//是否需要加载状态（可子类设置）
    private boolean needNetworkStatus = true;//是否需要无网络状态（可子类设置）
    private boolean needNetworkBar = true;//是否需要无网络提示状态栏（可子类设置）
    private boolean needOverlay = false;//是否使用覆盖式的toolbar（可子类设置）
    private boolean needEmptyStatus = true;//是否需要空数据状态（可子类设置）
    private boolean needCoordinate = false;//是否需要协同布局（可子类设置）
    private int layoutStatus = LAYOUT_STATUS_LOADING;//当前布局状态

    private Context mcontext;

    private ICPLoadingBehaviour loadingBehaviour;//加载布局行为管理

    private boolean toolbarViewStubed = false;//toolbar布局文件是否已经渲染(内部使用)
    private ViewStub viewStub;

    //system method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (needOverlay) {
            super.setContentView(R.layout.activity_base_boost_overlay);
        }else {
            if (needCoordinate){
                super.setContentView(R.layout.activity_base_boost_coordinate);
            }else {
                super.setContentView(R.layout.activity_base_boost);
            }
        }

        contentResId = layoutResID;
        mcontext = this;

        initBase();
        initViews();
        initial();
    }

    @Override
    protected void onDestroy() {
        if (innerPresenter != null){
            innerPresenter.releaseMemory();
            innerPresenter.destory();
        }
        innerPresenter = null;

        loadingView = null;
        networkView = null;
        contentView = null;

        loadingBehaviour = null;

        CPActivityManager.getAppManager().removeActivity(this);

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (innerPresenter != null) innerPresenter.onViewStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
        if (innerPresenter != null) innerPresenter.onViewResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
        if (innerPresenter != null) innerPresenter.onViewPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (innerPresenter != null) innerPresenter.onViewStop();
    }

    //base method
    @Override
    public void showToast(String msg){
        if (isActive) {
            ToastUtil.getInstance().showMessage(this, msg, ToastUtil.LENGTH_SHORT);
        }
    }

    public void showToast(int resId){
        if (isActive) {
            ToastUtil.getInstance().showMessage(this, getString(resId), ToastUtil.LENGTH_SHORT);
        }
    }

    @Override
    public void logDebug(String msg){
        CPLogUtil.logDebug(getLocalClassName(), msg);
    }

    @Override
    public void logError(String msg){
        CPLogUtil.logError(getLocalClassName(),msg);
    }

    public void setNeedLoadingStatus(boolean needLoadingStatus) {
        this.needLoadingStatus = needLoadingStatus;
    }

    public void setNeedNetworkBar(boolean needNetworkBar) {
        this.needNetworkBar = needNetworkBar;
    }

    public void setNeedNetworkStatus(boolean needNetworkStatus) {
        this.needNetworkStatus = needNetworkStatus;
    }

    public void setNeedOverlay(boolean needOverlay) {
        this.needOverlay = needOverlay;
    }


    public void setNeedCoordinate(boolean needCoordinate) {
        this.needCoordinate = needCoordinate;
    }

    public boolean getActiveStatus(){
        return isActive;
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
    public <E extends View> E findView(int viewId) {
        View view = findViewById(viewId);
        return (E) view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*设置toolbar布局文件，不需要toolbar时，子类设置View.NO_ID*/
    public int getBoostToolbarResId() {
        return R.layout.layout_boost_toolbar_normal;//默认toolbar
    }

    /*设置空数据布局文件,可子类设置*/
    public int getBoostEmptyResId(){return R.layout.layout_boost_empty_normal;}

    /*设置加载布局文件，可子类设置*/
    public int getLoadingResId(){ return R.layout.layout_boost_loading_normal;}

    /*设置无网络布局文件，可子类设置*/
    public int getNetworkResId(){ return R.layout.layout_boost_network_normal;}

    public void setLoadingBehaviour(ICPLoadingBehaviour behaviour){
        loadingBehaviour = behaviour;
    }

    public int getLayoutStatus() {
        return layoutStatus;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    /**
     * 初始化布局对象，findViews操作
     */
    @Override
    public void initViews(){

    }

    @Override
    public void initial(){

    }

    private void initBase(){
        viewStub = findView(R.id.boost_toolbar_viewstub);
        //要判断viewStub是否为空，为空则说明已经加载过，不为空说明是第一次加载
        toolbarResId = getBoostToolbarResId();
        if (toolbarResId != View.NO_ID) {
            if (viewStub.getParent() != null && !toolbarViewStubed){
                viewStub.setLayoutResource(toolbarResId);
                viewStub.inflate();
                toolbarViewStubed = true;
            }else {
                viewStub.setVisibility(View.VISIBLE);
            }

            initBoostToolbar();
        }else {
            viewStub.setVisibility(View.GONE);
        }

        //网络状态条
        noNetworkBar = findView(R.id.no_network_warn);

        //加载布局
        loadingView = findView(R.id.boost_loading_layout);
        if (getLoadingResId() != View.NO_ID){
            View v = LayoutInflater.from(this).inflate(getLoadingResId(),null);
            loadingView.addView(v);
        }

        //无网络布局
        networkView = findView(R.id.boost_network_layout);
        if (getNetworkResId() != View.NO_ID) {
            View v = LayoutInflater.from(this).inflate(getNetworkResId(),null);
            networkView.addView(v);

            networkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    networkValidRefresh();
                }
            });
        }

        //空数据布局
        emptyView = findView(R.id.boost_empty_layout);
        if (getBoostEmptyResId() != View.NO_ID) {
            View eptView = LayoutInflater.from(this).inflate(getBoostEmptyResId(), null);
            emptyView.addView(eptView);
        }

        //内容布局
        contentView = findView(R.id.boost_content_layout);
        if (contentResId != View.NO_ID) {
            View v = LayoutInflater.from(this).inflate(contentResId, null);
            contentView.addView(v);
        }

        if (needLoadingStatus){
            boostLayoutStatus(LAYOUT_STATUS_LOADING);
        }else {
            boostLayoutStatus(LAYOUT_STATUS_CONTENT);
        }

        CPActivityManager.getAppManager().addActivity(this);

        innerPresenter = createPresenter();

//        setTranslucentStatusBar();
    }

    /**
     * 初始化toolbar设置
     */
    protected void initBoostToolbar(){
        toolbar = findView(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            //默认开启点击按钮
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);//设置是否显示应用程序的图标
            actionBar.setHomeButtonEnabled(true);//将应用程序图标设置为可点击的按钮
            actionBar.setDisplayHomeAsUpEnabled(true);//将应用程序图标设置为可点击的按钮,并且在图标上添加向左的箭头
        }
    }

    /*布局状态设置*/
    private void boostLayoutStatus(int layoutStatus){
        if (layoutStatus == LAYOUT_STATUS_LOADING){
            //初始化加载布局行为管理
            if (loadingBehaviour == null){
                loadingBehaviour = new CPCircleLoadingHelper(this,loadingView);
            }
            if (needNetworkStatus) {
                if (CPNetworkUtil.isNetworkAvailable(this)){
                    layoutStatus = LAYOUT_STATUS_LOADING;
                    loadingView.setVisibility(View.VISIBLE);
                    if (loadingBehaviour != null){
                        loadingBehaviour.show();
                    }

                    networkView.setVisibility(View.GONE);
                    contentView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                }else {
                    layoutStatus = LAYOUT_STATUS_NETWORK;
                    networkView.setVisibility(View.VISIBLE);

                    contentView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    if (loadingBehaviour != null){
                        loadingBehaviour.hide();
                    }
                    emptyView.setVisibility(View.GONE);
                }
            }else {
                loadingView.setVisibility(View.VISIBLE);
                layoutStatus = LAYOUT_STATUS_LOADING;

                if (loadingBehaviour != null){
                    loadingBehaviour.show();
                }
                networkView.setVisibility(View.GONE);
                contentView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
            }
        }else{
            if (needNetworkStatus) {
                if (CPNetworkUtil.isNetworkAvailable(this)){
                    contentView.setVisibility(View.VISIBLE);
                    layoutStatus = LAYOUT_STATUS_CONTENT;

                    networkView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    if (loadingBehaviour != null){
                        loadingBehaviour.hide();
                    }
                    emptyView.setVisibility(View.GONE);
                }else {
                    networkView.setVisibility(View.VISIBLE);
                    layoutStatus = LAYOUT_STATUS_NETWORK;

                    contentView.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    if (loadingBehaviour != null){
                        loadingBehaviour.hide();
                    }
                    emptyView.setVisibility(View.GONE);
                }
            }else {
                contentView.setVisibility(View.VISIBLE);
                layoutStatus = LAYOUT_STATUS_CONTENT;

                loadingView.setVisibility(View.GONE);
                if (loadingBehaviour != null){
                    loadingBehaviour.hide();
                }
                networkView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 创建Presenter, 然后通过调用{@link #getPresenter()}来使用生成的Presenter
     * @return Presenter
     */
    @Override
    public abstract <V extends BasePresenter> V createPresenter();

    /**
     * 获取通过{@link #createPresenter()}生成的presenter对象
     * @return Presenter
     */
    @Override
    public <V extends BasePresenter> V getPresenter() {
        return (V)innerPresenter;
    }

    /*显示loading布局*/
    @Override
    public void showLoading() {
        if (needLoadingStatus){
            boostLayoutStatus(LAYOUT_STATUS_LOADING);
        }
    }

    /*隐藏loading布局，显示内容布局*/
    @Override
    public void hideLoading() {
        boostLayoutStatus(LAYOUT_STATUS_CONTENT);
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
        //网络变化，显示无网络状态 (可使用缓存数据，不根据网络情况改变状态，只控制networkbar的显示)
        if (success){
            if (needNetworkBar) {
                noNetworkBar.setVisibility(View.GONE);
            }

            //TODO 如果需要有网络时自动刷新界面无网络状态，则取消此注释(产品经理说服务器异常也当成网络异常处理，所以不需要自动刷新数据显示)
//            networkValidRefresh();
        }else {
            if (needNetworkBar) {
                if (networkView.getVisibility() != View.VISIBLE) {
                    noNetworkBar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 无网络布局点击通知子类刷新界面
     */
    public void networkValidRefresh(){
        //子类处理，刷新显示界面
    }

    //system status bar
    protected void setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            String brand = CPDeviceUtil.getBrand()+"";
            if(brand.indexOf("vivo")<0){//步步高手机不支持这项
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    protected void setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    protected void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 解析传入参数 （插件 or 非插件方式）
     */
    public int parseIntBundleParam(String key){
        return parseIntBundleParam(key,0);
    }

    public int parseIntBundleParam(String key,int defValue){
        int value = defValue;
        //standard apk
        Intent tent = getIntent();
        if (tent != null){
            if (tent.hasExtra(key)) {
                value = tent.getIntExtra(key, defValue);
            }
        }

        //Small plugin
        Uri uri = pluginOrAloneGetUri(this);
        if (uri != null) {
            String from = uri.getQueryParameter(key);
            if (!TextUtils.isEmpty(from)) {
                value = Integer.valueOf(from);
            }
        }
        return value;
    }

    public boolean parseBooleanBundleParam(String key){
        return parseBooleanBundleParam(key,false);
    }
    public boolean parseBooleanBundleParam(String key,boolean defValue){
        boolean value = defValue;
        Intent tent = getIntent();
        if (tent != null){
            if (tent.hasExtra(key)) {
                value = tent.getBooleanExtra(key, defValue);
            }
        }

        //Small plugin
        Uri uri = pluginOrAloneGetUri(this);
        if (uri != null) {
            String from = uri.getQueryParameter(key);
            if (!TextUtils.isEmpty(from)) {
                if ("true".equals(from)) {
                    value = true;
                }else {
                    value = false;
                }
//                value = Boolean.valueOf(from);
            }
        }
        return value;
    }

    public String parseStringBundleParam(String key){
        return parseStringBundleParam(key,"");
    }

    public String parseStringBundleParam(String key,String defValue){
        String value = defValue;
        //standard apk
        Intent tent = getIntent();
        if (tent != null){
            if (tent.hasExtra(key)) {
                value = tent.getStringExtra(key);
            }
        }

        //Small plugin
        Uri uri = pluginOrAloneGetUri(this);
        if (uri != null) {
            String from = uri.getQueryParameter(key);
            if (!TextUtils.isEmpty(from)) {
                value = from;
            }
        }
        return value;
    }

    //plugin
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
            context.startActivityForResult(intent, requestCode);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        logDebug("CPBaseBoostActivity onCreateOptionsMenu");
        return super.onCreateOptionsMenu(menu);
    }

    //memory manage
    /**
     * TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
     * TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
     * TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
     * TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level ==  TRIM_MEMORY_COMPLETE ){
            //后台高紧急释放
            backHighUrgentTrimMemory();
        }else if ( level == TRIM_MEMORY_MODERATE ){
            //后台中紧急释放
            backNormalUrgentTrimMemory();
        }else if (level == TRIM_MEMORY_BACKGROUND ){
            //后台低紧急释放
            backLowUrgentTrimMemory();
        }else if (level == TRIM_MEMORY_UI_HIDDEN ){
            //进入后台可以释放
            uiHiddenTrimMemory();
        }else if (level == TRIM_MEMORY_RUNNING_CRITICAL ){
            //前台高预警
            runningHighUrgentTrimMemory();
        }else if (level == TRIM_MEMORY_RUNNING_LOW ){
            //前台中预警
            runningNormalUrgentTrimMemory();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        if (innerPresenter != null && lowMemoryForceRelease()){
            innerPresenter.releaseMemory();
        }
    }

    /**
     * 应用处于后台运行，内存不足，进程即将被回收
     */
    public void backHighUrgentTrimMemory(){
        if (innerPresenter != null && lowMemoryForceRelease()){
            innerPresenter.releaseMemory();
        }
    }

    /**
     * 应用处于后台运行，内存不足，进程可能被回收
     */
    public void backNormalUrgentTrimMemory(){

    }

    /**
     * 用处于后台运行，内存不足，开始回收进程
     */
    public void backLowUrgentTrimMemory(){

    }

    /**
     * 用户点击了Home键或者Back键导致应用的UI界面不可见触发
     */
    public void uiHiddenTrimMemory(){

    }

    /**
     * 应用处于前台，内存不足，已结束大部分缓存进程，可能会结束正在运行的进程
     */
    public void runningHighUrgentTrimMemory(){

    }

    /**
     * 应用处于前台，内存不足，结束了部分缓存进程，
     */
    public void runningNormalUrgentTrimMemory(){

    }

    /**
     * 低内存强制释放presenter内存
     * @return
     */
    public boolean lowMemoryForceRelease(){
        return true;
    }

    //------------------------------------------------------------------动态申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //处理拒绝授权事件（可选）
        DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO Let's show a toast
//                        showToast("取消设置权限");
            }
        };

        // 设置不再询问后，可重新弹出询问框
        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(R.string.permission_ask_again),
                R.string.setting,
                R.string.cancel,
                cancelButtonListener,
                perms);

    }
}
