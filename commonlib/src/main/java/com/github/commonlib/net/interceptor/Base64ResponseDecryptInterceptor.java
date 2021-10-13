package com.github.commonlib.net.interceptor;

import com.github.commonlib.aes.AESUtil2;
import com.github.commonlib.aes.Base64Util;
import com.github.commonlib.net.INetworkRequiredInfo;
import com.github.commonlib.net.RetrofitServiceManager;
import com.github.commonlib.util.DLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class Base64ResponseDecryptInterceptor implements Interceptor {
    private String aesKey = "";

    public Base64ResponseDecryptInterceptor(INetworkRequiredInfo iNetworkRequiredInfo){
        if (iNetworkRequiredInfo != null) {
            aesKey = iNetworkRequiredInfo.getAesKey();
        }
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                BufferedSource source = body.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();
                MediaType mediaType = body.contentType();
                Charset charset = null;
                if (mediaType != null) {
                    charset = mediaType.charset();
                }
                if (charset == null)
                    charset = Charset.forName("UTF-8");
                String bodyString = buffer.clone().readString(charset);
                try {
                    DLog.e("整体解密前的数据：", bodyString);
                    String decrypt = AESUtil2.decrypt(bodyString, Base64Util.decodeBase64(aesKey));
                    DLog.e("整体解密的数据：", decrypt);
                    JSONObject jsonObject = new JSONObject(decrypt);
                    String s = jsonObject.getString("status");
                    if (s.equals("301")) {
                        RetrofitServiceManager.getInstance().getBusinessCallBack().gotoLogin();
                    } else if (s.equals("304")) {
                        RetrofitServiceManager.getInstance().getBusinessCallBack().updateApk();
                    }
                    List<String> segmentList = request.url().encodedPathSegments();
                    jsonObject.put("requestUrl", slashSegments(segmentList));
                    ResponseBody newResponseBody = ResponseBody.create(mediaType, jsonObject.toString());

                    DLog.i("Response", jsonObject.toString());
                    response = response.newBuilder().body(newResponseBody).build();
                } catch (JSONException e) {
                    e.printStackTrace();
                    return response;
                }
            }
        }
        return response;
    }

    String slashSegments(List<String> segments) {
        StringBuilder segmentString = new StringBuilder();
        for (String segment : segments) {
            segmentString.append("/").append(segment);
        }
        return segmentString.toString();
    }
}
