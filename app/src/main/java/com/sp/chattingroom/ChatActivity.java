package com.sp.chattingroom;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.sp.chattingroom.Adapter.ChatRecyclerAdpter;
import com.sp.chattingroom.Adapter.DBHelper;
import com.sp.chattingroom.Model.Msg;

import java.net.URISyntaxException;
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
    private ArrayList<Msg> msg_list=new ArrayList<Msg>(),msg_list_save=new ArrayList<>();
    private DBHelper dbHelper;
    private Cursor cursor;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dbHelper=new DBHelper(this);
        cursor=dbHelper.select();
        Log.e(TAG, "onCreate: cursor.getcount:"+cursor.getCount() );
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            Msg msg=new Msg();
            msg.setContent(cursor.getString(1));
            Log.e(TAG, "onCreate: cursor.getposition("+i+"):"+msg.getContent());
            msg.setType(cursor.getInt(2));
            msg_list.add(msg);
        }
        for (int i=0;i<msg_list.size();i++){
            Log.e(TAG, "onCreate: "+"msglist("+i+"):"+msg_list.get(i).getContent() );
        }
        adpter=new ChatRecyclerAdpter(this,msg_list);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adpter);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e(TAG, "onLayoutChange: " );
                recyclerView.scrollToPosition(msg_list.size()-1);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg_send=editText.getText().toString();
                Msg msg=new Msg();
                msg.setType(1);
                msg.setContent(msg_send);
                msg_list.add(msg);
                dbHelper.insert(msg);
                cursor.requery();
                Message.obtain(handler).sendToTarget();
                socket.emit("postMsg",msg_send);
                editText.setText("");
            }
        });
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e(TAG, "onCreate: start" );
                    socket= IO.socket("http://115.159.38.75:4000");
                    socket.connect();
                }catch (URISyntaxException e){
                    Log.e(TAG, "run: "+"error" );
                    e.printStackTrace();
                }
                while (!socket.connected()){
                }
                Log.e(TAG, "run: "+socket.connected());
                socket.emit("login","CallMeSp");
                socket.on("nickExisted", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.e(TAG, "nickExisted" );
                    }
                });
                socket.on("loginSuccess", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.e(TAG, "loginSuccess:"+Thread.currentThread().getStackTrace());
                    }
                });
                socket.on("newMsg", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        String newcontent=args[1].toString();
                        Msg msg=new Msg();
                        msg.setContent(newcontent);
                        msg.setType(0);
                        msg_list.add(msg);
                        dbHelper.insert(msg);
                        cursor.requery();
                        Log.e(TAG, "call: "+cursor.getCount());
                        Message.obtain(handler).sendToTarget();
                    }
                });
            }
        });
        thread.start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message message){
            //msg_list.clear();
            //msg_list.addAll(msg_list_save);
            adpter.notifyDataSetChanged();
            recyclerView.scrollToPosition(msg_list.size()-1);
        }
    };
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
        socket.disconnect();
        thread.stop();
    }
}
