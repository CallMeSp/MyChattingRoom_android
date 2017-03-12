package com.sp.chattingroom.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017/3/12.
 */

public  class KeepAliveReceiver extends BroadcastReceiver {
    private static final String TAG = "KeepAliveReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.log(TAG, "onReceive");
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            LogUtil.log(TAG, "off");
        } else if (action.equals(Intent.ACTION_SCREEN_ON)) {
            LogUtil.log(TAG, "on");
        }else if (action.equals(Intent.ACTION_USER_PRESENT)){
            LogUtil.log(TAG,"userpresent");
        }
    }
}
