package com.sien.aimanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.CoverAdapter;
import com.sien.aimanager.config.AppConfig;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.widgets.recyclerview.CPDividerGridItemDecoration;
import com.sien.lib.datapp.beans.CoverIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sien
 * @date 2017/2/7
 * @descript 选择封面图片
 */
public class SelectCoverAvtivity extends CPBaseBoostActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_cover);
    }

    @Override
    public int getBoostToolbarResId() {
        return super.getBoostToolbarResId();
    }

    @Override
    public void initViews() {
        super.initViews();

        recyclerView = findView(R.id.selectCoverList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(gridLayoutManager);

        CPDividerGridItemDecoration decoration = new CPDividerGridItemDecoration(this);
        recyclerView.addItemDecoration(decoration);
    }

    @Override
    public void initial() {
        super.initial();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("选择封面图片");
        }

        CoverAdapter adapter = new CoverAdapter(recyclerView,getCoverDatasource());
        adapter.setOnItemClickListener(new CPBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                setSelectedCoverAndReturn((String) data);
            }
        });
        recyclerView.setAdapter(adapter);

        showContentLayout();
    }

    /*设置选中图片并返回*/
    private void setSelectedCoverAndReturn(String cover){
        Intent intent = new Intent();
        intent.putExtra("selectCover",cover);
        setResult(AppConfig.REQUEST_CODE_SELECTCOVER,intent);
        finish();
    }

    private List<String> getCoverDatasource(){
        List<String> ds = new ArrayList<>();
        ds.add(CoverIcon.ICON_DAY.getValue());
        ds.add(CoverIcon.ICON_WEEK.getValue());
        ds.add(CoverIcon.ICON_MONTH.getValue());
        ds.add(CoverIcon.ICON_SEASON.getValue());
        ds.add(CoverIcon.ICON_HALFYEAR.getValue());
        ds.add(CoverIcon.ICON_YEAR.getValue());
        ds.add(CoverIcon.ICON_OTHER.getValue());
        return ds;
    }

    @Override
    public <V extends BasePresenter> V createPresenter() {
        return null;
    }
}
