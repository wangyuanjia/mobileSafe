package com.yuanjia.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {

	EditText et_setup3_phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_setup3_phone = (EditText) findViewById(R.id.et_setup3_phone);
		et_setup3_phone.setText(sp.getString("safeNumber", null));
		
	}
	


	@Override
	public void showNext() {
		String phone = et_setup3_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "��ȫ���뻹û����", 0).show();
			return ;
		}
		
		
		//����һ�°�ȫ����
		Editor editor = sp.edit();
		editor.putString("safeNumber", phone);
		editor.commit();
		
		
		
		Intent intent = new Intent(this,Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}

	@Override
	public void showPre() {
		//Ư������������
		String phone = et_setup3_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "��ȫ���뻹û����", 0).show();
			return ;
		}
		
		
		//����һ�°�ȫ����
		Editor editor = sp.edit();
		editor.putString("safeNumber", phone);
		editor.commit();
		
		
		
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
		
	}
	
	public void selectContact(View view){
		Intent intent = new Intent(this,SelectContactActivity.class);
		startActivityForResult(intent, 0);
		
	}
	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		
//		if(data == null)
//			return ;
//		
//		String phone = data.getStringExtra("phone").replace("-", "");
//		et_setup3_phone.setText(phone);
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data == null)
			return ;
		
		
		
		et_setup3_phone.setText(data.getStringExtra("phone").replace("-", ""));
	}
}
