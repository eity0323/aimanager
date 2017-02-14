package com.sien.lib.share.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.sien.lib.share.action.AbstractShareAction;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class DefautPlatformActionListener implements PlatformActionListener{

	private static final int ACTION_SINA_AUTHORIZING_SUCCESS = 0x0001;
	private static final int ACTION_SINA_AUTHORIZING_ERROR = 0x0002;
	private static final int ACTION_SINA_AUTHORIZING_CANCEL = 0x0003;
	private static final int ACTION_SINA_SHARE_SUCCESS = 0x0004;
	private static final int ACTION_SINA_SHARE_ERROR = 0x0005;
	private static final int ACTION_SINA_SHARE_CANCEL = 0x0006;
	private static final int ACTION_WECHATMOMENTS_SHARE_SUCCESS = 0x0007;
	private static final int ACTION_WECHATMOMENTS_SHARE_CANCEL = 0x0008;
	private static final int ACTION_WECHATMOMENTS_SHARE_ERROR = 0x0009;
	private static final int ACTION_QZONE_SHARE_SUCCESS = 0x00010;
	private static final int ACTION_QZONE_SHARE_CANCEL = 0x00011;
	private static final int ACTION_QZONE_SHARE_ERROR = 0x00012;
	private static final int ACTION_QQ_FRIEND_SHARE_SUCCESS = 0x00013;
	private static final int ACTION_QQ_FRIEND_SHARE_CANCEL = 0x00014;
	private static final int ACTION_QQ_FRIEND_SHARE_ERROR = 0x00015;
	private static final int ACTION_WE_CHAT_FRIEND_SHARE_SUCCESS = 0x00016;
	private static final int ACTION_WE_CHAT_FRIEND_SHARE_CANCEL = 0x00017;
	private static final int ACTION_WE_CHAT_FRIEND_SHARE_ERROR = 0x00018;
	
	private Handler shareHandle = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ACTION_SINA_AUTHORIZING_SUCCESS:
				if(mShareAction != null){
					mShareAction.shareSinaWeibo();
				}
				break;
			case ACTION_SINA_AUTHORIZING_ERROR:
				Toast.makeText(mContext, "新浪微博授权失败", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_SINA_SHARE_SUCCESS:
				Toast.makeText(mContext, "分享到新浪微博成功", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_SINA_SHARE_ERROR:
				Toast.makeText(mContext, "分享到新浪微博失败", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_SINA_SHARE_CANCEL:
				Toast.makeText(mContext, "分享到新浪微博操作已取消", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_WECHATMOMENTS_SHARE_SUCCESS:
				Toast.makeText(mContext, "分享到朋友圈成功", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_WECHATMOMENTS_SHARE_CANCEL:
				Toast.makeText(mContext, "分享到朋友圈操作已取消", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_WECHATMOMENTS_SHARE_ERROR:
				Toast.makeText(mContext, "分享到朋友圈失败", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_QZONE_SHARE_SUCCESS:
				Toast.makeText(mContext, "分享到QQ空间成功", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_QZONE_SHARE_CANCEL:
				Toast.makeText(mContext, "分享到QQ空间操作已取消", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_QZONE_SHARE_ERROR:
				Toast.makeText(mContext, "分享到QQ空间失败", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_QQ_FRIEND_SHARE_SUCCESS:
				Toast.makeText(mContext, "分享到QQ好友成功", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_QQ_FRIEND_SHARE_CANCEL:
				Toast.makeText(mContext, "分享到QQ好友操作已取消", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_QQ_FRIEND_SHARE_ERROR:
				Toast.makeText(mContext, "分享到QQ好友失败", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_WE_CHAT_FRIEND_SHARE_SUCCESS:
				Toast.makeText(mContext, "分享到微信好友成功", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_WE_CHAT_FRIEND_SHARE_CANCEL:
				Toast.makeText(mContext, "分享到微信好友操作已取消", Toast.LENGTH_SHORT).show();
				break;
			case ACTION_WE_CHAT_FRIEND_SHARE_ERROR:
				Toast.makeText(mContext, "分享到微信好友失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	private Context mContext;
	private AbstractShareAction mShareAction;
	public void setmShareAction(AbstractShareAction mShareAction) {
		this.mShareAction = mShareAction;
	}

	public DefautPlatformActionListener(Context context){
		this.mContext = context;
	}
	
	@Override
	public void onCancel(final Platform platform, final int action) {
		int what = -1;
		if (WechatMoments.NAME.equals(platform.getName())) {
			if (action == WechatMoments.ACTION_SHARE) {
				what = ACTION_WECHATMOMENTS_SHARE_CANCEL;
			}
		}else if (Wechat.NAME.equals(platform.getName())) {
			if (action == Wechat.ACTION_SHARE) {
				what = ACTION_WE_CHAT_FRIEND_SHARE_CANCEL;
			}
		}
		shareHandle.sendEmptyMessage(what);
	}

	@Override
	public void onComplete(final Platform platform, final int action, final HashMap<String, Object> res) {
		int what = -1;
		if (WechatMoments.NAME.equals(platform.getName())) {
			if (action == WechatMoments.ACTION_SHARE) {
				what = ACTION_WECHATMOMENTS_SHARE_SUCCESS;
			}
		}else if (Wechat.NAME.equals(platform.getName())) {
			if (action == Wechat.ACTION_SHARE) {
				what = ACTION_WE_CHAT_FRIEND_SHARE_SUCCESS;
			}
		}
		shareHandle.sendEmptyMessage(what);
	}

	@Override
	public void onError(final Platform platform, final int action, final Throwable t) {
		int what = -1;
		if (WechatMoments.NAME.equals(platform.getName())) {
			if (action == WechatMoments.ACTION_SHARE) {
				what = ACTION_WECHATMOMENTS_SHARE_ERROR;
			}
		}else if (Wechat.NAME.equals(platform.getName())) {
			if (action == Wechat.ACTION_SHARE) {
				what = ACTION_WE_CHAT_FRIEND_SHARE_ERROR;
			}
		}
		shareHandle.sendEmptyMessage(what);

		//cp.add ---调试分享失败的错误日志
		t.printStackTrace();
	}
}
