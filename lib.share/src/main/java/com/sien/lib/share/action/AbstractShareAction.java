package com.sien.lib.share.action;

import android.content.Context;

import com.sien.lib.share.listener.DefautPlatformActionListener;

/**
 * 
 * @author shaoyu
 *
 */
public abstract class AbstractShareAction {
	
	public static class ShareAction{
		public static final int WX_FRIEND = 1;
		public static final int WX_CIRCLE = 2;
		public static final int QQ_FRIEND = 3;
		public static final int QQ_ZONE = 4;
		public static final int WEIBO = 5;
		public static final int SEND_FRIEND = 6;
		public static final int COPY = 7;
		public static final int SYSTEM_SEND = 8;
	}

	protected Context mContext;
	protected DefautPlatformActionListener mDefautPlatformActionListener;
	public AbstractShareAction(Context context){
		this.mContext = context;
		mDefautPlatformActionListener = new DefautPlatformActionListener(context);
	}
	
	public abstract void sharePlantForm(int action);
	
	public abstract void shareSinaWeibo();
	
}
