package com.sien.aimanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.AimBeanAdapter;
import com.sien.aimanager.model.IMainAimObjectViewModel;
import com.sien.aimanager.presenter.MainAimObjectPresenter;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.utils.CPDateUtil;
import com.sien.lib.baseapp.utils.CollectionUtils;
import com.sien.lib.component.coverflow.CoverFlow;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import java.util.Date;

/**
 * @author sien
 * @date 2017/2/13
 * @descript 主页（aimobject版）
 */
public class MainAimObjectActivity extends CPBaseBoostActivity implements IMainAimObjectViewModel {
    private CoverFlow coverFlow;//TODO 更新数据
    private MainAimObjectPresenter presenter;
    private AimBeanAdapter adapter;

    private TextView emptyTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_aimobject);
    }

    @Override
    public void initViews() {
        super.initViews();

        emptyTV = findView(R.id.aimobject_empty_tips);
        coverFlow = findView(R.id.aimobject_flow);
        coverFlow.setUnselectedAlpha(0.5f);
        coverFlow.setmUnselectedScale(0.8f);
        coverFlow.setSpacing(20);
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        Date date = CPDateUtil.getRelativeDate(new Date() ,-1);
        presenter.requestAimBeanData(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aimtype,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.category){
            go2AimObjectListActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    /*跳转至分类列表页*/
    private void go2AimObjectListActivity(){
        startActivity(new Intent(this,AimTypeListActivity.class));
    }

    @Override
    public MainAimObjectPresenter createPresenter() {
        return new MainAimObjectPresenter(this);
    }

    @Override
    public void refreshRequestAimBean(RequestFreshStatus status) {
        if (status == RequestFreshStatus.REFRESH_SUCCESS){
            showContentLayout();

            if (adapter == null){
                if (presenter == null)  return;

//                adapter.refresh(presenter.getDatasource());
//                adapter.notifyDataSetChanged();

                if (!CollectionUtils.IsNullOrEmpty(presenter.getDatasource())) {
                    emptyTV.setVisibility(View.GONE);
                    coverFlow.setVisibility(View.VISIBLE);

                    adapter = new AimBeanAdapter(this, presenter.getDatasource());
                    coverFlow.setAdapter(adapter);
                }else {
                    emptyTV.setVisibility(View.VISIBLE);
                    coverFlow.setVisibility(View.GONE);
                }
            }
        }else {
            showEmptyLayout();
        }
    }
}
