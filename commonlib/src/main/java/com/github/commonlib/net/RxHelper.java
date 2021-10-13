/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2020-2020. All rights reserved.
 */

package com.github.commonlib.net;

import com.github.commonlib.util.DLog;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Rx调度类
 *
 * @author duwenbin
 * @since 2020-06-20
 */
public class RxHelper {
    private RxHelper() {
    }

    /**
     * ObservableTransformer 转换线程
     *
     * @param <T> 泛型T
     * @return 转换后的observable
     */
    public static <T> ObservableTransformer<T, T> observableIoToMain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public @NonNull
            ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> observableIoToIO() {
        return new ObservableTransformer<T, T>() {
            @Override
            public @NonNull
            ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }

    /**
     * ObservableTransformer 转换结果
     *
     * @param <T> 泛型T
     * @return 转换后的observable
     */
    public static <T> ObservableTransformer<BaseResponse<T>, BaseResponse<T>> HttpResultTransformer() {
        return new ObservableTransformer<BaseResponse<T>, BaseResponse<T>>() {
            @Override
            public @NonNull
            ObservableSource<BaseResponse<T>> apply(@NonNull Observable<BaseResponse<T>> upstream) {
                return upstream.map(new HttpResultFuc<>())
                        .onErrorResumeNext(new HttpResponseThrowableFunc<>());
            }
        };
    }

    public static class HttpResultFuc<T> implements Function<BaseResponse<T>, BaseResponse<T>> {
        @Override
        public BaseResponse<T> apply(@NonNull BaseResponse<T> response) throws Exception {
            DLog.i("Rx", response.toString());
            if (response.getStatus() == BaseResponse.RESPONSE_CODE) {
                return response;
            } else {
                throw new ServerException(response.getStatus(), response.getMessage());
            }
        }
    }

    public static class HttpResponseThrowableFunc<T> implements Function<Throwable, Observable<BaseResponse<T>>> {
        @Override
        public Observable<BaseResponse<T>> apply(@NonNull Throwable throwable) throws Exception {
            return Observable.error(throwable);
        }
    }
}
