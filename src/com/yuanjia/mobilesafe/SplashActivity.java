package com.yuanjia.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.yuanjia.mobilesafe.db.dao.AntivirusDao;
import com.yuanjia.mobilesafe.domain.Virus;
import com.yuanjia.mobilesafe.utils.StreamTools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


public class SplashActivity extends Activity {

    protected static final String TAG = "SplashActivity";
	protected static final int ENTER_HOME = 0;
	protected static final int SHOW_UPDATE_DIALOG = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
    private TextView tv_splash_version;
    private String description;
    private String apkurl;
    private TextView tv_update_info;
    private SharedPreferences sp;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本号：" + getVersionName());
        tv_update_info = (TextView) findViewById(R.id.tv_update_info);
       
        //创建桌面快捷方式
        installShortCut();
        
        
        Boolean update = sp.getBoolean("update", false);
        
        //拷贝数据库 data/data/
        
        copyDB("address.db");
        
        //拷贝病毒数据库
        copyDB("antivirus.db");
        
        //更新病毒数据库
        updateVirusDB();
        
        
        if(update){
        	//检查升级
        	   checkUpdate();
        }else{
        	//不检查升级，延迟2s进入主界面
        	handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					enterHome();
				}
			}, 2000);
        }
     
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(500);
        findViewById(R.id.rl_root_splash).setAnimation(aa);
        
    }

	/**
	 * 更新病毒数据库
	 */
	private void updateVirusDB() {
		System.out.println("md5:"+"-------");
		//联网从服务器获取最新的数据MD5的特征码
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				  try {
						URL url = new URL(getString(R.string.updatevirusdb));
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						conn.setReadTimeout(3000);
						conn.setConnectTimeout(3000);
						if(conn.getResponseCode()==200){
							InputStream is = conn.getInputStream();
//							System.out.println("md5:"+"-------"+is);
							String virusdata = StreamTools.readFromStream(is);
							JSONObject obj1 = new JSONObject(virusdata);
							String md5 = (String) obj1.get("md5");
							String desc = obj1.getString("desc");
//							System.out.println("md5:"+md5+"-------"+desc);
//							AntivirusDao.addVirusMD5Code(md5, desc);
							
							
							
							Gson gson = new Gson();
							Virus virus = gson.fromJson(virusdata, Virus.class);
							
							AntivirusDao.addVirusMD5Code(virus.md5, virus.desc);
							System.out.println("md5:"+virus.md5+"--------------1---------");
							
//							if(code == 200){
//								//联网成功
//								InputStream is = conn.getInputStream();
//								//把流转换成String
//								String result = StreamTools.readFromStream(is);
//								Log.i(TAG, "联网成功了" +result);
//								//json解析
//								JSONObject obj = new JSONObject(result);
//								String version = (String) obj.get("version");
//								
//								
//								description = (String) obj.get("description");
//							    apkurl = (String) obj.get("apkurl");
//							
							
							
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
			}
		}).start();
      
	}

	/**
	 * 创建桌面快捷方式
	 */
   
	private void installShortCut() {
		
		if( sp.getBoolean("shortcut", false))
			return;
		Editor editor = sp.edit();
		
		
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机小卫士");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher) );
		
        
        Intent shortcutIntent = new Intent();
        shortcutIntent.setAction("android.intent.action.MAIN");
        shortcutIntent.addCategory("android.intent.category.LAUNCHER");
	    shortcutIntent.setClassName(getPackageName(), "com.yuanjia.mobilesafe.SplashActivity");
	    intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	    
	    
	    sendBroadcast(intent);
	    editor.putBoolean("shortcut", true);
	    editor.commit();
	}


	/**
	 * 创建数据库
	 * path  把address.db这个数据库拷贝到data/data/
	 */
	
	private void copyDB(String dbName) {
		//只要你拷贝了一次，我就不要你拷贝了
				
		try {
			File file = new File(getFilesDir(),dbName);
			
			
			if(file.exists()&&file.length()>0){
				//正常不用拷贝
				Toast.makeText(this, "正常不用拷贝", 1).show();
			}else{
				InputStream is = getAssets().open(dbName);
			      
		        FileOutputStream fos = new FileOutputStream(file);
		        byte[] buffer = new byte[1024];
		        int len = 0;
		        while((len = is.read(buffer))!=-1){
		        	fos.write(buffer, 0, len);
		        }
		        is.close();
		        fos.close();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}



	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case ENTER_HOME:  //进入主界面
				Log.i(TAG, "进入主界面");
				enterHome();
				break;
            case SHOW_UPDATE_DIALOG://显示升级对话框
            	Log.i(TAG, "显示升级对话框");
            	showUpdateDialog();
				break;
			case URL_ERROR://URL错误
				Log.i(TAG, "URL错误");	
				Toast.makeText(getApplicationContext(), "URL错误", 0).show();
				enterHome();
				break;
			case NETWORK_ERROR://网络错误
				Log.i(TAG, "网络错误");
				enterHome();
				Toast.makeText(getApplicationContext(), "网络错误", 0).show();
				break;
			case JSON_ERROR://JSON解析出错
				Log.i(TAG, "JSON解析出错");
				enterHome();
				Toast.makeText(getApplicationContext(), "JSON解析出错", 0).show();
				break;
			default:
				break;
			}
		}
		
	};
	
	
    protected void enterHome() {
			// TODO Auto-generated method stub
		Intent intent =	new Intent(this, HomeActivity.class);
		startActivity(intent);
		    //关闭当前页面
		finish();
		}
	/**
	 * 
	 * 升级对话框
	 */
	
    protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("提醒升级");
	//	builder.setCancelable(false);
		builder.setOnCancelListener(new OnCancelListener(){

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				enterHome();
				dialog.dismiss();
			}
			
		});
		builder.setMessage(description);
		builder.setPositiveButton("立即升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载apk,并替换安装
				
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					//sd卡存在
					//ifinal
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkurl, 
							Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk",
							new AjaxCallBack<File>() {

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									super.onFailure(t, errorNo, strMsg);
									t.printStackTrace();
									Toast.makeText(getApplicationContext(), "下载失败！", 1).show();
								}

								@Override
								public void onLoading(long count, long current) {
									super.onLoading(count, current);
									tv_update_info.setVisibility(View.VISIBLE);
									int progress = (int) (current*100/count);
									tv_update_info.setText("下载进度:"+progress+"%");
								}

								@Override
								public void onSuccess(File t) {
									super.onSuccess(t);
									installAPK(t);
								}
                                    /**
                                     * 安装APK
                                     * @param t
                                     */
								private void installAPK(File t) {
									Intent intent = new Intent();
									intent.setAction("android.intent.action.VIEW");
                                    intent.addCategory("android.intent.category.DEFAULT");
                                    intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
								    startActivity(intent);
								}
					           
					});
					
				}else{
					Toast.makeText(getApplicationContext(), "没有sd卡，请安装上再试试", 1).show();
					enterHome();
				}
			}
		});
        builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();
			}
		});
        builder.show();
	}


	/**
     * 检查是否有新版本，如果有就升级
     * @return
     */
  	
    private void checkUpdate() {
		new Thread() {
		  public void run() {
			  Message msg = Message.obtain();
			  long startTime = System.currentTimeMillis();
				try {
					URL url = new URL(getString(R.string.serverurl));
					//联网
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					if(code == 200){
						//联网成功
						InputStream is = conn.getInputStream();
						//把流转换成String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "联网成功了" +result);
						//json解析
						JSONObject obj = new JSONObject(result);
						String version = (String) obj.get("version");
						
						
						description = (String) obj.get("description");
					    apkurl = (String) obj.get("apkurl");
						
						if(getVersionName().equals(version)) {
							//版本一致，没有新版本
							msg.what = ENTER_HOME;
						} else {
							//有新版本，弹出升级对话框
							msg.what = SHOW_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = NETWORK_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();
					msg.what = JSON_ERROR;
				} finally {
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;
					if(dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
				}
				
			}
		}.start();
	}
   
    
    /*
     * 得到应用程序的版本名
     * 
     */

	private String getVersionName(){
		
        PackageManager pm = getPackageManager();
        
        try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
            	
    	
    }

   

  
}
