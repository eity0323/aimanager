package com.sien.aimanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sien.aimanager.R;
import com.sien.aimanager.adapter.AimTypeAdapter;
import com.sien.aimanager.config.AppConfig;
import com.sien.aimanager.control.UpdateManager;
import com.sien.aimanager.model.IAimTypeListViewModel;
import com.sien.aimanager.presenter.AimTypeListPresenter;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.widgets.recyclerview.CPDividerItemDecoration;
import com.sien.lib.databmob.beans.AimTypeVO;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

/**
 * 目标分类列表页
 */
public class AimTypeListActivity extends CPBaseBoostActivity implements IAimTypeListViewModel {
    private boolean firstInit = false;

    private AimTypeListPresenter presenter;
    private RecyclerView recyclerView;
    private AimTypeAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    private boolean showFixType = false;//显示固定分类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNeedLoadingStatus(false);//首页不需要加载状态
        setContentView(R.layout.activity_aimtype_list);

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

        recyclerView = findView(R.id.aimTypeList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        CPDividerItemDecoration itemDecoration = new CPDividerItemDecoration(this,CPDividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        findView(R.id.newAimType).setOnClickListener(clickListener);

        refreshLayout = findView(R.id.aimTypeRefresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.indigo));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.requestAimTypeDatas(showFixType);
            }
        });
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        parseBundleData();

        if (getSupportActionBar() != null) {
            if (showFixType) {
                getSupportActionBar().setTitle("目标分类");
            }else {
                getSupportActionBar().setTitle("更多目标");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);//将应用程序图标设置为可点击的按钮,并且在图标上添加向左的箭头
        }

        presenter.requestVersionCheck();
        presenter.requestAimTypeDatas(showFixType);

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

    /*解析传入参数*/
    private void parseBundleData(){
        if (getIntent() != null && getIntent().hasExtra("showFixType")){
            showFixType = getIntent().getBooleanExtra("showFixType",false);
        }
    }

    /*操作面板*/
    private void showOperatePanel(final AimTypeVO aimTypeVO){
        String[] titles = {"修改","删除"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setItems(titles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int itemPosition) {
                if (itemPosition == 0) {
                    go2ModifyAimTypeActivity(aimTypeVO);
                } else if (itemPosition == 1) {
                    doDeleteAimType(aimTypeVO);
                }
            }
        });
        builder.create().show();
    }

    /*跳转至目标类型管理页*/
    private void go2AimTypeDetailActivity(AimTypeVO aimTypeVO){
        Intent intent = new Intent(this,AimTypeDetailActivity.class);
        intent.putExtra("ds",aimTypeVO);
        startActivityForResult(intent,AppConfig.REQUEST_CODE_EDIT_AIMTYPE);
    }

    /*跳转至修改目标类型*/
    private void go2ModifyAimTypeActivity(AimTypeVO aimTypeVO){
        Intent intent = new Intent(this,NewAimTypeActivity.class);
        intent.putExtra("ds",aimTypeVO);
        startActivityForResult(intent,AppConfig.REQUEST_CODE_NEW_AIMTYPE);
    }

    /*删除目标类型*/
    private void doDeleteAimType(AimTypeVO aimTypeVO){
        presenter.deleteAimType(aimTypeVO.getId());
    }

    /*展示变更列表数据*/
    private void displayChangedAdapter(){
        if (adapter == null){
            adapter = new AimTypeAdapter(recyclerView,presenter.getDatasource());
            adapter.setOnItemLongClickListener(new CPBaseRecyclerAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, Object data, int position) {
                    showOperatePanel((AimTypeVO)data);
                }
            });
            adapter.setOnItemClickListener(new CPBaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Object data, int position) {
                    go2AimTypeDetailActivity((AimTypeVO)data);
                }
            });
            recyclerView.setAdapter(adapter);
        }else {
            adapter.refresh(presenter.getDatasource());
            adapter.notifyDataSetChanged();
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
        Intent intent = new Intent(this,NewAimTypeActivity.class);
        intent.putExtra("showFixType",showFixType);
        startActivityForResult(intent,AppConfig.REQUEST_CODE_NEW_AIMTYPE);
    }

    @Override
    public void onBackPressed() {
//        if(RepeatClickUtil.isRepeatClick(2000)){
            super.onBackPressed();
//        }else {
//            showToast(R.string.exit_tips);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.REQUEST_CODE_NEW_AIMTYPE){
            if (resultCode == AppConfig.RESULT_CODE_OK){
                //新增页面返回刷新数据
                if (presenter != null) {
                    presenter.requestAimTypeDatas(showFixType);
                }
            }
        }else if (requestCode == AppConfig.REQUEST_CODE_EDIT_AIMTYPE){
            if (resultCode == AppConfig.RESULT_CODE_OK){
                //编辑页面返回刷新数据
                if (presenter != null) {
                    presenter.requestAimTypeDatas(showFixType);
                }
            }
        }
    }

    @Override
    public AimTypeListPresenter createPresenter() {
        return new AimTypeListPresenter(this);
    }

    @Override
    public void networkValidRefresh() {
        super.networkValidRefresh();

        // 重新请求数据
        if (presenter != null) {
            presenter.requestAimTypeDatas(showFixType);
        }
    }

    @Override
    public void refreshVersionCheck(RequestFreshStatus status) {
        if (!getActiveStatus()) return;

        if (status != RequestFreshStatus.REFRESH_ERROR){
            if (presenter.getVersionCheckVO() != null) {
//                BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_MIN_SUPPORTED_VERSION,presenter.getVersionCheckVO().getMinUpgradeVersion());
//                BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_VERSION_NAME,presenter.getVersionCheckVO().getVersionNumber());
//                BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_APK_DOWNLOAD_URL,presenter.getVersionCheckVO().getFileId());
//                BaseApplication.setSharePerfence(AppConfig.PRESH_CONFIG_UPGRADE_DESC,presenter.getVersionCheckVO().getRemark());

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

        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void refreshDelAimType(RequestFreshStatus status) {
        if (status == RequestFreshStatus.REFRESH_SUCCESS){
            if (presenter != null) {
                presenter.requestAimTypeDatas(showFixType);
            }
        }
    }

    @Override
    public boolean checkActiveStatus() {
        return getActiveStatus();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        //清除第三方缓存库
        ImageLoader.getInstance().clearMemoryCache();
        //清除内存缓存
//        CacheDataStorage.getInstance().releaseMemory();
    }
}
