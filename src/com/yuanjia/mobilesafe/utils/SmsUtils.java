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
   * ���ݶ��ŵĻص��ӿ�
   * @author Administrator
   *
   */
	public interface BackUpCallBack{
		/**
		 * ��ʼ���ݵ�ʱ�����ý��ȵ����ֵ
		 */
		public void beforeBackup(int max);
		
		/**
		 * ���ݹ����У����ӽ���
		 */
		public void onSmsBackup(int process);

	
	}
	
	
	/**
     * �����û��Ķ���
     * @param context ������
     *  @param pd �������Ի���
     */
	
	
	public static void backUpSms(Context context,BackUpCallBack callBack) throws Exception{
		ContentResolver resolver = context.getContentResolver();
		File file = new File(Environment.getExternalStorageDirectory(),"backup.xml");
		FileOutputStream fos = new FileOutputStream(file);
		//���û��Ķ���һ��һ���Ķ�����������һ����ʽȥд���ļ���
		 XmlSerializer serializer = Xml.newSerializer();
		//��ʼ��������
		 serializer.setOutput(fos, "utf-8");
		 serializer.startDocument("utf-8", true);
		 serializer.startTag(null, "smss");
		 
		
		 
		 Uri uri = Uri.parse("content://sms/");
		 Cursor cursor = resolver.query(uri, new String[]{"body","address","type","date"}, null, null, null);
	     //��ʼ���ݵ�ʱ�����ý����������ֵ
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
			 //���ݹ��������ӽ���
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
		//1.��ȡSD���ϵ�xml�ļ�
		//Xml.newPullParser();
		
		//2.��ȡmax
		
		//3.��ȡÿһ������    body date type address
		 
		//4.�Ѷ��Ų��뵽ϵͳ����Ӧ��
		 Uri uri = Uri.parse("content://sms/");
		 ContentValues values = new ContentValues();
		 values.put("body", "woshi duanxin de neirong");
		 values.put("date", "1395045035573");
		 values.put("type", "1");
		 values.put("address", "5558");
		 context.getContentResolver().insert(uri, values);
	}
}
