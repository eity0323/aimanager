package com.sien.lib.baseapp.control;

import android.app.Activity;
import android.text.TextUtils;

import java.util.Stack;

/**
 * @author sien
 * @date 2016/9/13
 * @descript 模拟Android Activity栈管理
 */
public class CPActivityManager {
    private static Stack<Activity> activityStack;

    private CPActivityManager() {
    }

    public static CPActivityManager getAppManager() {
        return CPActivityManagerHolder.INSTANCE;
    }

    private static class CPActivityManagerHolder{
        public static CPActivityManager INSTANCE = new CPActivityManager();
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity(堆栈中最后一个压入的)
     */
    public Activity currentActivity() {
        if (activityStack == null)  return null;

        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack != null) {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack !=null){
            if(activity != null) {
                activityStack.remove(activity);
                activity.finish();
            }
        }
        activity = null;
    }

    public void removeActivity(Activity activity) {
        if (activityStack !=null){
            if(activity != null) {
                activityStack.remove(activity);
            }
        }
        activity = null;
    }

    public void finishActivity(String activityName) {
        if (activityStack == null)  return;

        if (!TextUtils.isEmpty(activityName)) {
            if (null != activityStack && activityStack.size() != 0) {
                int size = activityStack.size();
                for (int i = size - 1; i >= 0; i--) {
                    Activity activity = activityStack.get(i);
                    if (activity.getClass().getName().equals(activityName)) {
                        activityStack.remove(activity);
                        activity.finish();
                        activity = null;
                        break;
                    }
                }
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack == null)  return;

        Activity activity = null;
        for (Activity a : activityStack) {
            if (a.getClass().equals(cls)) {
                activity = a;
                break;
            }
        }
        if (activity != null)
            finishActivity(activity);
    }

    /**
     * 获得指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        /*
		 * for (Activity activity : activityStack) { if
		 * (activity.getClass().equals(cls)) { return activity; } }
		 */
        // 应该由栈顶往下去遍历拿最近的activity去比较
        if (activityStack == null)  return null;

        if (null != activityStack && activityStack.size() != 0) {
            int size = activityStack.size();
            for (int i = size - 1; i >= 0; i--) {
                Activity activity = activityStack.get(i);
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**
     * 回退到栈指定页面
     * @param className
     */
    public void finishActivityExcept(String className){
        if (activityStack == null)  return;

        if (null != activityStack && activityStack.size() != 0) {
            int size = activityStack.size();
            for (int i = size - 1; i >= 0; i--) {
                Activity activity = activityStack.get(i);
                if (activity.getClass().getName().equals(className)) {
                    break;
                }else {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null)  return;

        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        finishAllActivity();
        System.exit(0);
    }
}
