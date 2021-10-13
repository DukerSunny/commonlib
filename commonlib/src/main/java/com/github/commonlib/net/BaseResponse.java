package com.github.commonlib.net;

import com.google.gson.annotations.SerializedName;

/**
 * 基础Response
 *
 * @author duwenbin
 * @param <T> 泛型 T
 * @since 2020-06-20
 */
public class BaseResponse<T> {
    /**
     * code码 200
     */
    public static final int RESPONSE_CODE = 200;

    @SerializedName("status")
    private int status; // status
    @SerializedName("body")
    private T body; // 具体的数据结果
    @SerializedName("message")
    private String message; // message 可用来返回接口的说明
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("requestUrl")
    private String requestUrl;


    public BaseResponse(Object data) {
        this.body = (T) data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", body=" + body +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                '}';
    }
}
