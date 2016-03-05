package com.zji.activity;

import android.app.Application;

/** 
* 用于存放中间变量
* @author phlofy
* @date 2016年3月4日 下午9:55:33 
*/
public class MyApplication extends Application{
	private static MyApplication myApplication = null;
	
	/**
	 *  “现在”短信要发送的目标
	 *  1.为了防止MyContentObserver.onChange方法短时间内被多次触发，
	 *  	造成还未来得及插入短信发送成功的记录，短信重复发送出去
	 *  2.解决传递给SendMessageReceiver的Intent没有数据为上一次（第一次）
	 *  	的数据。替代通过Intent得到number和name
	 */
	String number;
	String name;
	String content;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//由于Application类本身已经单例，所以直接按以下处理即可。
		myApplication = this;
	}
	/**
	 * 获取Application实例
	 * @return
	 */
	public static MyApplication getInstance(){
		return myApplication;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * 获取现在短信的目标号码
	 * @return
	 */
	public String getNumber() {
		return number;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取现在短信的目标者名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取短信内容
	 * @return
	 */
	public String getContent() {
		return content;
	}
}
