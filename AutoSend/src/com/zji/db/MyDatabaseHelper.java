package com.zji.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** 
* 数据库操作类
* @author phlofy
* @date 2016年3月3日 上午10:05:03 
*/
public class MyDatabaseHelper extends SQLiteOpenHelper{
	final static String DATABASE_NAME = "AutoSend.db3"; // 数据库名称
	public final static String REPLY_CONTENT = "reply_content"; // 回复内容表
	public final static String SEND_NOTES = "send_notes"; // 短信发送记录表
	final String CREATE_REPLY_CONTENT = "create table reply_content(id integer primary key autoincrement,name,content)";
	final String CREATE_SEND_NOTES = "create table send_notes(id integer primary key autoincrement,name,number,time)";
	public MyDatabaseHelper(Context context) {
		super(context,DATABASE_NAME,null,1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 第一次使用数据库时建表
		db.execSQL(CREATE_REPLY_CONTENT);
		db.execSQL(CREATE_SEND_NOTES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
