package com.sien.lib.baseapp.presenters;

import com.sien.lib.baseapp.events.BaseAppEvents;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * UI逻辑管理基类(注册了总线事件派发机制)
 * 
 * @author sien
 * 
 * @注
 * 继承自该类的子类，在页面销毁时都需调用父类的destory来取消订阅事件、回收变量
 * 
 */
public abstract class BusBaseBoostPresenter<T extends ICPBaseBoostViewModel>  extends BasePresenter {

	private boolean eventBusInited = false;

	private ICPBaseBoostViewModel innerImpl;

	public BusBaseBoostPresenter() {
		super();
		init();
	}

	public abstract <T extends ICPBaseBoostViewModel> T createViewModel();

	protected void init() {
		if (!eventBusInited) {
			EventBus.getDefault().register(this);
			eventBusInited = true;
		}
	}

	public ICPBaseBoostViewModel getBaseBoostViewModel(){
		return innerImpl;
	}

	@Subscribe
	public void NetworkChangeEventReceiver(BaseAppEvents.NetworkChangeEvent event){
		if (innerImpl == null){
			innerImpl = createViewModel();
		}

		if (event != null){
			if (event.checkStatus()){
				if (innerImpl != null) {
					innerImpl.innerNetworkRefresh(true);
				}
			}else {
				if (innerImpl != null) {
					innerImpl.innerNetworkRefresh(false);
				}
			}
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
