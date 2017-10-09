package com.yuanjia.mobilesafe.service;



import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanjia.mobilesafe.R;
import com.yuanjia.mobilesafe.db.dao.NumberAddressQueryUtils;

public class AddressService extends Service {
	
	/**
	 * ���������
	 */
	private WindowManager wm;
	private View view;

	/**
	 * �绰����
	 */

	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	
	private OutCallReceiver receiver;
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// ����������ڲ���
	//�㲥�����ߵ��������ںͷ���һ��
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ����������õ��Ĳ���ȥ�ĵ绰����
			String phone = getResultData();
			// ��ѯ���ݿ�
			String address = NumberAddressQueryUtils.queryNumber(phone);
			
//			Toast.makeText(context, address, 1).show();
			myToast(address);
		}

	}

	private class MyListenerPhone extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// state��״̬��incomingNumber���������
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ������������
				// ��ѯ���ݿ�Ĳ���
				String address = NumberAddressQueryUtils
						.queryNumber(incomingNumber);
				
//				Toast.makeText(getApplicationContext(), address, 1).show();
				myToast(address);

				break;
				
			case TelephonyManager.CALL_STATE_IDLE://�绰�Ŀ���״̬���ҵ绰������ܾ�
				//�����View�Ƴ�
				if(view != null ){
					wm.removeView(view);
				}
			
				
				break;

			default:
				break;
			}
		}

	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "��������", 1).show();
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		// ��������
		listenerPhone = new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);
		
		// �ô���ȥע��㲥������
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);
		
		// ʵ��������
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	/**
	 * �Զ�����˾
	 * @param address
	 */
	private  WindowManager.LayoutParams params;
    long[] mHits = new long[2];
	public void myToast(String address) {
	     view =   View.inflate(this, R.layout.address_show, null);
	    TextView textview  = (TextView) view.findViewById(R.id.tv_address);
	    //view��һ������¼�
	    view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
			      mHits[mHits.length-1] = SystemClock.uptimeMillis();
			      if (mHits[0] >= (SystemClock.uptimeMillis()-500)) {
			         //˫������
			    	  params.x =wm.getDefaultDisplay().getWidth()/2 - view.getWidth()/2;
			          wm.updateViewLayout(view, params);
			          //��¼λ��
			          Editor editor = sp.edit();
						editor.putInt("lastX", params.x);
						
						editor.commit();
				
			      }
			}
		});
	    //��view��������һ�������ļ�����
	    view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN://��ָ������Ļ
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE://��ָ����Ļ���ƶ�
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					
					int dx = newX - startX;
					int dy = newY - startY;
					
					//���´����λ��
					if(params.x<0){
						params.x = 0;
					}
					if(params.y<0){
						params.y = 0;
					}
					if(params.x>(wm.getDefaultDisplay().getWidth()-view.getWidth())){
						params.x = wm.getDefaultDisplay().getWidth()-view.getWidth();
					}
					if(params.y>(wm.getDefaultDisplay().getHeight()-view.getHeight())){
						params.y = wm.getDefaultDisplay().getHeight()-view.getHeight();
					}
					params.x+=dx;
					params.y+=dy;
					wm.updateViewLayout(view, params);
					//���³�ʼ�������λ��
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP://��ָ�뿪��Ļ
					//��¼�ؼ��������ϵ�����
					Editor editor = sp.edit();
					editor.putInt("lastX", params.x);
					editor.putInt("lastY", params.y);
					editor.commit();
					break;
				default:
					break;
				}
				return false;
			}
		});
	    
	    //"��͸��","������","��ʿ��","������","ƻ����"
	    int [] ids = {R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue
	    ,R.drawable.call_locate_gray,R.drawable.call_locate_green};
	    sp = getSharedPreferences("config", MODE_PRIVATE);
	    view.setBackgroundResource(ids[sp.getInt("which", 0)]);
	    textview.setText(address);
		//����Ĳ��������ú���
		 params = new WindowManager.LayoutParams();
		 
         params.height = WindowManager.LayoutParams.WRAP_CONTENT;
         params.width = WindowManager.LayoutParams.WRAP_CONTENT;
         params.gravity = Gravity.TOP+Gravity.LEFT;
         //���봰������ϵĶ�������100
         params.x = sp.getInt("lastX", 0);
         params.y = sp.getInt("lastY", 0);
         
         params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                 | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
         params.format = PixelFormat.TRANSLUCENT;
         //Androidϵͳ��һ�ֵ绰���ȼ��Ĵ������ͣ��ǵ����Ȩ��
         params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);
		
//		
//		 //����֮��ر�
//		new Thread(){
//    		public void run() {
//    			try {
//					Thread.sleep(9000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    			
//    		};
//    		}.start();
//    		
//    		if(view != null ){
//				wm.removeView(view);
//			}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ȡ����������
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone = null;
		
		//�ô���ȡ��ע��㲥������
		unregisterReceiver(receiver);
		receiver = null;

	}

}
