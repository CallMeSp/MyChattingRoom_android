// I_NewMessageArrived.aidl
package com.sp.chattingroom;

// Declare any non-default types here with import statements
import com.sp.chattingroom.Model.Msg;
interface I_NewMessageArrived {
    void newMessageArrive(in Msg msg);
}
