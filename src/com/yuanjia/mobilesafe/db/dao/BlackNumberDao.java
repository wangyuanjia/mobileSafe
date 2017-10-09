package com.yuanjia.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yuanjia.mobilesafe.db.AppLockDBOpenHelper;
import com.yuanjia.mobilesafe.db.BlackNumberOpenHelper;
import com.yuanjia.mobilesafe.domain.BlackNumberInfo;

/**
 * ����������ɾ�Ĳ�ҵ����
 * @author Administrator
 *
 */
public class BlackNumberDao {

	private BlackNumberOpenHelper helper;

    /**
     * ���췽��
     * @param context������
     */
	public BlackNumberDao(Context context) {
	
		helper = new BlackNumberOpenHelper(context);
		
	}
	
	
	/**
	 * ��ѯ���ֺ���������
	 * @param number
	 * 
	 * @param offset���ĸ�λ�ÿ�ʼ��ȡ����
	 * @param maxnumber һ������ȡ��������¼
	 * @return
	 */
	public List<BlackNumberInfo> findPart(int offset,int maxnumber){
	    List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc limit ? offset ?", 
			new String[]{String.valueOf(maxnumber),String.valueOf(offset)}	);
	    while(cursor.moveToNext()){
	    	BlackNumberInfo info = new BlackNumberInfo();
	    	String number = cursor.getString(0);
	    	String mode = cursor.getString(1);
	    	info.setMode(mode);
	    	info.setNumber(number);
	    	result.add(info);
	    }
		db.close();
		cursor.close();
		return result;
		
	}
	
	/**
	 * ��ѯ���к���������
	 * @return
	 */
	
	
	public List<BlackNumberInfo> findAll(){
	    List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc ", null);
	    while(cursor.moveToNext()){
	    	BlackNumberInfo info = new BlackNumberInfo();
	    	String number = cursor.getString(0);
	    	String mode = cursor.getString(1);
	    	info.setMode(mode);
	    	info.setNumber(number);
	    	result.add(info);
	    }
		db.close();
		cursor.close();
		return result;
		
	}
	
	
	/**
	 * ��ѯ���������������ģʽ
	 * @param number
	 * @return  ���غ��������ģʽ�����Ǻ��������뷵��null
	 */
	public String findMode(String number){
	    String result = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select mode from blacknumber where number = ?", new String[]{number});
	    if(cursor.moveToNext()){
	    	result = cursor.getString(0);
	    }
		db.close();
		cursor.close();
		return result;
		
	}
	
	
	/**
	 * ��ѯ�����������Ƿ����
	 * @param number
	 * @return
	 */
	public boolean find(String number){
	    boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber where number = ?", new String[]{number});
	    if(cursor.moveToNext()){
	    	result = true;
	    }
		db.close();
		cursor.close();
		return result;
		
	}
	
	/**
	 * ���Ӻ���������
	 * @param number
	 * @param mode
	 */
	
	public void add(String number,String mode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);
		db.close();
		
	}
	
	/**
	 * �޸ĺ��������������ģʽ
	 * @param number
	 * @param mode
	 */
	
	public void update(String number,String newmode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
	
		values.put("mode", newmode);
		db.update("blacknumber", values, "number = ?", new String[]{number});
		db.close();
		
	}
	
	/**
	 * ɾ���ĺ���������
	 * @param number
	 * @param mode
	 */
	
	public void delete(String number){
		SQLiteDatabase db = helper.getWritableDatabase();
	
		db.delete("blacknumber", "number = ?", new String[]{number});
		db.close();
		
	}
}
