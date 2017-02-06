package com.sien.lib.baseapp.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sien
 * @date 2016/11/4
 * @descript 正则工具类
 */
public class CPPatternUtil {
    /**
     * 从字符串中读出数字
     * @param message
     * @return
     */
    public static String filterNumberFormString(String message){
        Pattern pattern = Pattern.compile("[^0-9]"); //正则表达式.
        Matcher matcher = pattern.matcher(message);
        String codeText = matcher.replaceAll("");
        return codeText;
    }

    /**
     * 验证手机号码是否正确
     * 仅判断了长度是否11位
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        if (TextUtils.isEmpty(mobiles)){
            return false;
        }
        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        boolean match = m.matches();
        if (!match) {
            if (mobiles.indexOf("00") == 0 || mobiles.indexOf("+86") == 0) {
                match = true;
            }
        }
        return match;
    }

    /**
     * 读取短信验证码
     * @param message
     * @return
     */
    public static String filterSMSCode(String message){
        Pattern pattern = Pattern.compile("[0-9]{4}"); //正则表达式.
        Matcher matcher = pattern.matcher(message);
        String codeText = "";
        if (matcher.find()){
            codeText = matcher.group();
        }
        return codeText;
    }

    /*检测emoji输入*/
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("/[\u1F60-\u1F64]|[\u2702-\u27B0]|[\u1F68-\u1F6C]|[\u1F30-\u1F70]|[\u2600-\u26ff]/g");
        Matcher m = p.matcher(string);
        return m.matches();
    }

    public static boolean isEmoji2(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
