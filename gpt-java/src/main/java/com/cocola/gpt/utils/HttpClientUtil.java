package com.cocola.gpt.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;


/**
 * @Author: yangshiyuan
 * @Date: 2022/8/22
 * @Description: httpClient工具类
 */
@Slf4j
public class HttpClientUtil {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientUtil.class);

    private static CloseableHttpClient httpClient = null;
    private final static Charset CHARSET = Charset.forName("UTF-8");
    private final static Integer TIMEOUT = 60000;
    private final static Map<String, Object> POSTHEADER = new HashMap<>();
    // http请求线程池，最大连接数
    private static final Integer REQUESTMAXNUM = 500;

    static {
        POSTHEADER.put("Content-type", "application/x-www-form-urlencoded");
        httpClient = getHttpClient();
    }

    private HttpClientUtil() {
    }


    /**
     * 统一返回结果
     *
     * @param response
     * @return
     * @throws IOException
     */
    private static HttpCommonResposne getSuccessString(HttpResponse response)
            throws IOException {
        return new HttpCommonResposne()
                .setCode(response.getStatusLine().getStatusCode())
                .setSuccess(true)
                .setBody(EntityUtils.toString(response.getEntity(), CHARSET));
    }

    /**
     * POST
     *
     * @param url
     * @param json
     * @param header
     * @return
     */
    public static HttpCommonResposne postJsonForString(String url,
                                                       String json,
                                                       @Nullable Map<String, Object> header) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(getRequestConfig());
        httpPost.setEntity(getStringEntity(json));
        setHeader(header, httpPost);
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            log.info("HttpResponse is, response={}", response);
            if (HttpStatus.SC_CREATED == response.getStatusLine().getStatusCode()
                    || HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return getSuccessString(response);
            } else {
                return new HttpCommonResposne()
                        .setCode(response.getStatusLine().getStatusCode())
                        .setSuccess(false)
                        .setBody(EntityUtils.toString(response.getEntity(), CHARSET));
            }
        } catch (IOException e) {
            log.info("http post request fail: url is {},body is {}.", url, json);
        }
        return new HttpCommonResposne().setSuccess(false);
    }

    /**
     * POST
     *
     * @param url
     * @param formMap
     * @return
     */
    public static HttpCommonResposne postMapForString(String url,
                                                      Map<String, Object> formMap) {
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setConfig(getRequestConfig());
            setNameValuePari(formMap, httpPost);
            setHeader(POSTHEADER, httpPost);
            HttpResponse response = httpClient.execute(httpPost);
            log.info("HttpResponse is, response={}", response);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return getSuccessString(response);
            } else {
                return new HttpCommonResposne()
                        .setCode(response.getStatusLine().getStatusCode())
                        .setSuccess(false)
                        .setBody(EntityUtils.toString(response.getEntity(), CHARSET));
            }
        } catch (IOException e) {
            log.info("http post request fail: url is {}, body is {}.", url, formMap);
        }
        return new HttpCommonResposne().setSuccess(false);
    }


    /**
     * PUT
     *
     * @param url
     * @param json
     * @param header
     * @return
     */
    public static HttpCommonResposne putJsonForString(String url,
                                                      String json,
                                                      @Nullable Map<String, Object> header) {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(getRequestConfig());
        httpPut.setEntity(getStringEntity(json));
        setHeader(header, httpPut);
        HttpResponse response;
        try {
            response = httpClient.execute(httpPut);
            log.info("HttpResponse is, response={}", response);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return getSuccessString(response);
            } else {
                return new HttpCommonResposne()
                        .setCode(response.getStatusLine().getStatusCode())
                        .setSuccess(false)
                        .setBody(EntityUtils.toString(response.getEntity(), CHARSET));
            }
        } catch (IOException e) {
            log.info("http post request fail: url is {},body is {}.", url, json);
        }
        return new HttpCommonResposne().setSuccess(false);
    }


    /**
     * GET
     *
     * @param url
     * @param param
     * @return
     */
    public static HttpCommonResposne get(String url,
                                         Map<String, Object> param,
                                         @Nullable Map<String, Object> header) {
        String fullUrl = getQueryString(url, param).toString();
        log.info("url is {}", fullUrl);
        HttpGet httpGet = new HttpGet(fullUrl);
        httpGet.setConfig(getRequestConfig());
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            log.info("HttpResponse is, response={}", response);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return getSuccessString(response);
            } else {
                return new HttpCommonResposne()
                        .setCode(response.getStatusLine().getStatusCode())
                        .setSuccess(false)
                        .setBody(EntityUtils.toString(response.getEntity(), CHARSET));
            }
        } catch (IOException e) {
            log.info("http get request fail: url is {}, body is {}.", url, param);
        }
        return new HttpCommonResposne().setSuccess(false);
    }

    /**
     * 根据map获取get请求参数
     *
     * @param url
     * @param map
     * @return
     */
    public static StringBuilder getQueryString(String url, Map<String, Object> map) {
        StringBuilder sb = new StringBuilder(url);
        if (map != null && map.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }
        return sb;
    }

    /**
     * 根据参数转换为map
     *
     * @param url
     * @return
     */
    public static MultiValueMap<String, String> getUrlParams(String url) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(0);
        if (StringUtils.isEmpty(url)) {
            return map;
        }
        String[] params = url.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.add(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * GET
     *
     * @param url
     * @return
     */
    public static HttpCommonResposne get(String url,
                                         @Nullable Map<String, Object> header) {
        try {
            HttpGet httpGet = getHttpGet(url);
            setHeader(header, httpGet);
            if (null == httpGet) {
                return new HttpCommonResposne().setSuccess(false);
            }
            log.info("HttpRequest is, request={}, header={}", httpGet, httpGet.getAllHeaders());
            HttpResponse response = httpClient.execute(httpGet);
            log.info("HttpResponse is, response={}", response);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return getSuccessString(response);
            } else {
                return new HttpCommonResposne()
                        .setCode(response.getStatusLine().getStatusCode())
                        .setSuccess(false)
                        .setBody(EntityUtils.toString(response.getEntity(), CHARSET));
            }
        } catch (IOException e) {
            LOG.info("http get request fail: url is {}", url);
        }
        return new HttpCommonResposne().setSuccess(false);
    }

    private static HttpGet getHttpGet(String url) {
        HttpGet httpGet = null;
        try {
            url = URLDecoder.decode(url, "UTF-8");
            httpGet = new HttpGet(url);
            Optional.ofNullable(httpGet).ifPresent(e -> {
                e.setConfig(getRequestConfig());
            });
        } catch (Exception e) {
            LOG.error("http get request fail:", e);
        }
        return httpGet;
    }


    private static void setHeader(Map<String, Object> headParamsMap, HttpRequestBase httpRequestBase) {
        if (!CollectionUtils.isEmpty(headParamsMap)) {
            Set<Map.Entry<String, Object>> entrySet = headParamsMap.entrySet();
            for (Map.Entry<String, Object> entry : entrySet) {
                httpRequestBase.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    private static void setNameValuePari(Map<String, Object> formMap, HttpPost httpPost) {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        Iterator<Map.Entry<String, Object>> iterator = formMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> elem = iterator.next();
            nameValuePairs.add(new BasicNameValuePair(elem.getKey(), (String) elem.getValue()));
        }

        if (nameValuePairs.size() > 0) {
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(nameValuePairs, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            httpPost.setEntity(entity);
        }
    }

    public static HttpCommonResposne getForImage(String url,
                                                 HttpServletResponse servletResponse,
                                                 Map<String, Object> header) {
        HttpGet httpGet = new HttpGet(url);
        setHeader(header, httpGet);
        httpGet.setConfig(getRequestConfig());
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpGet);
            LOG.info("HttpResponse is, response={}", httpResponse);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                writeImageToResponse(httpResponse, servletResponse);
                return new HttpCommonResposne().setSuccess(true);
            }
        } catch (IOException e) {
            LOG.info("http get image fail: url is {}.", url);
        }
        return new HttpCommonResposne().setSuccess(false);
    }

    private static void writeImageToResponse(HttpResponse httpResponse, HttpServletResponse servletResponse) {
        try (OutputStream outputStream = servletResponse.getOutputStream()) {
            outputStream.write(EntityUtils.toByteArray(httpResponse.getEntity()));
        } catch (Exception e) {
            LOG.info("http down image fail");
        }
    }

    private static StringEntity getStringEntity(String json) {
        StringEntity stringEntity = new StringEntity(json, "UTF-8");
        stringEntity.setContentType("application/json");
        return stringEntity;
    }

    private static RequestConfig getRequestConfig() {
        return RequestConfig
                .custom()
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build();
    }

//    /**
//     * 配置httpClient
//     *
//     * @return
//     */
//    private static CloseableHttpClient getHttpClient() {
//        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
//                .register("http", PlainConnectionSocketFactory.getSocketFactory())
//                .register("https", SSLConnectionSocketFactory.getSocketFactory())
//                .build();
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
//        //设置整个连接池最大连接数 根据自己的场景决定
//        connectionManager.setMaxTotal(REQUESTMAXNUM);
//        //路由是对maxTotal的细分
//        connectionManager.setDefaultMaxPerRoute(REQUESTMAXNUM);
//        RequestConfig requestConfig = RequestConfig.custom()
//                //服务器返回数据(response)的时间，超过该时间抛出read timeout
//                .setSocketTimeout(5000 * 4)
//                //连接上服务器(握手成功)的时间，超出该时间抛出connect timeout
//                .setConnectTimeout(5000)
//                //从连接池中获取连接的超时时间，超过该时间未拿到可用连接，
//                // 会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
//                .setConnectionRequestTimeout(1000)
//                .build();
//        return HttpClientBuilder.create()
//                .setDefaultRequestConfig(requestConfig)
//                .setConnectionManager(connectionManager)
//                .build();
//    }

    private static CloseableHttpClient getHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        //指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        ConnectionConfig connConfig = ConnectionConfig.custom().setCharset(Charset.forName("utf-8")).build();
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(5000).build();
        connManager.setDefaultConnectionConfig(connConfig);
        connManager.setDefaultSocketConfig(socketConfig);
        // 连接池最大生成连接数
        connManager.setMaxTotal(500);
        // 默认设置route最大连接数
        connManager.setDefaultMaxPerRoute(500);
        //构建客户端
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    @Data
    @Accessors(chain = true)
    public static class HttpCommonResposne {

        /**
         *
         */
        private Integer code;

        /**
         *  处理结果
         */
        private Boolean success;

        /**
         *  返回值
         */
        private String body;
    }

}
