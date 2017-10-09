package com.yuanjia.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanjia.mobilesafe.db.dao.NumberAddressQueryUtils;

public class NumberAddressQueryActivity extends Activity {

	
	private EditText et_phone;
	private TextView  result;
	
	/**
	 * ϵͳ�ṩ���񶯷���
	 */
	private Vibrator vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_number_address_query);
	    et_phone = (EditText) findViewById(R.id.et_phone);
	    vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
	    et_phone.addTextChangedListener(new TextWatcher() {
			/**
			 * �ı������仯��ʱ��ص�
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//��ѯ���ݲ����ؽ��
				if(s.length()>=3&&(s!=null)){
					String address = NumberAddressQueryUtils.queryNumber(s.toString());
					result.setText(address);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    result =  (TextView) findViewById(R.id.result);
	}
	
	/**
	 * ��ѯ���������
	 * @param view
	 */
	
	public void numberAddressQuery(View view){
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "����Ϊ��", 0).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			
//			vibrator.vibrate(2000);
			long[] pattern = {200,200,300,300,1000,2000};
			vibrator.vibrate(pattern, -1);
//			shake.setInterpolator(new Interpolator() {
//				float y;
//				@Override
//				public float getInterpolation(float x) {
//					//����������
//					y = (float) (x*Math.sin(x));
//					return y;
//				}
//			});
			et_phone.startAnimation(shake);
			return;
		}else{
			String address = NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
			//ȥ���ݿ��ѯ���������
			//1.�����ѯ �� 2.�������ݿ�---���ݿ�
			//дһ�������ࡣȥ��ѯ���ݿ�
			Log.i("NumberAddressQueryActivity", "��Ҫ��ѯ�ĵ绰����=="+phone);
		}
		
		
	}
}
