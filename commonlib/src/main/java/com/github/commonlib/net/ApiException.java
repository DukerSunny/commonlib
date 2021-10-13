package com.github.commonlib.net;

/**
 * User: duwenbin
 * Date: 2016-10-13 15:17  create
 */
public class ApiException extends RuntimeException {
    private int mErrorCode;
    private String mErrorMessage;
    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
        mErrorMessage=errorMessage;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }
}