package com.sien.aimanager.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.sien.aimanager.control.GeneratePeroidAimUtils;
import com.sien.lib.databmob.beans.AimTypeVO;

/**
 * @author sien
 * @date 2017/2/9
 * @descript 创建服务
 *
 * 用于创建目标项,执行完结束服务
 */
public class GenerateAimServices extends IntentService {
    private Context mcontext;
    public GenerateAimServices(){
        super("com.sien.aimanager.services.GenerateAimServices");
        mcontext = this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //阻塞线程，创建目标项
        int period = -1;
        if (intent != null){
            period = intent.getIntExtra("period",-1);
        }

        int diff = -1;
        if (intent != null){
            diff = intent.getIntExtra("diff",-1);
        }

        if (period < 0) return;
        if (diff < 0 ) return;

        //天、周、月、季、半年、年
        if (period == AimTypeVO.PERIOD_DAY || period == AimTypeVO.PERIOD_WEEK || period == AimTypeVO.PERIOD_MONTH || period == AimTypeVO.PERIOD_SEASON || period == AimTypeVO.PERIOD_HALF_YEAR || period == AimTypeVO.PERIOD_YEAR) {
            GeneratePeroidAimUtils.generatePeriodAimByPeriod(mcontext,period,diff);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
