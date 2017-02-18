package com.sien.lib.photopick.activity.adapter;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.sien.lib.photopick.R;
import com.squareup.picasso.Picasso;

public class PhotoViewAdapter extends PagerAdapter {

	private List<String> picUrls;
	private Context context;

	public PhotoViewAdapter(Context contexts, List<String> picUrl) {
		this.context = contexts;
		this.picUrls = picUrl;
	}

	@Override
	public int getCount() {
		return null == picUrls ? 0 : picUrls.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public View instantiateItem(ViewGroup container, final int position) {
		if (null == picUrls) {
			return null;
		}
		PhotoView photoView = new PhotoView(container.getContext());
		String filepath = picUrls.get(position);
		if (!TextUtils.isEmpty(filepath)) {
			if (filepath.contains("http://")) {
				Picasso.with(context).load(filepath).into(photoView);
			} else if (filepath.contains("drawable://")) {
				filepath = filepath.substring(11);
				Picasso.with(context).load(context.getResources().getIdentifier(filepath, "drawable", context.getPackageName())).into(photoView);
			} else {
				Picasso.with(context).load(new File(filepath)).into(photoView);
			}

		} else {
			photoView.setImageResource(R.drawable.mis_skin_icon_img_load_fail);
		}

		container.addView(photoView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		photoView.setOnViewTapListener(new OnViewTapListener() {

			@Override
			public void onViewTap(View view, float x, float y) {
				if (null != onItemClickListener) {
					onItemClickListener.onClick(view);
				}
			}
		});
		
		photoView.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View view) {
				if (null != onItemClickListener) {
					onItemClickListener.onLongClick(view, picUrls.get(position));;
				}
				return true;
			}
			
		});
		return photoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	private OnItemClickListener onItemClickListener;

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListener {
		public void onClick(View view);
		public void onLongClick(View view, String imgUrl);
	}

}
