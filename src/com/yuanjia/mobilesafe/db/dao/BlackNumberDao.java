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
 * 黑名单的增删改查业务类
 * @author Administrator
 *
 */
public class BlackNumberDao {

	private BlackNumberOpenHelper helper;

    /**
     * 构造方法
     * @param context上下文
     */
	public BlackNumberDao(Context context) {
	
		helper = new BlackNumberOpenHelper(context);
		
	}
	
	
	/**
	 * 查询部分黑名单号码
	 * @param number
	 * 
	 * @param offset从哪个位置开始获取数据
	 * @param maxnumber 一次最多获取多少条记录
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
	 * 查询所有黑名单号码
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
	 * 查询黑名单号码的拦截模式
	 * @param number
	 * @return  返回号码的拦截模式，不是黑名单号码返回null
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
	 * 查询黑名单号码是否存在
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
	 * 增加黑名单号码
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
	 * 修改黑名单号码的拦截模式
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
	 * 删除的黑名单号码
	 * @param number
	 * @param mode
	 */
	
	public void delete(String number){
		SQLiteDatabase db = helper.getWritableDatabase();
	
		db.delete("blacknumber", "number = ?", new String[]{number});
		db.close();
		
	}
}
