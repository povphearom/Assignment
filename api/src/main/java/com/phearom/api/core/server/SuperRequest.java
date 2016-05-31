package com.phearom.api.core.server;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.phearom.api.utils.ServerConfig;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by phearom on 6/1/16.
 */
public abstract class SuperRequest<T> {
    protected final static String NO_KEY = "no_key";
    private ResponseCallback<T> mResponseCallback;
    private RequestQueue mQueue;
    private Context mContext;
    private Gson gson;

    public SuperRequest(Context context) {
        this.mContext = context;
    }

    protected abstract String getFunction();

    protected abstract void getRequestParams(Map<String, String> params);

    protected abstract T getDataResponse(String response);

    protected Gson getGson() {
        if (null == gson)
            gson = new Gson();
        return gson;
    }

    private RequestQueue getQueue() {
        if (null == mQueue)
            mQueue = Volley.newRequestQueue(mContext);
        return mQueue;
    }

    public void setOnResponseCallback(ResponseCallback<T> responseCallback) {
        this.mResponseCallback = responseCallback;
    }

    private String getServerUrl() {
        Map<String, String> params = new HashMap<>();
        String paramStr = null;
        getRequestParams(params);
        if (getMethod() == Request.Method.GET && params.size() > 0) {
            if (params.containsKey(NO_KEY)) {
                paramStr = params.get(NO_KEY);
                return ServerConfig.SERVER + getFunction() + "/" + paramStr;
            } else {
                paramStr = params.toString().replace("[", "?").replace("]", "");
                return ServerConfig.SERVER + getFunction() + paramStr;
            }
        }
        return ServerConfig.SERVER + getFunction();
    }

    protected int getMethod() {
        return Request.Method.GET;
    }

    public void execute() {
        Log.i("URL", getServerUrl());

        StringRequest request = new StringRequest(getMethod(), getServerUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (null != mResponseCallback)
                    mResponseCallback.onSuccess(getDataResponse(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != mResponseCallback)
                    mResponseCallback.onError(error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                String creds = String.format("%s:%s", ServerConfig.USER, ServerConfig.PASS);
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };
        getQueue().add(request);
    }
}
