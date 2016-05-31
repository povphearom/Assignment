package com.phearom.assignment.request;

import android.content.Context;
import android.text.TextUtils;

import com.phearom.api.core.server.SuperRequest;
import com.phearom.assignment.model.Book;

import java.util.List;
import java.util.Map;

/**
 * Created by phearom on 6/1/16.
 */
public class RequestGetBookDetails extends SuperRequest<Book> {
    private String id;

    public RequestGetBookDetails(Context context) {
        super(context);
    }

    @Override
    protected String getFunction() {
        return "items";
    }

    @Override
    protected void getRequestParams(Map<String, String> params) {
        if (!TextUtils.isEmpty(getId()))
            params.put(NO_KEY, getId());
    }

    @Override
    protected Book getDataResponse(String response) {
        return getGson().fromJson(response, Book.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
