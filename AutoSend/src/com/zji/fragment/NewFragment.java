package com.zji.fragment;

import com.zji.autosend.R;
import com.zji.db.MyDatabaseHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/** 
* 新增回复内容
* @author phlofy
* @date 2016年3月2日 下午9:25:58 
*/
public class NewFragment extends BaseFragment implements OnClickListener{
	View mRootView; //根view
	EditText name,content;
	Button add;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragmnet_new, null);
		findViews();
		return mRootView;
	}
	private void findViews() {
		name = (EditText) mRootView.findViewById(R.id.name);
		content = (EditText) mRootView.findViewById(R.id.content);
		add = (Button) mRootView.findViewById(R.id.add);
		add.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.add:{
			String contentString = content.getText().toString();
			String nameString = name.getText().toString();
			if(contentString.length() <= 70 && contentString.length() > 0){
				if(nameString.length() == 0){
					nameString = "未命名";
				}
				db.getReadableDatabase().execSQL("insert into "+db.REPLY_CONTENT+" values(null , ? , ?)",new String[]{nameString,contentString});
				toast("添加成功");
			}
			else{
				toast("短信内容应该在0～70个字符之间");
			}
			break;
		}
		}
	}
	
}
