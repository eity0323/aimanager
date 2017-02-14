package com.sien.lib.share.action;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * @author sien
 * @date 2016/12/13
 * @descript 分享建造者
 */
public class CPAppShareBuilder {
    private CPAppShareAction appShareAction;

    public CPAppShareAction buildShareText(Context context, String content){
        appShareAction = new CPAppShareAction(context);
        appShareAction.setShareText(content);

        return appShareAction;
    }

    public CPAppShareAction buildShareLink(Context context, String url, String title, int iconId){
        appShareAction = new CPAppShareAction(context);
        appShareAction.setShareLink(url,title,iconId);

        return appShareAction;
    }

    public CPAppShareAction buildShareLink(Context context, String url, String title, Bitmap bitmap){
        appShareAction = new CPAppShareAction(context);
        appShareAction.setShareLink(url,title,bitmap);

        return appShareAction;
    }

    public CPAppShareAction buildShareImage(Context context, String cover, boolean localCover){
        appShareAction = new CPAppShareAction(context);
        appShareAction.setShareImage(cover,localCover);

        return appShareAction;
    }

    public CPAppShareAction buildShareFile(Context context, String filePath, String cover, String url, String title, String content){
        appShareAction = new CPAppShareAction(context);
        appShareAction.setShareFile(filePath,cover,url,title,content);

        return appShareAction;
    }
}
