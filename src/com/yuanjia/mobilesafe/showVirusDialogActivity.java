package com.yuanjia.mobilesafe;

import com.yuanjia.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class showVirusDialogActivity extends Activity {

	private AlertDialog dialog;
	private Button uninstall;
	private Button cancel;
	private String virusPackage;
	private String appName;
	private TextView tv_virus_name1;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		virusPackage = intent.getPackage();
		appName = intent.getStringExtra("appName");
		System.out.println("111111111"+virusPackage);
		System.out.println("111111111"+appName);
		showVirusDialog();
	}

	private void showVirusDialog() {

		AlertDialog.Builder builder = new Builder(showVirusDialogActivity.this);
		//自定义一个布局文件
		View view = View.inflate(showVirusDialogActivity.this, R.layout.dialog_show_virus, null);
		tv_virus_name1 = (TextView) view.findViewById(R.id.tv_virus_name1);
		uninstall = (Button) view.findViewById(R.id.uninstall);
		cancel = (Button) view.findViewById(R.id.cancel);

		tv_virus_name1.setText(appName);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
		uninstall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//卸载程序
				uninstallApplication();
				//关闭dialog
				dialog.dismiss();
				finish();
			}
		});
		builder.setView(view);
	
		dialog = builder.show();
		
	}
	private void uninstallApplication() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
	    intent.addCategory("android.intent.category.DEFAULT");
	    intent.setData(Uri.parse("package:"+virusPackage));
	    startActivityForResult(intent, 0);
	}
}
