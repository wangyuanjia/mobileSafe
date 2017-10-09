package com.yuanjia.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.yuanjia.mobilesafe.domain.TaskInfo;

  /**
   * �ṩ�ֻ�����Ľ�����Ϣ
   * @author Administrator
   *
   */
public class TaskInfoProvider {

	static List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
	public static List<TaskInfo> getTaskInfos(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		 for(RunningAppProcessInfo info : processInfos){
	      TaskInfo taskInfo = new TaskInfo();
			 
			 //Ӧ�ó���İ���
			 String packname = info.processName;
			 
			 android.os.Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{info.pid});
			 long memsize = memoryInfos[0].getTotalPrivateDirty()*1024l;
			 
			 try {
			 	 
				ApplicationInfo applicationInfo = pm.getApplicationInfo(packname, 0);
				Drawable icon = applicationInfo.loadIcon(pm);
				String name = applicationInfo.loadLabel(pm).toString();
		        
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
					//�û�����
					taskInfo.setUserTask(true);
				}else{
					//ϵͳ����
					taskInfo.setUserTask(false);
				}
			 
				taskInfo.setPackname(packname);
				taskInfo.setName(name);
				taskInfo.setIcon(icon);
				taskInfo.setMemsize(memsize);
				
			 
			 } catch (Exception e) {
				e.printStackTrace();
			}
			 taskInfos.add(taskInfo);
		 }
		
		return taskInfos;
		
	}
	
}
