package com.phearom.api.core.listener;

import android.view.View;

public interface ClickHandler<T>
{
    void onClick(T viewModel, View v);
}