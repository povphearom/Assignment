package com.phearom.api.server;

import com.android.volley.VolleyError;

/**
 * Created by phearom on 6/1/16.
 */
public abstract class ResponseCallback<T> {
    public abstract void onSuccess(T response);

    public abstract void onError(VolleyError error);
}
