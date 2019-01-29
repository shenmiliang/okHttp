package com.android.huangl.myokhttp;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class HttpClientUtils {
    private volatile static HttpClientUtils instance;
    private int         TIME_OUT     = 30; //连接时间
    private int         TIME_READ    = 20; //读取时间
    private TimeUnit    timeUnit     = TimeUnit.SECONDS; //时间类型
    private InputStream certificates = null; //证书的inputstream
    private InputStream bksFile      = null; //本地证书的inputstream
    private String      password     = null; //本地证书的密码
    private OkHttpClient mOkHttpClient;


    public static HttpClientUtils getInstance() {
        if (instance == null) {
            synchronized (HttpClientUtils.class) {
                if (instance == null)
                    instance = new HttpClientUtils();

            }
        }
        return instance;
    }

    public void init(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, timeUnit)
                .readTimeout(TIME_READ, timeUnit);
        if (certificates != null) {
            ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
            builder.cookieJar(cookieJar);
            SSLParams sslParams = HttpClientUtils.getInstance().getSslSocketFactory(certificates, bksFile, password);
            if (sslParams != null && sslParams.sSLSocketFactory != null && sslParams.trustManager != null) {
                builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            }
        }
        HttpClientUtils.getInstance().setOkHttpClient(builder.build());
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.mOkHttpClient = okHttpClient;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    /**
     * 设置可访问所有的https网站(null,null,null)
     * 设置具体的证书(证书的inputstream, null, null)
     * 双向认证(证书的inputstream, 本地证书的inputstream, 本地证书的密码)
     *
     * @param certificates
     * @param bksFile
     * @param password
     * @return
     */
    public SSLParams getSslSocketFactory(InputStream certificates, InputStream bksFile, String password) {
        SSLParams sslParams = new SSLParams();
        try {
            TrustManager[]   trustManagers = prepareTrustManager(certificates);
            KeyManager[]     keyManagers   = prepareKeyManager(bksFile, password);
            SSLContext       sslContext    = SSLContext.getInstance("TLS");
            X509TrustManager trustManager  = null;
            if (trustManagers != null) {
                trustManager = new MyTrustManager(chooseTrustManager(trustManagers));
            } else {
                trustManager = new UnSafeTrustManager();
            }
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = trustManager;
            return sslParams;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e) {
            throw new AssertionError(e);
        } catch (KeyStoreException e) {
            throw new AssertionError(e);
        }
    }

    private TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates == null || certificates.length <= 0) return null;
        try {

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore           keyStore           = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e)

                {
                }
            }
            TrustManagerFactory trustManagerFactory = null;

            trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            return trustManagers;
        } catch (NoSuchAlgorithmException e) {
        } catch (CertificateException e) {
        } catch (KeyStoreException e) {
        } catch (Exception e) {
        }
        return null;

    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();

        } catch (KeyStoreException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (UnrecoverableKeyException e) {
        } catch (CertificateException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
        return null;
    }

    private class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }


        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }


        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    }

    public HttpClientUtils setTimeOut(int i) {
        this.TIME_OUT = i;
        return this;
    }

    public HttpClientUtils setTimeRead(int i) {
        this.TIME_READ = i;
        return this;
    }

    public HttpClientUtils setTimeUnit(TimeUnit unit) {
        this.timeUnit = unit;
        return this;
    }

    public HttpClientUtils setISCertificates(InputStream stream) {
        this.certificates = stream;
        return this;
    }

    public HttpClientUtils setISBksFile(InputStream stream) {
        this.bksFile = stream;
        return this;
    }

    public HttpClientUtils setPassWord(String pwd) {
        this.password = pwd;
        return this;
    }

    public HttpClientUtils setDebug(boolean b) {
        Utils.debug = b;
        return this;
    }
}
