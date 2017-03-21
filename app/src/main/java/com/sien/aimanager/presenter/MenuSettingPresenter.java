package com.sien.aimanager.presenter;

import android.content.Context;
import android.os.Message;

import com.sien.aimanager.R;
import com.sien.aimanager.beans.SettingBean;
import com.sien.aimanager.model.IMenuSettingViewModel;
import com.sien.lib.baseapp.config.CPConfiguration;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;
import com.sien.lib.databmob.beans.VersionCheckVO;
import com.sien.lib.databmob.cache.BaseRepository;
import com.sien.lib.databmob.config.DatappConfig;
import com.sien.lib.databmob.events.DatappEvent;
import com.sien.lib.databmob.network.result.VersionCheckResult;
import com.sien.lib.databmob.utils.CPDeviceUtil;
import com.sien.lib.databmob.utils.CPFileUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;

/**
 * @author sien
 * @date 2016/12/30
 * @descript 隐藏设置页
 */
public class MenuSettingPresenter extends BusBaseBoostPresenter {
    private final int MSG_UPDATE_VERSIONCHECK = 1;//版本校验

    private IMenuSettingViewModel impl;

    private VersionCheckVO versionCheckVO;

    private List<SettingBean> datasource;

    public MenuSettingPresenter(Context context){
        mcontext = context;
        impl = (IMenuSettingViewModel)context;

        updateMessageHander = new InnerHandler(this);
    }

    public List<SettingBean> getDatasource() {
        if (datasource == null){
            initSettingData();
        }
        return datasource;
    }

    public VersionCheckVO getVersionCheckVO() {
        return versionCheckVO;
    }

    public void requestCheckVersion(){
//        MainRequestAction.requestVersionCheck(mcontext);
    }

    @Override
    public void onStart() {

    }

    public void changeCacheData(){
        String cacheSize = "0K";
        try {
            cacheSize = CPFileUtils.getTotalCacheSize(mcontext);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (datasource != null){
            datasource.get(0).setValue(cacheSize);
        }
    }

    public void changeRequestSource(){
        String temp = "缓存优先";
        if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_ONLEY_CACHE){
            temp = "仅缓存";
        }else if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_ONLEY_NETWORK){
            temp = "仅网络";
        }else if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_BOTH) {
            temp = "先缓存再网络";
        }
        if (datasource != null){
            datasource.get(1).setValue(temp);
        }
    }

    public void changeVersion(){
        String res = CPDeviceUtil.getVersionName(mcontext);
        if (datasource != null){
            datasource.get(2).setValue(res);
        }
    }

    public void changeNetworkEnv(){
        String  temp = "正式环境";
        if (DatappConfig.enviromentType == DatappConfig.ENV_DEVELOP){
            temp = "开发环境";
        }else if (DatappConfig.enviromentType == DatappConfig.ENV_TEST){
            temp = "测试环境";
        }
        if (datasource != null){
            datasource.get(3).setValue(temp);
        }
    }

    public void changeLogStatus(){
        String res = "开";
        if (DatappConfig.IS_DEV){
            res = "开";
        }else {
            res = "关";
        }
        if (datasource != null){
            datasource.get(4).setValue(res);
        }
    }

    public void changePwdStatus(){
        String res = "关";
        if (CPConfiguration.USING_PASSWORD){
            res = "开";
        }else {
            res = "关";
        }
        if (datasource != null){
            datasource.get(5).setValue(res);
        }
    }

    private void initSettingData(){
        datasource = new ArrayList<>();
        //缓存
        SettingBean bean = new SettingBean();
        bean.setTitle(mcontext.getString(R.string.setting_clear_cache));
        String cacheSize = "0K";
        try {
            cacheSize = CPFileUtils.getTotalCacheSize(mcontext);
        }catch (Exception e){
            e.printStackTrace();
        }
        bean.setValue(cacheSize);
        datasource.add(bean);

        //数据来源
        bean = new SettingBean();
        bean.setTitle(mcontext.getString(R.string.setting_default_request));
        String temp = "缓存优先";
        if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_ONLEY_CACHE){
            temp = "仅缓存";
        }else if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_ONLEY_NETWORK){
            temp = "仅网络";
        }else if (DatappConfig.DEFAULT_REQUEST_MODEL == BaseRepository.REQUEST_BOTH) {
            temp = "先缓存再网络";
        }
        bean.setValue(temp);
        datasource.add(bean);

        //版本
        bean = new SettingBean();
        bean.setTitle(mcontext.getString(R.string.setting_new_version));
        String res = CPDeviceUtil.getVersionName(mcontext);
        bean.setValue(res);
        datasource.add(bean);

        //网络环境
        bean = new SettingBean();
        bean.setTitle(mcontext.getString(R.string.setting_change_environment));
        temp = "开发环境";
        if (DatappConfig.enviromentType == DatappConfig.ENV_DEVELOP){
            temp = "测试环境";
        }else if (DatappConfig.enviromentType == DatappConfig.ENV_OFFICAL){
            temp = "正式环境";
        }
        bean.setValue(temp);
        datasource.add(bean);

        //日志
        bean = new SettingBean();
        bean.setTitle(mcontext.getString(R.string.setting_switch_log));
        if (DatappConfig.IS_DEV){
            res = "开";
        }else {
            res = "关";
        }
        bean.setValue(res);
        datasource.add(bean);

        //密码
        bean = new SettingBean();
        bean.setTitle(mcontext.getString(R.string.setting_using_psd));
        if (CPConfiguration.USING_PASSWORD){
            res = "开";
        }else {
            res = "关";
        }
        bean.setValue(res);
        datasource.add(bean);

        //关于
        bean = new SettingBean();
        bean.setTitle(mcontext.getString(R.string.about_title));
        bean.setValue("");
        datasource.add(bean);
    }

    @Subscribe
    public void RequestVersionCheckEventReceiver(DatappEvent.RequestVersionCheckEvent event){
        if (event != null){
            postMessage2UI(event.getResult(),MSG_UPDATE_VERSIONCHECK);
        }
    }

    @Override
    protected void handleMessageFunc(BasePresenter helper, Message msg) {
        super.handleMessageFunc(helper, msg);

        if (helper == null){
            return;
        }

        if (msg.what == MSG_UPDATE_VERSIONCHECK){
            if (msg.obj != null){
                VersionCheckResult result = (VersionCheckResult) msg.obj;
                if (result != null && result.checkRequestSuccess()){
                    List<VersionCheckVO> list = result.getData();
                    if (list != null && list.size() > 0){
                        versionCheckVO = list.get(0);

                        impl.refreshVersionCheck(true);
                        return;
                    }
                }
            }
            impl.refreshVersionCheck(false);
        }
    }

    @Override
    public void releaseMemory() {
        super.releaseMemory();
        versionCheckVO = null;
    }

    @Override
    public void destory() {
        super.destory();
        impl = null;
    }

    @Override
    public IMenuSettingViewModel createViewModel() {
        return impl;
    }
}
