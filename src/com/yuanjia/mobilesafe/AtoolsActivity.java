package com.yuanjia.mobilesafe;

import com.yuanjia.mobilesafe.utils.SmsUtils;
import com.yuanjia.mobilesafe.utils.SmsUtils.BackUpCallBack;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AtoolsActivity extends Activity {

//	ProgressDialog pd ;
	private ProgressBar pb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
		pb = (ProgressBar) findViewById(R.id.progressBar1);
	}
	
	/**
	 * ����¼��������������ز�ѯ��ҳ��
	 * @param view
	 */
	
	public void numberQuery(View view){
		Intent intent = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intent);
		//finish();
	}
	
	

	/**
	 * ���ű���
	 * @param view
	 *
	 */
	public void smsBackUp(View view){
//		pd = new ProgressDialog(this);
//		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//		pd.setMessage("���ڱ��ݶ���");
//		pd.show();
	
		
		new Thread(){
			public void run() {
				try {
					SmsUtils.backUpSms(AtoolsActivity.this,new BackUpCallBack() {
						
						@Override
						public void onSmsBackup(int process) {
							pb.setProgress(process);
							
						}
						
						@Override
						public void beforeBackup(int max) {
							pb.setMax(max);
							
						}
					});
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "���ݳɹ�", 0).show();
//							pd.cancel();
						}
						
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							Toast.makeText(AtoolsActivity.this, "����ʧ��", 0).show();
							
						}
						
					});
				} finally{
//					pd.dismiss();
				}
				
			};
		}.start();
	}
	
	/**
	 * ���Ż�ԭ
	 * @param view
	 */

	public void smsRestore(View view){
	
		SmsUtils.restoreSms(getApplicationContext());
		Toast.makeText(AtoolsActivity.this, "��ԭ�ɹ�", 0).show();
	}
	
}
