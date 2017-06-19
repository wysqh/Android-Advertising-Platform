package com.example.gtk.platform.utils;

import android.util.Log;
import android.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by gutia on 2017-05-30.
 */

public class HttpUtils {
    public final static String IP_ADDRESS = "http://10.0.0.22:8080";
    public final static String BASE_URL = IP_ADDRESS;
    public final static String USER_URL = IP_ADDRESS + "/users";
    public final static String PRODUCT_URL = IP_ADDRESS + "/product";

    public static InputStream post(String path, Map<String, String> params){
        URL url = null;
        StringBuffer stringBuffer = getRequestData(params, "utf-8");
        byte[] data = null;
        if (stringBuffer != null) {
            data = stringBuffer.toString().getBytes();
        }
        try {
            url = new URL(path);
            Log.i("Http Utils", path);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setConnectTimeout(6000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            // 设置请求的头
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 设置请求的头
            if (data!=null) {
                urlConnection.setRequestProperty("Content-Length",
                        String.valueOf(data.length));
            }
            // 设置请求的头
            urlConnection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
            if (data != null) {
                OutputStream out = urlConnection.getOutputStream();
                out.write(data);
            }
            urlConnection.connect();

            int response = urlConnection.getResponseCode();
            Log.i("Http Utils", "response code:" + response);
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = urlConnection.getInputStream();
                return inputStream;
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.i("Http Utils", e.toString());
        }catch (IOException e){
            e.printStackTrace();
            Log.i("Http Utils", e.toString());
        }
        return null;
    }

    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        if (params == null) {
            return null;
        }

        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()){
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length()- 1);
        }catch (IOException e){
            e.printStackTrace();
        }
        Log.i("Http Utils", stringBuffer.toString());
        return stringBuffer;
    }

    public static String inputStream2String(InputStream inputStream){
        String resultData = null;
        ByteArrayOutputStream byteArrayOutputStream =
                new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }
}
