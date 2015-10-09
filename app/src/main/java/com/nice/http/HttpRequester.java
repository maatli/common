package com.nice.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequester {
//    private static 
    
    public static final String HEADER_USERANGEN = "useang";
    
    public static byte[] postConnection(String url, byte[] bytes, Map<String, String> headers) throws Exception {
        // 是否应该有网络是否可用的判断
        
        HttpURLConnection conn = buildURLConnection(url, headers);
        
//        conn.set
        
        
        return null;
    }
    
    private static HttpURLConnection buildURLConnection(String urlAddress, Map<String, String> header) throws Exception {
        URL url = new URL(urlAddress);
        String host = url.getHost();
        
        HttpURLConnection connection = null;
        
        if (url.getProtocol().equals("https")) {
            HttpsURLConnection httpcon = (HttpsURLConnection) url.openConnection();
            connection = httpcon;
            
            // 可以在这里设置CA
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        
        boolean customAngent = true;
        // 自定义user-agent
        if (header != null && header.containsKey(HEADER_USERANGEN)) {
            if ("false".equalsIgnoreCase(header.get(HEADER_USERANGEN))) {
                customAngent = false;
            }
        }
        
        if (customAngent) {
            connection.setRequestProperty("User-Agent", getUserAgent());
        }
        
        // TODO: 设置Cookie
        connection.setRequestProperty("cookie", "SESSIONID=" + getCookie());
        
        
        return connection;
    }
    
    private static String getCookie() {
        
        return null;
    }
    
    private static String userAgent = null;
    
    // TODO: 实现UserAgent方法
    private static String getUserAgent() {

        return userAgent;
    }
}
