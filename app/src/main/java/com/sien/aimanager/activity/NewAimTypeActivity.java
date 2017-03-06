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
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.network.base.RequestFreshStatus;
import com.sien.lib.datapp.utils.CPDateUtil;

import java.util.Date;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * @author sien
 * @date 2017/2/5
 * @descript 添加目标分类
 */
public class NewAimTypeActivity extends CPBaseBoostActivity implements INewAimTypeViewModel{

    private EditText titleET,descET;
    private SwitchCompat switchCompat,activeCompat;
    private ImageView coverIV;
    private TextView periodTV,priortyTV,startTV,endTV;
    private NewAimTypePresenter presenter;

    private OptionPicker periodPicker;
    private OptionPicker priorityPicker;

    private String cover;
    private int period = 1,priorty = 5;
    private boolean recyclable = false;//是否可循环
    private boolean activable = true;//是否激活目标
    private boolean modifyModel = false;//是否为修改模式，false 新建  true 修改
    private AimTypeVO aimTypeVO;
    private boolean showFixType = false;//创建固定项分类

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
        activeCompat = findView(R.id.aimtype_active);
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

        activeCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                activable = isChecked;
            }
        });
    }

    @Override
    public void initial() {
        super.initial();

        presenter = getPresenter();

        parseBundleData();

        if (getSupportActionBar() != null) {
            if (modifyModel){
                getSupportActionBar().setTitle("编辑目标分类");
            }else {
                getSupportActionBar().setTitle("新建目标分类");
            }
        }

        showContentLayout();
    }

    private void parseBundleData(){
        //编辑目标分类
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

        //新建固定（循环）or 自动创建（不可循环）目标分类
        if (getIntent().hasExtra("showFixType")){
            showFixType = getIntent().getBooleanExtra("showFixType",false);

            switchCompat.setChecked(showFixType);
        }

        displayTypeStatus();
    }

    /*根据分类类型判断显示状态（编辑模式）*/
    private void displayTypeStatus(){
        if (showFixType){
            findView(R.id.layout_active).setVisibility(View.VISIBLE);
            findView(R.id.layout_end).setVisibility(View.VISIBLE);
            findView(R.id.layout_period).setVisibility(View.VISIBLE);
            findView(R.id.layout_recyclerable).setVisibility(View.VISIBLE);
        }else {
            findView(R.id.layout_period).setVisibility(View.GONE);
            findView(R.id.layout_recyclerable).setVisibility(View.GONE);
            findView(R.id.layout_active).setVisibility(View.GONE);
            findView(R.id.layout_end).setVisibility(View.GONE);

            findView(R.id.layout_start).setEnabled(false);
        }
    }

    /*显示初始值*/
    private void initDisplayAimTypeData(AimTypeVO aimTypeVO){
        titleET.setText(aimTypeVO.getTypeName());
        descET.setText(aimTypeVO.getDesc());

        if (aimTypeVO.getRecyclable() != null) {
            recyclable = aimTypeVO.getRecyclable().booleanValue();
        }
        switchCompat.setChecked(recyclable);

        if (aimTypeVO.getActive() != null) {
            activable = aimTypeVO.getActive().booleanValue();
        }
        activeCompat.setChecked(activable);

        if (!recyclable) {
            findView(R.id.layout_period).setVisibility(View.GONE);
            findView(R.id.layout_active).setVisibility(View.GONE);
        }

        if (aimTypeVO.getTargetPeriod() != null) {
            period = aimTypeVO.getTargetPeriod();
            periodTV.setText(presenter.getPeriodTextByIndex(presenter.getIndexByPeriod(aimTypeVO.getTargetPeriod())));
        }
        if (aimTypeVO.getPriority() != null) {
            priorty = aimTypeVO.getPriority();

            priortyTV.setText(presenter.getPriorityTextByPriority(aimTypeVO.getPriority()));
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
        }else {
            cover = "drawable://" + R.mipmap.icon_day_en;
        }

        ImageLoader.getInstance().displayImage(cover, coverIV);
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
        int periodIndex = presenter.getIndexByPeriod(tempPeriod);
        String titles[] = getResources().getStringArray(R.array.aimtype_period_source);
        if (periodPicker == null) {
            periodPicker = new OptionPicker(this,titles);
            periodPicker.setSelectedIndex(periodIndex);
            periodPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int position, String option) {
                    period = presenter.getPeriodByIndex(position);

                    periodTV.setText(presenter.getPeriodTextByIndex(position));
                }
            });
        }else {
            periodPicker.setSelectedIndex(periodIndex);
        }
        periodPicker.show();
    }

    /*优先级面板*/
    private void showSelectPriortyPanel(){
        int tempPriority = 5;
        if (aimTypeVO != null){
            tempPriority = aimTypeVO.getPriority();
        }
        int priorityIndex = presenter.getIndexByPriority(tempPriority);
        String titles[] = getResources().getStringArray(R.array.aimtype_priorty_source);
        if (priorityPicker == null) {
            priorityPicker = new OptionPicker(this,titles);
            priorityPicker.setSelectedIndex(priorityIndex);
            priorityPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                @Override
                public void onOptionPicked(int position, String option) {
                    priorty = presenter.getPriorityByIndex(position);

                    priortyTV.setText(presenter.getPriorityTextByIndex(position));
                }
            });
        }else {
            priorityPicker.setSelectedIndex(priorityIndex);
        }
        priorityPicker.show();
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
        }
        //激活自动创建
        aimTypeVO.setActive(activable);

        //开始时间
        String startTime = startTV.getText().toString();
        Date startDate;
        if (!TextUtils.isEmpty(startTime)){
            startDate = CPDateUtil.getDateByParse(startTime,CPDateUtil.DATE_FORMAT_DEFAULT);
        }else {
            startDate = new Date();
        }
        aimTypeVO.setStartTime(startDate);

        //结束时间
        String endTime = endTV.getText().toString();
        Date endDate;
        if (!TextUtils.isEmpty(endTime)){
            endDate = CPDateUtil.getDateByParse(startTime,CPDateUtil.DATE_FORMAT_DEFAULT);
            aimTypeVO.setEndTime(endDate);
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
            setResult(AppConfig.RESULT_CODE_OK);

            finish();
        }else {
            showToast("添加失败");
        }
    }
}
