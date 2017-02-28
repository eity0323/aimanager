package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.beans.AimBean;
import com.sien.aimanager.model.IMainAimObjectViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.baseapp.utils.CollectionUtils;
import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.db.helper.AimItemDBHelper;
import com.sien.lib.datapp.db.helper.AimTypeDBHelper;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2017/2/13
 * @descript aimobject版主页presenter
 */
public class MainAimObjectPresenter extends BusBaseBoostPresenter {
    private final int MSG_UPDATE_AIMTYPE = 1;//查询自动创建目标类型
    private final int MSG_UPDATE_NEW_AIMTYPE = 2;//新建目标分类

    private IMainAimObjectViewModel impl;

    private List<AimBean> datasource;
//    private List<AimTypeVO> aimTypeVOList;

    public MainAimObjectPresenter(Context context){
        mcontext = context;
        updateMessageHander = new InnerHandler(this);

        impl = (IMainAimObjectViewModel)context;
    }

    public List<AimBean> getDatasource() {
        if (datasource == null) {
            datasource = new ArrayList<>();
        }
        return datasource;
    }

//    public List<AimTypeVO> getAimTypeVOList() {
//        if (aimTypeVOList == null) {
//            aimTypeVOList = new ArrayList<>();
//        }
//        return aimTypeVOList;
//    }

    /**
     * 请求当天目标数据
     * @param date
     */
    public void requestAimBeanData(Date date){
        if (datasource == null) {
            datasource = new ArrayList<>();
        }else {
            datasource.clear();
        }

        List<AimTypeVO> objectVOList = AimTypeDBHelper.requestAimTypeAutoByDate(mcontext,date);
        if (!CollectionUtils.IsNullOrEmpty(objectVOList)) {
            AimBean bean;
            List<AimItemVO> recordVOList;

            for (AimTypeVO item : objectVOList){
                bean = new AimBean();
                bean.aimTypeVO = item;

                recordVOList = AimItemDBHelper.requestAimItemDataSync(mcontext,item.getId());
                if (!CollectionUtils.IsNullOrEmpty(recordVOList)){
                    bean.aimItemVOList = recordVOList;
                }

                datasource.add(bean);
            }

            impl.refreshRequestAimBean(RequestFreshStatus.REFRESH_SUCCESS);
            return;
        }

        impl.refreshRequestAimBean(RequestFreshStatus.REFRESH_NODATA);
    }

//    public void requestAimTypeData(){
//        MainDatabaseAction.requestAimTypeAutoDatas(mcontext);
//    }

//    @Subscribe
//    public void AimTypeEventReceiver(DatappEvent.AimTypeEvent event){
//        if (event != null){
//            postMessage2UI(event.getResult(),MSG_UPDATE_AIMTYPE);
//        }
//    }

    @Subscribe
    public void insertAimTypeEventReceiver(DatappEvent.insertAimTypeEvent event){
        if (event != null){
            postMessage2UI(event.getResult(),MSG_UPDATE_NEW_AIMTYPE);
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        if (helper == null) return;

        MainAimObjectPresenter theActivity = (MainAimObjectPresenter) helper;
        if (theActivity == null)    return;

        if (impl == null)   return;

        if (msg.what == MSG_UPDATE_AIMTYPE){
//            if (msg.obj != null){
//                List<AimTypeVO> list = (List<AimTypeVO>) msg.obj;
//                if (list != null && list.size() > 0 ){
//                    aimTypeVOList = list;
//                    impl.refreshRequestAimType(RequestFreshStatus.REFRESH_SUCCESS);
//                    return;
//                }
//                impl.refreshRequestAimType(RequestFreshStatus.REFRESH_NODATA);
//                return;
//            }
//            impl.refreshRequestAimType(RequestFreshStatus.REFRESH_ERROR);
        }else if (msg.what == MSG_UPDATE_NEW_AIMTYPE){
            impl.refreshNewAimType(RequestFreshStatus.REFRESH_SUCCESS);
        }
    }

    @Override
    public IMainAimObjectViewModel createViewModel() {
        return impl;
    }
}
