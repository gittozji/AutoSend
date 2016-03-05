package com.zji.dialog;

import java.util.ArrayList;

import com.zji.autosend.R;
import android.app.Dialog;
import android.content.Context;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 用于列表展示
 * @author Administrator
 *
 */
public abstract class ListDialog{
	ListView mListView;
	TextView mTextView;
	ArrayAdapter<String> adapter;
	Dialog mDialog;
	public ListDialog(Context context, String title, ArrayList<String> array){
		mDialog = new Dialog(context, R.style.DialogTheme);
		mDialog.setContentView(R.layout.dialog_list);
		mListView = (ListView) mDialog.findViewById(R.id.list);
		mTextView = (TextView) mDialog.findViewById(R.id.title);
		mTextView.setText(title);
		adapter = new ArrayAdapter<String>(context, R.layout.list_item, array);
		mListView.setAdapter(adapter);
		click();
	}

	private void click() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onClick(position);
				mDialog.dismiss();
			}
		});
	}

	public void show(){
		mDialog.show();
	}
	
	public abstract void onClick(int position);
}

