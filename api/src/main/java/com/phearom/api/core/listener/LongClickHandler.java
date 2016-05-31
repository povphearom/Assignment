package com.phearom.api.core.listener;

import android.view.View;

public interface LongClickHandler<T>
{
    void onLongClick(T viewModel, View v);
}