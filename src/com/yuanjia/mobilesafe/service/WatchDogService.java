package com.yuanjia.mobilesafe.service;

import java.util.List;

import com.yuanjia.mobilesafe.EnterPwdActivity;
import com.yuanjia.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class WatchDogService extends Service {

	private ActivityManager am;
	private boolean flag;
	private AppLockDao dao;
	private InnerReceiver innerReceiver;
	private String tempStopProtectPackname;
	
	private List<String> protectPacknames;
	private Intent intent;
	private DataChangeReceiver dataChangeReceiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	

	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("临时保护--------------------------------------------");
			
			tempStopProtectPackname = intent.getStringExtra("packname");
			
		}
	}
	
	private class  DataChangeReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			protectPacknames = dao.findAll();
			
		}
		
		
	} 
	
	@Override
	public void onCreate() {
	//	tempStopProtectPackname = null;
		innerReceiver = new InnerReceiver();
		registerReceiver(innerReceiver, new IntentFilter("com.yuanjia.mobilesafe.tempstop"));
		
		dataChangeReceiver = new DataChangeReceiver();
		registerReceiver(dataChangeReceiver, new IntentFilter("com.yuanjia.mobilesafe"));
		
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        flag = true;
        dao = new AppLockDao(this);
        protectPacknames = dao.findAll();
        

     	//当前应有需要保护，弹出一个密码的界面
     	intent = new Intent(WatchDogService.this,EnterPwdActivity.class);
     	//服务应用没有任务栈信息的，在服务开启activity，要指定这个activity运行的任务栈
     	intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        
        new Thread(){
        	public void run() {
        	while(flag){
        		List<RunningTaskInfo> infos = am.getRunningTasks(1);
     	        String packname = infos.get(0).topActivity.getPackageName();
    	//        System.out.println("当前用户操作的应用程序:"+packname);
     	        if(protectPacknames.contains(packname)){//查询数据库太慢，改查内存
     	        	//判断程序是否是临时停止保护 
     	        	if(packname.equals(tempStopProtectPackname)){
     	        	//	tempStopProtectPackname = null;
     	        		System.out.println("-----------------------------------");
     	        	}else{
     	        	
     	        	//设置要保护程序的包名
     	        	intent.putExtra("packname", packname);
     	        	startActivity(intent);
     	        	}
     	        }
     	        
     	        try {
     				Thread.sleep(20);
     			} catch (InterruptedException e) {
     				e.printStackTrace();
     			}
        	}
        	};
        }.start();
        super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(innerReceiver);
		innerReceiver = null;
		
		unregisterReceiver(dataChangeReceiver);
		dataChangeReceiver = null;
		super.onDestroy();
	}
}
