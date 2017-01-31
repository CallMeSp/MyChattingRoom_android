package com.sp.chattingroom.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sp.chattingroom.Model.Msg;
import com.sp.chattingroom.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/31.
 */

public class ChatRecyclerAdpter extends RecyclerView.Adapter<ChatRecyclerAdpter.MyHolder> {
    private ArrayList<Msg> msg_list=new ArrayList<>();
    private LayoutInflater inflater;
    private int LEFT=0,RIGHT=1;
    public ChatRecyclerAdpter(Context context, ArrayList<Msg> list){
        msg_list=list;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getItemCount(){
        return msg_list.size();
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent,int type){
        View view=inflater.inflate(R.layout.msg_item,parent,false);
        MyHolder myHolder=new MyHolder(view);
        return myHolder;
    }
    @Override
    public void onBindViewHolder(MyHolder holder,final int position){
        if (msg_list.get(position).getType()==0){
            holder.text_send.setVisibility(View.GONE);
            holder.text_get.setVisibility(View.VISIBLE);
            holder.text_get.setText(msg_list.get(position).getContent());
        }else {
            holder.text_get.setVisibility(View.GONE);
            holder.text_send.setVisibility(View.VISIBLE);
            holder.text_send.setText(msg_list.get(position).getContent());
        }
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView text_send,text_get;
        public MyHolder(View view){
            super(view);
            text_get=(TextView)view.findViewById(R.id.left_msg);
            text_send=(TextView)view.findViewById(R.id.right_msg);
        }
    }
}
