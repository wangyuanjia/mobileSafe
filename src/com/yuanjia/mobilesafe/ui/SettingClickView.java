package com.yuanjia.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuanjia.mobilesafe.R;


/**
 * 
 * �����Զ��������ļ�����������textview��һ��imageView��һ��view
 * @author Administrator
 *
 */
public class SettingClickView extends RelativeLayout {
	
//	private CheckBox cb_status;
	private TextView tv_title;
	private TextView tv_desc;
	
	private String desc_on; 
	private String desc_off;
	
	/*
	 * ��ʼ�������ļ�
	 */
	private void iniView(Context context) {
		//��һ�������ļ����ص�settingItemView��
		View.inflate(context, R.layout.setting_click_view, this);
//		cb_status = (CheckBox) findViewById(R.id.cb_status);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
	}

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	/**
	 * ���������ķ����������ļ����õ�ʱ��ʹ��
	 * @param context
	 * @param attrs
	 */

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.yuanjia.mobilesafe", "title");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.yuanjia.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.yuanjia.mobilesafe", "desc_off");
		tv_title.setText(title);
		tv_desc.setText(desc_off);
	}

	public SettingClickView(Context context) {
		super(context);
		iniView(context);
	}

//	public boolean isChecked(){
//		return cb_status.isChecked();
//		
//	}
//	 
//	public void setChecked(boolean checked){
//		if(checked){
//			tv_desc.setText(desc_on);
//		}else{
//			tv_desc.setText(desc_off);
//		}
//		cb_status.setChecked(checked);
//	}

	
	
	public void setDesc(String text){
		tv_desc.setText(text);
	}

//	public void setTitle(String text){
//		tv_title.setText(text);
//	}
}
