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
			System.out.println("��ʱ����--------------------------------------------");
			
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
        

     	//��ǰӦ����Ҫ����������һ������Ľ���
     	intent = new Intent(WatchDogService.this,EnterPwdActivity.class);
     	//����Ӧ��û������ջ��Ϣ�ģ��ڷ�����activity��Ҫָ�����activity���е�����ջ
     	intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        
        new Thread(){
        	public void run() {
        	while(flag){
        		List<RunningTaskInfo> infos = am.getRunningTasks(1);
     	        String packname = infos.get(0).topActivity.getPackageName();
    	//        System.out.println("��ǰ�û�������Ӧ�ó���:"+packname);
     	        if(protectPacknames.contains(packname)){//��ѯ���ݿ�̫�����Ĳ��ڴ�
     	        	//�жϳ����Ƿ�����ʱֹͣ���� 
     	        	if(packname.equals(tempStopProtectPackname)){
     	        	//	tempStopProtectPackname = null;
     	        		System.out.println("-----------------------------------");
     	        	}else{
     	        	
     	        	//����Ҫ��������İ���
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
