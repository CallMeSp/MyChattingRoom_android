package com.sp.chattingroom.base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.sp.chattingroom.Model.LogUtil;
import com.sp.chattingroom.Model.Msg;
import com.sp.chattingroom.Service.ChatService;
import com.sp.chattingroom.Service.I_onMessageGet;

/**
 * Created by Administrator on 2017/2/22.
 */

public class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";
    public ChatService.MyBinder myBinder;
    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.log(TAG,"onServiceConnected");
            myBinder=(ChatService.MyBinder)service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.log(TAG,"onServiceDisconnected");
        }
    };
    @Override
    protected void onCreate(Bundle s){
        Intent startservice=new Intent(this,ChatService.class);
        bindService(startservice,serviceConnection,BIND_AUTO_CREATE);
        super.onCreate(s);
    }
}
