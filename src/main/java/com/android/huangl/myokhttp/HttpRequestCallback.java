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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpRequestCallback {
    private OKHttpConnection okHttpConnection = OKHttpConnection.getInstance();
    private Request.Builder  requestBuilder   = new Request.Builder();

    public void addHeader(String key, String value) {
        Utils.getInstance().addHeader(okHttpConnection, requestBuilder, key, value);
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
    public void enqueue(OkHttpClient mOkHttpClient, boolean method, String url, Map params, String describe, HashMap<String, File[]> files, ProgressListener listening, final CallbackInterface callbackInterface) throws IOException {
        Utils.getInstance().enqueue(okHttpConnection, requestBuilder, mOkHttpClient, method, url, params, describe, files, listening, callbackInterface);
    }

    /**
     * Json方式
     *
     * @param url    链接
     * @param params 参数对象
     **/
    public void enqueueJson(OkHttpClient mOkHttpClient, String url, String params, String describe, final CallbackInterface callbackInterface) throws IOException {
        Utils.getInstance().enqueueJson(okHttpConnection, requestBuilder, mOkHttpClient, url, params, describe, callbackInterface);
    }

}