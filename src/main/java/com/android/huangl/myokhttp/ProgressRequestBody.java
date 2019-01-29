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
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class ProgressRequestBody extends RequestBody {
    private String TAG = ProgressRequestBody.class.getSimpleName();
    private File file;
    private int i;
    private ProgressListener listening;
    private final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    public ProgressRequestBody(File file, int i, ProgressListener listening) {
        this.file = file;
        this.i = i;
        this.listening = listening;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MEDIA_TYPE_MARKDOWN;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source;
        try {
            source = Okio.source(file);
            Buffer buf = new Buffer();
            Long remaining = contentLength();
            for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                sink.write(buf, readCount);
                remaining -= readCount;
                String progress = (((contentLength() - remaining) * 100 / contentLength()) + "%");
                if (Utils.debug)
                    Log.d(TAG, file.getName() + ">" + progress);
                if (listening != null)
                    listening.progress(progress, i);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}