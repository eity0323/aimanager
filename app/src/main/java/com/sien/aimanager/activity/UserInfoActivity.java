package com.sien.aimanager.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.model.IUserInfoViewModel;
import com.sien.aimanager.presenter.UserInfoPresenter;
import com.sien.lib.baseapp.BaseApplication;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.beans.APPOperateEventBundle;
import com.sien.lib.baseapp.control.photo.CropHandler;
import com.sien.lib.baseapp.control.photo.CropHelper;
import com.sien.lib.baseapp.control.photo.CropParams;
import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.baseapp.utils.CPImageUtil;
import com.sien.lib.baseapp.utils.RepeatClickUtil;
import com.sien.lib.component.imageview.CircleImageView;
import com.sien.lib.databmob.beans.UserInfoVO;
import com.sien.lib.databmob.beans.UserResult;
import com.sien.lib.databmob.config.DatappConfig;
import com.sien.lib.databmob.network.base.RequestFreshStatus;
import com.sien.lib.databmob.utils.CPDeviceUtil;
import com.sien.lib.databmob.utils.CPFileUtils;
import com.sien.lib.databmob.utils.EventPostUtil;
import com.sien.lib.photopick.activity.MISActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author sien
 * @date 2017/3/27
 * @descript 用户主页
 */
public class UserInfoActivity extends CPBaseBoostActivity implements IUserInfoViewModel {
    private CircleImageView avatarIV;
    private TextView birthTV,sexTV,nicknameTV,mobileTV;//nicknameTV,
    private LinearLayout mobileBindLy;

    private TextView introduceTV;

    private CropParams mCropParams;
    //    private DatePicker pvTime;
    private DatePickerDialog pvTime;
    private static final int TYPE_CHOOSE_IMAGE = 0x00001;//修改头像
    private static final int TYPE_GENDER = 0x00002;//修改性别
    private final int REQUEST_IMAGE = 1025;//从图库选择图片

    private UserInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_userinfo);

    }

    @Override
    protected void onDestroy() {
        mCropParams = null;
        pvTime = null;
        cropHandler = null;

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropHelper.REQUEST_CROP || requestCode == CropHelper.REQUEST_CAMERA || requestCode == CropHelper.REQUEST_CAMERA_CROP) {
            CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
        }else if (requestCode == RC_SETTINGS_SCREEN) {
            boolean hasCameraPermissions = EasyPermissions.hasPermissions(UserInfoActivity.this,receiver_camera_storage );
            if (hasCameraPermissions){
                //设置页返回
            }
        }else if (requestCode == REQUEST_IMAGE){
            if (data == null)   return;

            List<String> imglist = data.getStringArrayListExtra(MISActivity.EXTRA_RESULT);
            if (imglist != null && imglist.size() > 0) {
                String uploadImagePath = imglist.get(0);

                Intent intent;
                mCropParams.uri = CropHelper.buildUri("file://"+ uploadImagePath);
                if (!TextUtils.isEmpty(CPDeviceUtil.getBrand()) && CPDeviceUtil.getBrand().toLowerCase().contains("meizu")){
                    intent = CropHelper.buildCropIntentMeiZu("com.android.camera.action.CROP",mCropParams);
                }else {
                    intent = CropHelper.buildCropFromUriIntent(mCropParams);
                }
                startActivityForResult(intent,  CropHelper.REQUEST_CROP);
                //裁剪图片


                //选择图片直接上传
//                if (!TextUtils.isEmpty(uploadImagePath)) {
//                    PersonalRequestAction.requestUploadImage(UserInfoActivity.this, new File(uploadImagePath), "users");
//                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(pvTime != null && pvTime.isShowing()){
                pvTime.dismiss();

                saveAndFinish();

                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public UserInfoPresenter createPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
        super.onBackPressed();
    }

    public void initViews(){
        Toolbar toolbar = findView(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.personal_account_info);
        findView(R.id.layout_modify_head).setOnClickListener(clickListener);
        findView(R.id.layout_modify_nickname).setOnClickListener(clickListener);
        findView(R.id.layout_modify_birth).setOnClickListener(clickListener);
        findView(R.id.layout_modify_sex).setOnClickListener(clickListener);
        findView(R.id.layout_modify_introduce).setOnClickListener(clickListener);//修改个人简介
        findView(R.id.logoutBtn).setOnClickListener(clickListener);

        mobileBindLy = findView(R.id.layout_bind_mobile);
        mobileBindLy.setOnClickListener(clickListener);//绑定手机号

        mobileTV = findView(R.id.userinfo_mobile);

        avatarIV = findView(R.id.userinfo_avatar);

        birthTV = findView(R.id.userinfo_birth);
        sexTV = findView(R.id.userinfo_sex);
        findView(R.id.layout_modify_introduce).setVisibility(View.VISIBLE);
        introduceTV = findView(R.id.userinfo_introduce);//个人简介
        nicknameTV = findView(R.id.userinfo_nickname);

    }

    @Override
    public int getBoostToolbarResId() {
        return View.NO_ID;
    }

    public void initial(){
        presenter = getPresenter();

        mCropParams = new CropParams();

        presenter.requestUserInfoFromNetwork();
    }

    //编辑用户
    public void showAlertMenu(final int type){
        String[] titles = null;
        if (type == TYPE_CHOOSE_IMAGE) {
            titles = getResources().getStringArray(
                    R.array.choose_image_source);
        } else if (type == TYPE_GENDER) {
            titles = getResources().getStringArray(R.array.gender_source);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setItems(titles, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int itemPosition) {
                if (itemPosition == 0) {
                    if (type == TYPE_CHOOSE_IMAGE) {
                        takePicture();//拍照
                    } else if (type == TYPE_GENDER) {
                        doModifyGender(0);//男
                    }
                } else if (itemPosition == 1) {
                    if (type == TYPE_CHOOSE_IMAGE) {
                        chooseImageFromAlbum();//从相册获取
                    } else if (type == TYPE_GENDER) {
                        doModifyGender(1);//女
                    }
                } else if (itemPosition == 2) {
                    if (type == TYPE_GENDER) {
                        doModifyGender(2);//保密
                    }
                }
            }
        });
        builder.create().show();
    }

    /*设置返回码并关闭页面*/
    private void saveAndFinish(){
        setResult(1632);
        finish();
    }

    /*初始化日期选择*/
    public void onYearMonthDayPicker(Date birth) {
        if (birth == null){
            //无生日数据时，默认为1990-01-01
            Calendar calendar = Calendar.getInstance();
            calendar.set(1990,0,1);
            birth = calendar.getTime();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(birth);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        Calendar calendarNow = Calendar.getInstance();
        int nowYear = calendarNow.get(Calendar.YEAR);
        int nowMonth = calendarNow.get(Calendar.MONTH);
        int nowDay = calendarNow.get(Calendar.DAY_OF_MONTH);

        if (pvTime == null) {

            pvTime = new DatePickerDialog(this,R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {//DatePickerDialog.THEME_HOLO_LIGHT旧版简单模式R.style.DialogTheme日历模式
                @Override
                public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    //初始化的月是1开始的，选择的月是0开始的
                    String birth = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;

                    if (birthTV != null) {
                        birthTV.setText(birth);
                    }

                    if (presenter != null) {
//                        UserInfoVO userInfoVO = presenter.getUserInfo();
//                        if (userInfoVO != null) {
//                            userInfoVO.setBirthday(CPDateUtil.getDateByParse(birth, CPDateUtil.DATE_FORMAT_3));
//                        }
//
//                        doUpdateUserInfo(userInfoVO);
                    }
                }
            }, year, month, day);
            pvTime.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            pvTime.getDatePicker().setMaxDate(calendarNow.getTimeInMillis());

        }else{

        }
    }

    /*拍照*/
    private void takePicture() {
        if (CPDeviceUtil.hasSdcard()) { // 判断是否存在sd卡,直接调用系统的照相机
            if (mCropParams != null) {
                String fileName = CropHelper.CROP_CACHE_FILE_NAME + String.valueOf(System.currentTimeMillis()) + ".jpg";
                mCropParams.uri = CropHelper.buildUri(CPFileUtils.getAppCacheRootDirectory(this),fileName);

                Intent intent = CropHelper.buildCaptureIntent(mCropParams.uri);
                startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
            }
        } else {
            showToast(R.string.sdcard_tips);
        }
    }

    /*相册*/
    private void chooseImageFromAlbum() {
//        startActivityForResult(CropHelper.buildCropFromGalleryIntent(mCropParams),CropHelper.REQUEST_CROP);
        if (CPDeviceUtil.hasSdcard()) {
            Intent intent = new Intent(UserInfoActivity.this,MISActivity.class);
            intent.putExtra(MISActivity.EXTRA_SHOW_CAMERA, true);
            intent.putExtra(MISActivity.EXTRA_SELECT_MODE, MISActivity.MODE_SINGLE);//单选
            String storageDir = CPFileUtils.getInnerCacheFilePath(UserInfoActivity.this);//CPConfiguration.getAppRootDirectory(FeedbackActivity.this).getAbsolutePath();
            intent.putExtra(MISActivity.EXTRA_SELECT_COUNT,1);
            intent.putExtra(MISActivity.EXTRA_IMAGE_CACHE_DIR, storageDir);

            startActivityForResult(intent, REQUEST_IMAGE);
        }
    }

    /*修改性别*/
    private void doModifyGender(int gender) {
//        if (gender == UserInfoVO.GENDER_MALE) {
//            sexTV.setText(R.string.personal_male);
//        }else if(gender == UserInfoVO.GENDER_FEMALE){
//            sexTV.setText(R.string.personal_female);
//        }else{
//            sexTV.setText("");
//        }
//
//        //调用修改接口
//
//        if (presenter != null) {
//            UserInfoVO userInfoVO = presenter.getUserInfo();
//            if (userInfoVO != null) {
//                userInfoVO.setGender(gender);
//            }
//
//            doUpdateUserInfo(userInfoVO);
//        }
    }

    /*调用修改接口*/
    private void doUpdateUserInfo(UserInfoVO userInfoVO){
        if (presenter != null) {
            if (userInfoVO != null) {
//                UpdateUserInfoRequest request = new UpdateUserInfoRequest();
//                request.setUserName(userInfoVO.getUserName());
//                request.setGender(userInfoVO.getGender());
//                if (userInfoVO.getBirthday() != null) {
//                    request.setBirthday(CPDateUtil.getDateToString(userInfoVO.getBirthday(),CPDateUtil.DATE_FORMAT_3));
//                }
//                request.setProfile(userInfoVO.getProfile());
//                request.setPhoto(userInfoVO.getPhoto());
//
//                presenter.updateUserInfo(request);
            }
        }
    }

    /*图片裁剪事件*/
    private CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            if (uri != null) {
                //cp.modify 2016-12-27 保存用户头像
                String uploadImagePath = CPImageUtil.save2LocalStorage(UserInfoActivity.this,"temp","", CropHelper.decodeUriAsBitmap(UserInfoActivity.this,uri),true);

                //上传图片
                if (!TextUtils.isEmpty(uploadImagePath)) {
//                    PersonalRequestAction.requestUploadImage(UserInfoActivity.this, new File(uploadImagePath), "users");
                }
            }
        }

        @Override
        public void onCropCancel() {

        }

        @Override
        public void onCropFailed(String message) {
            showToast(message);
        }

        @Override
        public CropParams getCropParams() {
            return mCropParams;
        }

        @Override
        public Activity getContext() {
            return UserInfoActivity.this;
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int vid = view.getId();
            if (vid == R.id.layout_modify_head){
                if (!RepeatClickUtil.isRepeatClick()) {
                    cameraAndStorageAuthorityAsk();
                }

            }else if (vid == R.id.layout_modify_nickname){
                if (!RepeatClickUtil.isRepeatClick()) {
                    String nickname = "";
                    if (presenter.getUserInfo() != null && !TextUtils.isEmpty(presenter.getUserInfo().getUsername())) {
                        nickname = presenter.getUserInfo().getUsername();
                    }
                    go2UserInfoModifyActivity(1, nickname);
                }
            }else if(vid == R.id.layout_modify_birth){
                if (!RepeatClickUtil.isRepeatClick()) {
//                    if (presenter.getUserInfo() != null) {
//                        Date birth = presenter.getUserInfo().getBirthday();
//                        onYearMonthDayPicker(birth);
//                    }
//                    if (pvTime != null) {
//                        pvTime.show();
//                    }
                }

            }else if (vid == R.id.layout_modify_sex){
                if (!RepeatClickUtil.isRepeatClick()) {
                    showAlertMenu(TYPE_GENDER);
                }
            }else if (vid == R.id.layout_bind_mobile){
                //绑定手机号
                if (!RepeatClickUtil.isRepeatClick()) {
                    goBindMobileActivity();
                }
            }else if (vid == R.id.userinfo_avatar){
                if (!RepeatClickUtil.isRepeatClick()) {
                    checkLoginShowAvatar();
                }
            }else if(vid == R.id.layout_modify_introduce){
                //编辑资料
//                if (!RepeatClickUtil.isRepeatClick()) {
//                    String profile = "";
//                    if (presenter.getUserInfo() != null && !TextUtils.isEmpty(presenter.getUserInfo().getProfile())) {
//                        profile = presenter.getUserInfo().getProfile();
//                    }
//                    go2UserInfoModifyActivity(2, profile);
//                }
            }else if (vid == R.id.logoutBtn){
                if (!RepeatClickUtil.isRepeatClick()) {
                    showActionSheet();
                }
            }
        }
    };

    /*跳转至修改昵称页*/
    private void go2UserInfoModifyActivity(int entryType,String value){
//        Intent intent = new Intent(this,UserInfoModifyActivity.class);
//        intent.putExtra("entryType",entryType);
//        intent.putExtra("oldValue",value);
//        startActivity(intent);
    }

    //校验登录状态，预览显示用户头像
    private void checkLoginShowAvatar(){
//        String logstatus = BaseApplication.getSharePerfence(DatappConfig.LOGIN_STATUS_KEY);
//        if ("true".equals(logstatus)) {
//            ArrayList<String> picUrls = new ArrayList<String>();
//            String path = "";
//            if (presenter.getUserInfo() != null && !TextUtils.isEmpty(presenter.getUserInfo().getPhoto())) {
//                path =  DatappConfig.remedyImageAppPath(presenter.getUserInfo().getPhoto());
//            }else{
//                path = CPImageUtil.save2LocalStorage(UserInfoActivity.this,"temp","customer", BitmapFactory.decodeResource(getResources(), R.mipmap.avatar_default_image));
//            }
//            picUrls.add(path);
//            PickImageUtils.showImageDisplay(UserInfoActivity.this, picUrls);
//        }else {
//            startActivity(new Intent(UserInfoActivity.this,LoginActivity.class));
//        }
    }

    /*本地注销*/
    private void doLogout(){
        //登录状态设未false
        BaseApplication.setSharePerfence(DatappConfig.LOGIN_STATUS_KEY, "false");
        BaseApplication.setSharePerfence(DatappConfig.LOGIN_USER_KEY,"");

        getSharedPreferences("cookie", Context.MODE_PRIVATE).edit().clear().commit();

        //游客用户
        DatappConfig.userAccount = "customer";

        postLogoutEvent();

        finish();
    }

    /*发送注销事件*/
    private void postLogoutEvent(){
        APPOperateEventBundle bundle = new APPOperateEventBundle();
        bundle.putString(APPOperateEventBundle.EVENTACTION,"logoutstatus");
        EventPostUtil.post(new BaseAppEvents.APPOperateEvent(BaseAppEvents.STATUS_SUCCESS,bundle));
    }

    /*发送用户信息变更事件*/
    private void postUserInfoChangeEvent(){
        APPOperateEventBundle bundle = new APPOperateEventBundle();
        bundle.putString(APPOperateEventBundle.EVENTACTION,"userInfoChange");
        EventPostUtil.post(new BaseAppEvents.APPOperateEvent(BaseAppEvents.STATUS_SUCCESS,bundle));
    }

    /*注销面板*/
    public void showActionSheet() {
        AlertDialog cancelDialog = new AlertDialog.Builder(this).setMessage(R.string.logout_confirm).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doLogout();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        cancelDialog.show();
    }

    /*跳转至绑定手机页*/
    private void goBindMobileActivity(){

    }

    @Override
    public void refreshUserInfo(RequestFreshStatus status) {
        if(presenter == null)   return;

        showContentLayout();

        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            //显示数据
            UserResult userInfoVO = presenter.getUserInfo();
            if (userInfoVO != null){
//                Date birth = userInfoVO.getBirthday();
//                if (birth != null) {
//                    birthTV.setText(CPDateUtil.getDateToString(birth,CPDateUtil.DATE_FORMAT_3));
//
//                    onYearMonthDayPicker(birth);
//                }
//
//                if (userInfoVO.getGender() == UserInfoVO.GENDER_MALE) {
//                    sexTV.setText(R.string.personal_male);
//                }else if(userInfoVO.getGender() == UserInfoVO.GENDER_FEMALE){
//                    sexTV.setText(R.string.personal_female);
//                }else{
//                    sexTV.setText("");
//                }
//
//                //设置用户头像
//                if (!TextUtils.isEmpty(userInfoVO.getPhoto())){
//                    String path = DatappConfig.remedyImageAppPath(userInfoVO.getPhoto());
////                    ImageLoader.getInstance().displayImage(path,avatarIV);
//                    //三星手机拍照图片无法显示
//                    Glide.with(this).load(path).asBitmap().placeholder(R.drawable.placeholder).centerCrop().into(avatarIV);
//                }
//
//                if (!TextUtils.isEmpty(userInfoVO.getProfile())){
//                    introduceTV.setText(userInfoVO.getProfile());
//                    introduceTV.setVisibility(View.VISIBLE);
//                }else {
//                    introduceTV.setVisibility(View.GONE);
//                }
//
//                if (!TextUtils.isEmpty(userInfoVO.getMobile())){
//                    mobileTV.setText(CPStringUtil.serectMobileNumber(userInfoVO.getMobile()));
//                    mobileBindLy.setVisibility(View.VISIBLE);
//                }else {
//                    mobileBindLy.setVisibility(View.GONE);
//                }
//
//                String name = userInfoVO.getUserName();
//                if (TextUtils.isEmpty(name)){
//                    name = CPStringUtil.serectMobileNumber(userInfoVO.getMobile());
//                }
//                nicknameTV.setText(name);
//                //统计进入用户信息
//                HashMap<String,String> map = new HashMap<String,String>();
//                map.put("gender",sexTV.getText().toString());
//                MobclickAgent.onEvent(this, "044", map);
            }
        }else {
            logDebug("获取用户详情失败");
        }
    }

    @Override
    public void refreshUpdateUserInfo(RequestFreshStatus status) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            logDebug("更新成功");

            //更新成功后重新获取数据
            if (presenter != null) {
                presenter.requestUserInfoFromNetwork();
            }
        }else {
            showToast("更新失败");
        }
    }

    @Override
    public void refresUploadImage(RequestFreshStatus status, String filePath) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            //更新用户头像
            if (!TextUtils.isEmpty(filePath)){

//                String path = DatappConfig.remedyImageAppPath(filePath);
////                ImageLoader.getInstance().displayImage(path,avatarIV);
//                Glide.with(this).load(path).asBitmap().placeholder(R.drawable.placeholder).centerCrop().into(avatarIV);
//
//                //修改用户信息
//                if (presenter != null) {
//                    UserInfoVO userInfoVO = presenter.getUserInfo();
//                    if (userInfoVO != null) {
//                        userInfoVO.setPhoto(filePath);
//                    }
//
//                    doUpdateUserInfo(userInfoVO);
//                }
//
//                //通知我的首页更新最新头像
//                postUserInfoChangeEvent();
            }
        }else {
            showToast("上传失败");
        }
    }

    @Override
    public void refreshServerError() {
        showToast(R.string.serve_error);
    }

    @Override
    public boolean lowMemoryForceRelease(){
        return false;
    }

    //------------------------------------------------------------------动态申请权限
    public static final int RC_CAMERA_STORAGE_PERM = 126;//摄像头、sdcard读写权限
    private static final int RC_SETTINGS_SCREEN = 125;//设置页返回

    private final String[] receiver_camera_storage = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };

    @AfterPermissionGranted(RC_CAMERA_STORAGE_PERM)
    public void cameraAndStorageAuthorityAsk() {
        if (EasyPermissions.hasPermissions(this, receiver_camera_storage)) {
            // Have permission, do the thing!
            showAlertMenu(TYPE_CHOOSE_IMAGE);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera_storage),RC_CAMERA_STORAGE_PERM, receiver_camera_storage);
        }
    }
}
