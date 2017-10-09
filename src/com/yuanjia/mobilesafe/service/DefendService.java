package com.yuanjia.mobilesafe.service;

import java.util.List;

import com.yuanjia.mobilesafe.AntiVirusActivity;
import com.yuanjia.mobilesafe.showVirusDialogActivity;
import com.yuanjia.mobilesafe.db.dao.AntivirusDao;
import com.yuanjia.mobilesafe.receiver.DefendReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.util.Log;

public class DefendService extends Service {

//	private static final String PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
//	private DefendReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		receiver = new DefendReceiver();
//		IntentFilter filter = new IntentFilter(); 
////		filter.addAction(PACKAGE_ADDED); 
//		registerReceiver(receiver, filter); 
		new Thread(new Runnable() {
    		
			@Override
			public void run() {
//				System.out.println("有应用被添加！");
//			  	pm = context.getPackageManager();
//	            packageName = intent.getDataString();  
////	            packageName = intent.getPackage();
//	            Log.i("Test","---------------" + packageName); 
//	  
//				List<PackageInfo> infos = pm.getInstalledPackages(0);
//				for (PackageInfo info : infos) {
//					 Log.i("Test","---------------"+info.packageName ); 
//	        		if(packageName.equals("package:"+info.packageName)){
//	        			System.out.println("-----------1");
//	        			String sourcedir = info.applicationInfo.sourceDir;
//	        			String md5 = AntiVirusActivity.getFileMd5(sourcedir);
//	        			if (!AntivirusDao.isVirus(md5)) {
//	    					// 发现病毒
//	        				System.out.println("-----------");
//	                   
//	                        Intent intentVirus = new Intent(context, showVirusDialogActivity.class);
//	                        intentVirus.setPackage(info.packageName);
//	                        intentVirus.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//	                        intentVirus.putExtra("appName", info.applicationInfo.loadLabel(pm).toString());
//	                        context.startActivity(intentVirus);
//	    				}else{
//	    					
//	    				}
//	        		};
//	        	}
			}
		});

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		unregisterReceiver(receiver);
//		receiver = null;
//		
	}
	
	
	

}
