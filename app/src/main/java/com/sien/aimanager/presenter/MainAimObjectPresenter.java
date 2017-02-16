package com.sien.aimanager.presenter;

import android.content.Context;

import com.sien.aimanager.beans.AimBean;
import com.sien.aimanager.model.IMainAimObjectViewModel;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.baseapp.utils.CollectionUtils;
import com.sien.lib.datapp.beans.AimItemVO;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.db.helper.AimItemDBHelper;
import com.sien.lib.datapp.db.helper.AimTypeDBHelper;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sien
 * @date 2017/2/13
 * @descript aimobject版主页presenter
 */
public class MainAimObjectPresenter extends BusBaseBoostPresenter {
    private IMainAimObjectViewModel impl;

    private List<AimBean> datasource;

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

        List<AimTypeVO> objectVOList = AimTypeDBHelper.requestAimTypeByDate(mcontext,date);
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

    @Override
    public IMainAimObjectViewModel createViewModel() {
        return impl;
    }
}
