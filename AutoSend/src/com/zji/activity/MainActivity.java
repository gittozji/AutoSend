package com.zji.activity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.zji.autosend.R;
import com.zji.db.MyDatabaseHelper;
import com.zji.dialog.EditDialog;
import com.zji.dialog.ListDialog;
import com.zji.fragment.MainFragment;
import com.zji.fragment.NewFragment;
import com.zji.fragment.SendNotesFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener{
	// 数据库操作
	MyDatabaseHelper db;
	
    //声明相关变量
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    
    TextView menuMain,menuNotes,menuNew,menuDelete;
    
    // 几个内容Fragment
    MainFragment mMaMainFragment;
    NewFragment mNewFragment;
    SendNotesFragment mSendNotesFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDatabaseHelper(MainActivity.this);
        findViews(); //获取控件
        toolbar.setTitle(R.string.app_name);//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.d_open, R.string.d_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        mMaMainFragment = new MainFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame, mMaMainFragment).commit();
    }
    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        menuMain = (TextView) findViewById(R.id.menu_main);
        menuNotes = (TextView) findViewById(R.id.menu_notes);
        menuNew = (TextView) findViewById(R.id.menu_new);
        menuDelete = (TextView) findViewById(R.id.menu_delete);
        menuMain.setOnClickListener(this);
        menuNotes.setOnClickListener(this);
        menuNew.setOnClickListener(this);
        menuDelete.setOnClickListener(this);
        
    }
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		// 主界面
		case R.id.menu_main:{
			mMaMainFragment = new MainFragment();
	        getFragmentManager().beginTransaction().replace(R.id.frame, mMaMainFragment).commit();
	        mDrawerLayout.closeDrawer(Gravity.LEFT);
			break;
		}
		// 短信发送记录界面
		case R.id.menu_notes:{
			mSendNotesFragment = new SendNotesFragment();
			getFragmentManager().beginTransaction().replace(R.id.frame, mSendNotesFragment).commit();
	        mDrawerLayout.closeDrawer(Gravity.LEFT);
			break;
		}
		// 新增回复内容界面
		case R.id.menu_new:{
			mNewFragment = new NewFragment();
	        getFragmentManager().beginTransaction().replace(R.id.frame, mNewFragment).commit();
	        mDrawerLayout.closeDrawer(Gravity.LEFT);
			break;
		}
		// 删除回复内容界面
		case R.id.menu_delete:{
			// 查询回复信息表
			final Cursor cursor = db.getReadableDatabase().rawQuery("select id , name , content from "+db.REPLY_CONTENT, null);
			ArrayList<String> array = new ArrayList<String>();
			int i=0;
			// 遍历查询结果，存放在在item数组
			while(cursor.moveToNext()){
				array.add(cursor.getString(cursor.getColumnIndex("name")));
			}
			// 弹出选择框
			ListDialog dialog = new ListDialog(MainActivity.this, "选择删除", array) {
				@Override
				public void onClick(final int position) {
					EditDialog editDialog = new EditDialog(MainActivity.this, "确认删除？",false) {
						
						@Override
						public void onClickEventForButton2(String text) {
							cursor.moveToFirst();
							cursor.move(position);
							int id = cursor.getInt(cursor.getColumnIndex("id"));
							db.getReadableDatabase().delete(db.REPLY_CONTENT, "id = ?", new String[]{id+""});
							Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
							if(cursor != null){
								cursor.close();
							}
						}
						
						@Override
						public void onClickEventForButton1() {
							if(cursor != null){
								cursor.close();
							}
						}
					};
					editDialog.show();
				}
			};
			dialog.show();
			break;
		}
		}
		
	}
}
