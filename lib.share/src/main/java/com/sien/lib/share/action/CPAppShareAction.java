package com.sien.lib.share.action;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.sien.lib.share.R;
import com.sien.lib.share.ShareConfig;

import java.io.File;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class CPAppShareAction extends AbstractShareAction {
	public final int SHARE_TYPE_LINK = 1;//分享链接
	public final int SHARE_TYPE_IMAGE = 2;//分享图片
	public final int SHARE_TYPE_TEXT = 3;//分享文本
	public final int SHARE_TYPE_FILE = 4;//分享文件

	private String mUrl="http://www.shoelives.com";//分享链接
	private String mTitle = "新年新期望，我先送自己一双满意的鞋子吧";//分享标题
	private String mContent = "根据不同脚型专属定制，快快下载APP体验吧";//分享文本
	private int mIconId = -1;//分享url icon
	private String mCover = "";//分享图片
	private boolean mLocalCover = true;
	private String mFile;//分享文件
	private Bitmap mBitmap;//分享link的图片

	private int shareType = SHARE_TYPE_TEXT;//默认分享文本

	protected CPAppShareAction(Context context) {
		super(context);
		mDefautPlatformActionListener.setmShareAction(this);
	}

	public void setShareText(String content){
		this.shareType = SHARE_TYPE_TEXT;
		this.mContent = content;
	}

	public void setShareLink(String url,String title,Bitmap bitmap){
		this.shareType = SHARE_TYPE_LINK;
		this.mUrl = url;
		this.mTitle = title;
		this.mBitmap = bitmap;
	}

	public void setShareLink(String url,String title,int iconId){
		this.shareType = SHARE_TYPE_LINK;
		this.mUrl = url;
		this.mTitle = title;
		this.mIconId = iconId;
	}

	public void setShareImage(String cover,boolean localCover){
		this.shareType = SHARE_TYPE_IMAGE;
		this.mCover = cover;
		this.mLocalCover = localCover;
	}

	public void setShareFile(String filePath,String cover,String url,String title,String content){
		this.shareType = SHARE_TYPE_FILE;
		this.mCover = cover;
		this.mUrl = url;
		this.mTitle = title;
		this.mContent = content;
		this.mFile = filePath;
	}

	@Override
	public void sharePlantForm(int action) {
		ShareSDK.initSDK(mContext, ShareConfig.APPKEY);
		switch (action) {
		case ShareAction.WX_FRIEND:
			share2WXFriend();
			break;
		case ShareAction.WX_CIRCLE:
			share2WXCircle();
			break;
		case ShareAction.COPY:// 复制
			duplicate2Clipboard();
			break;
		case ShareAction.SYSTEM_SEND://系统自带
			systemSend();
			break;

		}
	}
	private void systemSend(){
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT,mUrl);
		sendIntent.putExtra(Intent.EXTRA_TITLE, mUrl);
		sendIntent.setType("text/plain");
		mContext.startActivity(sendIntent);
	}
	@Override
	public void shareSinaWeibo() {
		//暂不支持微博分享
	}

	private void share2WXFriend(){
		if (shareType == SHARE_TYPE_LINK){
			if (mBitmap != null) {
				CPShareLink2WXFriend(mUrl, mTitle, mBitmap);
			}else if (mIconId != -1){
				CPShareLink2WXFriend(mUrl, mTitle, mIconId);
			}
		}else if (shareType == SHARE_TYPE_IMAGE){
			CPShareImage2WXFriend(mCover,mLocalCover);
		}else if (shareType == SHARE_TYPE_FILE){
			CPShareFile2WXFriend(mFile,mTitle,mContent,mUrl,"",mCover);
		}else {
			CPShareText2WXFriend(mContent);
		}
	}

	private void share2WXCircle(){
		if (shareType == SHARE_TYPE_LINK){
			if (mBitmap != null) {
				CPShareLink2WXFriend(mUrl, mTitle, mBitmap);
			}else if (mIconId != -1) {
				CPShareLink2WXCircle(mUrl, mTitle, mIconId);
			}
		}else if (shareType == SHARE_TYPE_IMAGE){
			CPShareImage2WXCircle(mCover,mLocalCover);
		}else if (shareType == SHARE_TYPE_FILE){
			//朋友圈不能分享文件
		}else {
			CPShareText2WXCircle(mContent);
		}
	}

	private void duplicate2Clipboard(){
		ClipboardManager cmb = (ClipboardManager) mContext
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip;
		if (shareType == SHARE_TYPE_LINK){
			clip = ClipData.newPlainText("", mUrl);
		}else if (shareType == SHARE_TYPE_IMAGE){
			clip = ClipData.newPlainText("", mCover);
		}else if (shareType == SHARE_TYPE_FILE){
			clip = ClipData.newPlainText("", mFile);
		}else {
			clip = ClipData.newPlainText("", mContent);
		}
		cmb.setPrimaryClip(clip);
		Toast.makeText(mContext, "已复制到剪切板", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 分享链接给微信好友
	 * @param url 链接
	 * @param title 链接标题
     * @param iconId 链接图片id
     */
	private void CPShareLink2WXFriend(String url,String title,int iconId){
		if (!TextUtils.isEmpty(url)) {
			Platform circle = ShareSDK.getPlatform(mContext, Wechat.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(Wechat.NAME, getWXAPPConfigParams());

			Wechat.ShareParams sp = new Wechat.ShareParams();
			sp.setShareType(Platform.SHARE_WEBPAGE);
			Bitmap bitmap;
			if (iconId != -1){
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), iconId);
			}else {
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.share_default_icon);
			}
			sp.setImageData(bitmap);
			sp.setTitle(title);
			sp.setText(mContent);
			sp.setUrl(url);
			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效链接地址", Toast.LENGTH_SHORT).show();
		}
	}

	private void CPShareLink2WXFriend(String url,String title,Bitmap bitmap){
		if (!TextUtils.isEmpty(url)) {
			Platform circle = ShareSDK.getPlatform(mContext, Wechat.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(Wechat.NAME, getWXAPPConfigParams());

			Wechat.ShareParams sp = new Wechat.ShareParams();
			sp.setShareType(Platform.SHARE_WEBPAGE);
			sp.setImageData(bitmap);
			sp.setTitle(title);
			sp.setText(mContent);
			sp.setUrl(url);
			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效链接地址", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 分享链接到微信朋友圈
	 * @param url 链接
	 * @param title 链接标题
	 * @param iconId 链接图片id
	 */
	private void CPShareLink2WXCircle(String url,String title,int iconId){
		if (!TextUtils.isEmpty(url)) {
			Platform circle = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(WechatMoments.NAME, getWXAPPConfigParams());

			WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
			sp.setShareType(Platform.SHARE_WEBPAGE);

			Bitmap bitmap;
			if (iconId != -1){
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), iconId);
			}else {
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.share_default_icon);
			}
			sp.setImageData(bitmap);
			sp.setTitle(title);
			sp.setUrl(url);
			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效链接地址", Toast.LENGTH_SHORT).show();
		}
	}

	private void CPShareLink2WXCircle(String url,String title,Bitmap bitmap){
		if (!TextUtils.isEmpty(url)) {
			Platform circle = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(WechatMoments.NAME, getWXAPPConfigParams());

			WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
			sp.setShareType(Platform.SHARE_WEBPAGE);

			sp.setImageData(bitmap);
			sp.setTitle(title);
			sp.setUrl(url);
			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效链接地址", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 分享图片给好友
	 * @param cover
	 * @param localImage
	 */
	private void CPShareImage2WXFriend(String cover,boolean localImage){
		if (!TextUtils.isEmpty(cover)) {
			Platform circle = ShareSDK.getPlatform(mContext, Wechat.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(Wechat.NAME, getWXAPPConfigParams());

			Wechat.ShareParams sp = new Wechat.ShareParams();
			sp.setShareType(Platform.SHARE_IMAGE);
			if (localImage) {
				if (cover.contains("http://")){
					sp.setImageUrl(cover);
				}else {
					File file = new File(cover);
					if (!file.exists()) {
						Toast.makeText(mContext, "分享失败，文件不存在", Toast.LENGTH_SHORT).show();
						return;
					}
					sp.setImagePath(cover);
				}
			}else {
				sp.setImageUrl(cover);
			}

			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效图片路径", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 分享图片到微信朋友圈
	 * @param cover
	 * @param localImage
	 */
	private void CPShareImage2WXCircle(String cover,boolean localImage){
		if (!TextUtils.isEmpty(cover)) {
			Platform circle = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(WechatMoments.NAME, getWXAPPConfigParams());

			WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
			sp.setShareType(Platform.SHARE_IMAGE);

			if (localImage) {
				if (cover.contains("http://")){
					sp.setImageUrl(cover);
				}else {
					File file = new File(cover);
					if (!file.exists()) {
						Toast.makeText(mContext, "分享失败，文件不存在", Toast.LENGTH_SHORT).show();
						return;
					}
					sp.setImagePath(cover);
				}
			}else {
				sp.setImageUrl(cover);
			}

			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效图片地址", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 分享文件给好友
	 * @param filePath 本地文件
	 * @param filePath
	 * @param title
	 * @param content
	 * @param url
     * @param fileDesc
	 * @param cover
     */
	private void CPShareFile2WXFriend(String filePath,String title,String content,String url,String fileDesc,String cover){
		if (!TextUtils.isEmpty(filePath)) {
			Platform circle = ShareSDK.getPlatform(mContext, Wechat.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}

			File file = new File(filePath);
			if (!file.exists()) {
				Toast.makeText(mContext, "分享失败，文件不存在", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(Wechat.NAME, getWXAPPConfigParams());

			Wechat.ShareParams sp = new Wechat.ShareParams();
			sp.setShareType(Platform.SHARE_FILE);
			sp.setFilePath(filePath);
			sp.setText(content);
			sp.setTitle(title);
			sp.setExtInfo(fileDesc);

			if (!TextUtils.isEmpty(cover) && cover.contains("http://")){
				sp.setImageUrl(cover);
			}else {
				sp.setImagePath(cover);
			}

			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效文件路径", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 分享文本给好友
	 * @param content
	 */
	private void CPShareText2WXFriend(String content){
		if (!TextUtils.isEmpty(content)) {
			Platform circle = ShareSDK.getPlatform(mContext, Wechat.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(Wechat.NAME, getWXAPPConfigParams());

			Wechat.ShareParams sp = new Wechat.ShareParams();
			sp.setShareType(Platform.SHARE_TEXT);

			sp.setText(content);
			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效内容", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 分享文本到微信朋友圈
	 * @param content
	 */
	private void CPShareText2WXCircle(String content){
		if (!TextUtils.isEmpty(content)) {
			Platform circle = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
			if (!circle.isClientValid()) {
				Toast.makeText(mContext, "分享失败，请先安装微信", Toast.LENGTH_SHORT).show();
				return;
			}
			circle.setPlatformActionListener(mDefautPlatformActionListener);

			ShareSDK.setPlatformDevInfo(WechatMoments.NAME, getWXAPPConfigParams());

			WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
			sp.setShareType(Platform.SHARE_TEXT);

			sp.setText(content);
			circle.share(sp);
		} else {
			Toast.makeText(mContext, "分享失败，无效内容", Toast.LENGTH_SHORT).show();
		}
	}

	/*获取微信配置参数*/
	private HashMap<String,Object> getWXAPPConfigParams(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("AppId", ShareConfig.APPID_CIRCLE_FRIEND);
		map.put("AppSecret", ShareConfig.APPSECRET_CIRCLE_FRIEND);
		map.put("Enable", ShareConfig.ENABLE_CIRCLE_FRIEND);
		map.put("BypassApproval", ShareConfig.BYPASSAPPROVAL_CIRCLE_FRIEND);
		return map;
	}
}
