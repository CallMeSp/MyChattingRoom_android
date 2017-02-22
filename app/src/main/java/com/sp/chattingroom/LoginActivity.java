package com.sp.chattingroom;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sp.chattingroom.Model.LogUtil;
import com.sp.chattingroom.Service.ChatService;
import com.sp.chattingroom.Service.I_loginResult;
import com.sp.chattingroom.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Administrator on 2017/2/21.
 */
public class LoginActivity extends Activity {
    @BindView(R.id.btn_login)Button button;
    @BindView(R.id.edt_username)EditText editText;
    private static final String TAG = "LoginActivity";
    private ChatService.MyBinder myBinder;
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
        super.onCreate(s);
        setContentView(R.layout.loginactivity);
        ButterKnife.bind(this);
        Intent service=new Intent(this,ChatService.class);
        bindService(service,serviceConnection,BIND_AUTO_CREATE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=editText.getText().toString();
                myBinder.login(name);
                myBinder.getLoginResult(new I_loginResult() {
                    @Override
                    public void loginSuccess() {
                        Intent intent=new Intent(LoginActivity.this,ChatActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void loginFailed() {
                        handler.obtainMessage().sendToTarget();

                    }
                });
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
}
