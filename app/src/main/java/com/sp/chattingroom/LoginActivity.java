package com.sp.chattingroom;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sp.chattingroom.base.LogUtil;
import com.sp.chattingroom.Service.ChatService;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Administrator on 2017/2/21.
 */
public class LoginActivity extends Activity {
    @BindView(R.id.btn_login)Button button;
    @BindView(R.id.edt_username)EditText editText;
    private static final String TAG = "LoginActivity";
    private IChatManager mRemoteManager;
    private I_GetLoginResult mResultListener=new I_GetLoginResult.Stub(){
        @Override
        public void loginSucceed() throws RemoteException {
            LogUtil.log(TAG,"log suc");
            Intent intent=new Intent(LoginActivity.this,ChatActivity.class);
            startActivity(intent);
            finish();
        }
        @Override
        public void loginFailed() throws RemoteException {
            handler.obtainMessage().sendToTarget();
        }
    };
    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.log(TAG,"onServiceConnected");
            mRemoteManager=IChatManager.Stub.asInterface(service);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteManager=null;
            LogUtil.log(TAG,"onServiceDisconnected");
        }
    };
    @Override
    protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.loginactivity);
        ButterKnife.bind(this);
        Intent service=new Intent(this,ChatService.class);
        bindService(service,serviceConnection,BIND_AUTO_CREATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editText.getText().toString();
                try {
                    mRemoteManager.Login(name, mResultListener);
                }
                catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message message){
            editText.setText("");
            Toast.makeText(LoginActivity.this,"用户名重复",Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onStart(){
        LogUtil.log(TAG,"onStart");
        super.onStart();
    }
    @Override
    protected void onResume(){
        LogUtil.log(TAG,"onResume");
        super.onResume();
    }
    @Override
    protected void onPause(){
        LogUtil.log(TAG,"onPause");
        super.onPause();
    }
    @Override
    protected void onStop(){
        LogUtil.log(TAG,"onStop");
        super.onStop();
    }
    @Override
    protected void onRestart(){
        LogUtil.log(TAG,"onRestart");
        super.onRestart();
    }
    @Override
    protected void onDestroy(){
        LogUtil.log(TAG,"onDestroy");
        super.onDestroy();
        unbindService(serviceConnection);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        LogUtil.log(TAG,"onSaveInstance");
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        LogUtil.log(TAG,"onRestoreInstance");
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onConfigurationChanged(Configuration configuration){
        super.onConfigurationChanged(configuration);
        LogUtil.log(TAG,"onConfigurationChanged:"+configuration.orientation);
    }
}
