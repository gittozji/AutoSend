package com.zji.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/** 
* 发送短信类
* @author phlofy
* @date 2016年3月3日 下午9:58:45 
*/
public class SendMessage {
	synchronized public static void sendTextMessage(Context context, String name, String number, String content){
		Intent in = new Intent("SENT_SMS_ACTION");  
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, in, 0);
		SmsManager.getDefault().sendTextMessage(number, null, content, pi, null);
	}
}
