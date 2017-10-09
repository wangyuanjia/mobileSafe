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
	//�õ�λ�÷���
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
	        //ע��λ�÷���
	        //��λ���ṩ����������
	        
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        String provider1 = lm.getBestProvider(criteria, false);
	        lm.requestLocationUpdates(provider1, 0, 0, listener);
	}




	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//ȡ������λ�÷���
    	lm.removeUpdates(listener);
    	listener = null;
	}

	
	 class MyLocationListener implements LocationListener {

	    	//��λ�øı��ʱ��ص�
	    	
			@Override
			public void onLocationChanged(Location location) { 	 	
				// TODO Auto-generated method stub
				String longitude = "���ȣ�" + location.getLongitude() + "\n";
				String latitude = "γ�ȣ�" + location.getLatitude() + "\n";
				String accurary = "���ȣ�" + location.getAccuracy() + "\n";
				
				//��GPS����ת���ɻ�������
				
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
			
						
		
			//״̬�����ı��ʱ��

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			//ĳһ��
			
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
