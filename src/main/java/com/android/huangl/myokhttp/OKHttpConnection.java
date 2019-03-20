package com.android.huangl.myokhttp;
//                  _ooOoo_
//                 o8888888o
//                 88" . "88
//                 (| -_- |)
//                 O\  =  /O
//              ____/`---'\____
//            .'  \\|     |//  `.
//           /  \\|||  :  |||//  \
//          /  _||||| -:- |||||-  \
//          |   | \\\  -  /// |   |
//          | \_|  ''\---/''  |   |
//           \  .-\__  `-`  ___/-. /
//         ___`. .'  /--.--\  `. . __
//      ."" '<  `.___\_<|>_/___.'  >'"".
//    | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//    \  \ `-.   \_ __\ /__ _/   .-` /  /
//=====`-.____`-.___\_____/___.-`____.-'======
//                  `=---='
//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//         佛祖保佑       永无BUG

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OKHttpConnection {
    private String TAG = OKHttpConnection.class.getSimpleName();
    private static OKHttpConnection anInterface;
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static OKHttpConnection getInstance() {
        if (anInterface == null) {
            synchronized (OKHttpConnection.class) {
                anInterface = new OKHttpConnection();
            }
        }
        return anInterface;
    }

    /**
     * 表单方式
     *
     * @param url       链接
     * @param method    传输方式 true为post方式，false为get方式
     * @param params    参数对象
     * @param describe  描述
     * @param files     文件数组
     * @param listening 文件上传监听
     **/
    public Request upload(Request.Builder requestBuilder, boolean method, String url, Map params, String describe, HashMap<String, File[]> files, ProgressListener listening) {
        requestBuilder.url(url); //添加URL
        if (method) { //默认POST方法
            requestBuilder.method("POST", AddParams(url, params, describe, files, listening)); //添加参数
        } else { //get方法
            if (Utils.debug)
                Log.d(TAG, describe + " GET: " + url);
            requestBuilder.method("GET", null);
        }
        return requestBuilder.build();
    }

    /**
     * 表单方式
     * 添加参数
     */
    private RequestBody AddParams(String url, Map params, String describe, HashMap<String, File[]> files, ProgressListener listening) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null) {
            StringBuffer b = new StringBuffer();
            b.append(describe).append(" POST: ").append(url).append("?");
            Iterator it = params.entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
                String              key   = (String) entry.getKey();
                String              value = (entry.getValue() != null) ? (String) entry.getValue() : "";
                requestBody.addFormDataPart(key, value); //加参数
                b.append(key).append("=").append(value).append("&");
            }
            if (Utils.debug)
                Log.d(TAG, b.toString());
        }
        if (files != null) {
            Iterator i$ = files.keySet().iterator();
            while (i$.hasNext()) {
                String key    = (String) i$.next();
                File[] mFiles = files.get(key);
                for (int i = 0; i < mFiles.length; i++) {
                    File File = mFiles[i];
                    if (Utils.debug)
                        Log.d("key>", key + ":filename:" + File.getName());
                    requestBody.addFormDataPart(key, File.getName(), new ProgressRequestBody(File, i, listening)); //加文件
                }
            }
        }
        return requestBody.build();
    }

    /**
     * Json方式
     *
     * @param url    链接
     * @param params 参数对象
     **/
    public Request uploadJson(Request.Builder requestBuilder, String url, String params, String describe) {
        if (Utils.debug)
            Log.d(TAG, describe + url + params);
        requestBuilder.url(url); //添加URL
        RequestBody requestBody = RequestBody.create(JSON, params);
        requestBuilder.post(requestBody);
        return requestBuilder.build();
    }
}