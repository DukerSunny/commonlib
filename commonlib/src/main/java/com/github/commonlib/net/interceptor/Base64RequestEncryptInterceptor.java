package com.github.commonlib.net.interceptor;

import android.text.TextUtils;

import com.github.commonlib.aes.AESUtil2;
import com.github.commonlib.aes.Base64Util;
import com.github.commonlib.net.INetworkRequiredInfo;
import com.github.commonlib.util.DLog;
import com.github.commonlib.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import androidx.collection.ArrayMap;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class Base64RequestEncryptInterceptor implements Interceptor {
    private String aesKey = "";

    private final Charset charset;
    /**
     * 加密请求体时候，是否启动编码
     */
    public static final String ContentEncodedHeaderName = "Content-Encoded";

    /**
     * 网络请求信息
     */
    private INetworkRequiredInfo iNetworkRequiredInfo;

    public Base64RequestEncryptInterceptor(INetworkRequiredInfo iNetworkRequiredInfo){
        this.iNetworkRequiredInfo = iNetworkRequiredInfo;
        charset = Charset.forName("UTF-8");
        if (iNetworkRequiredInfo != null) {
            aesKey = iNetworkRequiredInfo.getAesKey();
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url();

        DLog.e("URL :", url.url().toString());
        String method = request.method().toLowerCase().trim();
        if (method.equals("get")) {
            int querySize = url.querySize();
            String encrypt = null;
            ArrayMap<String, String> map = new ArrayMap<>();
            for (int i = 0; i < querySize; i++) {
                map.put(url.queryParameterName(i), url.queryParameterValue(i));
                DLog.e("get 参数:", url.queryParameterName(i) + ":" + url.queryParameterValue(i));
            }
            if (!map.isEmpty())
                encrypt = AESUtil2.encrypt(GsonUtil.GsonString(map), Base64Util.decodeBase64(aesKey));
            if (!TextUtils.isEmpty(encrypt)) {
                HttpUrl.Builder builder = url.newBuilder();
                for (int i = 0; i < querySize; i++) {
                    builder.removeAllQueryParameters(url.queryParameterName(i));
                }
                HttpUrl httpUrl = builder.addQueryParameter("encryptData", encrypt).build();
                request = request.newBuilder().url(httpUrl).build();
            }
            map.clear();
        } else if (method.equals("post")) {
            RequestBody body = request.body();
            String encrypt = null;
            if (body != null)
                if (body instanceof FormBody) {
                    FormBody formBody = (FormBody) body;
                    int size = formBody.size();
                    if (size > 0) {
                        ArrayMap<String, String> map = new ArrayMap<>();
                        for (int i = 0; i < size; i++) {
                            map.put(formBody.name(i), formBody.value(i));
                            DLog.e("post 参数:", formBody.name(i) + ":" + formBody.value(i));
                        }
                        encrypt = AESUtil2.encrypt(GsonUtil.GsonString(map), Base64Util.decodeBase64(aesKey));
                        map.clear();
                    }
                } else {
                    Buffer buffer = new Buffer();
                    body.writeTo(buffer);
                    String decode = URLDecoder.decode(buffer.readString(charset).trim(), "utf-8");
                    try {
                        if (!TextUtils.isEmpty(decode) && decode.startsWith("{")) {
                            JSONObject jsonObject = new JSONObject(decode);
                            encrypt = AESUtil2.encrypt(jsonObject.toString(), Base64Util.decodeBase64(aesKey));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            if (encrypt != null) {
                encrypt = encrypt.replaceAll("\r|\n*", "");
                String header = request.header(ContentEncodedHeaderName);
                boolean value = Boolean.parseBoolean(header);
                FormBody formBody = new FormBody.Builder().add("encryptData", encrypt).build();
                Request.Builder post = request.newBuilder().post(formBody);
                if (value) {
                    post.addHeader("Content-Type", "application/x-www-form-urlencoded MIME");
                }
                request = post.build();
            }

        }
        DLog.e("请求:", request.url().toString());
        return chain.proceed(request);
    }
}
