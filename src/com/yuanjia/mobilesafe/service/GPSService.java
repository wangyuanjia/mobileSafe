package com.yuanjia.mobilesafe.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class GPSService extends Service {

	private static final String TAG = "MainActivity";
	//用到位置服务
	private LocationManager lm ;
	private MyLocationListener listener;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	
	}

	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
	    lm = (LocationManager) getSystemService(LOCATION_SERVICE);
	        
//	        List<String> provider = lm.getAllProviders();
//	        for(String l : provider){
//	        	Log.i(TAG, l);
//	        	Toast.makeText(this, l, 0).show();
//	        }
	        
	        listener = new MyLocationListener();
	        //注册位置服务
	        //给位置提供者设置条件
	        
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        String provider1 = lm.getBestProvider(criteria, false);
	        lm.requestLocationUpdates(provider1, 0, 0, listener);
	}




	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//取消监听位置服务
    	lm.removeUpdates(listener);
    	listener = null;
	}

	
	 class MyLocationListener implements LocationListener {

	    	//当位置改变的时候回调
	    	
			@Override
			public void onLocationChanged(Location location) { 	 	
				// TODO Auto-generated method stub
				String longitude = "经度：" + location.getLongitude() + "\n";
				String latitude = "纬度：" + location.getLatitude() + "\n";
				String accurary = "精度：" + location.getAccuracy() + "\n";
				
				//把GPS坐标转换成火星坐标
				
				try {
					InputStream is = getAssets().open("axisoffset.dat");
					ModifyOffset offset = ModifyOffset.getInstance(is);
					PointDouble double1 =  offset.s2c(new PointDouble(location.getLongitude(), location.getLatitude()));
					longitude = "j:" + offset.X + "\n";
		            latitude = "w:" +offset.Y+ "\n";
					
					SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
					Editor editor = sp.edit(); 
					editor.putString("lastLocation", longitude + latitude + accurary);
					editor.commit();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				
				
			}
			
						
		
			//状态发生改变的时候

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			//某一个
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
	    	
	    }
	
	 
}
