package com.yuanjia.mobilesafe.db.dao;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {
	
	
	private static String path = "data/data/com.yuanjia.mobilesafe/files/antivirus.db";
	
    /**
     * 病毒数据库查询业务类
     * @param md5
     * @return
     */
	
	public static boolean isVirus(String md5){
		 
		boolean virus = false;
		
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select _id from datable where md5 = ?", new String[]{md5});
		
		while(cursor.moveToNext()){
			virus = true;
		}
		db.close();
		cursor.close();
		return virus;
		
	}
	
	public static void addVirusMD5Code(String md5,String desc){
		
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		
		ContentValues values = new ContentValues();
        values.put("md5", md5);
        values.put("desc", desc);
        values.put("type", 6);
        values.put("name", "Android.virus");
 
        
		db.insert("datable", null, values);
		db.close();
	}
}
