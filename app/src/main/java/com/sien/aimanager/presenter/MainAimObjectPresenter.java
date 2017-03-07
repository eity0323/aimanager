package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.beans.AimBean;
import com.sien.aimanager.config.AppConfig;
import com.sien.aimanager.control.PriorityComparator;
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
import com.sien.lib.datapp.utils.CPDateUtil;

import java.util.ArrayList;
import java.util.Collections;
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

        //根据优先级排序
        List<AimTypeVO> objectVOList = AimTypeDBHelper.requestAimTypeAutoByDate(mcontext,date);
        if (!CollectionUtils.IsNullOrEmpty(objectVOList)) {
            // 排序(优先级高的往前排)
            Collections.sort(objectVOList, new PriorityComparator());

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

    /*校验并生成自动创建今日目标*/
    public void checkAndCreateAutoAimType(Date date){
        if (date == null)   return;

        // 1、查询starttime为今日的固定分类
        List<AimTypeVO> fixList = AimTypeDBHelper.requestAimTypeFixedByDate(mcontext,date);

        if (CollectionUtils.IsNullOrEmpty(fixList))     return;

        for (AimTypeVO fixAimType : fixList) {
            //非激活状态不自动创建
            if (fixAimType.getActive() != null && !fixAimType.getActive().booleanValue()) {
                continue;
            }

            //有开始日期，且未到开始日期不创建
            if (fixAimType.getStartTime() != null) {
                int startDiff = CPDateUtil.getTimeDiffDays(fixAimType.getModifyTime(), fixAimType.getStartTime());
                if (startDiff < 0) continue;
            }

            //有结束日期，且超过结束日期不创建
            if (fixAimType.getEndTime() != null) {
                int endDiff = CPDateUtil.getTimeDiffDays(fixAimType.getEndTime(), fixAimType.getModifyTime());
                if (endDiff < 0) continue;
            }

            //无目标项则不创建该分类对象
            long count = AimItemDBHelper.requestAimItemCountSync(mcontext,fixAimType.getId());
            if (count <= 0) continue;

            //2、查询是否为固定分类创建今日事项
            List<AimTypeVO> list = AimTypeDBHelper.requestAimTypeAutoByAimTypeIdSync(mcontext, fixAimType.getId());

            //3、如果不存在该自动创建分类则生成;否则不做处理由创建机制自动创建
            if (CollectionUtils.IsNullOrEmpty(list)) {
                AimTypeVO aimTypeVO = new AimTypeVO();
                aimTypeVO.setCustomed(true);
                aimTypeVO.setDesc(fixAimType.getDesc());
                aimTypeVO.setFinishPercent(0);
                aimTypeVO.setFinishStatus(AimTypeVO.STATUS_UNDO);
                aimTypeVO.setPeriod(fixAimType.getPeriod());
                aimTypeVO.setModifyTime(fixAimType.getModifyTime());
                aimTypeVO.setStartTime(fixAimType.getStartTime());
                aimTypeVO.setEndTime(fixAimType.getEndTime());
                aimTypeVO.setPriority(AimTypeVO.PRIORITY_FIVE);
                aimTypeVO.setRecyclable(false);
                aimTypeVO.setPlanProject(fixAimType.getPlanProject());
                aimTypeVO.setTargetPeriod(fixAimType.getTargetPeriod());
                aimTypeVO.setCover(fixAimType.getCover());
                aimTypeVO.setTypeName(AppConfig.formatGenerateTypeName(fixAimType.getTypeName(),fixAimType.getModifyTime()));
                aimTypeVO.setFirstExtra(String.valueOf(fixAimType.getId()));

                //创建目标分类
                AimTypeDBHelper.insertOrReplaceAimTypeSync(mcontext, aimTypeVO);

                //创建分享项
                generateAimItemByAimType(fixAimType.getId(), aimTypeVO.getId(), fixAimType.getStartTime());
            }
        }
    }

    /*批量插入目标项*/
    private void generateAimItemByAimType(Long aimTypeId,Long newAimTypeId,Date nowDate){
        List<AimItemVO> dayList = AimItemDBHelper.requestAimItemDataSync(mcontext,aimTypeId);

        if (dayList != null && dayList.size() > 0) {
            List<AimItemVO> newDataList = new ArrayList<>();

            AimItemVO itemVO;
            for (AimItemVO item : dayList){
                itemVO = new AimItemVO();
                itemVO.setAimName(item.getAimName());
                itemVO.setDesc(item.getDesc());
                item.setStartTime(nowDate);
                itemVO.setModifyTime(nowDate);
                itemVO.setFinishStatus(AimItemVO.STATUS_UNDO);
                itemVO.setPriority(AimItemVO.PRIORITY_FIVE);
                itemVO.setFinishPercent(0);
                itemVO.setAimTypeId(newAimTypeId);

                newDataList.add(itemVO);
            }

            if (newDataList.size() > 0) {
                //批量插入目标项
                AimItemDBHelper.insertOrReplaceAimItemListSync(mcontext, newDataList);
            }
        }
    }

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
