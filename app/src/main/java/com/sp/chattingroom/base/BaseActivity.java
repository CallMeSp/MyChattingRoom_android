package com.sp.chattingroom.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;

import com.sp.chattingroom.ScreenObserver;

/**
 * Created by Administrator on 2017/3/12.
 */

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    ScreenObserver observer;
    private Context context=this;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.log(TAG,"oncreate");
        super.onCreate(savedInstanceState);
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        observer=new ScreenObserver(this);
        observer.startObserver(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                LogUtil.log(TAG,"ON!!!!!!!!!!!!!!!!!!!!!!!");

            }
            @Override
            public void onScreenOff() {
                LogUtil.log(TAG,"OFF!!!!!!!!!!!!!!!!!!!!!!!");
                KeepAliveActivity.Start(context);
            }
            @Override
            public void onUserPresent() {
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        observer.shutdownObserver();
    }

}
