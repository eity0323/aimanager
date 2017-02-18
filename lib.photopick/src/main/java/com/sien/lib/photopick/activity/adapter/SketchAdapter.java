package com.sien.lib.photopick.activity.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.sien.lib.photopick.R;
import com.sien.lib.photopick.activity.SketchActivity;
import com.sien.lib.photopick.bean.SketchBean;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by mabeijianxi on 2015/1/5.
 */
public class SketchAdapter extends PagerAdapter implements PhotoViewAttacher.OnPhotoTapListener {
    private ImageLoader mImageLoader = ImageLoader.getInstance();
    private List<SketchBean> mPicData;
    private View mCurrentView;
    private Context mContext;
    private DisplayImageOptions pager_options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.mis_skin_icon_img_load_fail)
            .showImageOnFail(R.drawable.mis_skin_icon_img_load_fail)
            .imageScaleType(ImageScaleType.EXACTLY)//会对图片进一步的缩放
            .bitmapConfig(Bitmap.Config.RGB_565)//此种模消耗的内存会很小,2个byte存储一个像素
            .considerExifParams(true)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    public SketchAdapter(Context mContext, List<SketchBean> data) {
        super();
        this.mPicData = data;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mPicData != null) {
            return mPicData.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentView = (View) object;
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View inflate = LayoutInflater.from(mContext).inflate(R.layout.layout_sketch_item, container, false);
        PhotoView imageView = (PhotoView) inflate.findViewById(R.id.pv);
        imageView.setOnPhotoTapListener(this);
        final SketchBean mPicDataBean = mPicData.get(position);

        if (mPicDataBean != null && mPicDataBean.imageBigUrl != null && !"null".equals(mPicDataBean.imageBigUrl)) {
            setupNetImage(mPicDataBean,imageView);

            savaPicDetection(mPicDataBean,imageView);
        } else {
            imageView.setImageResource(R.drawable.mis_skin_icon_img_load_fail);
        }

        container.addView(inflate);//将ImageView加入到ViewPager中
        return inflate;
    }

    private void savaPicDetection(final SketchBean ealuationPicBean,ImageView imageView) {
        //TODO 长按保存图片
    }

    /**
     * 设置网络图片加载规则
     * @param ealuationPicBean
     * @param imageView
     */
    private void setupNetImage(final SketchBean ealuationPicBean, final PhotoView imageView) {
        mImageLoader.displayImage(ealuationPicBean.imageBigUrl, imageView, pager_options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                showExcessPic(ealuationPicBean, imageView);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    /**
     * 展示过度图片
     *
     * @param ealuationPicBean
     * @param imageView
     */
    private void showExcessPic(SketchBean ealuationPicBean, PhotoView imageView) {
        Bitmap bitmap = getBitmapFromCache(ealuationPicBean.smallImageUrl, mImageLoader);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.mis_skin_icon_img_load_fail);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

    /**
     * 单击屏幕关闭
     *
     * @param view - View the user tapped.
     * @param x    - where the user tapped from the of the Drawable, as
     *             percentage of the Drawable width.
     * @param y    - where the user tapped from the top of the Drawable, as
     */
    @Override
    public void onPhotoTap(View view, float x, float y) {
        ((SketchActivity) mContext).startActivityAnim();
    }

    public static Bitmap getBitmapFromCache(String uri, ImageLoader imageLoader) {//这里的uri一般就是图片网址
        List<String> memCacheKeyNameList = MemoryCacheUtils.findCacheKeysForImageUri(uri, imageLoader.getMemoryCache());
        if (memCacheKeyNameList != null && memCacheKeyNameList.size() > 0) {
            for (String each : memCacheKeyNameList) {
            }
            return imageLoader.getMemoryCache().get(memCacheKeyNameList.get(0));
        }

        return null;
    }
}
