package com.sien.lib.datapp.control;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * SharedPreferences工具类, 可以通过传入实体对象保存其至SharedPreferences中,
 * 并通过实体的类型Class将保存的对象取出. 支持不带泛型的对象以及List集合
 *
 * @author sien
 */
public class CPSharedPreferenceManager {
    /**
     * 缓存数据加密密钥
     */
    public static final String DB_SECRET_KEY = "com.sien.lib.baseapp";
    /**
     * 数据库字段是否加密
     */
    public static boolean isEncryptByAESForData = true;

    private static CPSharedPreferenceManager appSharedPreferences;

    private static final String LIST_TAG = ".LIST";
    private static SharedPreferences sharedPreferences;
    /**
     * 是否需要显示引导页
     */
    public static final String SETTINGS_SHOW_GUIDE = "SETTINGS_SHOW_GUIDE";

    /**
     * 是否需要创建初始数据
     */
    public static final String SETTINGS_DEFAULT_DATA = "SETTINGS_DEFAULT_DATA";

    private CPSharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    /**
     * @param context 请传入ApplicationContext避免内存泄漏
     */
    public static CPSharedPreferenceManager getInstance(Context context) {
        if (null == appSharedPreferences) {
            synchronized (CPSharedPreferenceManager.class) {
                if (appSharedPreferences == null) {
                    appSharedPreferences = new CPSharedPreferenceManager(context.getApplicationContext());
                }
            }
        }
        return appSharedPreferences;
    }

    private static void checkInit() {
        if (sharedPreferences == null) {
            throw new IllegalStateException("Please call init(context) first.");
        }
    }

    /**
     * 保存对象数据至SharedPreferences, key默认为类名, 如
     * <pre>
     * CPSharedPreferencesUtils.saveData(saveUser);
     * </pre>
     *
     * @param data 不带泛型的任意数据类型实例
     */
    public <T> void saveData(T data) throws ParseDataException {
        saveData(data.getClass().getName(), data);
    }

    /**
     * 根据key保存对象数据至SharedPreferences, 如
     * <pre>
     * CPSharedPreferencesUtils.saveData(key, saveUser);
     * </pre>
     *
     * @param data 不带泛型的任意数据类型实例
     */
    public <T> void saveData(String key, T data) throws ParseDataException{
        checkInit();
        if (data == null)
            throw new IllegalStateException("data should not be null.");

        sharedPreferences.edit().putString(key, JsonMananger.beanToJson(data)).apply();
    }

    /**
     * 保存List集合数据至SharedPreferences, 请确保List至少含有一个元素, 如
     * <pre>
     * CPSharedPreferencesUtils.saveData(users);
     * </pre>
     *
     * @param data List类型实例
     */
    public <T> void saveData(List<T> data) throws ParseDataException{
        checkInit();
        if (data == null || data.size() <= 0)
            throw new IllegalStateException(
                    "List should not be null or at least contains one element.");
        Class returnType = data.get(0).getClass();
        sharedPreferences.edit().putString(returnType.getName() + LIST_TAG,
                    JsonMananger.beanToJson(data)).apply();
    }

    /**
     * 将数据从SharedPreferences中取出, key默认为类名, 如
     * <pre>
     * User user = CPSharedPreferencesUtils.getData(key, User.class)
     * </pre>
     */
    public <T> T getData(Class<T> clz)  throws ParseDataException {
        return getData(clz.getName(), clz);
    }

    /**
     * 根据key将数据从SharedPreferences中取出, 如
     * <pre>
     * User user = CPSharedPreferencesUtils.getData(User.class)
     * </pre>
     */
    public <T> T getData(String key, Class<T> clz) throws ParseDataException{
        checkInit();
        String json = sharedPreferences.getString(key, "");
        return JsonMananger.jsonToBean(json, clz);
    }

    /**
     * 将数据从SharedPreferences中取出, 如
     * <pre>List<User> users = CPSharedPreferencesUtils.getData(List.class, User.class)</pre>
     */
    public <T> List<T> getData(Class<List> clz, Class<T> gClz)  throws ParseDataException{
        checkInit();
        String json = sharedPreferences.getString(gClz.getName() + LIST_TAG, "");
        return JsonMananger.jsonToList(json,gClz);
    }

    /**
     * 简易字符串保存, 仅支持字符串
     */
    public void saveData(String key, String data) {
        sharedPreferences.edit().putString(key, data).apply();
    }

    /**
     * 简易字符串获取, 仅支持字符串
     */
    public String getData(String key) {
        return sharedPreferences.getString(key, "");
    }

    /**
     * 删除保存的对象
     */
    public void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * 删除保存的对象
     */
    public void remove(Class clz) {
        remove(clz.getName());
    }

    /**
     * 清理数据
     */
    public void clean(){
        sharedPreferences.edit().clear().commit();
    }
    /**
     * 删除保存的数组
     */
    public void removeList(Class clz) {
        sharedPreferences.edit().remove(clz.getName() + LIST_TAG).apply();
    }

}
