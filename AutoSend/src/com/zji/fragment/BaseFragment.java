package com.zji.fragment;

import com.zji.db.MyDatabaseHelper;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

/** 
* 基Fragment
* @author phlofy
* @date 2016年3月1日 下午9:31:47 
*/
public class BaseFragment extends Fragment{
	MyDatabaseHelper db;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new MyDatabaseHelper(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(db != null){
			db.close();
		}
	}

	public void toast(String msg){
		try{
			Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
		}
		catch(Exception e){
			System.out.println("toast空指针异常。");
		}
	}
}
