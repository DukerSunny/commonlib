/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.github.commonlib.net;

import com.github.commonlib.net.gson_adapter.DoubleDefaultAdapter;
import com.github.commonlib.net.gson_adapter.IntegerDefaultAdapter;
import com.github.commonlib.net.gson_adapter.LongDefaultAdapter;
import com.github.commonlib.net.gson_adapter.StringNullAdapter;
import com.github.commonlib.net.interceptor.Base64RequestEncryptInterceptor;
import com.github.commonlib.net.interceptor.Base64ResponseDecryptInterceptor;
import com.github.commonlib.net.interceptor.ParametersInterceptor;
import com.github.commonlib.util.DLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 实例的管理类
 *
 * @author duwenbin
 * @since 2020-06-20
 */
public class RetrofitServiceManager {
    private static final String TAG = "RetrofitServiceManager";

    private static final int CONNECT_TIME_OUT = 30;

    private static final int READ_TIME_OUT = 60;

    private NetBusinessCallBack mCallBack;

    private INetworkRequiredInfo networkRequiredInfo;

    private OkHttpClient okHttpClient;

    private volatile static RetrofitServiceManager instance = null;

    private Retrofit mRetrofit;

    private Gson gson;

    private String mBaseUrl;

    private boolean isInit = false;

    private RetrofitServiceManager() {
    }

    /**
     * 获取Retrofit单例对象
     * @return 单例对象
     */
    public static RetrofitServiceManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitServiceManager.class) {
                if (instance == null) {
                    instance = new RetrofitServiceManager();
                }
            }
        }
        return instance;
    }

    public synchronized void init(String baseUrl, INetworkRequiredInfo requiredInfo, NetBusinessCallBack mCallBack) {
        isInit = true;
        this.mBaseUrl = baseUrl;
        this.mCallBack = mCallBack;
        this.networkRequiredInfo = requiredInfo;
        gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                .registerTypeAdapter(long.class, new LongDefaultAdapter())
                .registerTypeAdapter(String.class, new StringNullAdapter())
                .create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    DLog.e(TAG, URLDecoder.decode(message, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    DLog.e(TAG, message);
                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        SSLParamUtils.SSLParams sslParams = SSLParamUtils.getSslSocketFactory();
        // 初始化OkHttpClient对象，并配置相关的属性
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new ParametersInterceptor(networkRequiredInfo))
                .addInterceptor(new Base64RequestEncryptInterceptor(networkRequiredInfo))
                .addInterceptor(new Base64ResponseDecryptInterceptor(networkRequiredInfo))
                .addInterceptor(loggingInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        if (!isInit) {
            throw new RuntimeException("RetrofitServiceManager is not initialized...");
        }
        return mRetrofit;
    }

    public OkHttpClient getOkHttpClient() {
        if (!isInit) {
            throw new RuntimeException("RetrofitServiceManager is not initialized...");
        }
        return okHttpClient;
    }

    public NetBusinessCallBack getBusinessCallBack() {
        return mCallBack;
    }
}
