package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.model.INewAimTypeViewModel;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.datapp.beans.AimTypeVO;
import com.sien.lib.datapp.events.DatappEvent;
import com.sien.lib.datapp.network.action.MainDatabaseAction;
import com.sien.lib.datapp.network.base.RequestFreshStatus;

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

    /*根据优先级别值获取索引*/
    public int getIndexByPriority(int priority){
        int position = 0;
        if (priority == 1){
            position = 4;
        }else if (priority == 2){
            position = 3;
        }else if (priority == 3){
            position = 2;
        }else if (priority == 4){
            position = 1;
        }else if (priority == 5){
            position = 0;
        }
        return position;
    }

    /*根据优先级别值获取文本*/
    public String getPriorityTextByPriority(int position){
        String priority = "5级";
        if (position == 1){
            priority = "5级";
        }else if (position == 2){
            priority = "4级";
        }else if (position == 3){
            priority = "3级";
        }else if (position == 4){
            priority = "2级";
        }else if (position == 5){
            priority = "1级";
        }
        return priority;
    }

    /*根据索引获取优先级别值*/
    public int getPriorityByIndex(int position){
        int priority = 5;
        if (position == 0){
            priority = 5;
        }else if (position == 1){
            priority = 4;
        }else if (position == 2){
            priority = 3;
        }else if (position == 3){
            priority = 2;
        }else if (position == 4){
            priority = 1;
        }
        return priority;
    }

    /*根据索引获取优先级别文本*/
    public String getPriorityTextByIndex(int position){
        String priority = "5级";
        if (position == 0){
            priority = "5级";
        }else if (position == 1){
            priority = "4级";
        }else if (position == 2){
            priority = "3级";
        }else if (position == 3){
            priority = "2级";
        }else if (position == 4){
            priority = "1级";
        }
        return priority;
    }

    /*根据周期值获取索引*/
    public int getIndexByPeriod(int period){
        int position = 0;
        if (period == 1){
            position = 0;
        }else if (period == 7){
            position = 1;
        }else if (period == 30){
            position = 2;
        }else if (period == 90){
            position = 3;
        }else if (period == 180){
            position = 4;
        }else if (period == 365){
            position = 5;
        }
        return position;
    }

    /*根据索引获取周期值*/
    public int getPeriodByIndex(int position){
        int period = 1;
        if (position == 0){
            period = 1;
        }else if (position == 1){
            period = 7;
        }else if (position == 2){
            period = 30;
        }else if (position == 3){
            period = 90;
        }else if (position == 4){
            period = 180;
        }else if (position == 5){
            period = 365;
        }
        return period;
    }

    /*根据索引获取周期文本*/
    public String getPeriodTextByIndex(int position){
        String period = "1天";
        if (position == 0){
            period = "1天";
        }else if (position == 1){
            period = "1周";
        }else if (position == 2){
            period = "1月";
        }else if (position == 3){
            period = "1季";
        }else if (position == 4){
            period = "半年";
        }else if (position == 5){
            period = "1年";
        }
        return period;
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
