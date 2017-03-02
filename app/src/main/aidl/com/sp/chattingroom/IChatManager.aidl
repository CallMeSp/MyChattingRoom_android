// IChatManager.aidl
package com.sp.chattingroom;
// Declare any non-default types here with import statements
import com.sp.chattingroom.Model.Msg;
import com.sp.chattingroom.I_NewMessageArrived;
import com.sp.chattingroom.I_GetLoginResult;
interface IChatManager {
    void registerNewMsgListener(I_NewMessageArrived listener);
    void Login(in String name,I_GetLoginResult listener);
    void SendMsg(in Msg msg);
}
