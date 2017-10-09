package com.yuanjia.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.yuanjia.mobilesafe.service.AddressService;
import com.yuanjia.mobilesafe.service.CallSmsSafeService;
import com.yuanjia.mobilesafe.service.WatchDogService;
import com.yuanjia.mobilesafe.ui.SettingClickView;
import com.yuanjia.mobilesafe.ui.SettingItemView;
import com.yuanjia.mobilesafe.utils.ServiceUtils;

public class SettingActivity extends Activity {
    //设置是否开启自动更新
	private SettingItemView  siv_update;
	private SharedPreferences sp; 
	
	//设置是否开启显示归属地
	private SettingItemView siv_show_address;
	private Intent showAddress;

	//设置归属地显示框背景
	private SettingClickView scv_changebg;
	
	//黑名单拦截设置
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;
	
	//程序锁看门狗设置
	private SettingItemView siv_watch_dog;
	private Intent watchDogIntent;
	
	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "有没有服务！", 1).show();
		callSmsSafeIntent = new Intent(SettingActivity.this,CallSmsSafeService.class);
		watchDogIntent = new Intent(SettingActivity.this,WatchDogService.class);
		 showAddress = new Intent(SettingActivity.this,AddressService.class);
	        Boolean isServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.yuanjia.mobilesafe.service.AddressService");  
	        if(isServiceRunning){
	        	//监听地址的服务是开启的
	        	siv_show_address.setChecked(true);
	        }else{
	        	siv_show_address.setChecked(false);
	        }
	        
	        Boolean isCallSmsRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.yuanjia.mobilesafe.service.CallSmsSafeService");  
	        if(isCallSmsRunning){
	        	//监听来电的服务是开启的
	        	siv_callsms_safe.setChecked(true);
	        }else{
	        	siv_callsms_safe.setChecked(false);
	        }
	        
	        Boolean isWatchDogRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.yuanjia.mobilesafe.service.WatchDogService");  
	        if(isWatchDogRunning){
	        	//监听看门狗的服务是开启的
	        	siv_watch_dog.setChecked(true);
	        }else{
	        	siv_watch_dog.setChecked(false);
	        }
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        
      
        
        
        
        
        
      
        //初始设置是否开启自动更新
        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        
        Boolean update = sp.getBoolean("update", false);
        if(update){
        	//自动升级已经开启
        	siv_update.setChecked(true);
			//siv_update.setDesc("自动升级已经开启");
        }else{
        	//自动升级已经关闭
			siv_update.setChecked(false);
			//siv_update.setDesc("自动升级已经关闭");
        	
        }
        siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				if(siv_update.isChecked()){
					siv_update.setChecked(false);
					//siv_update.setDesc("自动升级已经关闭");
					editor.putBoolean("update", false);
				}else{
					siv_update.setChecked(true);
					//siv_update.setDesc("自动升级已经开启");
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
        
        
        
        
        
        
        
        
        //设置号码归属地显示控件
        siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
       
        
        
        siv_show_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(siv_show_address.isChecked()){
					//变为非选中状态
					siv_show_address.setChecked(false);
					stopService(showAddress);
				}else{
					//变为选中状态
					siv_show_address.setChecked(true);
					startService(showAddress);
				}
			}
		});
        
        
        
        
        
        
        
      //设置号码归属地的背景
      		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
//      		scv_changebg.setTitle("归属地提示框风格");
       		final String[] items = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
      		final int which = sp.getInt("which", 0);
      		scv_changebg.setDesc(items[which]);
      		
      		scv_changebg.setOnClickListener(new OnClickListener() {
      			
      			@Override
      			public void onClick(View v) {
 //     				int dd = sp.getInt("which", 0);
      				// 弹出一个对话框
      				AlertDialog.Builder builder = new Builder(SettingActivity.this);
      				builder.setTitle("归属地提示框风格");
      				builder.setSingleChoiceItems(items,which, new DialogInterface.OnClickListener() {
      					
      					@Override
      					public void onClick(DialogInterface dialog, int which) {
      						
      						//保存选择参数
      						Editor editor = sp.edit();
      						editor.putInt("which", which);
      						editor.commit();
      						scv_changebg.setDesc(items[which]);
      						
      						//取消对话框
      						dialog.dismiss();
      					}
      				});
      				builder.setNegativeButton("cancel", null);
      				builder.show();
      				
      			}
      		});
        
        //黑名单拦截设置
      	
      		siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
           
            
                      
      		siv_callsms_safe.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				
    				if(siv_callsms_safe.isChecked()){
    					//变为非选中状态
    					siv_callsms_safe.setChecked(false);
    					stopService(callSmsSafeIntent);
    				}else{
    					//变为选中状态
    					siv_callsms_safe.setChecked(true);
    					startService(callSmsSafeIntent);
    				}
    			}
    		});
            
  		
  		 //程序锁设置
      	
      		siv_watch_dog = (SettingItemView) findViewById(R.id.siv_watch_dog);
       
        
        
      		siv_watch_dog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(siv_watch_dog.isChecked()){
					//变为非选中状态
					siv_watch_dog.setChecked(false);
					stopService(watchDogIntent);
				}else{
					//变为选中状态
					siv_watch_dog.setChecked(true);
					startService(watchDogIntent);
				}
			}
		});
      		
	}
	
	
}
