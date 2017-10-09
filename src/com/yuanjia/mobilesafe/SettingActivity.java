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
    //�����Ƿ����Զ�����
	private SettingItemView  siv_update;
	private SharedPreferences sp; 
	
	//�����Ƿ�����ʾ������
	private SettingItemView siv_show_address;
	private Intent showAddress;

	//���ù�������ʾ�򱳾�
	private SettingClickView scv_changebg;
	
	//��������������
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;
	
	//���������Ź�����
	private SettingItemView siv_watch_dog;
	private Intent watchDogIntent;
	
	@Override
	protected void onResume() {
		super.onResume();
		Toast.makeText(this, "��û�з���", 1).show();
		callSmsSafeIntent = new Intent(SettingActivity.this,CallSmsSafeService.class);
		watchDogIntent = new Intent(SettingActivity.this,WatchDogService.class);
		 showAddress = new Intent(SettingActivity.this,AddressService.class);
	        Boolean isServiceRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.yuanjia.mobilesafe.service.AddressService");  
	        if(isServiceRunning){
	        	//������ַ�ķ����ǿ�����
	        	siv_show_address.setChecked(true);
	        }else{
	        	siv_show_address.setChecked(false);
	        }
	        
	        Boolean isCallSmsRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.yuanjia.mobilesafe.service.CallSmsSafeService");  
	        if(isCallSmsRunning){
	        	//��������ķ����ǿ�����
	        	siv_callsms_safe.setChecked(true);
	        }else{
	        	siv_callsms_safe.setChecked(false);
	        }
	        
	        Boolean isWatchDogRunning = ServiceUtils.isServiceRunning(SettingActivity.this, "com.yuanjia.mobilesafe.service.WatchDogService");  
	        if(isWatchDogRunning){
	        	//�������Ź��ķ����ǿ�����
	        	siv_watch_dog.setChecked(true);
	        }else{
	        	siv_watch_dog.setChecked(false);
	        }
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        
      
        
        
        
        
        
      
        //��ʼ�����Ƿ����Զ�����
        siv_update = (SettingItemView) findViewById(R.id.siv_update);
        
        Boolean update = sp.getBoolean("update", false);
        if(update){
        	//�Զ������Ѿ�����
        	siv_update.setChecked(true);
			//siv_update.setDesc("�Զ������Ѿ�����");
        }else{
        	//�Զ������Ѿ��ر�
			siv_update.setChecked(false);
			//siv_update.setDesc("�Զ������Ѿ��ر�");
        	
        }
        siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				if(siv_update.isChecked()){
					siv_update.setChecked(false);
					//siv_update.setDesc("�Զ������Ѿ��ر�");
					editor.putBoolean("update", false);
				}else{
					siv_update.setChecked(true);
					//siv_update.setDesc("�Զ������Ѿ�����");
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});
        
        
        
        
        
        
        
        
        //���ú����������ʾ�ؼ�
        siv_show_address = (SettingItemView) findViewById(R.id.siv_show_address);
       
        
        
        siv_show_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(siv_show_address.isChecked()){
					//��Ϊ��ѡ��״̬
					siv_show_address.setChecked(false);
					stopService(showAddress);
				}else{
					//��Ϊѡ��״̬
					siv_show_address.setChecked(true);
					startService(showAddress);
				}
			}
		});
        
        
        
        
        
        
        
      //���ú�������صı���
      		scv_changebg = (SettingClickView) findViewById(R.id.scv_changebg);
//      		scv_changebg.setTitle("��������ʾ����");
       		final String[] items = {"��͸��","������","��ʿ��","������","ƻ����"};
      		final int which = sp.getInt("which", 0);
      		scv_changebg.setDesc(items[which]);
      		
      		scv_changebg.setOnClickListener(new OnClickListener() {
      			
      			@Override
      			public void onClick(View v) {
 //     				int dd = sp.getInt("which", 0);
      				// ����һ���Ի���
      				AlertDialog.Builder builder = new Builder(SettingActivity.this);
      				builder.setTitle("��������ʾ����");
      				builder.setSingleChoiceItems(items,which, new DialogInterface.OnClickListener() {
      					
      					@Override
      					public void onClick(DialogInterface dialog, int which) {
      						
      						//����ѡ�����
      						Editor editor = sp.edit();
      						editor.putInt("which", which);
      						editor.commit();
      						scv_changebg.setDesc(items[which]);
      						
      						//ȡ���Ի���
      						dialog.dismiss();
      					}
      				});
      				builder.setNegativeButton("cancel", null);
      				builder.show();
      				
      			}
      		});
        
        //��������������
      	
      		siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
           
            
                      
      		siv_callsms_safe.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				
    				if(siv_callsms_safe.isChecked()){
    					//��Ϊ��ѡ��״̬
    					siv_callsms_safe.setChecked(false);
    					stopService(callSmsSafeIntent);
    				}else{
    					//��Ϊѡ��״̬
    					siv_callsms_safe.setChecked(true);
    					startService(callSmsSafeIntent);
    				}
    			}
    		});
            
  		
  		 //����������
      	
      		siv_watch_dog = (SettingItemView) findViewById(R.id.siv_watch_dog);
       
        
        
      		siv_watch_dog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(siv_watch_dog.isChecked()){
					//��Ϊ��ѡ��״̬
					siv_watch_dog.setChecked(false);
					stopService(watchDogIntent);
				}else{
					//��Ϊѡ��״̬
					siv_watch_dog.setChecked(true);
					startService(watchDogIntent);
				}
			}
		});
      		
	}
	
	
}
