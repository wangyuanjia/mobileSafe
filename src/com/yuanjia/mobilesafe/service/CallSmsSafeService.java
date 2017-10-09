package com.yuanjia.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.yuanjia.mobilesafe.db.dao.BlackNumberDao;

public class CallSmsSafeService extends Service {

	private InnerSmsReceiver receiver;
	private BlackNumberDao dao ;
	private TelephonyManager tm;
	private MyListener listener ;
	
	@Override
	public IBinder onBind(Intent intent) {


		return null;
	}

	private class InnerSmsReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//检查发件人是否是黑名单号码，设置短信拦截全部拦截
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for(Object obj:objs){
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
	        //得到发件人
			String sender = smsMessage.getOriginatingAddress();
			String result = dao.findMode(sender);
			if("2".equals(result)||"3".equals(result)){
				abortBroadcast();
			}
			
			//演示代码
			String body = smsMessage.getMessageBody();
			if(body.contains("fapiao")){
				System.out.println("拦截发票短信");
				abortBroadcast();
			}
		}	
			
		}
				
	
	}
	
	@Override
	public void onCreate() {	
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		dao = new BlackNumberDao(this);
		receiver = new InnerSmsReceiver();
		
		registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	
		 
	}
	
	
	
	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	
	}

	class MyListener  extends PhoneStateListener{
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String result = dao.findMode(incomingNumber);
				if("1".equals(result)||"3".equals(result)){
					System.out.println("挂断电话");
					endCall();//另外一个进程里面运行的远程服务的方法
					//删除呼叫记录
					//另外一个应用程序联系人的应用的私有数据
					//deleteCallLog(incomingNumber);
					//观察呼叫记录数据库内容的变化
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new CallLogObserver(incomingNumber, new Handler())
					);
				}
				break;

			default:
				break;
			}
		}

	

		private class CallLogObserver extends ContentObserver {
            String incomingNumber;
			public CallLogObserver(String incomingNumber,Handler handler) {
				super(handler);
				this.incomingNumber = incomingNumber;
				deleteCallLog(incomingNumber);
			}

			@Override
			public void onChange(boolean selfChange) {
				
				super.onChange(selfChange);
				//观察到数据库改变了，就删除呼叫记录
				deleteCallLog(incomingNumber);
			}
			
			
		}



		private void endCall() {
//          //加载servicemanager的细节码
//			Class clazz = CallSmsService.class.getClassLoader().loadClass("android.os.ServiceManager");
//            Method method = clazz.getDeclaredMethod("getService", String.class);
//			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
//			ITelephony.Stub.asInterface(ibinder).endCall();
		}
	}

	/**
	 * 利用内容提供者删除呼叫记录
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {
		ContentResolver resolver = getContentResolver();
		//呼叫记录的路径
		Uri uri = Uri.parse("content://call_log/calls");
		resolver.delete(uri, "number = ?", new String[]{incomingNumber});
		
		
	}
	
	
	
}
