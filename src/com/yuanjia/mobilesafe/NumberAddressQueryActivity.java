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
	 * 系统提供的振动服务
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
			 * 文本发生变化的时候回调
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//查询数据并返回结果
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
	 * 查询号码归属地
	 * @param view
	 */
	
	public void numberAddressQuery(View view){
		String phone = et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone)){
			Toast.makeText(this, "号码为空", 0).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			
//			vibrator.vibrate(2000);
			long[] pattern = {200,200,300,300,1000,2000};
			vibrator.vibrate(pattern, -1);
//			shake.setInterpolator(new Interpolator() {
//				float y;
//				@Override
//				public float getInterpolation(float x) {
//					//函数的运算
//					y = (float) (x*Math.sin(x));
//					return y;
//				}
//			});
			et_phone.startAnimation(shake);
			return;
		}else{
			String address = NumberAddressQueryUtils.queryNumber(phone);
			result.setText(address);
			//去数据库查询号码归属地
			//1.网络查询 、 2.本地数据库---数据库
			//写一个工具类。去查询数据库
			Log.i("NumberAddressQueryActivity", "您要查询的电话号码=="+phone);
		}
		
		
	}
}
