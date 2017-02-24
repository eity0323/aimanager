package com.sien.lib.datapp.config;

import com.sien.lib.datapp.cache.BaseRepository;

/**
 * @author sien
 * @date 2017/1/23
 * @descript datapp模块配置
 */
public class DatappConfig {
    public static final int ENV_DEVELOP = 1;
    public static final int ENV_TEST = 2;
    public static final int ENV_OFFICAL = 3;

    public static final String REQUEST_MODEL_KEY = "request_model_key";//数据请求方式
    public static final String ENVIRONMENT_KEY = "environment_key";//网络环境
    public static final String PWDLOCK_KEY = "pwdlock_key";//密码锁

    public static String userAccount = "customer";      //默认用户，游客身份，用来区分用户缓存数据
    public static int enviromentType = ENV_OFFICAL;               //网络环境：1开发环境，2测试环境，3正式环境

    public static boolean IS_DEV = true;        //开发模式

    public static String DATABASENAME = "datapp_aimanager_db";//数据库名称

    public static String APP_BASE_URL = "https://api.bmob.cn/1/classes/";//接口api地址
    public static String APP_BASE_CUSTOM_IMAGE_URL = "http://shoelives.oss-cn-shenzhen.aliyuncs.com/";//定制商品图片地址

    public static int DEFAULT_REQUEST_MODEL = BaseRepository.REQUEST_ONLEY_NETWORK;//默认数据请求方式（从网络获取）

    /**
     * 设置正式环境
     */
    public static void setOfficalEnvironment(){
        enviromentType = ENV_OFFICAL;
        APP_BASE_URL = "https://api.bmob.cn/1/classes/";//接口api地址
        APP_BASE_CUSTOM_IMAGE_URL = "http://shoelives.oss-cn-shenzhen.aliyuncs.com/";//定制商品图片地址
    }

    public static void setDevelopEnvironment(){
        enviromentType = ENV_DEVELOP;
        APP_BASE_URL = "https://api.bmob.cn/1/classes/";//接口api地址
        APP_BASE_CUSTOM_IMAGE_URL = "http://shoelives.oss-cn-shenzhen.aliyuncs.com/";//定制商品图片地址
    }

    public static void setTestEnvironment(){
        enviromentType = ENV_TEST;
        APP_BASE_URL = "https://api.bmob.cn/1/classes/";//接口api地址
        APP_BASE_CUSTOM_IMAGE_URL = "http://shoelives.oss-cn-shenzhen.aliyuncs.com/";//定制商品图片地址
    }
}
