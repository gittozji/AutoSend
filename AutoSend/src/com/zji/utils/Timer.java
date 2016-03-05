package com.zji.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
* 时间处理类
* @author phlofy
* @date 2016年3月3日 下午8:27:31 
*/
public class Timer {
	/**
	 * Long型转换为“yyyy-MM-dd HH:mm:ss”型时间
	 * @param time
	 * @return
	 */
	public static String transferLongToDate(Long time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(time));
	}
	/**
	 * 获取当前系统时间
	 * @return
	 */
	public static long getNowDate(){
		return new Date().getTime();
	}
}
