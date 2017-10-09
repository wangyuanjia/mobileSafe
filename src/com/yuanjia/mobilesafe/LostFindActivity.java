package com.yuanjia.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LostFindActivity extends Activity {

	
	private static final String TAG = "LostFindActivity";
	private SharedPreferences sp;
	TextView tv_lost_find_phone;
	ImageView iv_protecting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
	//	Editor editor = sp.edit();
        Boolean configed = sp.getBoolean("configed", false);
		if(configed){
		    setContentView(R.layout.activity_lost_find);
		    tv_lost_find_phone = (TextView) findViewById(R.id.tv_lost_find_phone);
		    iv_protecting = (ImageView) findViewById(R.id.iv_protecting);
		    
		    String safeNumber = sp.getString("safeNumber", null);
			Log.i(TAG, safeNumber);
			if(TextUtils.isEmpty(safeNumber)){
				Toast.makeText(this, "安全号码还没设置", 0).show();
				return ;
			}else{
				tv_lost_find_phone.setText(safeNumber);
			}
			
			boolean checked4 = sp.getBoolean("checked4", false);
			if(checked4){
				iv_protecting.setImageResource(R.drawable.lock);
			}else{
				iv_protecting.setImageResource(R.drawable.unlock);
			}
			
		}else{
			Intent intent = new Intent(this,Setup1Activity.class);
			startActivity(intent);
			finish();
		}
      
		
		
		
	}
	
	/**
	 * 重新进入设置向导界面
	 * @param view
	 */
	public void reEnterUpdate(View view){
		Intent intent = new Intent(this, Setup1Activity.class);
		startActivity(intent);
		finish();
		
	}
	
}
