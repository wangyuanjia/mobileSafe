package com.yuanjia.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public  class Setup4Activity extends BaseSetupActivity {

//	SharedPreferences sp;
	CheckBox cb_setup4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_setup4 = (CheckBox) findViewById(R.id.cb_setup4);
//		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean checked4 = sp.getBoolean("checked4", false);
		if(checked4){
			cb_setup4.setChecked(true);
			cb_setup4.setText("您已经开启防盗保护");
		}else{
			cb_setup4.setChecked(false);
			cb_setup4.setText("您还没有开启防盗保护");
		}
		cb_setup4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Editor editor = sp.edit();
				if(isChecked){
					cb_setup4.setChecked(true);
					
					editor.putBoolean("checked4", true);
					
					cb_setup4.setText("您已经开启防盗保护");
				}else{
				
					cb_setup4.setChecked(false);
					
					editor.putBoolean("checked4", false);
					
					cb_setup4.setText("您还没有开启防盗保护");
				}
				editor.commit();
			}
		});

		
		
	}
	


	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		
		Editor editor = sp.edit();
		editor.putBoolean("configed",true);
		editor.commit();
		Intent intent = new Intent(this,LostFindActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
	
}
