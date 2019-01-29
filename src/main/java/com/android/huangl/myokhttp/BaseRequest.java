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


public class BaseRequest extends LibRequest {
    private static final long serialVersionUID = -5026400410563891198L;
    private String url;
    private boolean method = true; //传输方式，默认true---->post,false--->get

    public BaseRequest() {
    }

    public BaseRequest(String url) {
        this.url = url;
    }

    public void setMethod(boolean method) {
        this.method = method;
    }

    @Override
    public String fetchUrl() {
        return url;
    }

    @Override
    public boolean method() {
        return method;
    }
}
