package com.sp.chattingroom.base;

import android.content.Context;

/**
 * Created by Administrator on 2017/3/12.
 */

public class KeepAliveManager {
    public KeepAliveActivity keepAliveActivity;
    public KeepAliveManager manager;
    public static KeepAliveManager getInstance(){
        return new KeepAliveManager();
    }
    public void stopActivity(){

    }

}
