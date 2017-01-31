package com.sp.chattingroom.Model;

/**
 * Created by Administrator on 2017/1/31.
 */

public class Msg{
    int type;
    String content;
    public void setType(int type){
        this.type=type;
    }
    public void setContent(String content){
        this.content=content;
    }
    public String getContent(){
        return content;
    }
    public int getType(){
        return type;
    }
}
