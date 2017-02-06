package com.sien.lib.datapp.network.base;

/**
 * @author sien
 * @date 2016/11/18
 * @descript 请求刷新状态
 */
public enum RequestFreshStatus {
    REFRESH_SUCCESS(1),REFRESH_NODATA(2),REFRESH_ERROR(3);

    //1 刷新成功;2 无数据;3 刷新失败
    private int status;

    private RequestFreshStatus(int status){
        this.status = status;
    }
}
