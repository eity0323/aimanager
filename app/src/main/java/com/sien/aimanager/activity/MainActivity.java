package com.sien.aimanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sien.aimanager.R;
import com.sien.aimanager.adapter.MainAdapter;
import com.sien.aimanager.config.AppConfig;
import com.sien.aimanager.control.UpdateManager;
import com.sien.aimanager.model.IMainViewModel;
import com.sien.aimanager.presenter.MainPresenter;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.widgets.recyclerview.CPDividerItemDecoration;
import com.sien.lib.datapp.cache.CacheDataStorage;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

public class MainActivity extends CPBaseBoostActivity implements IMainViewModel{
    private boolean firstInit = false;

    private MainPresenter presenter;
    private RecyclerView recyclerView;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNeedLoadingStatus(false);//首页不需要加载状态
        setContentView(R.layout.activity_main);

        firstInit = true;
    }

    @Override
    public int getBoostEmptyResId() {
        return R.layout.layout_boost_empty_normal;
    }

    @Override
    public int getBoostToolbarResId() {
        return R.layout.layout_boost_toolbar_normal;
    }

    @Override
    public void initViews() {
        super.initViews();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("AIManager");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);//将应用程序图标设置为可点击的按钮,并且在图标上添加向左的箭头
        }

        recyclerView = findView(R.id.aimTypeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        CPDividerItemDecoration itemDecoration = new CPDividerItemDecoration(this,CPDividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        findView(R.id.newAimType).setOnClickListener(clickListener);
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        presenter.requestVersionCheck();
        presenter.requestAimTypeDatas();

        showContentLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!firstInit) {// 非第一次进onResume
            // 检测是否存在强制版本更新
            String forcedUpgrade = BaseApplication.getSharePerfence(AppConfig.PRESH_KEY_UPGRADE_FORCED);
            if ("true".equals(forcedUpgrade)) {
                UpdateManager updateManager = new UpdateManager(this, false);
                updateManager.autoUpdate();
            }
        } else {
            firstInit = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting){
            startActivity(new Intent(this,AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /*展示变更列表数据*/
    private void displayChangedAdapter(){
        if (adapter == null){
            adapter = new MainAdapter(recyclerView,presenter.getDatasource());
            recyclerView.setAdapter(adapter);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.newAimType){
                go2NewAimTypeActivity();
            }
        }
    };

    /*跳转至新建目标分类页*/
    private void go2NewAimTypeActivity(){
        startActivity(new Intent(this,NewAimTypeActivity.class));
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void networkValidRefresh() {
        super.networkValidRefresh();

        // 重新请求数据
        if (presenter != null) {
            presenter.requestAimTypeDatas();
        }
    }

    @Override
    public void refreshVersionCheck(RequestFreshStatus status) {
        if (!getActiveStatus()) return;

        if (status != RequestFreshStatus.REFRESH_ERROR){
            if (presenter.getVersionCheckVO() != null) {
                BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_MIN_SUPPORTED_VERSION,presenter.getVersionCheckVO().getMinUpgradeVersion());
                BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_VERSION_NAME,presenter.getVersionCheckVO().getVersionNumber());
                BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_APK_DOWNLOAD_URL,presenter.getVersionCheckVO().getFileId());
                BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_DESC,presenter.getVersionCheckVO().getRemark());

                // 版本升级
                UpdateManager updateManager = new UpdateManager(this, false);
                updateManager.setIgnoreNextCheck(false);
                updateManager.autoUpdate();
            }
        }
    }

    @Override
    public void refreshRequestAimType(RequestFreshStatus status) {
        if (presenter == null)  return;

        if (status != RequestFreshStatus.REFRESH_ERROR){
            displayChangedAdapter();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        //清除第三方缓存库
        ImageLoader.getInstance().clearMemoryCache();
        //清除内存缓存
        CacheDataStorage.getInstance().releaseMemory();
    }
}
