package com.sien.aimanager.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sien.aimanager.R;
import com.sien.aimanager.adapter.FeedbackUploadAdapter;
import com.sien.aimanager.model.IFeedbackViewModel;
import com.sien.aimanager.presenter.FeedbackPresenter;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.control.photo.CropHandler;
import com.sien.lib.baseapp.control.photo.CropHelper;
import com.sien.lib.baseapp.control.photo.CropParams;
import com.sien.lib.baseapp.utils.CPImageUtil;
import com.sien.lib.baseapp.utils.CPPatternUtil;
import com.sien.lib.baseapp.utils.RepeatClickUtil;
import com.sien.lib.datapp.network.action.MainRequestAction;
import com.sien.lib.datapp.network.base.RequestFreshStatus;
import com.sien.lib.datapp.utils.CPDeviceUtil;
import com.sien.lib.datapp.utils.CPFileUtils;
import com.sien.lib.photopick.activity.MISActivity;
import com.sien.lib.photopick.activity.PhotoViewActivity;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author sien
 * @date 2016/9/8
 * @descript 意见反馈
 */
public class FeedbackActivity extends CPBaseBoostActivity implements IFeedbackViewModel {

    private final int PHOTO_NUMBER_MAX = 3;
    private final int REQUEST_IMAGE = 1025;//从图库选择图片
    private final int AVALIBLE_CHAR_LENGTH = 5;//5个中文字符

    private EditText feedbackET;
    private RecyclerView rv_img;
    private MenuItem toolbarRightAction;

    private FeedbackUploadAdapter uploadAdapter;//图片列表和添加图片按钮在这里面

    private FeedbackPresenter presenter;
    private CropParams mCropParams;

    private String uploadpath="";
    private ArrayList<String> path;//图片路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (uploadAdapter != null){
            uploadAdapter.releaseMemory();
        }
        uploadAdapter = null;

        mCropParams = null;

        if (path != null){
            path.clear();
        }
        path = null;

        toolbarRightAction = null;

        if (rv_img != null){
            rv_img.destroyDrawingCache();
            rv_img.removeAllViews();
        }
        rv_img = null;
    }

    public void initViews(){
        getSupportActionBar().setTitle(R.string.feedback);

        feedbackET = findView(R.id.feedback_edit);
        rv_img = findView(R.id.rv_feedback_imgs);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rv_img.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit,menu);

        toolbarRightAction = menu.findItem(R.id.submit);
        toolbarRightAction.setEnabled(false);

        return super.onCreateOptionsMenu(menu);
    }

    /*顶部右测按钮状态变更*/
    private void topBarRightButtonStatus(boolean isOpen){
        if (toolbarRightAction != null) {
            toolbarRightAction.setEnabled(isOpen);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.submit){
            if (!RepeatClickUtil.isRepeatClick()) {
                checkAndUploadFeedback();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void initial(){
        presenter = getPresenter();
        path = new ArrayList<>();

        mCropParams = new CropParams();

        uploadAdapter = new FeedbackUploadAdapter(this,path,this);
        uploadAdapter.setItemButtonListener(new FeedbackUploadAdapter.OnItemButtonClickListener() {
            @Override
            public void onItemClick(String data) {
                //删除图片
                presenter.removePhotoPath(data);

                uploadAdapter.refresh(presenter.getPhotosPathExceptHolder());
                uploadAdapter.notifyDataSetChanged();
            }
        });
        rv_img.setAdapter(uploadAdapter);

        feedbackET.addTextChangedListener(textWatcher);
    }

    /*上传图片并提交反馈内容*/
    private void checkAndUploadFeedback(){
        String feed = feedbackET.getText().toString();
        if (TextUtils.isEmpty(feed)){
            showToast(getString(R.string.feedback_advice_input));
            return;
        }

        if (CPPatternUtil.isEmoji2(feed)){
            showToast("不支持输入Emoji表情符号");
            return;
        }

        // 有上传图，则先上传图片再提交申请，无图片则直接提交申请
        if (presenter.getPhotosPathExceptHolder().size() > 0) {
            uploadFeedbackImages();
        }else{
            doFeedBack();
        }
    }

    /*调用反馈接口*/
    private void doFeedBack(){
        String feed = feedbackET.getText().toString();
        //调用反馈接口
        MainRequestAction.requestFeedback(this,feed,presenter.getUploadPhotoUrls());
    }

    /*相册 or 拍照*/
    @Override
    public void chooseImageFromAlbum() {
        cameraTask();
    }

    @Override
    public void review(int position) {
        //查看大图
        Intent photoViewIntent = new Intent(this,PhotoViewActivity.class);
        ArrayList<String> picUrls = new ArrayList<String>();

        //预览大图
        if (presenter != null && presenter.getPhotosPathExceptHolder().size() > 0) {
            //头像
            picUrls.clear();
            picUrls.addAll(presenter.getPhotosPathExceptHolder());
        } else {
            //默认图片
            String path = CPImageUtil.save2LocalStorage(this,"temp","", BitmapFactory.decodeResource(getResources(),R.mipmap.file_default_image));
            picUrls.add(path);
        }

        photoViewIntent.putStringArrayListExtra("picUrls", picUrls);
        photoViewIntent.putExtra("initIndex", 0);
        photoViewIntent.putExtra(PhotoViewActivity.EXTRA_OPEN_LONG_CLICK_ACTION, false);
        photoViewIntent.putExtra(PhotoViewActivity.EXTRA_LONG_CLICK_IMAGE_SAVE_FILE_NAME,"shoelives");
        startActivity(photoViewIntent);

    }

    /*校验权限并选择图片(非系统选择图片方式【选多张】，需要申请权限)*/
    private void checkAuthorityAndChoosePhotos(){
        doChoosePicture();//从相册获取
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

    /*从相册选择图片 (非系统选择图片方式，需要申请权限)*/
    private void doChoosePicture(){
        if (CPDeviceUtil.hasSdcard()) {
            Intent intent = new Intent(FeedbackActivity.this,MISActivity.class);
            intent.putExtra(MISActivity.EXTRA_SHOW_CAMERA, true);
            intent.putExtra(MISActivity.EXTRA_SELECT_MODE,
                    MISActivity.MODE_MULTI);
            String storageDir = CPFileUtils.getInnerCacheFilePath(FeedbackActivity.this);//CPConfiguration.getAppRootDirectory(FeedbackActivity.this).getAbsolutePath();
            intent.putExtra(MISActivity.EXTRA_SELECT_COUNT,
                    PHOTO_NUMBER_MAX);
            intent.putExtra(MISActivity.EXTRA_IMAGE_CACHE_DIR, storageDir);

            ArrayList<String> selectedPath =  presenter.getPhotosPathExceptHolder();
            if (selectedPath.size() > 0) {
                intent.putExtra(MISActivity.EXTRA_DEFAULT_SELECTED_LIST, selectedPath);
            }
            startActivityForResult(intent, REQUEST_IMAGE);
        }
    }

    /*上传图片*/
    private void uploadFeedbackImages(){
        MainRequestAction.requestUploadMultiFile(FeedbackActivity.this,presenter.getPhotosPathExceptHolder(),"users/feedback");
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() > AVALIBLE_CHAR_LENGTH) {
                topBarRightButtonStatus(true);
            } else {
                topBarRightButtonStatus(false);
            }
        }
    };

    /*图片裁剪事件*/
    private CropHandler cropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            if (mCropParams != null) {
                String uploadImagePath = CPImageUtil.save2LocalStorage(FeedbackActivity.this,"temp","", CropHelper.decodeUriAsBitmap(FeedbackActivity.this, mCropParams.uri),true);

                if (!TextUtils.isEmpty(uploadImagePath)) {
                    presenter.addPhotoPath(uploadImagePath);
                    uploadAdapter.refresh(presenter.getPhotosPathExceptHolder());
                    uploadAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onCropCancel() {

        }

        @Override
        public void onCropFailed(String message) {

        }

        @Override
        public CropParams getCropParams() {
            return mCropParams;
        }

        @Override
        public Activity getContext() {
            return FeedbackActivity.this;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE){
            if (presenter != null && data != null) {
                presenter.setPhotosPath(data.getStringArrayListExtra(MISActivity.EXTRA_RESULT));

                uploadAdapter.refresh(presenter.getPhotosPathExceptHolder());
                uploadAdapter.notifyDataSetChanged();
            }
        }else if (requestCode == RC_SETTINGS_SCREEN) {
            boolean hasCameraPermissions = EasyPermissions.hasPermissions(FeedbackActivity.this, receiver_camera_storage);
            if (hasCameraPermissions){
                //设置页返回
            }
        }else  if (requestCode == CropHelper.REQUEST_CROP
                || requestCode == CropHelper.REQUEST_CAMERA || requestCode == CropHelper.REQUEST_CAMERA_CROP) {
            CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
        }
    }

    @Override
    public FeedbackPresenter createPresenter() {
        return new FeedbackPresenter(this);
    }

    @Override
    public void refreshFeedback(RequestFreshStatus status) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            showToast("感谢您的反馈");
            finish();
        }else {
            showToast("提交失败！");
        }
    }

    @Override
    public void refreshUploadImage(RequestFreshStatus status) {
        if (!RequestFreshStatus.REFRESH_ERROR.equals(status)){
            //调用反馈接口
            doFeedBack();
        }else {
            showToast("上传失败！");
//            hideLoading();
        }
    }

    //------------------------------------------------------------------动态申请权限
    private static final int RC_SETTINGS_SCREEN = 521;//设置页返回
    public static final int RC_CAMERA_STORAGE_PERM = 126;//摄像头、sdcard读写权限

    private final String[] receiver_camera_storage = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE };

    @AfterPermissionGranted(RC_CAMERA_STORAGE_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(this, receiver_camera_storage)) {
            // Have permission, do the thing!
            checkAuthorityAndChoosePhotos();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera_storage),RC_CAMERA_STORAGE_PERM, receiver_camera_storage);
        }
    }

}
