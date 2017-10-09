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
			
				tv_scan_status.setText("初始化扫描引擎。。。");
				break;
			case SCANING:
				tv_scan_status.setText("正在扫描中。。。");
				TextView tv_scan = new TextView(AntiVirusActivity.this);
				ScanInfo1 scaninfo = (ScanInfo1) msg.obj;
				if(scaninfo.virus){
					tv_scan.setTextColor(Color.RED);
					tv_scan.setText(scaninfo.name+"    是病毒");
					
				}else{
					tv_scan.setTextColor(Color.BLACK);
					tv_scan.setText(scaninfo.name+"    扫面安全");
				}
			  
		
				tv_scanning_virus.addView(tv_scan,0);
				//自动滚动
				scrollView.post(new Runnable(){

					@Override
					public void run() {

						scrollView.fullScroll(scrollView.FOCUS_FORWARD);
					}
					
				});
				break;
            case FINISH:
            	tv_scan_status.setText("扫描完成");
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
		//设置动画无限循环
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
	 * 扫描病毒
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
//			tv_scan_status.setText("正在初始化8核杀毒引擎。。。");
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
				// apk文件的完整的路径
				String sourcedir = info.applicationInfo.sourceDir;
				String md5 = getFileMd5(sourcedir);
				ScanInfo1 scaninfo = new ScanInfo1();
				 System.out.println(info.applicationInfo.loadLabel(pm)+":"+md5);//名字
				 System.out.println(info.packageName);
				 scaninfo.name = info.applicationInfo.loadLabel(pm).toString();//包名
				 scaninfo.packname = info.packageName;
				// 查询MD5信息，是否在病毒数据库里面
				if (AntivirusDao.isVirus(md5)) {
					// 发现病毒
					 scaninfo.virus = true;
 
				} else {
					// 扫描安全
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
	 * 获取文件的MD5值
	 * 
	 * @param path
	 * 文件的全路径名称
	 * @return
	 */

	public static String getFileMd5(String path) {
		try {
			// 获取一个文件的特征信息，签名信息
			File file = new File(path);
			// 数字摘要
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
				// 与运算
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
