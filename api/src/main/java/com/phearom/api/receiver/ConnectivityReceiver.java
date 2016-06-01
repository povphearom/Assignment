package com.phearom.api.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.phearom.api.utils.BK;
import com.phearom.api.utils.IntentKey;
import com.phearom.api.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

public class ConnectivityReceiver extends BroadcastReceiver {
    private List<OnNetworkListener> listeners = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(IntentKey.ACTION_CONNECTION_CHANGED)) ;
        for (OnNetworkListener listener : listeners) {
            listener.onConnectionChange(NetUtils.init(context).isNetworkConnected());
        }
    }

    public void addOnNetworkListener(OnNetworkListener listener) {
        listeners.add(listener);
    }

    public interface OnNetworkListener {
        void onConnectionChange(boolean connected);
    }
}