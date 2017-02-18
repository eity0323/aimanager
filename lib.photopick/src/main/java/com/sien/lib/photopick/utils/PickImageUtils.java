package com.sien.lib.photopick.utils;

import android.app.Activity;
import android.content.Intent;

import com.sien.lib.photopick.activity.PhotoViewActivity;
import java.util.ArrayList;

/**
 * @author sien
 * @date 2016/9/5
 * @descript 图片选择工具
 */
public class PickImageUtils {
    /*预览大图*/
    public static void showImageDisplay(Activity context,ArrayList<String> picUrls) {
        Intent photoViewIntent = new Intent(context,PhotoViewActivity.class);
        photoViewIntent.putStringArrayListExtra("picUrls", picUrls);
        photoViewIntent.putExtra("initIndex", 0);
        photoViewIntent.putExtra(PhotoViewActivity.EXTRA_OPEN_LONG_CLICK_ACTION, false);
        photoViewIntent.putExtra(PhotoViewActivity.EXTRA_LONG_CLICK_IMAGE_SAVE_FILE_NAME,"shoelives");
        context.startActivity(photoViewIntent);
    }

}
