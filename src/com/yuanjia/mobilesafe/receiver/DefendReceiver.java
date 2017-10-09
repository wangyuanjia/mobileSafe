package com.yuanjia.mobilesafe.receiver;

import java.util.List;

import com.yuanjia.mobilesafe.AntiVirusActivity;
import com.yuanjia.mobilesafe.HomeActivity;
import com.yuanjia.mobilesafe.showVirusDialogActivity;
import com.yuanjia.mobilesafe.db.dao.AntivirusDao;
import com.yuanjia.mobilesafe.service.DefendService;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

public class DefendReceiver extends BroadcastReceiver {

	private PackageManager pm;
	private String TAG = "mainActivity";
	private String packageName;

	@Override
	public void onReceive( Context context, Intent intent) {
		// ��ȡ itent �е� action ����ֵ

		Log.d(TAG, "����װ111111111111111");

		String action = intent.getAction();
		// �жϸù㲥�ǲ��ǰ�װӦ�ó���Ĺ㲥

		if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
			Toast.makeText(context, "��Ӧ�ñ����", Toast.LENGTH_LONG).show();

			// apk�ļ���������·��
			// String sourcedir = info.applicationInfo.sourceDir;
			// String md5 = getFileMd5(sourcedir);

			// ActivityManager activityManager = (ActivityManager)
			// context.getSystemService(Context.ACTIVITY_SERVICE);
			// List<ActivityManager.RunningTaskInfo> tasks =
			// activityManager.getRunningTasks(1);
			// ActivityManager.RunningTaskInfo taskInfo = tasks.get(0);
			// int sourcedir = taskInfo.describeContents();
			// Intent intentVirus = new Intent(context, DefendService.class);
			// context.startService(intentVirus);

			System.out.println("��Ӧ�ñ���ӣ�");
			pm = context.getPackageManager();
			packageName = intent.getDataString();
			// packageName = intent.getPackage();
			Log.i("Test", "---------------" + packageName);

			List<PackageInfo> infos = pm.getInstalledPackages(0);
			for (PackageInfo info : infos) {
				Log.i("Test", "---------------" + info.packageName);
				if (packageName.equals("package:" + info.packageName)) {
					System.out.println("-----------1");
					String sourcedir = info.applicationInfo.sourceDir;
					String md5 = AntiVirusActivity.getFileMd5(sourcedir);
					if (!AntivirusDao.isVirus(md5)) {
						// ���ֲ���
						System.out.println("-----------");

						Intent intentVirus = new Intent(context,
								showVirusDialogActivity.class);
						intentVirus.setPackage(info.packageName);
						intentVirus.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
						intentVirus.putExtra("appName", info.applicationInfo
								.loadLabel(pm).toString());
						context.startActivity(intentVirus);
					} else {

					}
				}
				;
			}
//
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//
//				}
//			});

		} else if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
			Toast.makeText(context, "��Ӧ�ñ�ɾ��", Toast.LENGTH_LONG).show();
		}
		/*
		 * else if(Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())){
		 * Toast.makeText(context, "��Ӧ�ñ��ı�", Toast.LENGTH_LONG).show(); }
		 */
		else if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
			Toast.makeText(context, "��Ӧ�ñ��滻", Toast.LENGTH_LONG).show();
		}
		/*
		 * else if(Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())){
		 * Toast.makeText(context, "��Ӧ�ñ�����", Toast.LENGTH_LONG).show(); }
		 */
		else if (Intent.ACTION_PACKAGE_INSTALL.equals(intent.getAction())) {
			Toast.makeText(context, "��Ӧ�ñ���װ", Toast.LENGTH_LONG).show();

		}

	}

}
