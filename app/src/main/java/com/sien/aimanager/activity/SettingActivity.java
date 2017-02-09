package com.sien.aimanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.SettingAdapter;
import com.sien.aimanager.model.ISettingViewModel;
import com.sien.aimanager.presenter.SettingPresenter;
import com.sien.lib.baseapp.activity.CPBaseActivity;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.widgets.CPRefreshView;
import com.sien.lib.baseapp.widgets.recyclerview.CPDividerItemDecoration;
import com.sien.lib.datapp.network.base.RequestFreshStatus;
import com.sien.libcomponent.widgets.CircleImageView;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 设置页
 */
public class SettingActivity extends CPBaseActivity implements ISettingViewModel {
    private RecyclerView recyclerView;
    private CPRefreshView refreshView;

    private SettingPresenter presenter;
    private FrameLayout headLayout;
    private LinearLayout profileLayout;

    private CircleImageView avatarIV;
    private Button btn_editProfile;
    private TextView nameTV,profileTV;
    private ImageView headerBgIV;

    private SettingAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initViews() {
        super.initViews();

        btn_editProfile =findView(R.id.btn_person_edit_profile);
        avatarIV = findView(R.id.person_avatar);
        nameTV = findView(R.id.person_name);
        profileTV = findView(R.id.main_profile);
        headerBgIV = findView(R.id.person_header_bg);

        nameTV.setOnClickListener(clickListener);
        avatarIV.setOnClickListener(clickListener);

        btn_editProfile.setOnClickListener(clickListener);

        headLayout = findView(R.id.person_header_layout);
        headLayout.setFocusableInTouchMode(true);
        headLayout.setFocusable(true);
        headLayout.requestFocus();

        refreshView = findView(R.id.refreshPersonal);
        refreshView.setColorSchemeColors(ContextCompat.getColor(this,R.color.red_pink));
        refreshView.setOnRefreshListener(refreshListener);

        profileLayout = findView(R.id.main_profile_layout);

        recyclerView = findView(R.id.person_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        CPDividerItemDecoration divider = new CPDividerItemDecoration(this,CPDividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(divider);
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        adapter = new SettingAdapter(recyclerView,presenter.getSettingDatas());
        adapter.setOnItemClickListener(new CPBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object data, int position) {
                if (position == 0){

                }else if (position == 1){

                }else if (position == 2){
                    go2AboutActivity();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        presenter.requestUserInfo();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.person_name){

            }else if(vid == R.id.btn_person_edit_profile){

            }else if (vid == R.id.person_avatar){
                //显示大图
            }
        }
    };

    private void go2AboutActivity(){
        startActivity(new Intent(this,AboutActivity.class));
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (presenter != null) {
                presenter.requestUserInfo();
            }
        }
    };

    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    public void refreshUserInfo(RequestFreshStatus status) {

    }

    @Override
    public void refreshLogout(RequestFreshStatus status) {

    }
}