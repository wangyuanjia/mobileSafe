package com.yuanjia.mobilesafe;

import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CleanCacheActivity extends Activity {
	private static final String TAG = "CleanCacheActivity";
	private ProgressBar progressBar1;
	private TextView tv_scan_status;
	private PackageManager pm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clean_cache);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		
		scanCache();
	}
	/**
	 * 扫描手机里面所有应用程序的缓存信息
	 */
	
	private void scanCache() {
		pm = getPackageManager();
		new Thread(){
			public void run() {
				Method getPackageSizeInfoMethod = null;
				Method[] methods = PackageManager.class.getMethods();
				for(Method method : methods){
				//	System.out.println(method.getName());
					if("getPackageSizeInfo".equals(method.getName())){
						getPackageSizeInfoMethod = method;
					}
				}
				List<PackageInfo> packInfos = pm.getInstalledPackages(0);
				progressBar1.setMax(packInfos.size());
				int progress = 0;
				for(PackageInfo packInfo:packInfos){
			try {
						//		getPackageSizeInfoMethod.invoke(pm, packInfo.packageName,);
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			progress++;
			progressBar1.setProgress(progress);
				}
			}
		}.start();
	}
//	
//	private class MyDataObserver extends IPackageStatsObserver.Stub{
//		
//	
//
//		public void onGetStatsCompleted(PackageStats pStats,boolean succeeded)
//		            throws RemoteException {
//			     long cache = pStats.cacheSize;
//			     long code = pStats.codeSize;
//			     long data = pStats.dataSize;
//			     Log.d(TAG, "cache:"+Formatter.formatFileSize(getApplicationContext(),cache));
//			     Log.d(TAG, "code:"+Formatter.formatFileSize(getApplicationContext(),code));
//			     Log.d(TAG, "data:"+Formatter.formatFileSize(getApplicationContext(),data));
//			     System.out.println("-------------------");
//		}
//		
//	}
//	
	
}
