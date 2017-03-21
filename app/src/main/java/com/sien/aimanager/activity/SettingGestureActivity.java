package com.sien.aimanager.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sien.aimanager.R;
import com.sien.aimanager.config.AppConfig;
import com.sien.lib.baseapp.activity.CPBaseBoostActivity;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.component.pwdlock.LockPatternIndicator;
import com.sien.lib.component.pwdlock.LockPatternUtil;
import com.sien.lib.component.pwdlock.LockPatternView;
import com.sien.lib.component.pwdlock.LockStatus;
import com.sien.lib.databmob.cache.disk.DiskLruCacheManager;
import com.sien.lib.databmob.utils.CPFileUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sien
 * @date 2016/9/8
 * @descript 设置手势锁
 */
public class SettingGestureActivity extends CPBaseBoostActivity {

	LockPatternIndicator lockPatternIndicator;
	LockPatternView lockPatternView;
	Button resetBtn;
	TextView messageTv;

	private List<LockPatternView.Cell> mChosenPattern = null;
	private static final long DELAYTIME = 600L;
	private static final String TAG = "SettingGestureActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_gesture);
	}

	@Override
	public void initViews() {
		super.initViews();

		if (getSupportActionBar() != null) {
			getSupportActionBar().setTitle("设置手势");
		}

		lockPatternIndicator = findView(R.id.lockPatterIndicator);
		lockPatternView = findView(R.id.lockPatternView);
		resetBtn = findView(R.id.resetBtn);
		messageTv = findView(R.id.messageTv);

		resetBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mChosenPattern = null;
				lockPatternIndicator.setDefaultIndicator();
				updateStatus(LockStatus.GEN_DEFAULT, null);
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
			}
		});
	}

	@Override
	public void initial() {
		super.initial();

		lockPatternView.setOnPatternListener(patternListener);

		showContentLayout();
	}

	@Override
	public <V extends BasePresenter> V createPresenter() {
		return null;
	}

	/**
	 * 手势监听
	 */
	private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

		@Override
		public void onPatternStart() {
			lockPatternView.removePostClearPatternRunnable();
			//updateStatus(Status.DEFAULT, null);
			lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
		}

		@Override
		public void onPatternComplete(List<LockPatternView.Cell> pattern) {
			//Log.e(TAG, "--onPatternDetected--");
			if(mChosenPattern == null && pattern.size() >= 4) {
				mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
				updateStatus(LockStatus.GEN_CORRECT, pattern);
			} else if (mChosenPattern == null && pattern.size() < 4) {
				updateStatus(LockStatus.GEN_LESSERROR, pattern);
			} else if (mChosenPattern != null) {
				if (mChosenPattern.equals(pattern)) {
					updateStatus(LockStatus.GEN_CONFIRMCORRECT, pattern);
				} else {
					updateStatus(LockStatus.GEN_CONFIRMERROR, pattern);
				}
			}
		}
	};

	/**
	 * 更新状态
	 * @param status
	 * @param pattern
     */
	private void updateStatus(LockStatus status, List<LockPatternView.Cell> pattern) {
		messageTv.setTextColor(getResources().getColor(status.colorId));
		messageTv.setText(status.strId);
		switch (status) {
			case GEN_DEFAULT:
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case GEN_CORRECT:
				updateLockPatternIndicator();
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case GEN_LESSERROR:
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case GEN_CONFIRMERROR:
				lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
				lockPatternView.postClearPatternRunnable(DELAYTIME);
				break;
			case GEN_CONFIRMCORRECT:
				saveChosenPattern(pattern);
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				setLockPatternSuccess();
				break;
		}
	}

	/**
	 * 更新 Indicator
	 */
	private void updateLockPatternIndicator() {
		if (mChosenPattern == null)
			return;
		lockPatternIndicator.setIndicator(mChosenPattern);
	}

	/**
	 * 成功设置了手势密码(跳到首页)
     */
	private void setLockPatternSuccess() {
		Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();

		finish();
	}

	/**
	 * 保存手势密码
	 */
	private void saveChosenPattern(List<LockPatternView.Cell> cells) {
		byte[] bytes = LockPatternUtil.patternToHash(cells);
		try {
			DiskLruCacheManager diskLruCacheManager = new DiskLruCacheManager(this, CPFileUtils.getAppFileRootDirectory(this));
			diskLruCacheManager.put(AppConfig.GESTURE_PASSWORD, bytes);
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
