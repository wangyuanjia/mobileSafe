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
	 * 获取正在运行的进程的数量
	 * @param context
	 * @return
	 */
	public static int getRunningProcessCount(Context context){
	    //packageManager //包管理器 相当于程序管理器 静态的内容
		//ActivityManager 进程管理器，管理手机的活动信息 动态的内容
		
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		 List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		
		return infos.size();
		
	}
	
	/**
	 * 获取手机可用的剩余内存
	 * @return
	 */
	
	public static long getAvailMem(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		
		return outInfo.availMem;

	}
	
	
	/**
	 * 获取手机总的剩余内存
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
