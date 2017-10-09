package com.yuanjia.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.yuanjia.mobilesafe.R;
import com.yuanjia.mobilesafe.service.GPSService;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	private SharedPreferences sp;
	private DevicePolicyManager dpm;

	@Override
	public void onReceive(Context context, Intent intent) {

		dpm = (DevicePolicyManager) context
				.getSystemService(context.DEVICE_POLICY_SERVICE);
		// 写接受短信的代码
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);

		for (Object b : objs) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) b);
			// 发送者
			String sender = sms.getOriginatingAddress();
			String body = sms.getMessageBody();
			String safeNumber = sp.getString("safeNumber", null);

			if (sender.contains(safeNumber)) {

				if ("#*location*#".equals(body)) {
					Log.i(TAG, "得到手机的GPS");
					// 启动服务
					Intent intent1 = new Intent(context, GPSService.class);
					context.startService(intent1);
					sp = context.getSharedPreferences("config",
							context.MODE_PRIVATE);
					String lastLocation = sp.getString("lastLocation", null);
					if (TextUtils.isEmpty(lastLocation)) {
						// 位置没有得到
						SmsManager.getDefault().sendTextMessage(sender, null,
								"getting location....", null, null);

					} else {
						SmsManager.getDefault().sendTextMessage(sender, null,
								lastLocation, null, null);

					}
					// 把这个广播终止
					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					Log.i(TAG, "播放报警音乐");

					MediaPlayer player = MediaPlayer.create(context, R.raw.e);
					player.setLooping(false);
					player.setVolume(1.0f, 1.0f);
					player.start();

					abortBroadcast();
				} else if ("#*wipedata*#".equals(body)) {
					Log.i(TAG, "远程清楚数据");
					// 创建一个Intent
					Intent intent1 = new Intent(
							DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
					// 我要激活谁
					ComponentName mDeviceAdminSample = new ComponentName(
							context, MyAdmin.class);
					intent1.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
							mDeviceAdminSample);
					// 劝说用户开启管理员权限
					intent1.putExtra(
							DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN, "姐，开开");
					context.startActivity(intent1);

					ComponentName who = new ComponentName(context,
							MyAdmin.class);
					if (dpm.isAdminActive(who)) {
						dpm.lockNow();
						dpm.resetPassword("123", 0);
					} else {

						return;
					}
					abortBroadcast();
				} else if ("#*lockscreen*#".equals(body)) {
					Log.i(TAG, "远程锁屏");
					abortBroadcast();
				}
			}

		}
	}

}
