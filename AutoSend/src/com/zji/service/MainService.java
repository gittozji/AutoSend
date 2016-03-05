package com.zji.service;

import java.util.Date;

import com.zji.activity.MyApplication;
import com.zji.broadcase.SendMessageReceiver;
import com.zji.db.MyDatabaseHelper;
import com.zji.utils.SendMessage;
import com.zji.utils.Timer;
import com.zji.utils.WriteAndRead;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.widget.Toast;

/** 
* 后台运行的服务，负责开启监听通话记录表的变化
* @author phlofy
* @date 2016年3月3日 下午2:13:29 
*/
public class MainService extends Service{
	MyContentObserver mMyContentObserver;
	SendMessageReceiver mSendMessageReceiver;
	public static boolean isWorking = false; // 方便MainFragment知道是否开启后台监听服务
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 注册通话记录表监听
		mMyContentObserver = new MyContentObserver(this, new Handler());
		this.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, false, mMyContentObserver);
		
		// 注册短信发送状态监听
		IntentFilter intentFilter = new IntentFilter("SENT_SMS_ACTION");
		mSendMessageReceiver = new SendMessageReceiver();
		this.registerReceiver(mSendMessageReceiver, intentFilter);
		
		isWorking = true;
		try{
			Toast.makeText(this, "服务开始运行", Toast.LENGTH_LONG).show();
		}catch(Exception e){}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销两个监听
		this.getContentResolver().unregisterContentObserver(mMyContentObserver);
		this.unregisterReceiver(mSendMessageReceiver);
		
		isWorking = false;
		try{
			Toast.makeText(this, "服务停止运行", Toast.LENGTH_LONG).show();
		}catch(Exception e){}
	}
}
/**
 * 通话记录表变化的监听者
 * @author Administrator
 *
 */
class MyContentObserver extends ContentObserver{
	Context context;
	MyDatabaseHelper db;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	public MyContentObserver(Context context, Handler handler) {
		super(handler);
		this.context = context;
		db = new MyDatabaseHelper(context);
		preferences = context.getSharedPreferences("autosend", Context.MODE_WORLD_READABLE);
		editor = preferences.edit();
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		/****************获取到通话记录表的最新一条消息******************/
		Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, new String[]{Calls.NUMBER,Calls.CACHED_NAME,Calls.DATE,Calls.TYPE}, Calls.TYPE+" = ?", new String[]{Calls.MISSED_TYPE+""}, Calls.DEFAULT_SORT_ORDER);
		cursor.moveToFirst();
		String name = cursor.getString(cursor.getColumnIndex(Calls.CACHED_NAME));
		String number = cursor.getString(cursor.getColumnIndex(Calls.NUMBER));
		long date = cursor.getLong(cursor.getColumnIndex(Calls.DATE));
		int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));
		if(cursor != null){
			cursor.close();
		}
		
		/**
		 *  判断该未接来电是否是该软件安装后发生。
		 *  防止没有未接来电，但onChange还是被执行的情况。
		 *  解决软件第一次安装后自动发送一条短信
		 */
		long lifeStart  = preferences.getLong("life_start", 0); //试图获取软件安装时间
		if(lifeStart == 0){
			// 为0说明软件第一次执行，记录此时时间为软件安装时间
			editor.putLong("life_start", new Date().getTime());
			editor.commit();
		}
		if(lifeStart == 0 || date < lifeStart){
			// 忽略掉软件安装前的未接来电
			return;
		}
		
		/*******************查找短信发送表中近“经济时间”内是否有该号码********************/
		long whereTime = date - preferences.getInt("time", 30)*60000; // 记录的时间 - “经济时间” 
		// 该号码在短信发送表中的近“经济时间”内的记录
		Cursor cursorDb = db.getReadableDatabase().rawQuery("select * from "+db.SEND_NOTES+" where "+Calls.NUMBER+" = ? and time > ? ", new String[]{number,whereTime+""});
		
		/*********************短信操作***********************/
		if(cursorDb.moveToNext()){
			// 有记录，不发送短信
		}
		else{
			// 没有记录，发送短信
			MyApplication instance = MyApplication.getInstance();
			if(instance.getNumber() != null) {
				// 已经规定MyApplication中的name、number、content为“现在”变量，
				// 因此过一定时间（一般为短信开始发送到发送成功的时间）后将为被置空
				// 如果不为空，说明发生了onChange短时间被多次触发
				return;
			}
			instance.setName(name);
			instance.setNumber(number);
			instance.setContent(preferences.getString("content", "抱歉，未能及时接听您的来电。\n【来电管家自动回复】"));
			SendMessage.sendTextMessage(context, name, number, instance.getContent());			
		}
		if(cursorDb != null){
			cursorDb.close();
		}
		if(db != null){
			db.close();
		}
	}
	
}