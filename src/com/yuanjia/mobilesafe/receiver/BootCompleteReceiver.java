package com.yuanjia.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver{

	SharedPreferences sp;
	TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		
		//读取之前保存的SIM卡信息
		String saveSim = sp.getString("sim", null)+"afu";
		//读取当前的SIM卡信息
		String realSim = tm.getSimSerialNumber();
		//比较两者的信息
		if(saveSim.equals(realSim)){
			//是否是同一哥们
			Toast.makeText(context, "sim卡已变更", 1).show();
		}
	}

	
	
}
