package com.sien.aimanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.AimBeanAdapter;
import com.sien.aimanager.model.IMainAimObjectViewModel;
import com.sien.aimanager.presenter.MainAimObjectPresenter;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.utils.CollectionUtils;
import com.sien.lib.baseapp.widgets.CPRefreshView;
import com.sien.lib.component.coverflow.CoverFlow;
import com.sien.lib.component.coverflow.TwoWayAdapterView;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

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

    private TextView emptyTV;

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
                initRequestData();
            }
        });

        emptyTV = findView(R.id.aimobject_empty_tips);
        emptyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2NewAimTypeActivity();
            }
        });

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
                go2AimTypeListActivity(false);
            }
        });
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

                updateCoverAdapter();
            }else {
                coverIndex = -1;
                emptyTV.setVisibility(View.VISIBLE);
                coverFlow.setVisibility(View.GONE);
                newAimItemLatyout.setVisibility(View.GONE);
            }

        }else {
            coverIndex = -1;
//            showEmptyLayout();
            emptyTV.setVisibility(View.VISIBLE);
            coverFlow.setVisibility(View.GONE);
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
