package com.github.commonlib.net;

import android.content.Context;

/**
 * App运行信息接口
 * @author duwenbin
 */
public interface INetworkRequiredInfo {
    /**
     * 判断是否为Debug模式
     */
    boolean isDebug();

    /**
     * 获取全局上下文参数
     */
    Context getApplicationContext();

    /**
     * 获取请求参数
     */
    String getRequestParams();

    /**
     * 获取Aes Key
     */
    String getAesKey();

    String getTokenKey();
}
