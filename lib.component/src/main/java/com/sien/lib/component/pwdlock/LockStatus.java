package com.sien.lib.component.pwdlock;

import com.sien.lib.component.R;

/**
 * @author sien
 * @date 2017/2/23
 * @descript 密码锁状态
 */
public enum LockStatus {
    //默认的状态
    DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
    //密码输入错误
    ERROR(R.string.gesture_error, R.color.red_f4333c),
    //密码输入正确
    CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5),

    //默认的状态，刚开始的时候（初始化状态）
    GEN_DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
    //第一次记录成功
    GEN_CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
    //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
    GEN_LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
    //二次确认错误
    GEN_CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
    //二次确认正确
    GEN_CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

    private LockStatus(int strId, int colorId) {
        this.strId = strId;
        this.colorId = colorId;
    }
    public int strId;
    public int colorId;
}
