package com.sien.aimanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sien.aimanager.R;
import com.sien.aimanager.config.AppConfig;
import com.sien.aimanager.model.INewAimTypeViewModel;
import com.sien.aimanager.presenter.NewAimTypePresenter;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.datapp.utils.CPDateUtil;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import java.util.Date;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * @author sien
 * @date 2017/2/5
 * @descript 添加目标分类
 */
public class NewAimTypeActivity extends CPBaseBoostActivity implements INewAimTypeViewModel{

    private EditText titleET,descET;
    private SwitchCompat switchCompat;
    private ImageView coverIV;
    private TextView periodTV,priortyTV,startTV,endTV;
    private NewAimTypePresenter presenter;

    private OptionPicker periodPicker;
    private OptionPicker priorityPicker;

    private String cover;
    private int period = 1,priorty = 5;
    private boolean recyclable = false;
    private boolean modifyModel = false;//是否为修改模式，false 新建  true 修改
    private AimTypeVO aimTypeVO;

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
        startTV = findView(R.id.aimtype_start);
        endTV = findView(R.id.aimtype_end);

        findView(R.id.layout_cover).setOnClickListener(clickListener);
        findView(R.id.layout_period).setOnClickListener(clickListener);
        findView(R.id.layout_priorty).setOnClickListener(clickListener);
        findView(R.id.layout_start).setOnClickListener(clickListener);
        findView(R.id.layout_end).setOnClickListener(clickListener);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recyclable = isChecked;
            }
        });
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("新建目标分类");
        }

        parseBundleData();

        showContentLayout();
    }

    private void parseBundleData(){
        if (getIntent().hasExtra("ds")){
            modifyModel = true;

            aimTypeVO = (AimTypeVO) getIntent().getSerializableExtra("ds");
            if (aimTypeVO != null){
                initDisplayAimTypeData(aimTypeVO);
            }
        }else {
            modifyModel = false;

            startTV.setText(CPDateUtil.getDateString(new Date()));
            endTV.setText("");
        }
    }

    /*显示初始值*/
    private void initDisplayAimTypeData(AimTypeVO aimTypeVO){
        titleET.setText(aimTypeVO.getTypeName());
        descET.setText(aimTypeVO.getDesc());

        recyclable = aimTypeVO.getRecyclable().booleanValue();
        switchCompat.setChecked(recyclable);

        if (!recyclable) {
            findView(R.id.layout_period).setVisibility(View.GONE);
            findView(R.id.layout_recyclerable).setVisibility(View.GONE);
        }

        if (aimTypeVO.getTargetPeriod() != null) {
            period = aimTypeVO.getTargetPeriod();
            periodTV.setText(getPeriodTextByIndex(getIndexByPeriod(aimTypeVO.getTargetPeriod())));
        }
        if (aimTypeVO.getPriority() != null) {
            priorty = aimTypeVO.getPriority();

            priortyTV.setText(getPriorityTextByIndex(getIndexByPriority(aimTypeVO.getPriority())));
        }

        if (aimTypeVO.getStartTime() != null){
            startTV.setText(CPDateUtil.getDateString(aimTypeVO.getStartTime()));
        }else {
            startTV.setText(CPDateUtil.getDateString(new Date()));
        }

        if (aimTypeVO.getEndTime() != null){
            endTV.setText(CPDateUtil.getDateString(aimTypeVO.getEndTime()));
        }else {
            endTV.setText("");
        }

        if (!TextUtils.isEmpty(aimTypeVO.getCover())) {
            cover = aimTypeVO.getCover();

            ImageLoader.getInstance().displayImage(aimTypeVO.getCover(), coverIV);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int vid = v.getId();
            if (vid == R.id.layout_cover){
                go2SelectCoverActivity();
            }else if (vid == R.id.layout_period){
                showSelectPeriodPanel();
            }else if (vid == R.id.layout_priorty){
                showSelectPriortyPanel();
            }
        }
    };

    /*选择封面图片*/
    private void go2SelectCoverActivity(){
        Intent intent = new Intent(this,SelectCoverAvtivity.class);
        if (aimTypeVO != null && !TextUtils.isEmpty(aimTypeVO.getCover())){
            intent.putExtra("cover",aimTypeVO.getCover());
        }
        startActivityForResult(intent, AppConfig.REQUEST_CODE_SELECTCOVER);
    }

    /*周期面板*/
    private void showSelectPeriodPanel(){
        int tempPeriod = 1;
        if (aimTypeVO != null){
            tempPeriod = aimTypeVO.getTargetPeriod();
        }
        int periodIndex = getIndexByPeriod(tempPeriod);
        String titles[] = getResources().getStringArray(R.array.aimtype_period_source);
        if (periodPicker == null) {
            periodPicker = new OptionPicker(this,titles);
            periodPicker.setSelectedIndex(periodIndex);
            periodPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int position, String option) {
                    period = getPeriodByIndex(position);

                    periodTV.setText(getPeriodTextByIndex(position));
                }
            });
        }else {
            periodPicker.setSelectedIndex(periodIndex);
        }
        periodPicker.show();
    }

    private int getIndexByPeriod(int period){
        int position = 0;
        if (period == 1){
            position = 0;
        }else if (period == 7){
            position = 1;
        }else if (period == 30){
            position = 2;
        }else if (period == 90){
            position = 3;
        }else if (period == 180){
            position = 4;
        }else if (period == 365){
            position = 5;
        }
        return position;
    }

    private int getPeriodByIndex(int position){
        int period = 1;
        if (position == 0){
            period = 1;
        }else if (position == 1){
            period = 7;
        }else if (position == 2){
            period = 30;
        }else if (position == 3){
            period = 90;
        }else if (position == 4){
            period = 180;
        }else if (position == 5){
            period = 365;
        }
        return period;
    }

    private String getPeriodTextByIndex(int position){
        String period = "1天";
        if (position == 0){
            period = "1天";
        }else if (position == 1){
            period = "1周";
        }else if (position == 2){
            period = "1月";
        }else if (position == 3){
            period = "1季";
        }else if (position == 4){
            period = "半年";
        }else if (position == 5){
            period = "1年";
        }
        return period;
    }

    /*优先级面板*/
    private void showSelectPriortyPanel(){
        int tempPriority = 5;
        if (aimTypeVO != null){
            tempPriority = aimTypeVO.getPriority();
        }
        int priorityIndex = getIndexByPriority(tempPriority);
        String titles[] = getResources().getStringArray(R.array.aimtype_priorty_source);
        if (priorityPicker == null) {
            priorityPicker = new OptionPicker(this,titles);
            priorityPicker.setSelectedIndex(priorityIndex);
            priorityPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int position, String option) {
                    priorty = getPriorityByIndex(position);

                    priortyTV.setText(getPriorityTextByIndex(position));
                }
            });
        }else {
            priorityPicker.setSelectedIndex(priorityIndex);
        }
        priorityPicker.show();
    }

    private int getIndexByPriority(int priority){
        int position = 0;
        if (priority == 1){
            position = 4;
        }else if (priority == 2){
            position = 3;
        }else if (priority == 3){
            position = 2;
        }else if (priority == 4){
            position = 1;
        }else if (priority == 5){
            position = 0;
        }
        return position;
    }

    private int getPriorityByIndex(int position){
        int priority = 5;
        if (position == 0){
            priority = 5;
        }else if (position == 1){
            priority = 4;
        }else if (position == 2){
            priority = 3;
        }else if (position == 3){
            priority = 2;
        }else if (position == 4){
            priority = 1;
        }
        return priority;
    }

    private String getPriorityTextByIndex(int position){
        String priority = "5级";
        if (position == 0){
            priority = "5级";
        }else if (position == 1){
            priority = "4级";
        }else if (position == 2){
            priority = "3级";
        }else if (position == 3){
            priority = "2级";
        }else if (position == 4){
            priority = "1级";
        }
        return priority;
    }

    /*校验并添加记录*/
    private void checkAndInsertAimType(){
        String title = titleET.getText().toString();
        if (TextUtils.isEmpty(title)){
            showToast("分类名称不能为空");
            return;
        }

        String desc = descET.getText().toString();

        if (!modifyModel){
            aimTypeVO = new AimTypeVO();
            aimTypeVO.setCustomed(true);
            aimTypeVO.setFinishPercent(0);
            aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
            aimTypeVO.setStartTime(new Date());
        }

        aimTypeVO.setTypeName(title);
        aimTypeVO.setDesc(desc);

        aimTypeVO.setModifyTime(new Date());

        aimTypeVO.setPeriod(period);
        aimTypeVO.setPriority(priorty);

        aimTypeVO.setRecyclable(recyclable);
        aimTypeVO.setPlanProject(false);
        aimTypeVO.setTargetPeriod(period);
        aimTypeVO.setCover(cover);

        if (presenter != null) {
            presenter.insertOrReplaceAimTypeRecord(aimTypeVO);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.REQUEST_CODE_SELECTCOVER){
            if (resultCode == AppConfig.REQUEST_CODE_SELECTCOVER){
                if (data != null && data.hasExtra("selectCover")){
                    cover = data.getStringExtra("selectCover");

                    //预览图片
                    if (!TextUtils.isEmpty(cover)){
                        ImageLoader.getInstance().displayImage(cover,coverIV);
                    }
                }
            }
        }
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
