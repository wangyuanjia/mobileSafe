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
		
		//��ȡ֮ǰ�����SIM����Ϣ
		String saveSim = sp.getString("sim", null)+"afu";
		//��ȡ��ǰ��SIM����Ϣ
		String realSim = tm.getSimSerialNumber();
		//�Ƚ����ߵ���Ϣ
		if(saveSim.equals(realSim)){
			//�Ƿ���ͬһ����
			Toast.makeText(context, "sim���ѱ��", 1).show();
		}
	}

	
	
}
