package com.sien.aimanager.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.sien.lib.datapp.utils.CPLogUtil;
import com.sien.lib.baseapp.utils.ToastUtil;
import com.sien.lib.share.ShareConfig;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;
import de.greenrobot.event.EventBus;

/**
 * @author sien
 * @date 2016/12/13
 * @descript 微信登录、分享回传页面
 */
public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {

	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        setContentView(R.layout.entry);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, ShareConfig.APPID_CIRCLE_FRIEND, false);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		int type = req.getType();
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
		String result = "failure";
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "success";
				//登录成功
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "cancel";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "deny";
				break;
			default:
				result = "unknown";
				break;
		}
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//微信登录
			String code = ((SendAuth.Resp) resp).code;
			postWeLoginStatusEvent(result,code);
		}else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){//微信分享

			CPLogUtil.logError("share","share status " + result);
			postWeShareStatusEvent(result,resp.openId);
		}
	}

	/*发送微信登录状态事件*/
	private void postWeLoginStatusEvent(String status,String code){
		Bundle bundle = new Bundle();
		bundle.putString("action","wechatLoginCallBack");
		bundle.putString("loginStatus",status);
		bundle.putString("loginCode",code);
		EventBus.getDefault().post(bundle);

		finish();
	}

	/*发送分享状态事件*/
	private void postWeShareStatusEvent(String status,String code){

	}

	/**
	 * 处理微信发出的向第三方应用请求app message
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
		startActivity(iLaunchMyself);
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null && (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			ToastUtil.getInstance().showMessage(this, obj.extInfo,ToastUtil.LENGTH_SHORT);
		}
	}
}
