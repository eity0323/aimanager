package com.sien.lib.baseapp.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.sien.lib.datapp.utils.CPFileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author sien
 * @date 2016/11/10
 * @descript 图片处理类
 */
public class CPImageUtil {
    /** cp.add 图片存储到系统图库 */
    public static String saveImageToGallery(Context context,Bitmap bmp,String fileName) {
        // 首先保存图片
        File appDir = new File(CPFileUtils.getAppCacheRootFilePath(context),"temp");
        String path = "";
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // 其次把文件插入到系统图库
            path = appDir.getAbsolutePath() + File.separator + fileName;
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        file.getAbsolutePath(), fileName, fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            if (!TextUtils.isEmpty(path)) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + path)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }

    public static String saveImageToGallery(Context context,Bitmap bmp) {
        String fileName = "szo_" + System.currentTimeMillis() + ".jpg";
        return saveImageToGallery(context, bmp,fileName);
    }

    /**
     * TODO 处理内存溢出问题
     * 保存图片到文件夹
     * @param context
     * @param dirName
     * @param fileName 带后缀，无后缀时默认使用jpg
     * @param bitmap
     * @param needCompress 是否需要压缩
     */
    public static String save2LocalStorage(Context context,String dirName,String fileName,Bitmap bitmap,boolean needCompress){
        // 生成文件夹
        String path = "";
        if (bitmap == null){
            return path;
        }
        File appDir = new File(CPFileUtils.getInnerCacheFilePath(context),dirName);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        String innerFileName = fileName;
        if (TextUtils.isEmpty(fileName)){
            innerFileName = "szo_" + System.currentTimeMillis() + ".jpg";
        }else if (!fileName.contains(".")){
            innerFileName = fileName + ".jpg";
        }
        //保存文件
        File file = new File(appDir, innerFileName);

        int quality = 100;//图片质量
        if (needCompress){ //需要压缩
            int bitmapSize = getBitmapSize(bitmap);

            quality = getQualityBySize(bitmapSize);
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            if (fileName.contains(".png")){
                bitmap.compress(Bitmap.CompressFormat.PNG,quality,fos);
            }else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            }
            fos.flush();
            fos.close();

            path = appDir.getAbsolutePath() + File.separator + innerFileName;

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return path;
    }

    /*根据文件尺寸获取文件压缩质量*/
    private static int getQualityBySize(int size){
        //Byte
        if (size < 1024) {
            return 100;
        }

        //KB
        double kiloByte = size / 1024;
        if (kiloByte < 200) {
            return 90;
        }

        if (kiloByte < 500) {
            return 75;
        }

        if (kiloByte < 1024) {
            return 60;
        }

        //MB
//        double megaByte = kiloByte / 1024;
        return 50;
    }

    /**
     * 保存图片到文件夹
     * @param context
     * @param dirName
     * @param fileName 带后缀，无后缀时默认使用jpg
     * @param bitmap
     * @return
     */
    public static String save2LocalStorage(Context context,String dirName,String fileName,Bitmap bitmap){
        return save2LocalStorage(context, dirName, fileName, bitmap,false);
    }

    /*添加图片到相册*/
    public static void addFile2Gallery(Context context,String path,String fileName){
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),path, fileName, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        if (!TextUtils.isEmpty(path)) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://" + path)));
        }
    }

    /*获取图片像素所占内存*/
    public static long getImagePixelMemory(Drawable drawable){
        long memSize = 0;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitDrawable = (BitmapDrawable) drawable;
            Bitmap bit = bitDrawable.getBitmap();
            int rowBytes = bit.getRowBytes();
            int height = bit.getHeight();
            memSize = rowBytes * height;
        }
        return memSize;
    }
    /*获取图片像素所占内存*/
    public static long getImagePixelMemory(ImageView img){
        long memSize = 0;
        Drawable drawable = img.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitDrawable = (BitmapDrawable) drawable;
            Bitmap bit = bitDrawable.getBitmap();
            int rowBytes = bit.getRowBytes();
            int height = bit.getHeight();
            memSize = rowBytes * height;
        }

        return memSize;
    }
    /*获取图片像素所占内存*/
    public static long getImagePixelMemory(Bitmap bit){
        long memSize = 0;
        if (bit != null) {
            int rowBytes = bit.getRowBytes();
            int height = bit.getHeight();
            memSize = rowBytes * height;
        }

        return memSize;
    }
    /*获取图片像素所占内存*/
    public static long getImagePixelMemory(Bitmap bit,int unit/*1024(KB);1024*1024(MB)*/){
        long memSize = 0;
        if (bit != null) {
            int rowBytes = bit.getRowBytes();
            int height = bit.getHeight();
            memSize = rowBytes * height;
            if (unit != 0){
                memSize /= unit;
            }

            //memSize;  //单位Byte
            //memSize/1024;   //单位KB
            //memSize/1024 /1024; //单位MB
        }

        return memSize;
    }

    /**
     * 获取bitmap图片大小
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }
}
