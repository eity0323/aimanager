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
import com.sien.lib.component.pwdlock.LockPatternCache;
import com.sien.lib.component.pwdlock.LockPatternIndicator;
import com.sien.lib.component.pwdlock.LockPatternUtil;
import com.sien.lib.component.pwdlock.LockPatternView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sien
 * @date 2016/9/8
 * @descript 设置手势锁
 */
public class CreateGestureActivity extends CPBaseBoostActivity {

	LockPatternIndicator lockPatternIndicator;
	LockPatternView lockPatternView;
	Button resetBtn;
	TextView messageTv;

	private LockPatternCache aCache;

	private List<LockPatternView.Cell> mChosenPattern = null;
	private static final long DELAYTIME = 600L;
	private static final String TAG = "CreateGestureActivity";

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
				updateStatus(Status.DEFAULT, null);
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
			}
		});
	}

	@Override
	public void initial() {
		super.initial();

		lockPatternView.setOnPatternListener(patternListener);
		aCache = LockPatternCache.get(CreateGestureActivity.this);

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
				updateStatus(Status.CORRECT, pattern);
			} else if (mChosenPattern == null && pattern.size() < 4) {
				updateStatus(Status.LESSERROR, pattern);
			} else if (mChosenPattern != null) {
				if (mChosenPattern.equals(pattern)) {
					updateStatus(Status.CONFIRMCORRECT, pattern);
				} else {
					updateStatus(Status.CONFIRMERROR, pattern);
				}
			}
		}
	};

	/**
	 * 更新状态
	 * @param status
	 * @param pattern
     */
	private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
		messageTv.setTextColor(getResources().getColor(status.colorId));
		messageTv.setText(status.strId);
		switch (status) {
			case DEFAULT:
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case CORRECT:
				updateLockPatternIndicator();
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case LESSERROR:
				lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
				break;
			case CONFIRMERROR:
				lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
				lockPatternView.postClearPatternRunnable(DELAYTIME);
				break;
			case CONFIRMCORRECT:
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
		aCache.put(AppConfig.GESTURE_PASSWORD, bytes);
//		try {
//			String res = new String(bytes,"UTF-8");
//			CPSharedPreferenceManager.getInstance(this).saveData(AppConfig.GESTURE_PASSWORD, res);
//		}catch (Exception ex){
//			ex.printStackTrace();
//		}
	}

	private enum Status {
		//默认的状态，刚开始的时候（初始化状态）
		DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
		//第一次记录成功
		CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
		//连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
		LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
		//二次确认错误
		CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
		//二次确认正确
		CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

		private Status(int strId, int colorId) {
			this.strId = strId;
			this.colorId = colorId;
		}
		private int strId;
		private int colorId;
	}
}
