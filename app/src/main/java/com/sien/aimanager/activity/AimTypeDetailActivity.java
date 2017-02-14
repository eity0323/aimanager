package com.sien.aimanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.AimItemAdapter;
import com.sien.aimanager.model.IAimTypeViewModel;
import com.sien.aimanager.presenter.AimTypePresenter;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import java.util.Date;

/**
 * @author sien
 * @date 2017/2/8
 * @descript 目标分类详情页
 */
public class AimTypeDetailActivity extends CPBaseBoostActivity implements IAimTypeViewModel{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private AimTypePresenter presenter;
    private AimItemAdapter adapter;

    private Long aimTypeId;
    private AimTypeVO aimTypeVO;
    private AimItemVO itemVO;

    private View aimItemLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aimtype_detail);
    }

    @Override
    public void initViews() {
        super.initViews();

        recyclerView = findView(R.id.aimItemList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        findView(R.id.newAimItem).setOnClickListener(clickListener);

        refreshLayout = findView(R.id.aimItemRefresh);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this,R.color.indigo));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (aimTypeId != null) {
                    presenter.requestAimItemDatas(aimTypeId);
                }
            }
        });
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        parseBundleData();

        if (getSupportActionBar() != null) {
            if (aimTypeVO != null) {
                getSupportActionBar().setTitle(aimTypeVO.getTypeName());
            }else {
                getSupportActionBar().setTitle("目标分类管理");
            }
        }

        if (aimTypeId != null) {
            presenter.requestAimItemDatas(aimTypeId);
        }
    }

    private void parseBundleData(){
        if (getIntent() != null){
            aimTypeVO = (AimTypeVO) getIntent().getSerializableExtra("ds");
            aimTypeId = aimTypeVO.getId();
        }
    }

    /*显示新增目标记录面板*/
    private void showNewAimItemPanel(final boolean isCreate){
        aimItemLayout = getLayoutInflater().inflate(R.layout.layout_new_aimitem,null);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(R.string.new_aimitem_title).setView(aimItemLayout).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkAndNewAimItem(isCreate);
            }
        }).setNegativeButton(R.string.cancel, null).create();
        dialog.show();

        if (!isCreate) {
            if (aimItemLayout != null) {
                EditText titleTV = (EditText) aimItemLayout.findViewById(R.id.aimitem_title);
                if (itemVO != null) {
                    String title = itemVO.getAimName();
                    if (!TextUtils.isEmpty(title)) {
                        titleTV.setText(title);
                        titleTV.setSelection(title.length());
                    }
                }
            }
        }
    }

    /*显示目标记录的变化数据*/
    private void displayChandedAimItem(){
        if (adapter == null){
            adapter = new AimItemAdapter(recyclerView,presenter.getDatasource());
            adapter.setOnItemClickListener(new CPBaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Object data, int position) {
                    changeAimItemStatus((AimItemVO) data);
                }
            });
            adapter.setOnItemButtonClickListener(new AimItemAdapter.ItemButtonClickListener() {
                @Override
                public void onItemClick(View v, AimItemVO data, int position) {
                    if (data != null) {
                        itemVO = data;
                        showNewAimItemPanel(false);
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        }else {
            adapter.refresh(presenter.getDatasource());
            adapter.notifyDataSetChanged();
        }
    }

    /*改变目标项的完成状态*/
    private void changeAimItemStatus(AimItemVO aimItemVO){
        if (aimItemVO != null){
            if (aimItemVO.getFinishStatus() == AimItemVO.STATUS_DONE) {
                aimItemVO.setFinishStatus(AimItemVO.STATUS_UNDO);
            }else {
                aimItemVO.setFinishStatus(AimItemVO.STATUS_DONE);
            }
            presenter.insertOrReplaceAimItem(aimItemVO);
        }
    }

    /*校验并添加 or 编辑目标记录*/
    private void checkAndNewAimItem(boolean isCreate){
        if (aimItemLayout != null) {
            TextView titleTV = (TextView) aimItemLayout.findViewById(R.id.aimitem_title);
            String title = titleTV.getText().toString();
            if (TextUtils.isEmpty(title)){
                showToast("请输入目标名称");
                return;
            }

            if (isCreate) {
                itemVO = new AimItemVO();
                itemVO.setAimName(title);
                itemVO.setDesc(title);
                itemVO.setModifyTime(new Date());
                itemVO.setFinishStatus(AimItemVO.STATUS_UNDO);
                itemVO.setPriority(AimItemVO.PRIORITY_FIVE);
                itemVO.setFinishPercent(0);
                itemVO.setAimTypeId(aimTypeId);
            }else {
                if (itemVO != null){
                    itemVO.setAimName(title);
                    itemVO.setDesc(title);
                }
            }

            presenter.insertOrReplaceAimItem(itemVO);
        }
    }

    /*跳转只编辑目标分类页*/
    private void go2NewAimTypeActivity(){
        if (aimTypeVO != null){
            if (aimTypeVO.getCustomed() != null && !aimTypeVO.getCustomed().booleanValue()) {
                showToast("系统目标类型，不能进行操作");
                return;
            }

            Intent intent = new Intent(this, NewAimTypeActivity.class);
            intent.putExtra("ds", aimTypeVO);
            startActivity(intent);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.newAimItem){
                showNewAimItemPanel(true);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modify,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.modify){
            go2NewAimTypeActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public AimTypePresenter createPresenter() {
        return new AimTypePresenter(this);
    }

    @Override
    public void refreshAimItem(RequestFreshStatus status) {
        itemVO = null;//清空新建 or 修改值
        if (status != RequestFreshStatus.REFRESH_ERROR){
            showContentLayout();

            displayChandedAimItem();
        }

        if (refreshLayout != null){
            refreshLayout.setRefreshing(false);
        }
    }
}
