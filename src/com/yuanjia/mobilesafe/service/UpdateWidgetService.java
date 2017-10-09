package com.yuanjia.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import com.yuanjia.mobilesafe.MyWidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

public class UpdateWidgetService extends Service {

	private Timer timer;
	private AppWidgetManager awm;
	private TimerTask task;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		
		awm = AppWidgetManager.getInstance(this);
		timer = new Timer();
		task = new TimerTask(){

			@Override
			public void run() {
				//设置更新的组件
				ComponentName provider = new ComponentName(UpdateWidgetService.this,MyWidget.class);
		        
			
			}
			
		};
	}
	
	
}
