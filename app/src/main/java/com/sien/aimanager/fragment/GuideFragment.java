package com.sien.aimanager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sien.aimanager.R;
import com.sien.aimanager.activity.GuideActivity;
import com.sien.lib.baseapp.fragment.CPBaseFragment;
import com.sien.lib.baseapp.presenters.BasePresenter;

/**
 * @author sien
 * @date 2016/8/25
 * @descript 引导页fragment
 */
public class GuideFragment extends CPBaseFragment {
    final static String LAYOUT_ID = "layoutid";

    public static GuideFragment newInstance(int layoutId){
        GuideFragment pane = new GuideFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID,layoutId);
        pane.setArguments(args);
        return pane;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(getArguments().getInt(LAYOUT_ID,-1),container,false);
        if(rootView.findViewById(R.id.btn_guide_start) != null){
            rootView.findViewById(R.id.btn_guide_start).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((GuideActivity)getActivity()).endTutorial();
                }
            });
        }
        return rootView;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initial() {

    }

    @Override
    public <V extends BasePresenter> V createPresenter() {
        return null;
    }
}
