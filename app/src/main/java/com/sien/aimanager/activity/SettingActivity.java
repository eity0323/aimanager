package com.sien.aimanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.activity.CPBaseActivity;
import com.sien.lib.baseapp.adapter.CPBaseRecyclerAdapter;
import com.sien.lib.baseapp.utils.CPPatternUtil;
import com.sien.lib.baseapp.widgets.CPRefreshView;
import com.sien.lib.baseapp.widgets.recyclerview.CPDividerItemDecoration;
import com.sien.lib.component.imageview.CircleImageView;
import com.sien.lib.databmob.config.DatappConfig;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.utils.CPStringUtil;
import com.sien.lib.share.ShareConfig;
import com.sien.lib.share.ShareDialog;
import com.sien.lib.share.action.CPAppShareAction;
import com.sien.lib.share.action.CPAppShareBuilder;

import cn.qqtheme.framework.picker.TimePicker;

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

    private TimePicker pvTime;
    private String remindTime = "";

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
                    showSharePanel();
                }else if (position == 1){
//                    go2FeedbackActivity();
//                }else if (position == 2){
                    go2AboutActivity();
                }else if (position == 2){
                    settingRemindTime();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        if (CPPatternUtil.isMobileNumber(DatappConfig.userAccount)){
            nameTV.setText(CPStringUtil.serectMobileNumber(DatappConfig.userAccount));
        }else {
            nameTV.setText(DatappConfig.userAccount);
        }

        presenter.requestUserInfo(DatappConfig.userAccount);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.person_name){
                checkAndLogin();
            }else if(vid == R.id.btn_person_edit_profile){

            }else if (vid == R.id.person_avatar){
                //显示大图
            }
        }
    };

    /*设置提醒时间*/
    private void settingRemindTime(){
        if (!TextUtils.isEmpty(remindTime)) {
            String ay[] = remindTime.split(":");
            if (ay.length > 1){
                onTimePicker(Integer.valueOf(ay[0]),Integer.valueOf(ay[1]));
                return;
            }if (ay.length > 0){
                onTimePicker(Integer.valueOf(ay[0]),0);
                return;
            }
        }
        onTimePicker(8,0);
    }

    /*校验、登录*/
    private void checkAndLogin(){
        if(!checkLoginStatus()){
            startActivity(new Intent(this,LoginActivity.class));
        }else {
            startActivity(new Intent(this,UserInfoActivity.class));
        }
    }

    /**
     * 校验登录状态
     * @return
     */
    private boolean checkLoginStatus(){
        String logStatus = BaseApplication.getSharePerfence(DatappConfig.LOGIN_STATUS_KEY);
        if (!"true".equals(logStatus)){
            return false;
        }

        return true;
    }

    /*显示分享面板*/
    private void showSharePanel(){
        CPAppShareAction shareAction = new CPAppShareBuilder().buildShareLink(this,"http://www.shoelives.com", ShareConfig.SHARE_APP_TITLE,R.mipmap.ic_launcher);
        ShareDialog shareDialog = new ShareDialog(this,shareAction);
        shareDialog.show();
    }

    /*初始化日期选择*/
    public void onTimePicker(int hour,int minute) {
        if (pvTime == null) {
            pvTime = new TimePicker(this,TimePicker.HOUR_24);
            pvTime.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                @Override
                public void onTimePicked(String hour, String minute) {
                    remindTime = hour+ ":" + minute;

                    BaseApplication.setSharePerfence(DatappConfig.REMIND_TIME_KEY,remindTime);
                }
            });
            pvTime.show();
        }else{
            pvTime.setSelectedItem(hour,minute);
            pvTime.show();
        }
    }

    /*跳转至关于页面*/
    private void go2AboutActivity(){
        startActivity(new Intent(this,AboutActivity.class));
    }

    /*跳转至意见反馈页面*/
    private void go2FeedbackActivity(){
        startActivity(new Intent(this,FeedbackActivity.class));
    }

    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            if (presenter != null) {
                presenter.requestUserInfo(DatappConfig.userAccount);
            }
        }
    };

    @Override
    public SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    public void refreshUserInfo(RequestFreshStatus status) {
        if (status != RequestFreshStatus.REFRESH_ERROR){
            if (CPPatternUtil.isMobileNumber(DatappConfig.userAccount)){
                nameTV.setText(CPStringUtil.serectMobileNumber(DatappConfig.userAccount));
            }else {
                nameTV.setText(DatappConfig.userAccount);
            }
        }
    }

    @Override
    public void refreshLogout(RequestFreshStatus status) {

    }
}
