package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.model.INewAimTypeViewModel;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.db.helper.AimTypeDBHelper;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.action.MainDatabaseAction;
import com.sien.lib.datapp.network.base.RequestFreshStatus;
import com.sien.lib.datapp.utils.CPDateUtil;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2017/2/5
 * @descript 新建目标分类presenter
 */
public class NewAimTypePresenter extends BusBaseBoostPresenter {
    private final int MSG_UPDATE_INSERTAIMTYPE = 1;//添加目标分类

    private INewAimTypeViewModel impl;

    public NewAimTypePresenter(Context context){
        mcontext = context;
        updateMessageHander = new InnerHandler(this);

        impl = (INewAimTypeViewModel) context;
    }

    public void insertOrReplaceAimTypeRecord(AimTypeVO aimTypeVO){
        MainDatabaseAction.insertOrReplaceAimType(mcontext,aimTypeVO);
    }

    /*校验并生成自动创建分类*/
    public void checkAndCreateAutoAimType(AimTypeVO fixAimType){
        //check auto create condition

        //非激活状态不自动创建
        if (fixAimType.getActive() != null && !fixAimType.getActive().booleanValue()){
            return;
        }

        //有开始日期，且未到开始日期不创建
        if (fixAimType.getStartTime() != null) {
            int startDiff = CPDateUtil.getTimeDiffDays(fixAimType.getModifyTime(), fixAimType.getStartTime());
            if (startDiff < 0) return;
        }

        //有结束日期，且超过结束日期不创建
        if (fixAimType.getEndTime() != null) {
            int endDiff = CPDateUtil.getTimeDiffDays(fixAimType.getEndTime(), fixAimType.getModifyTime());
            if (endDiff < 0) return;
        }

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
        aimTypeVO.setTypeName(CPDateUtil.getDateToString(fixAimType.getModifyTime(), CPDateUtil.DATE_FORMAT_3));

        //创建目标分类
        AimTypeDBHelper.insertOrReplaceAimTypeSync(mcontext,aimTypeVO);
    }

    public void searchAimTypeRecord(){
        MainDatabaseAction.requestAimTypeFixedDatas(mcontext);
    }

    @Subscribe
    public void insertAimTypeEventReceiver(DatappEvent.insertAimTypeEvent event){
        if (event != null){
            if (updateMessageHander != null){
                updateMessageHander.sendEmptyMessage(MSG_UPDATE_INSERTAIMTYPE);
            }
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        if (helper == null) return;

        if (impl == null)   return;

        NewAimTypePresenter theActivity = (NewAimTypePresenter) helper;
        if (theActivity == null)    return;

        if (msg.what == MSG_UPDATE_INSERTAIMTYPE){
            impl.refreshInsertAimType(RequestFreshStatus.REFRESH_SUCCESS);
        }
    }

    @Override
    public ICPBaseBoostViewModel createViewModel() {
        return impl;
    }

    @Override
    public void releaseMemory() {
        super.releaseMemory();

        impl = null;
    }
}
