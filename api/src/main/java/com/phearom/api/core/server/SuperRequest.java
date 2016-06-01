package com.phearom.api.core.server;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.phearom.api.utils.ServerConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phearom on 6/1/16.
 */
public abstract class SuperRequest<T> {
    protected final static String NO_KEY = "no_key";
    private int count = 10, offset = 0;
    private ResponseCallback<T> mResponseCallback;
    private RequestQueue mQueue;
    private Context mContext;
    private Gson gson;

    public SuperRequest(Context context) {
        this.mContext = context;
    }

    protected abstract String getFunction();

    protected String getRequestParams() {
        return null;
    }

    protected abstract T getDataResponse(String response);

    protected Gson getGson() {
        if (null == gson)
            gson = new GsonBuilder().create();
        return gson;
    }

    protected boolean isPagination() {
        return false;
    }

    private RequestQueue getQueue() {
        if (null == mQueue)
            mQueue = Volley.newRequestQueue(mContext);
        return mQueue;
    }

    public void setOnResponseCallback(ResponseCallback<T> responseCallback) {
        this.mResponseCallback = responseCallback;
    }

    private String pagination() {
        return "offset=" + getOffset() + "&count=" + getCount();
    }

    private String getServerUrl() {
        if (!TextUtils.isEmpty(getRequestParams())) {
            return ServerConfig.SERVER + getFunction() + "/" + getRequestParams();
        } else {
            if (isPagination())
                return ServerConfig.SERVER + getFunction() + "?" + pagination();
            else
                return ServerConfig.SERVER + getFunction();
        }
    }

    private int getMethod() {
        return Request.Method.GET;
    }

    public void execute() {
        Log.i("URL ", getServerUrl());
        StringRequest request = new StringRequest(getMethod(), getServerUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (null != mResponseCallback)
                    mResponseCallback.onSuccess(getDataResponse(response));
                destroy();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mResponseCallback)
                    mResponseCallback.onError(error);
                destroy();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return getHeader();
            }
        };
        getQueue().add(request);
        request = null;
    }

    private Map<String, String> getHeader() {
        HashMap<String, String> params = new HashMap<>();
        String creds = String.format("%s:%s", ServerConfig.USER, ServerConfig.PASS);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }

    public void destroy() {
        mResponseCallback = null;
        mContext = null;
        mQueue = null;
        gson = null;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
