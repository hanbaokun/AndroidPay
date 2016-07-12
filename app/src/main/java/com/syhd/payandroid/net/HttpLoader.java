package com.syhd.payandroid.net;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装volley的post&get请求
 */
public class HttpLoader {

    public HttpLoader(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    private static HttpLoader httpLoader = null;
    private RequestQueue mQueue;

    public synchronized static HttpLoader getInstance(Context context) {
        if (httpLoader == null) {
            httpLoader = new HttpLoader(context);
        }
        return httpLoader;
    }

    /**
     * 封装volley的post&get请求
     * @param url
     * @param map
     * @param mStringCallBack
     */
    public void postRequest(String url, final Map<String, String> map, final StringCallback mStringCallBack) {
        StringRequest stringRequest = new StringRequest(Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mStringCallBack.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mStringCallBack.onError(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    /**
     * 封装volley的post&get请求
     * @param url
     * @param mStringCallBack
     */
    public void getRequest(String url, final StringCallback mStringCallBack) {
        StringRequest stringRequest = new StringRequest(Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mStringCallBack.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mStringCallBack.onError(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return new HashMap<>();
            }
        };
        mQueue.add(stringRequest);
    }
}
