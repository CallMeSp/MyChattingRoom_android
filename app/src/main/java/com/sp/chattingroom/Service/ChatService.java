package com.sp.chattingroom.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sp.chattingroom.Model.LogUtil;

import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ChatService extends Service {
    private static final String TAG = "ChatService";
    private MyBinder binder=new MyBinder();
    private Socket socket=null;
    private boolean result=true;
    @Override
    public void onCreate(){
        super.onCreate();
        try {
            Log.e(TAG, "test:start" );
            socket= IO.socket("http://115.159.38.75:4000");
            socket.connect();
        }catch (URISyntaxException e){
            Log.e(TAG, "run: "+"error" );
            e.printStackTrace();
        }
        while (!socket.connected()){}
        Log.e(TAG, "inner oncreate: "+socket.connected());
        Log.e(TAG, "onCreate: " +this.getClass().getName());
    }
    @Override
    public int onStartCommand(Intent i,int flags,int startId){
        Log.e(TAG, "onStartCommand: " );
        return super.onStartCommand(i,flags,startId);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        while (socket.connected()){
            socket.disconnect();
        }
        socket.off();
        Log.e(TAG, "onDestroy: "+socket.connected());
    }
    @Override
    public IBinder onBind(Intent intent){
        return binder;
    }
    public class MyBinder extends Binder{
        public void getMsg(final I_onMessageGet i_onMessageGet){
            socket.on("newMsg", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String newcontent=args[1].toString();
                    i_onMessageGet.newMsg(newcontent);
                }
            });
        }
        public void sendMSg(String msg){
            socket.emit("postMsg",msg);
        }
        public void login(String name){
            socket.emit("login",name);
        }
        public void getLoginResult(final I_loginResult i_loginResult){
            socket.on("nickExisted", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, "nickExisted" );
                    i_loginResult.loginFailed();
                    result=false;
                }
            });
            socket.on("loginSuccess",new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, "loginSuccess:"+Thread.currentThread().getId());
                    i_loginResult.loginSuccess();
                }
            });
        }
    }
}
