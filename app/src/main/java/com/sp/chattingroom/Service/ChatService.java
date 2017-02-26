package com.sp.chattingroom.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ChatService extends Service {
    private static final String TAG = "ChatService";
    private MyBinder binder=new MyBinder();
    private Socket socket=null;
    @Override
    public void onCreate(){
        super.onCreate();
        /*
         *在这里初始化socket链接
         */
        try {
            socket= IO.socket("http://115.159.38.75:3000");
            socket.connect();
        }catch (URISyntaxException e){
            Log.e(TAG, "run: "+"error" );
            e.printStackTrace();
        }
        while (!socket.connected()){}
    }
    @Override
    public int onStartCommand(Intent i,int flags,int startId){
        Log.e(TAG, "onStartCommand: " );
        return super.onStartCommand(i,flags,startId);
    }
    @Override
    public void onDestroy(){
        /*
         *关闭连接，停止监听
         */
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
        /*
         *在获取信息和获取登陆结果用了两个interface来回调结果
         */
    public class MyBinder extends Binder{
        /*
         *用于获取新信息
         */
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
        /*
         *获取登陆结果
         */
        public void getLoginResult(final I_loginResult i_loginResult){
            socket.on("nickExisted", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, "nickExisted" );
                    i_loginResult.loginFailed();
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
