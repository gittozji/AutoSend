package com.zji.fragment;

import com.zji.autosend.R;
import com.zji.utils.Timer;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/** 
* 短信发送记录
* @author phlofy
* @date 2016年3月3日 下午10:33:25 
*/
public class SendNotesFragment extends BaseFragment{
	View mRootView; //根view
	EditText mEditText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_send_notes, null);
		mEditText = (EditText) mRootView.findViewById(R.id.notes);
		Cursor cursor = db.getReadableDatabase().rawQuery("select * from "+db.SEND_NOTES+" order by time desc", null);
		StringBuffer notes = new StringBuffer();
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			if(name == null)
				name = "未知";
			notes.append("『姓名』 "+name);
			notes.append("\n");
			notes.append("『号码』 "+cursor.getString(cursor.getColumnIndex("number")));
			notes.append("\n");
			notes.append("『时间』 "+Timer.transferLongToDate(cursor.getLong(cursor.getColumnIndex("time"))));
			notes.append("\n----------\n");
		}
		if(cursor != null){
			cursor.close();
		}
		mEditText.setText(notes);
		return mRootView;
	}

	
}
