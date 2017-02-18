package com.sien.lib.baseapp.control.photo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.sien.lib.datapp.utils.CPDeviceUtil;
import com.sien.lib.datapp.utils.CPLogUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created with Android Studio.
 * User: ryan@xisue.com
 * Date: 10/1/14
 * Time: 11:08 AM
 * Desc: CropHelper
 * Revision:
 * 魅族手机选择相册图片resultCode为0，拍照返回为-1，大部分手机返回-1（未处理好兼容问题）
 * 考虑兼容问题，从手机相册选图片建议使用MISActivity，该类仅处理拍照选择图片
 */
public class CropHelper {

    public static final String TAG = "CropHelper";

    /**
     * request code of Activities or Fragments
     * You will have to change the values of the request codes below if they conflict with your own.
     */
    public static final int REQUEST_CROP = 127;
    public static final int REQUEST_CAMERA = 128;
    public static final int REQUEST_CAMERA_CROP = 129;

    public static final String CROP_CACHE_FILE_NAME = "shoelives_crop_cache_file";

    public static Uri buildUri() {
        return Uri
                .fromFile(Environment.getExternalStorageDirectory())
                .buildUpon()
                .appendPath(CROP_CACHE_FILE_NAME)
                .build();
    }

    public static Uri buildUri(File Directory,String fileName) {
        return Uri
                .fromFile(Directory)
                .buildUpon()
                .appendPath(fileName)
                .build();
    }

    public static Uri buildUri(String filePath){
        return Uri.parse(filePath); //Uri.fromFile(new File(filePath));
    }

    public static void handleResult(CropHandler handler, int requestCode, int resultCode, Intent data) {
        if (handler == null) return;

        if (resultCode == Activity.RESULT_CANCELED) {
            handler.onCropCancel();
        } else if (resultCode == Activity.RESULT_OK) {
            CropParams cropParams = handler.getCropParams();
            if (cropParams == null) {
                handler.onCropFailed("CropHandler's params MUST NOT be null!");
                return;
            }
            switch (requestCode) {
                case REQUEST_CROP://相册选择截图（兼容处理部分手机不在data中传递图片数据）
                    CPLogUtil.logDebug(TAG, "Photo cropped! from Gallery " );
//                    handler.onPhotoCropped(handler.getCropParams().uri);
                    //cp.add 2017-01-04 修复小米手机无法选择相册图片问题
                    Uri uri = data.getData();
                    if (uri == null){
                        uri = handler.getCropParams().uri;
                    }
                    handler.onPhotoCropped(uri);
                    break;
                case REQUEST_CAMERA_CROP://拍照截图
                    CPLogUtil.logDebug(TAG, "Photo cropped! from Capture");
                    handler.onPhotoCropped(handler.getCropParams().uri);
                    break;
                case REQUEST_CAMERA:
                    Intent intent;
                    if (!TextUtils.isEmpty(CPDeviceUtil.getBrand()) && CPDeviceUtil.getBrand().toLowerCase().contains("meizu")){
                        intent = buildCropIntentMeiZu("com.android.camera.action.CROP", handler.getCropParams());
                    }else {
                        intent = buildCropFromUriIntent(handler.getCropParams());
                    }
                    Activity context = handler.getContext();
                    if (context != null) {
                        context.startActivityForResult(intent, REQUEST_CAMERA_CROP);
                    } else {
                        handler.onCropFailed("CropHandler's context MUST NOT be null!");
                    }
                    break;
            }
        }
    }

    public static boolean clearCachedCropFile(Uri uri) {
        if (uri == null) return false;

        File file = new File(uri.getPath());
        if (file.exists()) {
            boolean result = file.delete();
            if (result)
                CPLogUtil.logInfo(TAG, "Cached crop file cleared.");
            else
                CPLogUtil.logError(TAG, "Failed to clear cached crop file.");
            return result;
        } else {
            CPLogUtil.logWarn(TAG, "Trying to clear cached crop file but it does not exist.");
        }
        return false;
    }

    public static Intent buildCropFromUriIntent(CropParams params) {
        return buildCropIntent("com.android.camera.action.CROP", params);
    }

    public static Intent buildCropFromGalleryIntent(CropParams params) {
        return buildCropIntent(Intent.ACTION_PICK, params);
    }

    public static Intent buildCaptureIntent(Uri uri) {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        .putExtra(MediaStore.EXTRA_OUTPUT, uri);
    }

    public static Intent buildCropIntent(String action, CropParams params) {
        return new Intent(action, null)
                .setDataAndType(params.uri, params.type)
                        //.setType(params.type)
                //cp.comment 上传头像进行尺寸裁剪
                .putExtra("crop", params.crop)
                .putExtra("scale", params.scale)
                .putExtra("aspectX", params.aspectX)
                .putExtra("aspectY", params.aspectY)
                .putExtra("outputX", params.outputX)
                .putExtra("outputY", params.outputY)
                .putExtra("return-data", params.returnData)
                .putExtra("outputFormat", params.outputFormat)
                .putExtra("noFaceDetection", params.noFaceDetection)
                .putExtra("scaleUpIfNeeded", params.scaleUpIfNeeded)
                //cp.comment 传入输出路径,避免文件被压缩
                .putExtra(MediaStore.EXTRA_OUTPUT, params.uri);

    }

    //兼容处理魅族手机裁剪时异常
    public static Intent buildCropIntentMeiZu(String action,CropParams params){
        Intent intent = new Intent(action);
        intent.setDataAndType(params.uri, params.type);
        intent.putExtra("crop", params.crop);//发送裁剪信号
        intent.putExtra("outputX", params.outputX);//裁剪区的宽
        intent.putExtra("outputY", params.outputY);//裁剪区的高
        intent.putExtra("aspectX", params.aspectX);//X方向上的比例
        intent.putExtra("aspectY", params.aspectY);//Y方向上的比例
        intent.putExtra("scale", params.scale);//是否保留比例
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, params.uri);//直接输出文件
        intent.putExtra("return-data", params.returnData); //是否返回数据
//        intent.putExtra("outputFormat", params.outputFormat);
        intent.putExtra("noFaceDetection", params.noFaceDetection); //关闭人脸检测
        return intent;
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        if (context == null || uri == null) return null;

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
