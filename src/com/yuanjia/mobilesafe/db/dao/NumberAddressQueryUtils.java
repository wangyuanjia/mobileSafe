package com.yuanjia.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAddressQueryUtils {

	
	private static String path = "data/data/com.yuanjia.mobilesafe/files/address.db";
	/**
	 * 传一个号码，返回一个归属地
	 * 
	 */
	public static String queryNumber(String number){
		String address = number;
		//path  把address.db这个数据库拷贝到data/data/
		//手机号码13 14 15 16 18
		//手机号码的正则表达式
		//^1[24568]\d{9}$
		SQLiteDatabase database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		if(number.matches("^1[24568]\\d{9}$")){
			//手机号码						
			
			Cursor cursor = database.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)", 
					new String[]{number.substring(0,7)});
			
			while(cursor.moveToNext()){
				String location = cursor.getString(0);
				address = location;
			}
			cursor.close();
		} else{
			//其它号码
			switch (number.length()) {
			case 3:
				//110
				address = "匪警号码";
				break;
			case 4:
				//5554
				address = "模拟器";
				break;
			case 5:
				//10086
				address = "客服电话";
				break;
				
			default:
				//长途电话
				if(number.length()>10&&number.startsWith("0")){
					Cursor cursor = database.rawQuery("select location from data2 where area = ?" , new String[]{number.substring(1, 3)});
			       while(cursor.moveToNext()){
			    	   String location = cursor.getString(0);
			           address = location.substring(0, location.length()-2);
			       } 
				
			       cursor.close();
				}
				break;
			}
		}
		
		
	
		return address;
		
	}
	
}
