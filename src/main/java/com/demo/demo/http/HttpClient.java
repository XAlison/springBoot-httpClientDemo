package com.demo.demo.http;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: httpClient
 * @Author: xiewl
 * @param:
 * @Date: 2018/7/27 9:38
 * @Version 1.0
 */
@Configuration
public class HttpClient {

    /*@Value("100")
    private Integer maxTotal;

    @Value("20")
    private Integer defaultMaxPerRoute;

    @Value("1000")
    private Integer connectTimeout;

    @Value("500")
    private Integer connectionRequestTimeout;

    @Value("10000")
    private Integer socketTimeout;

    @Value("true")
    private boolean staleConnectionCheckEnabled;*/
    @Value("${http.maxTotal}")
    private Integer maxTotal;

    @Value("${http.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${http.connectTimeout}")
    private Integer connectTimeout;

    @Value("${http.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${http.socketTimeout}")
    private Integer socketTimeout;

    @Value("${http.staleConnectionCheckEnabled}")
    private boolean staleConnectionCheckEnabled;


    /**
     * @Description: 连接池管理器, 设置最大连接数、并发连接数
     * @Author: xiewl
     * @param:
     * @Date: 2018/7/27 9:38
     * @Version 1.0
     */
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        httpClientConnectionManager.setMaxTotal(maxTotal);
        // 并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }


    /**
     * @Description: 实例化连接池，设置连接池管理器
     * @Author: xiewl
     * @param:
     * @Date: 2018/7/27 9:37
     * @Version 1.0
     */
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder getHttpClientBuilder(@Qualifier("httpClientConnectionManager") PoolingHttpClientConnectionManager httpClientConnectionManager) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        httpClientBuilder.setConnectionManager(httpClientConnectionManager);

        return httpClientBuilder;
    }

    /**
     * @Description: 注入连接池，用于获取httpClient
     * @Author: xiewl
     * @param:
     * @Date: 2018/7/27 9:37
     * @Version 1.0
     */
    @Bean
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }


    /**
     * @Description: 获取Builder对象，设置builder的连接信息，可设置设置proxy，cookieSpec等属性
     * @Author: xiewl
     * @param:
     * @Date: 2018/7/27 9:36
     * @Version 1.0
     */
    @Bean(name = "builder")
    public RequestConfig.Builder getBuilder() {
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .setStaleConnectionCheckEnabled(staleConnectionCheckEnabled);
    }

    /**
     * @Description: builder构建一个RequestConfig对象
     * @Author: xiewl
     * @param:
     * @Date: 2018/7/27 9:36
     * @Version 1.0
     */
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("builder") RequestConfig.Builder builder) {
        return builder.build();
    }

}
