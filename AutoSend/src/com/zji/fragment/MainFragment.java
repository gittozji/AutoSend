package com.zji.fragment;

import java.util.ArrayList;

import com.zji.activity.MainActivity;
import com.zji.autosend.R;
import com.zji.db.MyDatabaseHelper;
import com.zji.dialog.EditDialog;
import com.zji.dialog.ListDialog;
import com.zji.service.MainService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/** 
* 主Fragment
* @author phlofy
* @date 2016年3月1日 下午9:31:16 
*/
public class MainFragment extends BaseFragment implements OnClickListener{
	// View对象
	View mRootView; //根view
	TextView content,change,hint,time,replace;
	Switch swi;
	
	// 数据存储对象
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_main, null);
		findViews();
		initView();
		return mRootView;
	}
	/**
	 * 通过id得到View
	 */
	private void findViews() {
		content = (TextView) mRootView.findViewById(R.id.content);
		change = (TextView) mRootView.findViewById(R.id.change);
		hint = (TextView) mRootView.findViewById(R.id.hint);
		time = (TextView) mRootView.findViewById(R.id.time);
		replace = (TextView) mRootView.findViewById(R.id.replace);
		swi = (Switch) mRootView.findViewById(R.id.swi);
		change.setOnClickListener(this);
		replace.setOnClickListener(this);
		if(MainService.isWorking){
			swi.setChecked(true);
		}
		// 开关按钮
		swi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Intent in = new Intent("com.zji.service.MainService");
				if(isChecked){
					// 手动开启后台服务
					getActivity().startService(in);
				}
				else{
					// 收到关闭后台服务
					getActivity().stopService(in);
				}
			}
		});
	}
	/**
	 * 初始化View
	 */
	private void initView(){
		preferences = getActivity().getSharedPreferences("autosend", Context.MODE_WORLD_READABLE);
		editor = preferences.edit();
		content.setText(preferences.getString("content", "抱歉，未能及时接听您的来电。\n【来电管家自动回复】"));
		time.setText(preferences.getInt("time", 30)+"分钟");
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		// 更改回复内容
		case R.id.change:{	
			// 查询回复信息表
			final Cursor cursor = db.getReadableDatabase().rawQuery("select name , content from "+db.REPLY_CONTENT, null);
			ArrayList<String> array = new ArrayList<String>();
			int i=0;
			// 遍历查询结果，存放在在item数组
			while(cursor.moveToNext()){
				array.add(cursor.getString(cursor.getColumnIndex("name")));
			}
			// 弹出选择框
			ListDialog dialog = new ListDialog(getActivity(), "选择更改", array) {
				@Override
				public void onClick(final int position) {
					cursor.moveToFirst();
					cursor.move(position);
					String newContent = cursor.getString(cursor.getColumnIndex("content"));
					content.setText(newContent);
					// 将新内容保存到SharedPreferences
					editor.putString("content", newContent);
					editor.commit();
					if(cursor != null){
						cursor.close();
					}
				}
			};
			dialog.show();
			break;
		}
		// 更改“经济时间”
		case R.id.replace:{
			EditDialog editDialog = new EditDialog(getActivity(),preferences.getInt("time", 30)+"",true) {
				
				@Override
				public void onClickEventForButton2(String text) {
					if(text.length() == 0){
						toast("未更改时间");
					}
					else{
						time.setText(text+"分钟");
						editor.putInt("time", Integer.parseInt(text));
						editor.commit();
					}
				}
				
				@Override
				public void onClickEventForButton1() {
					
				}
			};
			editDialog.setOnlySupportsDigital();
			editDialog.show();
			break;
		}
		}
		
	}
	
}
