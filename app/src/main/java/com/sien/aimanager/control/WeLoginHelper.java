package com.sien.aimanager.control;

import android.content.Context;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author sien
 * @date 2016/11/9
 * @descript 微信登录工具类
 */
public class WeLoginHelper {
    public static final String WE_APP_ID = "wx598e21bc1d406482";// 微信开放平台申请到的app_id
    private static final String WEIXIN_SCOPE = "snsapi_userinfo";// 用于请求用户信息的作用域
    private static final String WEIXIN_STATE = "login_state"; // 自定义

    public static String mobile = "";//登录手机号

    private boolean registered = false;

    private IWXAPI api;

    private Context mcontext;
    public WeLoginHelper(Context context){
        mcontext = context;

        api = WXAPIFactory.createWXAPI(mcontext, WE_APP_ID,false);
    }

    public void registerWechat(){
        if (api == null){
            api = WXAPIFactory.createWXAPI(mcontext, WE_APP_ID,false);
        }
        if (!registered) {
            // 将该app注册到微信
            api.registerApp(WE_APP_ID);

            registered = true;
        }
    }

    /*登录*/
    public void authLogin(){
        if (!registered){
            registerWechat();
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        api.sendReq(req);
    }

}
