package com.phearom.api.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.phearom.api.receiver.ConnectivityReceiver;
import com.phearom.api.utils.BK;
import com.phearom.api.utils.IntentKey;
import com.phearom.api.utils.NetUtils;

/**
 * Created by phearom on 6/1/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements ConnectivityReceiver.OnNetworkListener {

    private ConnectivityReceiver receiver;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new ConnectivityReceiver();
        receiver.addOnNetworkListener(this);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(IntentKey.ACTION_CONNECTION_CHANGED);
    }

    @Override
    protected void onStart() {
        registerReceiver(receiver, mIntentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(receiver);
        super.onStop();
    }
}
