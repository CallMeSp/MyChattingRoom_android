package com.sp.chattingroom.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sp.chattingroom.IChatManager;
import com.sp.chattingroom.I_GetLoginResult;
import com.sp.chattingroom.I_NewMessageArrived;
import com.sp.chattingroom.Model.Msg;

import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ChatService extends Service {
    private static final String TAG = "ChatService";
    private Socket socket=null;
    private I_GetLoginResult i_getLoginResult;
    private I_NewMessageArrived i_newMessageArrived;
    private Binder mBinder=new IChatManager.Stub(){
        @Override
        public void SendMsg(Msg msg) throws RemoteException {
            socket.emit("postMsg",msg.getContent());
        }
        @Override
        public void Login(String name, final I_GetLoginResult listener) throws RemoteException {
            i_getLoginResult=listener;
            socket.emit("login",name);
            socket.on("nickExisted", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, "nickExisted" );
                    try {
                        listener.loginFailed();
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                }
            });
            socket.on("loginSuccess",new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.e(TAG, "loginSuccess:"+Thread.currentThread().getId());
                    try {
                        listener.loginSucceed();
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void registerNewMsgListener(I_NewMessageArrived listener) throws RemoteException {
            i_newMessageArrived=listener;
        }
    };
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
        socket.on("newMsg", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String newcontent=args[1].toString();
                try {
                    i_newMessageArrived.newMessageArrive(new Msg(0,newcontent));
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
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
        return mBinder;
    }
}
