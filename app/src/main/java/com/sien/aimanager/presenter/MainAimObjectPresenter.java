package com.sien.aimanager.presenter;

import android.content.Context;

import com.sien.aimanager.model.IMainAimObjectViewModel;
import com.sien.lib.baseapp.model.ICPBaseBoostViewModel;
import com.sien.lib.baseapp.presenters.BusBaseBoostPresenter;

/**
 * @author sien
 * @date 2017/2/13
 * @descript aimobject版主页presenter
 */
public class MainAimObjectPresenter extends BusBaseBoostPresenter {
    private IMainAimObjectViewModel impl;

    public MainAimObjectPresenter(Context context){
        mcontext = context;
        updateMessageHander = new InnerHandler(this);

        impl = (IMainAimObjectViewModel)context;
    }

    @Override
    public ICPBaseBoostViewModel createViewModel() {
        return impl;
    }
}
