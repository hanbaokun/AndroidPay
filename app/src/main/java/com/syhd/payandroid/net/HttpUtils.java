package com.syhd.payandroid.net;

import android.content.Context;

import java.util.Map;


/**
 * 使用volley6.0进行网络访问
 * Created by 韩宝坤 on 2016/1/14.
 */
public class HttpUtils {
    /**
     * post请求
     * @param url             地址
     * @param map             参数
     * @param mStringCallBack 回调
     */
    public static void httpPost(Context context,String url, Map<String, String> map, StringCallback mStringCallBack) {
        HttpLoader.getInstance(context).postRequest(url, map, mStringCallBack);
    }

    public static void httpGet(Context context,String url, StringCallback mStringCallBack){
        HttpLoader.getInstance(context).getRequest(url,mStringCallBack);
    }

}
