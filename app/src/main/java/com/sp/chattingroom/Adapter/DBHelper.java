package com.sp.chattingroom.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sp.chattingroom.Model.Msg;

/**
 * Created by Administrator on 2017/2/12.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private final static String DB_Name="Chatlog.db";
    private final static int DB_Version=1;
    private final static String TABLE_Name="my";
    private final static String ID="id";
    private SQLiteDatabase sqLiteDatabase=getWritableDatabase();
    public DBHelper(Context context){
        super(context,DB_Name,null,DB_Version);
        Log.e(TAG, "DBHelper: ");
    }
    @Override
    public void onCreate(SQLiteDatabase database){
        Log.e(TAG, "onCreate: ");
        database.execSQL("create table "+TABLE_Name+"(id INTEGER PRIMARY KEY AUTOINCREMENT,content TEXT,type INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldversion,int newversion){
    }
    public long insert(Msg msg){
        Log.e(TAG, "insert: "+msg.getContent() );
        ContentValues contentValues=new ContentValues();
        contentValues.put("content",msg.getContent());
        contentValues.put("type",msg.getType());
        long row=sqLiteDatabase.insert(TABLE_Name,null,contentValues);
        return row;
    }
    public Cursor select(){
        Cursor cursor=sqLiteDatabase.query(TABLE_Name,null,null,null,null,null,null);
        return cursor;
    }
}
