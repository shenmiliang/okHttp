package com.android.huangl.myokhttp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建 ：lynnhuang@zmodo.com
 * 时间 ：11:00 on 2019/1/29.
 * 描述 ：
 * 修改 ：
 */
public class Utils {
    public static Utils instance;
    public static boolean debug = true;

    public static Utils getInstance() {
        if (instance == null) {
            synchronized (Utils.class) {
                instance = new Utils();
            }
        }
        return instance;
    }

    public void addHeader(OKHttpConnection okHttpConnection, Request.Builder requestBuilder, String key, String value) {
        requestBuilder.addHeader(key, value);
    }

    public void enqueue(OKHttpConnection okHttpConnection, Request.Builder requestBuilder, OkHttpClient mOkHttpClient, boolean method, String url, Map params, String describe, HashMap<String, File[]> files, ProgressListener listening, final CallbackInterface callbackInterface) throws IOException {
        callbackInterface.onStart();
        Request request = okHttpConnection.upload(requestBuilder, method, url, params, describe, files, listening);
        if (request == null) {
            callbackInterface.onFinish();
            return;
        }
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackInterface.onFailure(call, e);
                callbackInterface.onFinish();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackInterface.onSuccess(call, response);
                callbackInterface.onFinish();
            }
        });
    }

    /**
     * Json方式
     *
     * @param url    链接
     * @param params 参数对象
     **/
    public void enqueueJson(OKHttpConnection okHttpConnection, Request.Builder requestBuilder, OkHttpClient mOkHttpClient, String url, String params, String describe, final CallbackInterface callbackInterface) throws IOException {
        callbackInterface.onStart();
        Request request = okHttpConnection.uploadJson(requestBuilder, url, params, describe);
        if (request == null) {
            callbackInterface.onFinish();
            return;
        }
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callbackInterface.onFinish();
                callbackInterface.onFailure(call, e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackInterface.onSuccess(call, response);
                callbackInterface.onFinish();
            }
        });
    }
}
