package com.sien.aimanager.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.model.INewAimTypeViewModel;
import com.sien.aimanager.presenter.NewAimTypePresenter;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import java.util.Date;

/**
 * @author sien
 * @date 2017/2/5
 * @descript 添加目标分类
 */
public class NewAimTypeActivity extends CPBaseBoostActivity implements INewAimTypeViewModel{
    private EditText titleET,descET;
    private SwitchCompat switchCompat;
    private ImageView coverIV;
    private TextView periodTV,priortyTV;
    private NewAimTypePresenter presenter;

    private String cover;
    private int period,priorty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_aimtype);
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
    public NewAimTypePresenter createPresenter() {
        return new NewAimTypePresenter(this);
    }

    @Override
    public void initViews() {
        super.initViews();

        titleET = findView(R.id.aimtype_title);
        descET = findView(R.id.aimtype_desc);
        switchCompat = findView(R.id.aimtype_recyclerable);
        coverIV = findView(R.id.aimtype_cover);
        periodTV = findView(R.id.aimtype_period);
        priortyTV = findView(R.id.aimtype_priorty);

        findView(R.id.layout_cover).setOnClickListener(clickListener);
        findView(R.id.layout_period).setOnClickListener(clickListener);
        findView(R.id.layout_priorty).setOnClickListener(clickListener);
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        showContentLayout();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.layout_cover){
                go2SelectCoverActivity();
            }else if (vid == R.id.layout_period){

            }else if (vid == R.id.layout_priorty){

            }
        }
    };

    private void go2SelectCoverActivity(){

    }

    /*校验并添加记录*/
    private void checkAndInsertAimType(){
        String title = titleET.getText().toString();
        if (TextUtils.isEmpty(title)){
            showToast("分类名称不能为空");
            return;
        }

        String desc = descET.getText().toString();
        boolean recyclable = switchCompat.getShowText();

        AimTypeVO aimTypeVO = new AimTypeVO();
        aimTypeVO.setCustomed(true);
        aimTypeVO.setDesc(desc);
        aimTypeVO.setFinishPercent(0);
        aimTypeVO.setFinishStatus(0);
        aimTypeVO.setPeriod(period);
        aimTypeVO.setModifyTime(new Date());
        aimTypeVO.setPriority(priorty);
        aimTypeVO.setRecyclable(recyclable);
        aimTypeVO.setTargetPeriod(period);
        aimTypeVO.setCover(cover);
        aimTypeVO.setTypeName(title);

        if (presenter != null) {
            presenter.insertAimTypeRecord(aimTypeVO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit){
            checkAndInsertAimType();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void refreshInsertAimType(RequestFreshStatus status) {
        if (status != RequestFreshStatus.REFRESH_ERROR){
            //添加成功后，重新请求数据
            presenter.searchAimTypeRecord();

            finish();
        }else {
            showToast("添加失败");
        }
    }
}
