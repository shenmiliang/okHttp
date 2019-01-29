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

import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public interface ClientInterface {
    /**
     * @param context
     * @param request   参数
     * @param response  返回数据
     * @param files     文件
     * @param listening 上传监听
     * @param TYPE      回调标识
     * @param isDialog  弹出层
     * @param describe  描述
     * @param mHandler
     */
    void upload(Context context, BaseRequest request, BaseResponse response, HashMap<String, File[]> files, ProgressListener listening, final int TYPE, boolean isDialog, String describe, final Handler mHandler) throws IOException;

    /**
     * @param context
     * @param request  参数
     * @param response 返回数据
     * @param TYPE     回调标识
     * @param isDialog 弹出层
     * @param describe 描述
     * @param mHandler
     */
    void uploadJson(Context context, BaseRequest request, BaseResponse response, final int TYPE, boolean isDialog, String describe, final Handler mHandler) throws IOException;

    /**
     * @param context
     * @param request  参数
     * @param response 返回数据
     * @param TYPE     回调标识
     * @param isDialog 弹出层
     * @param describe 描述
     * @param FileName 文件名字
     * @param mHandler
     */
    void dowload(Context context, BaseRequest request, BaseResponse response, final int TYPE, boolean isDialog, String describe, String FileName, final Handler mHandler) throws IOException;

}