package com.sien.lib.datapp.network.download;

/**
 * @author sien
 * @descript 下载进度监听器
 */
public interface ProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
