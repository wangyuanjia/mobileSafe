package com.yuanjia.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yuanjia.mobilesafe.db.BlackNumberOpenHelper;
import com.yuanjia.mobilesafe.db.AppLockDBOpenHelper;

/**
 * ��������dao
 * @author Administrator
 *
 */
public class AppLockDao {

	private AppLockDBOpenHelper helper;
    private Context context;
    
    /**
     * ���췽��
     * @param context������
     */
	public AppLockDao(Context context) {
	
		helper = new AppLockDBOpenHelper(context);
		this.context = context;
	}
	
	/**
	 * ���һ��Ҫ����Ӧ�ó���İ���
	 * @param packname
	 */
	
	public void add(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
	    ContentValues values = new ContentValues();
		values.put("packname", packname);
	    
	    db.insert("applock", null, values);
	    db.close();
	    
	    Intent intent = new Intent();
	    intent.setAction("com.yuanjia.mobilesafe");
	    context.sendBroadcast(intent);
	}
	
	//ɾ������
	public void delete(String packname){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("applock", "packname=?", new String[]{packname});
	
		db.close();
		Intent intent = new Intent();
	    intent.setAction("com.yuanjia.mobilesafe");
	    context.sendBroadcast(intent);
	}
	//�ҵ��������
	public boolean find(String packname){
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", null, "packname=?", new String[]{packname}, null, null, null);
		if(cursor.moveToNext()){
			result = true;
		}
		
		db.close();
		cursor.close();
		return result;
		
	}
	
	public boolean find1(String packname1){
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", null, "packname=?", new String[]{packname1}, null, null, null);
		while(cursor.moveToNext()){
			result = true;
		}
		db.close();
		cursor.close();
		return result;
		
	}
	/**
	 * ��ѯȫ���İ���
	 * @param packname
	 * @return
	 */
	public List<String> findAll(){
		List<String> protectPacknames = new ArrayList<String>();
		
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("applock", new String[]{"packname"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			result = true;
		    protectPacknames.add(cursor.getString(0));
		}
		
		db.close();
		cursor.close();
		return protectPacknames;
		
	}
}
