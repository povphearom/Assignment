package com.phearom.assignment.request;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.phearom.api.core.server.SuperRequest;
import com.phearom.assignment.model.Book;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by phearom on 6/1/16.
 */
public class RequestGetBook extends SuperRequest<List<Book>> {

    public RequestGetBook(Context context) {
        super(context);
    }

    @Override
    protected String getFunction() {
        return "items";
    }

    @Override
    protected void getRequestParams(Map<String, String> params) {
    }

    @Override
    protected List<Book> getDataResponse(String response) {
        Type listType = new TypeToken<ArrayList<Book>>() {
        }.getType();
        return getGson().fromJson(response, listType);
    }
}
