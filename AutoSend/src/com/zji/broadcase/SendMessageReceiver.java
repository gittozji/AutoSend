package com.zji.broadcase;

import com.zji.activity.MyApplication;
import com.zji.db.MyDatabaseHelper;
import com.zji.utils.SendMessage;
import com.zji.utils.Timer;
import com.zji.utils.WriteAndRead;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendMessageReceiver extends BroadcastReceiver{
	MyDatabaseHelper db;
	static int errCount = 0; // 记录一条短信发送失败次数
	@Override
	public void onReceive(Context context, Intent intent) {
		if("SENT_SMS_ACTION".equals(intent.getAction())){
			try{
				MyApplication instance = MyApplication.getInstance();
				switch (getResultCode()) {
				// 短信发送成功
				case Activity.RESULT_OK:
					db = new MyDatabaseHelper(context);
					db.getReadableDatabase().execSQL("insert into "+db.SEND_NOTES+" values(null , ? , ? , ?)",new String[]{instance.getName(),instance.getNumber(),Timer.getNowDate()+""});
					if(db != null){
						db.close();
					}
					errCount = 0;
					// 短时间变量，用完后将其置空
					instance.setName(null);
					instance.setNumber(null);
					instance.setContent(null);
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
				case SmsManager.RESULT_ERROR_NO_SERVICE:
				case SmsManager.RESULT_ERROR_NULL_PDU:
				case SmsManager.RESULT_ERROR_RADIO_OFF:
                	if(errCount < 2){
                		// 最多可以尝试发送三遍
            			SendMessage.sendTextMessage(context, instance.getName(), instance.getNumber(), instance.getContent());
                		errCount++;
                	}
                	else {
                		// 尝试发送三遍仍然发送不出去，放弃发送
                		errCount = 0;
                		// 短时间变量，用完后将其置空
    					instance.setName(null);
    					instance.setNumber(null);
    					instance.setContent(null);
					}
					break;
				default:
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			finally{
			}
		}
	}

}
