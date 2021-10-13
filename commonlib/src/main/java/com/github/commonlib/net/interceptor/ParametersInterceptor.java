package com.github.commonlib.net.interceptor;

import com.github.commonlib.aes.AESUtil2;
import com.github.commonlib.net.INetworkRequiredInfo;
import com.github.commonlib.util.DLog;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 公共参数添加
 */
public class ParametersInterceptor implements Interceptor {

    private String tokenKey = "";
    private String requestParams = "";

    public ParametersInterceptor(INetworkRequiredInfo iNetworkRequiredInfo) {
        if (iNetworkRequiredInfo != null) {
            tokenKey = iNetworkRequiredInfo.getTokenKey();
            requestParams = iNetworkRequiredInfo.getRequestParams();
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        DLog.e("token*******:", requestParams);
        Request.Builder builder = request.newBuilder();
        String encrypt = AESUtil2.encrypt(requestParams, tokenKey);
        builder.addHeader("APP-TOKEN", encrypt);
        DLog.e("APP-TOKEN*******:", encrypt);
        return chain.proceed(builder.build());
    }
}