package com.yuanjia.mobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import android.widget.ProgressBar;

public class SmsUtils {
  /**
   * 备份短信的回调接口
   * @author Administrator
   *
   */
	public interface BackUpCallBack{
		/**
		 * 开始备份的时候，设置进度的最大值
		 */
		public void beforeBackup(int max);
		
		/**
		 * 备份过程中，增加进度
		 */
		public void onSmsBackup(int process);

	
	}
	
	
	/**
     * 备份用户的短信
     * @param context 上下文
     *  @param pd 进度条对话框
     */
	
	
	public static void backUpSms(Context context,BackUpCallBack callBack) throws Exception{
		ContentResolver resolver = context.getContentResolver();
		File file = new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream fos = new FileOutputStream(file);
		//把用户的短信一条一条的读出来，按照一定格式去写到文件里
		 XmlSerializer serializer = Xml.newSerializer();
		//初始化生成器
		 serializer.setOutput(fos, "utf-8");
		 serializer.startDocument("utf-8", true);
		 serializer.startTag(null, "smss");
		 
		
		 
		 Uri uri = Uri.parse("content://sms/");
		 Cursor cursor = resolver.query(uri, new String[]{"body","address","type","date"}, null, null, null);
	     //开始备份的时候，设置进度条的最大值
		 int max = cursor.getCount();
//		 pb.setMax(max);
		 int process = 0;
	     callBack.beforeBackup(max);
		 
	     serializer.attribute(null, "max", max+"");
		 
		 while(cursor.moveToNext()){
			 Thread.sleep(500);
			 String body = cursor.getString(0);
			 String address = cursor.getString(1);
			 String type = cursor.getString(2);
			 String date = cursor.getString(3);
			 
			 serializer.startTag(null, "sms");
			 serializer.startTag(null, "body");
			 serializer.text(body);
			 serializer.endTag(null, "body");
			 
			 serializer.startTag(null, "body");
			 serializer.text(address);
			 serializer.endTag(null, "body");
			 
			 serializer.startTag(null, "body");
			 serializer.text(type);
			 serializer.endTag(null, "body");
			 
			 serializer.startTag(null, "body");
			 serializer.text(date);
			 serializer.endTag(null, "body");
			 serializer.endTag(null, "sms");
			 //备份过程中增加进度
		     process++;
//			 pb.setProgress(process);
			 callBack.onSmsBackup(process);
		 }
		 cursor.close();
		 serializer.endTag(null, "smss");
		 serializer.endDocument();
		 fos.close();
	} 
	
	public static void restoreSms(Context context){
		//1.读取SD卡上的xml文件
		//Xml.newPullParser();
		
		//2.读取max
		
		//3.读取每一条短信    body date type address
		 
		//4.把短信插入到系统短信应用
		 Uri uri = Uri.parse("content://sms/");
		 ContentValues values = new ContentValues();
		 values.put("body", "woshi duanxin de neirong");
		 values.put("date", "1395045035573");
		 values.put("type", "1");
		 values.put("address", "5558");
		 context.getContentResolver().insert(uri, values);
	}
}
