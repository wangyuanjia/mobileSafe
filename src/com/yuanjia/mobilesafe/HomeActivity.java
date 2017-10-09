package com.yuanjia.mobilesafe;

import com.yuanjia.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	protected static final String TAG = "HomeActivity";
	private GridView list_home;
    private MyAdapter adapter;
    private SharedPreferences sp;
    private static String[] names = {
    	"手机防盗","通讯卫士","软件管理",
    	"进程管理","流量统计","手机杀毒",
    	"缓存清理","高级工具","设置中心"
    	};

    private static int[] ids = {
    	R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
    	R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
    	R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		list_home = (GridView) findViewById(R.id.list_home);
	    adapter = new MyAdapter();
	    list_home.setAdapter(adapter);
	    list_home.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				
				case 0://防盗中心
					showLostFindDialog();
					break;
				case 1://通信卫士
					intent = new Intent(HomeActivity.this, CallSmsSafeActivity.class);
					startActivity(intent);
					break;	
				case 2://软件管理
					intent = new Intent(HomeActivity.this, AppManagerActivity.class);
					startActivity(intent);
					break;	
				case 3://进程管理
					intent = new Intent(HomeActivity.this, TaskManagerActivity.class);
					startActivity(intent);
					break;	
				case 4://流量统计
					intent = new Intent(HomeActivity.this,TrafficManagerActivity.class);
			        startActivity(intent);
				    break;
				case 5://手机杀毒
					intent = new Intent(HomeActivity.this,AntiVirusActivity.class);
			        startActivity(intent);
				    break;
				case 6://清理缓存
					intent = new Intent(HomeActivity.this,CleanCacheActivity.class);
			        startActivity(intent);
				    break;
				case 7://进入高级工具
					intent = new Intent(HomeActivity.this,AtoolsActivity.class);
				    startActivity(intent);
				    break;
				case 8://设置中心
					intent = new Intent(HomeActivity.this,SettingActivity.class); 
					startActivity(intent);
					break;
				default:
					break;
				}
			}
	    	
	    });
	}
	
	
    
	   /**
        * 判断是否设置过密码
        */
	
		protected void showLostFindDialog() {
		//判断是否设置过密码
			if(isSetupPassword()){
				//已经设置过密码，弹出输入对话框
				showEnterDialog();
			}else{
				//未设置密码，弹出设置密码对话框
				showSetupPasswordDialog();
			}
	}

		
		private EditText et_setup_pwd;
		private EditText et_setup_confirm;
		private Button ok;
		private Button cancel;
		private AlertDialog dialog;
		/**
		 * 设置密码对话框
		 */
		private void showSetupPasswordDialog() {
			AlertDialog.Builder builder = new Builder(HomeActivity.this);
			//自定义一个布局文件
			View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
			et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
			et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
			ok = (Button) view.findViewById(R.id.ok);
			cancel = (Button) view.findViewById(R.id.cancel);
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
			ok.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//取出密码
				String 	password = et_setup_pwd.getText().toString().trim();
				String  password_confirm =	et_setup_confirm.getText().toString().trim();
					if(TextUtils.isEmpty(password)||TextUtils.isEmpty(password_confirm)){
						Toast.makeText(HomeActivity.this, "密码为空", 0).show();
						return;						
					}
					
					//判断是否一致才去保存
					if(password.equals(password_confirm)){
						//一致的话，就保存密码，把对话框消掉，还要进入手机防盗页面
						Editor editor = sp.edit();
						editor.putString("password", MD5Utils.md5Password(password));//保存加密后的算法
						editor.commit();
						dialog.dismiss();
						Log.i(TAG, "一致的话，就保存密码，把对话框消掉，还要进入手机防盗页面");
						Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
						startActivity(intent);
					}else{
						Toast.makeText(HomeActivity.this, "密码不一致", 0).show();
						return;
					}
				}
			});
			builder.setView(view);
			dialog = builder.show();
		}

        /**
         * 
         * 输入密码对话框
         */

		private void showEnterDialog() {
		
			AlertDialog.Builder builder = new Builder(HomeActivity.this);
			//自定义一个布局文件
			View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
			et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
			//et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
			ok = (Button) view.findViewById(R.id.ok);
			cancel = (Button) view.findViewById(R.id.cancel);
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
			ok.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//取出密码
				String 	password = et_setup_pwd.getText().toString().trim();
				//String  password_confirm =	et_setup_confirm.getText().toString().trim();
					if(TextUtils.isEmpty(password)){
						Toast.makeText(HomeActivity.this, "密码为空", 0).show();
						return;						
					}
					
					//判断密码是否一致
					String savePassword = sp.getString("password", null);
					if(MD5Utils.md5Password(password).equals(savePassword)){
						//进入主界面，消掉对话框
						dialog.dismiss();
						Log.i(TAG, "进入手机防盗界面");
						Intent intent = new Intent(HomeActivity.this,LostFindActivity.class);
						startActivity(intent);
					}else{
						Toast.makeText(HomeActivity.this, "密码错误", 0).show();
						return;
					}
				}
			});
			builder.setView(view);
			dialog = builder.show();
		}



		/**
		 * 判断是否设置过密码
		 * @return
		 */
		
	    private boolean isSetupPassword(){
	    	String password = sp.getString("password", null);
//	    	if(TextUtils.isEmpty(password)){
//	    		return false;
//	    	}else{
//	    		return true;
//	    	}
	    	return !TextUtils.isEmpty(password);
	    }


		private class MyAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return names.length;
				
			}

			@Override
			public Object getItem(int position) {
				return null;
				
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				View view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
				ImageView iv_item_fuction_image = (ImageView) view.findViewById(R.id.iv_item_fuction_image);
				TextView tv_item_fuction = (TextView) view.findViewById(R.id.tv_item_fuction);
				tv_item_fuction.setText(names[position]);
				iv_item_fuction_image.setImageResource(ids[position]);
				return view;
			}
			
		}
		
	
}
