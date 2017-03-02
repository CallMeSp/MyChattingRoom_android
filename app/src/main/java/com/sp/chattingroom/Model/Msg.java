package com.sp.chattingroom.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/1/31.
 */

public class Msg implements Parcelable{
    int type;
    String content;
    public Msg(int type,String content){
        this.type=type;
        this.content=content;
    }
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(type);
        out.writeString(content);
    }
    public static final Parcelable.Creator<Msg> CREATOR=new Parcelable.Creator<Msg>(){
        @Override
        public Msg createFromParcel(Parcel source) {
            return new Msg(source);
        }

        @Override
        public Msg[] newArray(int size) {
            return new Msg[size];
        }
    };
    private Msg(Parcel in){
        type=in.readInt();
        content=in.readString();
    }
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
