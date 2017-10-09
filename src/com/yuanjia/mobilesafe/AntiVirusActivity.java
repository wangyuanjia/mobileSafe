package com.yuanjia.mobilesafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.yuanjia.mobilesafe.db.dao.AntivirusDao;	
import com.yuanjia.mobilesafe.domain.ScanInfo;
import com.yuanjia.mobilesafe.engine.AppInfoProvider;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class AntiVirusActivity extends Activity {

	private static final int BEGIN = 0;
	private static final int SCANING = 1;
	protected static final int FINISH = 2;
	private ProgressBar progressBar1;
	private PackageManager pm;
	private TextView tv_scan_status;
	private Message msg;
	private LinearLayout tv_scanning_virus;
	private ImageView iv_scanning;
	private ScrollView scrollView;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BEGIN:
			
				tv_scan_status.setText("��ʼ��ɨ�����档����");
				break;
			case SCANING:
				tv_scan_status.setText("����ɨ���С�����");
				TextView tv_scan = new TextView(AntiVirusActivity.this);
				ScanInfo1 scaninfo = (ScanInfo1) msg.obj;
				if(scaninfo.virus){
					tv_scan.setTextColor(Color.RED);
					tv_scan.setText(scaninfo.name+"    �ǲ���");
					
				}else{
					tv_scan.setTextColor(Color.BLACK);
					tv_scan.setText(scaninfo.name+"    ɨ�氲ȫ");
				}
			  
		
				tv_scanning_virus.addView(tv_scan,0);
				//�Զ�����
				scrollView.post(new Runnable(){

					@Override
					public void run() {

						scrollView.fullScroll(scrollView.FOCUS_FORWARD);
					}
					
				});
				break;
            case FINISH:
            	tv_scan_status.setText("ɨ�����");
            	iv_scanning.clearAnimation();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
	    iv_scanning = (ImageView) findViewById(R.id.iv_scanning);
	    tv_scanning_virus = (LinearLayout) findViewById(R.id.tv_scanning_virus);
	    scrollView = (ScrollView) findViewById(R.id.scrollView);
		
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(4000);
//		rotateAnimation.setRepeatMode(Animation.INFINITE);
		//���ö�������ѭ��
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		iv_scanning.startAnimation(rotateAnimation);
		
//		progressBar1.setMax(100);
//		new Thread() {
//			public void run() {
//				for (int i = 0; i < 100; i++) {
//					try {
//						Thread.sleep(100);
//						progressBar1.setProgress(i);
//						
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			};
//		}.start();

		scanVirus();
	}

	/**
	 * ɨ�財��
	 */
	private void scanVirus() {
	   new Thread(new Runnable() {
		
		@Override
		public void run() {
			
			msg = Message.obtain();
			msg.what = BEGIN;
			handler.sendMessage(msg);
			
			pm = getPackageManager();
//			List<AppInfo> appInfos = AppInfoProvider.getAppInfos(AntiVirusActivity.this);
//			tv_scan_status.setText("���ڳ�ʼ��8��ɱ�����档����");
			List<PackageInfo> infos = pm.getInstalledPackages(0);
//			try {	
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			progressBar1.setMax(infos.size());
			int progress = 0;
			for (PackageInfo info : infos) {
				
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				SystemClock.sleep(10);
				// apk�ļ���������·��
				String sourcedir = info.applicationInfo.sourceDir;
				String md5 = getFileMd5(sourcedir);
				ScanInfo1 scaninfo = new ScanInfo1();
				 System.out.println(info.applicationInfo.loadLabel(pm)+":"+md5);//����
				 System.out.println(info.packageName);
				 scaninfo.name = info.applicationInfo.loadLabel(pm).toString();//����
				 scaninfo.packname = info.packageName;
				// ��ѯMD5��Ϣ���Ƿ��ڲ������ݿ�����
				if (AntivirusDao.isVirus(md5)) {
					// ���ֲ���
					 scaninfo.virus = true;
 
				} else {
					// ɨ�谲ȫ
					 scaninfo.virus = false;
				}

//				Message msg = Message.obtain();
				msg = Message.obtain();
				msg.obj = scaninfo;
				msg.what = SCANING;
				
				handler.sendMessage(msg);
		
				progress++;
				progressBar1.setProgress(progress);

			}
			msg = Message.obtain();
			msg.what = FINISH;
		
			handler.sendMessage(msg);
		}
	}).start();
	   
	   
	}
	
	private class ScanInfo1{
		String name;
		String packname;
		boolean virus;
	}

	/**
	 * ��ȡ�ļ���MD5ֵ
	 * 
	 * @param path
	 * �ļ���ȫ·������
	 * @return
	 */

	public static String getFileMd5(String path) {
		try {
			// ��ȡһ���ļ���������Ϣ��ǩ����Ϣ
			File file = new File(path);
			// ����ժҪ
			MessageDigest digest = MessageDigest.getInstance("sha1");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] result = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				// ������
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				// System.out.println(str)
				if (str.length() == 1) {
					sb.append("0"+str);
				}else{
				    sb.append(str);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
