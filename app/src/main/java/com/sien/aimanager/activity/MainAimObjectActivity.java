package com.sien.aimanager.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.AimBeanAdapter;
import com.sien.aimanager.model.IMainAimObjectViewModel;
import com.sien.aimanager.presenter.MainAimObjectPresenter;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.utils.CollectionUtils;
import com.sien.lib.baseapp.widgets.CPRefreshView;
import com.sien.lib.component.coverflow.CoverFlow;
import com.sien.lib.component.coverflow.TwoWayAdapterView;
import com.sien.lib.databmob.beans.AimTypeVO;
import com.sien.lib.databmob.config.DatappConfig;
import com.sien.lib.databmob.control.CPSharedPreferenceManager;
import com.sien.lib.databmob.network.base.RequestFreshStatus;

import java.util.Date;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 主页（aimobject版）
 */
public class MainAimObjectActivity extends CPBaseBoostActivity implements IMainAimObjectViewModel {
    private CoverFlow coverFlow;

    private MainAimObjectPresenter presenter;
    private AimBeanAdapter adapter;
    private CPRefreshView refreshLayout;

    private LinearLayout newAimItemLatyout;

    private TextView emptyTV,editTV;

    private int coverIndex = -1;//分类索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_aimobject);
    }

    @Override
    public void initViews() {
        super.initViews();

        coverFlow = findView(R.id.aimbean_flow);
        coverFlow.setUnselectedAlpha(0.5f);
        coverFlow.setmUnselectedScale(0.8f);
        coverFlow.setSpacing(20);
        coverFlow.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
                if (presenter != null && presenter.getDatasource() != null) {
                    coverIndex = position;

                    if (presenter.getDatasource().size() <= position)  return;

                    AimTypeVO item = presenter.getDatasource().get(position).aimTypeVO;
                    if (item == null)   return;

                    go2AimTypeDetailActivity(item);
                }
            }
        });

        coverFlow.setOnItemSelectedListener(new TwoWayAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(TwoWayAdapterView<?> parent, View view, int position, long id) {
                coverIndex = position;
            }

            @Override
            public void onNothingSelected(TwoWayAdapterView<?> parent) {

            }
        });

        refreshLayout = findView(R.id.aimBeanRefresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.indigo));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                freshGenerateOrRequestData();
            }
        });

        emptyTV = findView(R.id.aimobject_empty_tips);
        emptyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2NewAimTypeActivity();
            }
        });

        editTV = findView(R.id.aimObject_edit);

        newAimItemLatyout = findView(R.id.newAimType);
        newAimItemLatyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coverIndex != -1) {
                    //1、有今日事项，跳转至变更目标事项
                    AimTypeVO item = presenter.getDatasource().get(coverIndex).aimTypeVO;
                    if (item == null)   return;
                    go2AimTypeDetailActivity(item);
                }else {
                    //2、没有事项，跳转至添加目标实现
                    go2NewAimTypeActivity();
                }
            }
        });

        findView(R.id.aimobject_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                go2AimTypeListActivity(false);

                remindOperation(MainAimObjectActivity.this);
            }
        });

        initialShareData();

    }

    public final int mId = 32715;
    private void remindOperation(Context context){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.icon_share)
                        .setContentTitle("目标管理")
                        .setContentText("记得完成今天的任务");
        Intent resultIntent = new Intent(context, MainAimObjectActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainAimObjectActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, mBuilder.build());
    }

    /*初始化缓存数据*/
    private void initialShareData(){
        //读取缓存数据
        String userName = CPSharedPreferenceManager.getInstance(this).getData(DatappConfig.LOGIN_USER_KEY);
        if (!TextUtils.isEmpty(userName)){
            DatappConfig.userAccount = userName;
        }

        String userId= CPSharedPreferenceManager.getInstance(this).getData(DatappConfig.LOGIN_USERID_KEY);
        if (!TextUtils.isEmpty(userId)){
            DatappConfig.userAccountId = userId;
        }

        //默认数据请求方式
//        String requestModel = CPSharedPreferenceManager.getInstance(this).getData(DatappConfig.REQUEST_MODEL_KEY);
//        if (!TextUtils.isEmpty(requestModel)){
//            if (String.valueOf(BaseRepository.REQUEST_ONLEY_CACHE).equals(requestModel)) {
//                DatappConfig.DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_ONLEY_CACHE;
//            }else if (String.valueOf(BaseRepository.REQUEST_ONLEY_NETWORK).equals(requestModel)) {
//                DatappConfig.DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_ONLEY_NETWORK;
//            }else if (String.valueOf(BaseRepository.REQUEST_BOTH).equals(requestModel)) {
//                DatappConfig.DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_BOTH;
//            }else {
//                DatappConfig.DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_AUTO;
//            }
//        }

        //网络环境（发布版本，屏蔽网络环境切换入口）
        if (!DatappConfig.IS_RELEASE) {
            String environmentModel = BaseApplication.getSharePerfence(DatappConfig.ENVIRONMENT_KEY);
            if (!TextUtils.isEmpty(environmentModel)) {
                if (String.valueOf(DatappConfig.ENV_DEVELOP).equals(environmentModel)) {
                    //开发环境
                    DatappConfig.setDevelopEnvironment();
                } else if (String.valueOf(DatappConfig.ENV_TEST).equals(environmentModel)) {
                    //测试环境
                    DatappConfig.setTestEnvironment();
                } else if (String.valueOf(DatappConfig.ENV_OFFICAL).equals(environmentModel)) {
                    //正式环境
                    DatappConfig.setOfficalEnvironment();
                }
            }
        }
    }


    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        initRequestData();
    }

    /*初始请求今日事项数据*/
    private void initRequestData(){
        Date date = new Date();//CPDateUtil.getRelativeDate(new Date() ,-1);
        presenter.requestAimBeanData(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.category){
            go2AimTypeListActivity(true);
        }else if (item.getItemId() == R.id.setting){
            startActivity(new Intent(this,SettingActivity.class));
        }else if (item.getItemId() == R.id.synchronous){
            //synchronous data to server
        }
        return super.onOptionsItemSelected(item);
    }

    /*跳转至分类列表页(目标 or 全部分类项)*/
    private void go2AimTypeListActivity(boolean showFixType){
        Intent intent = new Intent(this,AimTypeListActivity.class);
        intent.putExtra("showFixType",showFixType);
        startActivity(intent);
    }

    /*跳转至添加分类页*/
    private void go2NewAimTypeActivity(){
        startActivity(new Intent(this,NewAimTypeActivity.class));
    }

    /*跳转至目标分类详情*/
    private void go2AimTypeDetailActivity(AimTypeVO aimTypeVO){
        Intent intent = new Intent(this,AimTypeDetailActivity.class);
        intent.putExtra("ds",aimTypeVO);
        startActivity(intent);
    }

    /*更新cover数据显示*/
    private void updateCoverAdapter(){
        if (adapter == null){
            adapter = new AimBeanAdapter(this, presenter.getDatasource());
            coverFlow.setAdapter(adapter);
        }else {
            adapter.refresh(presenter.getDatasource());
            adapter.notifyDataSetChanged();
        }
    }

    /*下拉请求创建并获取今日目标*/
    private void freshGenerateOrRequestData(){
        Date date = new Date();
        if (presenter != null) {
            presenter.checkAndCreateAutoAimType(date);
            presenter.requestAimBeanData(date);
        }
    }

    @Override
    public MainAimObjectPresenter createPresenter() {
        return new MainAimObjectPresenter(this);
    }

    @Override
    public void refreshRequestAimBean(RequestFreshStatus status) {
        showContentLayout();

        if (status == RequestFreshStatus.REFRESH_SUCCESS){
            if (presenter == null)  return;

            if (!CollectionUtils.IsNullOrEmpty(presenter.getDatasource())) {
                coverIndex = 0;

                emptyTV.setVisibility(View.GONE);
                coverFlow.setVisibility(View.VISIBLE);
                newAimItemLatyout.setVisibility(View.VISIBLE);

                editTV.setText(R.string.aimobject_edit);

                updateCoverAdapter();
            }else {
                coverIndex = -1;
                emptyTV.setVisibility(View.VISIBLE);
                coverFlow.setVisibility(View.GONE);
                newAimItemLatyout.setVisibility(View.GONE);

                editTV.setText(R.string.aimobject_add);
            }

        }else {
            coverIndex = -1;
//            showEmptyLayout();
            emptyTV.setVisibility(View.VISIBLE);
            coverFlow.setVisibility(View.GONE);

            editTV.setText(R.string.aimobject_add);
        }

        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void refreshNewAimType(RequestFreshStatus status) {
        if (status == RequestFreshStatus.REFRESH_SUCCESS){
            initRequestData();
        }
    }
}
