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
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.sien.lib.baseapp.R;
import com.sien.lib.baseapp.config.CPConfiguration;
import com.sien.lib.baseapp.control.CPActivityManager;
import com.sien.lib.baseapp.control.CPAloneBundle;
import com.sien.lib.baseapp.events.IActivityOperater;
import com.sien.lib.baseapp.events.IPluginOrAloneChecker;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.utils.ToastUtil;
import com.sien.lib.databmob.utils.CPDeviceUtil;
import com.sien.lib.databmob.utils.CPLogUtil;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author sien
 * @date 2017/1/22
 * @descript 基类activity
 * 注：基类管理presenter的生命周期，布局完全由子类管理，提供了统一调用独立开发模块页面功能（使用配置文件配置插件模块，插件模块对宿主透明，仅开发配置文件中提供的接口）
 */
public abstract class CPBaseActivity extends AppCompatActivity implements IPluginOrAloneChecker,IActivityOperater,EasyPermissions.PermissionCallbacks{
    private Toolbar toolbar;
    private BasePresenter innerPresenter;
    private boolean isActive = false;//当前页面是否处于激活状态

    //system method
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

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

        CPActivityManager.getAppManager().removeActivity(this);
        super.onDestroy();
    }

    private void initBase(){
        initBoostToolbar();

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

    public boolean getActiveStatus(){
        return isActive;
    }

    @Override
    public void showToast(String msg){
        if (isActive) {
            ToastUtil.getInstance().showShortMessage(this, msg);
        }
    }

    public void showToast(int resId){
        if (isActive) {
            ToastUtil.getInstance().showShortMessage(this, getString(resId));
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initial() {

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

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void logDebug(String msg){
        CPLogUtil.logDebug(getLocalClassName(),msg);
    }

    @Override
    public void logError(String msg){
        CPLogUtil.logError(getLocalClassName(), msg);
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

    //system status bar style
    protected void setTranslucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            String brand = CPDeviceUtil.getBrand()+"";
            if(brand.indexOf("vivo")<0){//步步高手机不支持这项
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            setFullScreen();
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
    //plugin interface
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
