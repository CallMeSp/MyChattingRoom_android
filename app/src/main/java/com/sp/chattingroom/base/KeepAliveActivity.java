package com.sp.chattingroom.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sp.chattingroom.ScreenObserver;

/**
 * Created by Administrator on 2017/3/12.
 */

public class KeepAliveActivity extends Activity{
    private static final String TAG = "KeepAliveActivity";
    ScreenObserver observer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.log(TAG,"onCreate");
        Window window=getWindow();
        window.setGravity(Gravity.LEFT|Gravity.TOP);
        WindowManager.LayoutParams params=window.getAttributes();
        params.x=0;
        params.y=0;
        params.height=1;
        params.width=1;
        window.setAttributes(params);

        observer=new ScreenObserver(this);
        observer.startObserver(new ScreenObserver.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                LogUtil.log(TAG,"ON!!!!!!!!!!!!!!!!!!!!!!!");

            }
            @Override
            public void onScreenOff() {
                LogUtil.log(TAG,"OFF!!!!!!!!!!!!!!!!!!!!!!!");
            }
            @Override
            public void onUserPresent() {
                LogUtil.log(TAG,"USERPRESENT!!!!!!!!!!!!!!!!!!!!!!!"+this.getClass().getName());
                stopme();
            }
        });

    }

    public static void Start(Context context){
        Intent intent=new Intent(context,KeepAliveActivity.class);
        context.startActivity(intent);
    }
    public void stopme(){
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        observer.shutdownObserver();
    }
}
