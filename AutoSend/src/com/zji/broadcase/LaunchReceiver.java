package com.zji.broadcase;

import java.text.SimpleDateFormat;

import com.zji.service.MainService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** 
* 开机自启
* @author phlofy
* @date 2016年3月3日 下午10:30:20 
*/
public class LaunchReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent){
		Intent tintent = new Intent(context,MainService.class);
		context.startService(tintent);
	}
}
