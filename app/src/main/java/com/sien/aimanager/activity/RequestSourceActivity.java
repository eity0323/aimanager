package com.sien.aimanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sien.aimanager.R;
import com.sien.aimanager.config.AppConfig;
import com.sien.lib.baseapp.activity.CPBaseActivity;
import com.sien.lib.baseapp.presenters.BasePresenter;
import com.sien.lib.databmob.cache.BaseRepository;
import com.sien.lib.databmob.config.DatappConfig;
import com.sien.lib.databmob.control.CPSharedPreferenceManager;
import com.sien.lib.databmob.control.DataCleanManager;
import com.sien.lib.databmob.utils.CPFileUtils;

import java.util.ArrayList;

/**
 * @author sien
 * @date 2016/12/2
 * @descript 选择数据请求方式 or 网络环境切换
 */
public class RequestSourceActivity extends CPBaseActivity {
    private ListView listView;
    private ArrayList<String> ds;
    private TextView titleTV;

    private int entryType = 1;//选择数据请求方式，2 网络环境切换

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_request_source);
    }

    @Override
    public void initViews() {
        super.initViews();

        titleTV = findView(R.id.request_source_close);

        titleTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (entryType == 2){
                    setResult(AppConfig.REQUEST_CODE_ENVIRONMENT);
                }else {
                    setResult(AppConfig.REQUEST_CODE_DATE_SOURCE);
                }

                finish();
            }
        });

        listView = findView(R.id.request_source_list);

        if (getIntent().hasExtra("entryModel")){
            entryType = getIntent().getIntExtra("entryModel",1);
        }

        //数据请求方式
        String[] titles = getResources().getStringArray(
                R.array.setting_reqeust_source);

        if (entryType == 2){
            //网络环境
            titles = getResources().getStringArray(R.array.setting_environment_source);
            titleTV.setText(R.string.setting_change_environment);
        }

        ds = new ArrayList<>();
        for (String str : titles){
            ds.add(str);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.layout_request_source_item, R.id.demo_aty_pop_item_text,ds);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < ds.size()) {
                    if (entryType == 2){
                        cleanAppData();

                        //网络环境
                        if (position == 0) {
                            //开发环境
                            DatappConfig.setDevelopEnvironment();
                        }else if (position == 1){
                            //测试环境
                            DatappConfig.setTestEnvironment();
                        }else if (position == 2){
                            //正式环境
                            DatappConfig.setOfficalEnvironment();
                        }

                        CPSharedPreferenceManager.getInstance(RequestSourceActivity.this).saveData(DatappConfig.ENVIRONMENT_KEY, String.valueOf(DatappConfig.enviromentType));

                    }else {
                        //数据请求方式
                        if (position == 0) {
                            DatappConfig.DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_ONLEY_CACHE;
                            CPSharedPreferenceManager.getInstance(RequestSourceActivity.this).saveData(DatappConfig.REQUEST_MODEL_KEY, String.valueOf(BaseRepository.REQUEST_ONLEY_CACHE));
                        } else if (position == 1) {
                            DatappConfig.DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_ONLEY_NETWORK;
                            CPSharedPreferenceManager.getInstance(RequestSourceActivity.this).saveData(DatappConfig.REQUEST_MODEL_KEY, String.valueOf(BaseRepository.REQUEST_ONLEY_NETWORK));
                        } else if (position == 3) {
                            DatappConfig.DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_BOTH;
                            CPSharedPreferenceManager.getInstance(RequestSourceActivity.this).saveData(DatappConfig.REQUEST_MODEL_KEY, String.valueOf(BaseRepository.REQUEST_BOTH));
                        } else {
                            DatappConfig.DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_AUTO;
                            CPSharedPreferenceManager.getInstance(RequestSourceActivity.this).saveData(DatappConfig.REQUEST_MODEL_KEY, String.valueOf(BaseRepository.REQUEST_AUTO));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (entryType == 2){
            setResult(AppConfig.REQUEST_CODE_ENVIRONMENT);
        }else {
            setResult(AppConfig.REQUEST_CODE_DATE_SOURCE);
        }
        super.onBackPressed();
    }

    private void cleanAppData(){
        //清除缓存数据
//        CacheDataStorage.getInstance().releaseMemory();

        getSharedPreferences("cookie", Context.MODE_PRIVATE).edit().clear().commit();
        CPSharedPreferenceManager.getInstance(getApplicationContext()).clean();
        DataCleanManager.cleanSharedPreference(getApplicationContext());
        DataCleanManager.cleanExternalCache(getApplicationContext());
        DataCleanManager.cleanInternalCache(getApplicationContext());
        //删除本地缓存文件夹 /mnt/sdcard/Android/data/[app package]/cache
        DataCleanManager.deleteFielOrDirectoryByPath(CPFileUtils.getAppCacheRootFilePath(getApplicationContext()));

        CPSharedPreferenceManager.getInstance(RequestSourceActivity.this).saveData(DatappConfig.ENVIRONMENT_KEY, String.valueOf(DatappConfig.enviromentType));
    }

    @Override
    public void initial() {
        super.initial();
    }

    @Override
    public <V extends BasePresenter> V createPresenter() {
        return null;
    }
}
