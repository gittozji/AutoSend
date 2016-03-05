package com.zji.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

import android.R.integer;
import android.content.Context;
import android.database.ContentObserver;

public class WriteAndRead {
	String file_nameString;
	Context context;
	public WriteAndRead(Context context,String file_nameString){
		this.context = context;
		this.file_nameString = file_nameString;
	}
	public String read(){
		try{
			FileInputStream fileInputStream = context.openFileInput(file_nameString);
			byte[] buff = new byte[1024];
			int hasRead = 0;
			StringBuilder stringBuilder = new StringBuilder("");
			while((hasRead = fileInputStream.read(buff))>0){
				stringBuilder.append(new String(buff,0,hasRead));
			}
			fileInputStream.close();
			
			return stringBuilder.toString();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public void write(String writeString){
		try{
			FileOutputStream fileOutputStream = context.openFileOutput(file_nameString, context.MODE_WORLD_WRITEABLE|context.MODE_APPEND);
			PrintStream printStream = new PrintStream(fileOutputStream);
			printStream.println(writeString);
			printStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void flush(){
		try{
			FileOutputStream fileOutputStream = context.openFileOutput(file_nameString, context.MODE_WORLD_WRITEABLE);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
