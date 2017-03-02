package com.sp.chattingroom;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.Socket;
import com.sp.chattingroom.Adapter.ChatRecyclerAdpter;
import com.sp.chattingroom.Adapter.DBHelper;
import com.sp.chattingroom.base.LogUtil;
import com.sp.chattingroom.Model.Msg;
import com.sp.chattingroom.Service.ChatService;
import com.sp.chattingroom.Service.I_onMessageGet;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";
    @BindView(R.id.text_list)RecyclerView recyclerView;
    @BindView(R.id.send_btn)Button button;
    @BindView(R.id.chat_edit)EditText editText;
    private static String IPAddress="115.159.38.75";
    private static int PORT=4000;
    private Socket socket=null;
    private ChatRecyclerAdpter adpter;
    private ArrayList<Msg> msg_list=new ArrayList<>();
    private DBHelper dbHelper;
    private Cursor cursor;
    private Thread thread;
    private NotificationManager notificationManager;
    private ChatService.MyBinder binder;
    private IChatManager iChatManager;
    private I_NewMessageArrived i_newMessageArrived=new I_NewMessageArrived.Stub(){
        @Override
        public void newMessageArrive(Msg msg) throws RemoteException {
            Log.e(TAG, "newMsg: "+msg.getContent());
            SendNotification(msg.getContent());
            msg_list.add(msg);
            dbHelper.insert(msg);
            cursor.requery();
            Message.obtain(handler).sendToTarget();
        }
    };
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.log("from util","onServiceConnected: "+name.toString());
            iChatManager=IChatManager.Stub.asInterface(service);
            try {
                iChatManager.registerNewMsgListener(i_newMessageArrived);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.log(TAG, "onServiceDisconnected: "+name.toString());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivity);
        ButterKnife.bind(this);
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        dbHelper=new DBHelper(this);
        cursor=dbHelper.select();
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            Msg msg=new Msg(cursor.getInt(2),cursor.getString(1));
            //msg.setContent(cursor.getString(1));
            //msg.setType(cursor.getInt(2));
            msg_list.add(msg);
        }
        adpter=new ChatRecyclerAdpter(this,msg_list);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adpter);
        //绑定service
        Intent startservice=new Intent(this,ChatService.class);
        bindService(startservice,connection,BIND_AUTO_CREATE);

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.scrollToPosition(msg_list.size()-1);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_send=editText.getText().toString();
                //binder.sendMSg(msg_send);
                Msg msg=new Msg(1,msg_send);
                try {
                    iChatManager.SendMsg(msg);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                //msg.setType(1);
                //msg.setContent(msg_send);
                msg_list.add(msg);
                dbHelper.insert(msg);
                cursor.requery();
                Message.obtain(handler).sendToTarget();
                editText.setText("");
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message message){
            adpter.notifyDataSetChanged();
            recyclerView.scrollToPosition(msg_list.size()-1);
        }
    };
    private void SendNotification(String content){
        LogUtil.log(TAG, "SendNotification: "+content );
        /*这里要注意一个细节。
         *如果当前Activity存在的话，不应该再create一个新的活动，应该是回到当前活动。
         * 所以要给Intent添加对应的flag
         */
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setComponent(new ComponentName(this,ChatActivity.class));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent p=PendingIntent.getActivity(this,0,i,0);
        Notification notification=new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentText(content)
                .setContentTitle("新消息")
                .setTicker("Ticker")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(p)
                .build();
        notificationManager.notify(0,notification);
    }
    @Override
    public void onDestroy(){
        unbindService(connection);
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
    @Override
    public void onBackPressed(){
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
}
