package com.yuanjia.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;
import android.telephony.SmsMessage;
import android.util.Log;

public class CopyOfSmsReceiver extends BroadcastReceiver {


	SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {

		//接受短信
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("config1", context.MODE_PRIVATE);
		
		for(Object b:objs){
			
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
			//发送者
			String sender = sms.getDisplayOriginatingAddress();
			String body = sms.getMessageBody();
			String safeNunber = sp.getString("safeNumber", null);
			
			if (sender.contains(safeNunber)) {
				
				if ("location".equals(body)) {
					
				} else if ("alarm".equals(body)) {
					
				} else if("wipeData".equals(body)){

				} else if("lockScreen".equals(body)){
					
				}

				
				
			}
			
		}
		
	}

}
