package com.sien.lib.share;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sien.lib.share.action.AbstractShareAction;
import com.sien.lib.share.action.AbstractShareAction.ShareAction;

import java.util.List;

/**
 * @author  sien
 * @descrition 分享面板
 */
public class ShareDialog extends Dialog implements
		View.OnClickListener {

	private AbstractShareAction mShareAction;
	private LinearLayout ll_wx_friend;
	private LinearLayout ll_wx_circle;
	private LinearLayout ll_more;
	private LinearLayout ll_cancle;

	public String qqPackage = "com.tencent.mobileqq";
	public String wxPackage = "com.tencent.mm";
	public String wbPackage = "com.sina.weibo";

	private boolean hideCopy = false;
	private boolean hideSendFriend = false;

	private Context mCotext;

	public void setShareAction(AbstractShareAction shareAction) {
		this.mShareAction = shareAction;
	}

	public ShareDialog(Context context, AbstractShareAction shareAction) {
		super(context);
		mCotext = context;
		this.mShareAction = shareAction;
	}

	public ShareDialog(Context context, int stytle,
			AbstractShareAction shareAction) {
		super(context, stytle);
		mCotext = context;
		this.mShareAction = shareAction;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share);
		initWindowParams();
		initView();
	}

	private void initWindowParams() {
		Window window = getWindow();
		window.setBackgroundDrawableResource(android.R.color.transparent);
		window.setGravity(Gravity.BOTTOM);
		window.setWindowAnimations(R.style.AnimBottom);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	private void initView() {
		ll_wx_friend = (LinearLayout) findViewById(R.id.ll_wx_friend);
		ll_wx_circle = (LinearLayout) findViewById(R.id.ll_wx_circle);
		ll_more = (LinearLayout) findViewById(R.id.ll_more);

		ll_cancle = (LinearLayout) findViewById(R.id.ll_cancle);

		ll_wx_friend.setOnClickListener(this);
		ll_wx_circle.setOnClickListener(this);
		ll_more.setOnClickListener(this);
		ll_cancle.setOnClickListener(this);

		if (hideCopy) {
			ll_more.setVisibility(View.INVISIBLE);
		}
	}

	public void hideSendFriendButton() {
		hideSendFriend = true;
	}

	public void hidCopyButton() {
		hideCopy = true;
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		if (vid == R.id.ll_wx_friend){
			if (!this.isAvilible(mCotext, this.wxPackage)) {
				Toast.makeText(mCotext, "应用未安装!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (null != mShareAction) {
				mShareAction.sharePlantForm(ShareAction.WX_FRIEND);
			}
			dismiss();
		}else if (vid == R.id.ll_wx_circle){
			if (!this.isAvilible(mCotext, this.wxPackage)) {
				Toast.makeText(mCotext, "应用未安装!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (null != mShareAction) {
				mShareAction.sharePlantForm(ShareAction.WX_CIRCLE);
			}
			dismiss();
		}else if (vid == R.id.ll_more){
			if (null != mShareAction) {
				mShareAction.sharePlantForm(ShareAction.SYSTEM_SEND);
			}
			dismiss();
		}else if (vid == R.id.ll_cancle){
			dismiss();
		}
	}

	private boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
	}
}
