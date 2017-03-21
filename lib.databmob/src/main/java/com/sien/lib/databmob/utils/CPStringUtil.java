package com.sien.lib.databmob.utils;

import android.text.TextUtils;

import com.sien.lib.databmob.config.DatappConfig;

/**
 * @author sien
 * @date 2016/11/2
 * @descript 字符串工具类
 *
 */
public class CPStringUtil {
    /**
     * 字符串为null时转换成空字符串
     * @param orignal 输入字符串
     */
    public static String safeString(String orignal) {
        return TextUtils.isEmpty(orignal) ? "" : orignal;
    }

    /**
     * 字符拼接符
     */
    public static String appendIdentifier(String value,float... suffixs){
        return appendIdentifierSeperate(value,"_",suffixs);
    }
    public static String appendIdentifierSeperate(String value,String seperate,float... suffixs){
        if (seperate == null){
            seperate = "_";
        }
        for (float suffix : suffixs) {
            value += seperate + suffix;
        }
        return value;
    }

    /**
     * 字符拼接符
     */
    public static String appendIdentifier(String value,String suffix){
        return appendIdentifierSeperate(value,"_",suffix);
    }
    public static String appendIdentifierSeperate(String value,String seperate,String suffix){
        if (seperate == null){
            seperate = "_";
        }
        value += seperate + suffix;
        return value;
    }

    public static String appendIdentifier(String value,String... suffixs){
        return appendIdentifierSeperate(value,"_",suffixs);
    }

    /**
     * 字符拼接符
     */
    public static String appendIdentifierSeperate(String value,String seperate,String... suffixs){
        if (seperate == null){
            seperate = "_";
        }
        for (String suffix : suffixs) {
            value += seperate + suffix;
        }
        return value;
    }

    /** 手机号保密 */
    public static String serectMobileNumber(String mobile) {
        String res = "";
        if (!TextUtils.isEmpty(mobile)) {
            res = mobile.substring(0, 3) + "****" + mobile.substring(7);
        }
        return res;
    }

    /**
     * 字符串反转
     * @param value
     */
    public static String convertStr(String value){
        String strReverse = new StringBuffer(value).reverse().toString();
        return strReverse;
    }

    /**
     * <pre>
     * 实现将整形数字反转
     * </pre>
     *
     * @param num
     *            要反转的数字
     * @return 反转后的数字
     */
    public static int numReverse(int num) {
        int modNum = 0;// 余数
        int result = 0;

        while (num / 10 != 0) {// 当num为一位数时，跳出循环
            modNum = num % 10;
            num = num / 10;
            result = result * 10 + modNum;
        }
        return result * 10 + num;// 当num为一位数时，返回结果
    }

    /**
     * 以万为单位缩短数字
     * @return
     */
    public static String numShortUnit(int num){
        return numShortUnit(num,10000,"万");
    }

    private static String numShortUnit(int num,int unit,String unitStr){
        if (unit == 0){
            return "";
        }

        String value = "";
        if (num / unit != 0){//小于1万时，直接显示
            value = num / unit + unitStr;
        }else{
            value = String.valueOf(num);
        }
        return value;
    }

    /**
     * 比较两个字符串的大小，按字母的ASCII码比较
     * @param pre
     * @param next
     * @return
     * */
    public static boolean boostCompare(String pre, String next){
        if(null == pre || null == next || "".equals(pre) || "".equals(next)){
            CPLogUtil.logError("CPStringUtil ","字符串比较数据不能为空！");
            return false;
        }

        char[] c_pre = pre.toCharArray();
        char[] c_next = next.toCharArray();

        int minSize = Math.min(c_pre.length, c_next.length);

        for (int i = 0; i < minSize; i++) {
            if((int)c_pre[i] > (int)c_next[i]){
                return true;
            }else if((int)c_pre[i] < (int)c_next[i]){
                return false;
            }
        }
        if(c_pre.length > c_next.length){
            return true;
        }

        return false;
    }

    /**
     * 中括号包裹字符 【msg】 or val(null or “”)
     * @param val
     * @return
     */
    public static String bracketString(String val){
        if (!TextUtils.isEmpty(val)) {
            return "【" + val + "】";
        }else {
            return val;
        }
    }

    /**
     * 补充图片路径 （定制规格图片）
     */
    public static String remediImagePath(String path){
        String wholeIcon = path;
        if (!TextUtils.isEmpty(path) && !path.contains("http://")){
            wholeIcon = DatappConfig.APP_BASE_CUSTOM_IMAGE_URL + path;
        }

        return wholeIcon;
    }
}
