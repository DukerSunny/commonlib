package com.github.commonlib.net;

import com.github.commonlib.util.DLog;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * 创建Base抽象类实现Observer
 *
 * @author duwenbin
 * @param <T> 泛型T
 * @since 2020-06-20
 */
public abstract class BaseObserver<T> extends DisposableObserver<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";

    @Override
    public void onNext(BaseResponse<T> response) {
        onSuccess(response.getBody());
    }

    @Override
    public void onError(Throwable throwable) {
        DLog.e(TAG, "response data error! " + throwable.toString());
        if (throwable instanceof HttpException) {
            // HTTP错误
            onFailure(throwable, ExceptionReason.BAD_NETWORK);
        } else if (throwable instanceof ServerException) {
            // 服务器返回错误
            onFailure(throwable, ExceptionReason.ERROR_CODE);
        } else if (throwable instanceof ConnectException
                || throwable instanceof UnknownHostException) {
            // 连接错误
            onFailure(throwable, ExceptionReason.CONNECT_ERROR);
        } else if (throwable instanceof InterruptedIOException) {
            // 连接超时
            onFailure(throwable, ExceptionReason.CONNECT_TIMEOUT);
        } else if (throwable instanceof JsonParseException
                || throwable instanceof JSONException
                || throwable instanceof ParseException) {
            // 解析错误
            onFailure(throwable, ExceptionReason.PARSE_ERROR);
        } else {
            onFailure(throwable, ExceptionReason.UNKNOWN_ERROR);
        }
    }

    @Override
    public void onComplete() {
    }

    /**
     * 请求数据成功
     *
     * @param data 数据
     */
    public abstract void onSuccess(T data);

    /**
     * 请求数据失败
     * @param throwable 异常
     * @param errorMsg 异常信息
     */
    public abstract void onFailure(Throwable throwable,ExceptionReason errorMsg);

    /**
     * 请求网络失败原因
     *
     * @author duwenbin duwenbin3@huawei.com
     * @since 2020-06-20
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
        /**
         * 错误code码
         */
        ERROR_CODE,
    }
}
