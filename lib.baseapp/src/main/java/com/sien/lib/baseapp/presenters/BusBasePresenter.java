package com.sien.lib.baseapp.presenters;

import de.greenrobot.event.EventBus;

/**
 * UI逻辑管理基类(注册了总线事件派发机制)
 * 
 * @author sien
 * 
 * @注
 * 继承自该类的子类，在页面销毁时都需调用父类的destory来取消订阅事件、回收变量
 * 
 */
public abstract class BusBasePresenter  extends BasePresenter {

	private boolean eventBusInited = false;

	public BusBasePresenter() {
		super();
		init();
	}

	protected void init() {
		if (!eventBusInited) {
			EventBus.getDefault().register(this);
			eventBusInited = true;
		}
	}

	public void destory() {
		if (updateMessageHander != null) {
			updateMessageHander.removeCallbacksAndMessages(null);
			updateMessageHander = null;
		}

		if (eventBusInited) {
			EventBus.getDefault().unregister(this);
			eventBusInited = false;
		}

		super.destory();
	}

}
