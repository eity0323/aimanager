package com.sien.lib.photopick.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sien.lib.photopick.R;
import com.sien.lib.photopick.activity.adapter.SketchAdapter;
import com.sien.lib.photopick.bean.SketchBean;
import com.sien.lib.photopick.utils.CommonUtils;
import com.sien.lib.photopick.utils.EvaluateUtil;
import com.sien.lib.photopick.view.SketchViewPager;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * @author sien
 * @date 2017-01-20
 * @description 预览大图（listview or gridview 带转场动画）
 */
public class SketchActivity extends Activity implements View.OnClickListener, ViewTreeObserver.OnPreDrawListener, SketchViewPager.HackyViewPagerDispatchListener {
    private List<SketchBean> picDataList;
    public static final String PICDATALIST = "PICDATALIST";
    public static final String CURRENTITEM = "CURRENTITEM";
    private int currentItem;
    public int mPositon;
    private SketchAdapter imageScaleAdapter;
    private SketchViewPager viewPager;
    private TextView tv_back;
    private TextView tv_pager;
    private LinearLayout ll_root;
    private RelativeLayout rl_title;
    private int height;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch);
        getData();
        intiView();
        setUpEvent();
    }

    private void setUpEvent() {
        viewPager.setmHackyViewPagerDispatchListener(this);
        viewPager.setAdapter(imageScaleAdapter);
        viewPager.setCurrentItem(currentItem);
        setTitleNum(currentItem);
        tv_back.setOnClickListener(this);
        setPagerChangeListener(viewPager);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);
    }

    private void getData() {
        Intent intent = getIntent();
        picDataList = (List<SketchBean>) intent.getSerializableExtra("PICDATALIST");
        currentItem = intent.getIntExtra(CURRENTITEM, 0);
        mPositon = currentItem;
        imageScaleAdapter = new SketchAdapter(this, picDataList);
    }

    private void intiView() {
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        ll_root = (LinearLayout) findViewById(R.id.ll_root);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_pager = (TextView) findViewById(R.id.tv_pager);
        viewPager = (SketchViewPager) findViewById(R.id.viewpager);
    }

    /**
     * 绘制前开始动画
     *
     * @return
     */
    @Override
    public boolean onPreDraw() {
        viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
        final View view = imageScaleAdapter.getPrimaryItem();
        final PhotoView imageView = (PhotoView) ((ViewGroup) view).getChildAt(0);

        computeImageWidthAndHeight(imageView);

        final SketchBean picBean = picDataList.get(mPositon);
        final float vx = picBean.width * 1.0f / width;
        final float vy = picBean.height * 1.0f / height;

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedFraction = animation.getAnimatedFraction();

                view.setTranslationX(EvaluateUtil.evaluateInt(animatedFraction, picBean.x + picBean.width / 2 - imageView.getWidth() / 2, 0));
                view.setTranslationY(EvaluateUtil.evaluateInt(animatedFraction, picBean.y + picBean.height / 2 - imageView.getHeight() / 2, 0));
                view.setScaleX(EvaluateUtil.evaluateFloat(animatedFraction, vx, 1));
                view.setScaleY(EvaluateUtil.evaluateFloat(animatedFraction, vy, 1));

                ll_root.setBackgroundColor((int) EvaluateUtil.evaluateArgb(animatedFraction, 0x0, 0xff000000));
            }
        });

        addIntoListener(valueAnimator);
        valueAnimator.setDuration(300);
        valueAnimator.start();
        return true;
    }

    /**
     * 进场动画过程监听
     *
     * @param valueAnimator
     */
    private void addIntoListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_root.setBackgroundColor(0x0);
                rl_title.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                rl_title.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    /**
     * 页面改动监听
     *
     * @param viewPager
     */
    private void setPagerChangeListener(SketchViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPositon = position;
                setTitleNum(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setTitleNum(int position) {
        tv_pager.setText((position + 1) + "/" + picDataList.size());
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_back){
            startActivityAnim();
        }

    }

    /**
     * 旋转
     *
     * @param v
     */
    private void rotation(float v) {
        View primaryView = imageScaleAdapter.getPrimaryItem();
        if (primaryView != null) {
            primaryView.getRotation();
            float rotation = primaryView.getRotation();
            primaryView.setRotation(rotation + v);
            primaryView.requestLayout();
        }
    }

    @Override
    public void onBackPressed() {
        startActivityAnim();
    }

    /**
     * 开始activity的动画
     */
    public void startActivityAnim() {
//        得到当前页的View
        final View view = imageScaleAdapter.getPrimaryItem();
        final PhotoView imageView = (PhotoView) ((ViewGroup) view).getChildAt(0);
//      当图片被放大时，需要把其缩放回原来大小再做动画
        imageView.setZoomable(false);
        computeImageWidthAndHeight(imageView);

        final SketchBean ealuationPicBean = picDataList.get(mPositon);
        final float vx = ealuationPicBean.width * 1.0f / width;
        final float vy = ealuationPicBean.height * 1.0f / height;

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedFraction = animation.getAnimatedFraction();

                view.setTranslationX(EvaluateUtil.evaluateInt(animatedFraction, 0, ealuationPicBean.x + ealuationPicBean.width / 2 - imageView.getWidth() / 2));
                view.setTranslationY(EvaluateUtil.evaluateInt(animatedFraction, 0, ealuationPicBean.y + ealuationPicBean.height / 2 - imageView.getHeight() / 2));
                view.setScaleX(EvaluateUtil.evaluateFloat(animatedFraction, 1, vx));
                view.setScaleY(EvaluateUtil.evaluateFloat(animatedFraction, 1, vy));
                ll_root.setBackgroundColor((int) EvaluateUtil.evaluateArgb(animatedFraction, 0xff000000, 0x0));

                if (animatedFraction > 0.95) {
                    view.setAlpha(1 - animatedFraction);
                }
            }
        });
        addOutListener(valueAnimator);
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    /**
     * 计算图片的宽高
     * @param imageView
     */
    private void computeImageWidthAndHeight(PhotoView imageView) {

//      获取真实大小
        Drawable drawable = imageView.getDrawable();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Log.e("drawable","intrinsicHeight:"+intrinsicHeight);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        Log.e("drawable","intrinsicWidth:"+intrinsicWidth);
//        计算出与屏幕的比例，用于比较以宽的比例为准还是高的比例为准，因为很多时候不是高度没充满，就是宽度没充满
        float h = CommonUtils.getScreenSizeHeight(this) * 1.0f / intrinsicHeight;
        float w = CommonUtils.getScreenSizeWidth(this) * 1.0f / intrinsicWidth;
        if (h > w) {
            h = w;
        } else {
            w = h;
        }
//      得出当宽高至少有一个充满的时候图片对应的宽高
        height = (int) (intrinsicHeight * h);
        width = (int) (intrinsicWidth * w);
    }

    /**
     * 退场动画过程监听
     *
     * @param valueAnimator
     */
    private void addOutListener(ValueAnimator valueAnimator) {
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_root.setBackgroundColor(0x0);
                rl_title.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void isDown() {

    }

    @Override
    public void isUp() {

    }

    @Override
    public void isCancel() {

    }
}
