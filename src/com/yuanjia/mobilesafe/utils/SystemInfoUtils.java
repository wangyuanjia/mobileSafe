package com.yuanjia.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;


public class SystemInfoUtils {

//	private static ActivityManager am ; 
	
	/**
	 * ��ȡ�������еĽ��̵�����
	 * @param context
	 * @return
	 */
	public static int getRunningProcessCount(Context context){
	    //packageManager //�������� �൱�ڳ�������� ��̬������
		//ActivityManager ���̹������������ֻ��Ļ��Ϣ ��̬������
		
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		 List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		
		return infos.size();
		
	}
	
	/**
	 * ��ȡ�ֻ����õ�ʣ���ڴ�
	 * @return
	 */
	
	public static long getAvailMem(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		
		return outInfo.availMem;

	}
	
	
	/**
	 * ��ȡ�ֻ��ܵ�ʣ���ڴ�
	 * @return 
	 */
	
	public static long getTotalMem(Context context){
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		MemoryInfo outInfo = new MemoryInfo();
//		am.getMemoryInfo(outInfo);
//		
//		return outInfo.availMem;
        //MemTotal
		StringBuilder sb;
		try {
			File file = new File("/proc/meminfo");
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = br.readLine();
			sb = new StringBuilder();
			for(char c :line.toCharArray()){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
		} catch (Exception e) {
		
			e.printStackTrace();
			return 0;
		} 
		
		return Long.parseLong(sb.toString())*1024;
	}
	
	
}
