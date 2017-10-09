package com.yuanjia.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.Loader.ForceLoadContentObserver;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanjia.mobilesafe.db.dao.AppLockDao;
import com.yuanjia.mobilesafe.domain.AppInfo;
import com.yuanjia.mobilesafe.engine.AppInfoProvider;
import com.yuanjia.mobilesafe.utils.DensityUtil;

public class AppManagerActivity extends Activity implements OnClickListener {

	private static final String TAG = "AppManagerActivity";
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private ListView lv_app_manager;
	private LinearLayout ll_loading;
	/**
	 * ����Ӧ�ó������Ϣ
	 */
	private List<AppInfo> appInfos;  		
	/**
	 * �û�Ӧ�ó���ļ���
	 */
	private List<AppInfo> userAppInfos;
	/**
	 * ϵͳӦ�ó���ļ���
	 */
	private List<AppInfo> systemAppInfos;
	/**
	 * ��ǰ������Ϣ��״̬
	 */
	private TextView tv_status;
	/**
	 * ��ʾ��������
	 */
	private PopupWindow popupWindow;
	
	private LinearLayout ll_start;//����
	private LinearLayout ll_uninstall;//ж��
	private LinearLayout ll_share;//����
	
	/**
	 * �������Ŀ
	 */
	private AppInfo appInfo;
	
	private AppManagerAdapter adapter;
	
	private AppLockDao dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		
		dao = new AppLockDao(this);
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		long sdsize = getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		long romsize = getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
		
		tv_avail_sd.setText("SD�����ÿռ䣺"+Formatter.formatFileSize(this,sdsize ));
		tv_avail_rom.setText("�ڴ���ÿռ䣺"+Formatter.formatFileSize(this,romsize ));

		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		fillData();
		//listview  �����¼�
		lv_app_manager.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			//������ʱ����õķ���
			//firstVisibleItem ��һ���ɼ���Ŀ��listview���������λ��
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				    dismissPopupWindow();
				if(userAppInfos!=null&&systemAppInfos!=null){
					if(firstVisibleItem>userAppInfos.size()){
						tv_status.setText("ϵͳ����"+systemAppInfos.size()+"��");
						
					}else{
						tv_status.setText("�û�����"+userAppInfos.size()+"��");
					}
				}
		
			}
		});
		
		
		//listview item �ĵ���¼�
		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {
//            AppInfo appInfo;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				dismissPopupWindow();
				
				AppInfo obj = (AppInfo) lv_app_manager.getItemAtPosition(position);
				if (obj!=null&&obj instanceof AppInfo) {
					appInfo = (AppInfo)obj;
					View contentView = View.inflate(getApplicationContext(), R.layout.popup_app_item, null);
					ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start); 
					ll_share = (LinearLayout) contentView.findViewById(R.id.ll_share); 
					ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall ); 
					ll_start.setOnClickListener(AppManagerActivity.this);
					ll_share.setOnClickListener(AppManagerActivity.this);
					ll_uninstall.setOnClickListener(AppManagerActivity.this);
					
				
					ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
					sa.setDuration(1000);
					AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
					aa.setDuration(1000);
					AnimationSet set = new AnimationSet(false);
					set.addAnimation(sa);
					set.addAnimation(aa);
					contentView.startAnimation(set);
					
					
					//��������ʾ��popupWindow
					popupWindow = new PopupWindow(contentView, -2, -2);
					//һ��Ҫ���ã�������ʾ������
					popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					int[] location = new int[2];
					view.getLocationInWindow(location);
					
					int dip = 60;
					int px = DensityUtil.dip2px(getApplicationContext(), dip);
					popupWindow.showAtLocation(parent, Gravity.LEFT|Gravity.TOP, px, location[1]);
					
				}
//				if(position == 0){
//					return;   //Ҫ�ǵ÷���
//				}else if(position == userAppInfos.size()+1){
//					return;
//				}else if(position < userAppInfos.size()+1){
//					int newPosition = position -1;
//					appInfo = userAppInfos.get(newPosition);
//				}else {
//					int newPosition = position -1-userAppInfos.size()-1;
//					appInfo = systemAppInfos.get(newPosition);
//				}
			//	dismissPopupWindow();
				
				
//				TextView contentView = new TextView(getApplicationContext());
//				contentView.setText(appInfo.getPackname());
//				contentView.setTextColor(Color.BLUE);
	     		
			
		
			}
		
		});
		
	    
		//listview item ������¼�
		lv_app_manager.setOnItemLongClickListener(new OnItemLongClickListener() {
 
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
//				if(position == 0){
//					return true;   //Ҫ�ǵ÷���
//				}else if(position == userAppInfos.size()+1){
//					return true;
//				}else if(position < userAppInfos.size()+1){
//					int newPosition = position -1;
//					appInfo = userAppInfos.get(newPosition);
//				}else {
//					int newPosition = position -1-userAppInfos.size()-1;
//					appInfo = systemAppInfos.get(newPosition);
//				}
				
				AppInfo obj = (AppInfo) lv_app_manager.getItemAtPosition(position);
				if (obj!=null&&obj instanceof AppInfo) {
					appInfo = (AppInfo)obj;
				
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				
				//�ж���Ŀ�Ƿ�����ڳ��������ݿ�����
			    if(dao.find(appInfo.getPackname())){
			    	//�������ĳ��򣬽�����������½���Ϊ�򿪵�С��ͼƬ
			    	dao.delete(appInfo.getPackname());
			    	viewHolder.iv_status.setImageResource(R.drawable.unlock);
			    }else{
			    	//�������򣬸��½���Ϊ�رյ���
			    	dao.add(appInfo.getPackname());
			    	viewHolder.iv_status.setImageResource(R.drawable.lock);
			    }
				}
				return true;
				
				
			}
		});
		
	}
	
	
	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		
		new Thread(){
			public void run() {
				appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for(AppInfo info:appInfos){
					if(info.isUserApp()){
						userAppInfos.add(info);
					}else{
						systemAppInfos.add(info);
						
					}
				}
				
				//����listView������
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						
						if(adapter == null){
							adapter = new AppManagerAdapter();
							lv_app_manager.setAdapter(adapter);
							
						} else {
							adapter.notifyDataSetChanged();
						}
						ll_loading.setVisibility(View.INVISIBLE);
					}
				});
			};
		}.start();
	}
	
	
	
	private class AppManagerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
//			return appInfos.size();
			return userAppInfos.size() + systemAppInfos.size() + 1 +1;
		}

		@Override
		public Object getItem(int position) {
			if(position == 0){
				return null;   //Ҫ�ǵ÷���
			}else if(position == userAppInfos.size()+1){
				return null;
			}else if(position < userAppInfos.size()+1){
				int newPosition = position -1;
				appInfo = userAppInfos.get(newPosition);
			}else {
				int newPosition = position -1-userAppInfos.size()-1;
				appInfo = systemAppInfos.get(newPosition);
			}
			return appInfo;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//		    TextView  tv = new TextView(getApplicationContext());
//		    tv.setText(appInfos.get(position).toString());
			
			
			if(position == 0){
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("�û�����"+userAppInfos.size()+"��");
				//������return
				return tv;
			}else if(position == (userAppInfos.size() + 1)){
				TextView tv = new TextView(getApplicationContext());
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText("ϵͳ����"+systemAppInfos.size()+"��");
				//������return
				return tv;
			}else if(position<=userAppInfos.size()){
				int newPosition = position-1;
				appInfo = userAppInfos.get(newPosition);
			}else{
				int newPosition = position-1-userAppInfos.size()-1;
				appInfo = systemAppInfos.get(newPosition);
			}
			
			View view;
		    ViewHolder holder;
		    if(convertView != null && convertView instanceof RelativeLayout){
		    	view = convertView;
		    	holder = (ViewHolder) view.getTag();
		    }else{
		    	view = View.inflate(getApplicationContext(), R.layout.list_item_appinfo, null);
		        holder = new ViewHolder();
		        holder.tv_appsize = (TextView) view.findViewById(R.id.tv_app_size);
		        holder.iv_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
		        holder.tv_location = (TextView) view.findViewById(R.id.tv_app_location);
		        holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
		        holder.iv_status = (ImageView) view.findViewById(R.id.iv_status);
		        view.setTag(holder);
		    }
	//	    AppInfo appinfo = appInfos.get(position);
		    
		
		    holder.tv_appsize.setText(Formatter.formatFileSize(getApplication(), appInfo.getAppsize()));
		    holder.iv_icon.setImageDrawable(appInfo.getIcon());
		    holder.tv_name.setText(appInfo.getName());
		    if(appInfo.isInRom()){
		       holder.tv_location.setText("�ֻ��ڴ�" + "   uid:" + appInfo.getUid());
		    }else {
		       holder.tv_location.setText("�ⲿ�洢" + "   uid:" + appInfo.getUid());
		    }
		    if(dao.find(appInfo.getPackname())){
		    	holder.iv_status.setImageResource(R.drawable.lock);
		    	
		    }else{
		    	holder.iv_status.setImageResource(R.drawable.unlock);
		    }
		    
//		    holder.iv_status.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
		    
			return view;
		}
		
	}
	
	static class ViewHolder{
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;
		ImageView iv_status;
		TextView tv_appsize;
	}
	
	private long getAvailSpace(String path){
	    StatFs statf = new StatFs(path);
	    statf.getBlockCount();
	    long size = statf.getBlockSize();
		long count = statf.getAvailableBlocks();
		return size*count;
	}
	

	private void dismissPopupWindow() {
		//�ر���������
		if(popupWindow!=null&&popupWindow.isShowing()){
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		dismissPopupWindow();
	}
	
	/**
	 * ���ֶ�Ӧ�ĵ���¼�
	 */
	@Override
	public void onClick(View v) {
		dismissPopupWindow();
      switch (v.getId()) {
	   case R.id.ll_start:
		Log.i(TAG, "������"+appInfo.getName());
		startApplication();
		break;
 
       case R.id.ll_share:
    	   Log.i(TAG, "����"+appInfo.getName());
    	   shareApplication();
		break;
       case R.id.ll_uninstall:
    	   if(appInfo.isUserApp()){
    		   Log.i(TAG, "ж�أ�"+appInfo.getName());
        	   uninstallApplication();
    	   }else{
    		   Toast.makeText(this, "ϵͳֻ�л�ȡrootȨ�޲ſ���ж��", 0).show(); 
    	   }
    	
	   break;
	
	}
		
	}
	
	/**
	 * ����һ��Ӧ�ó���
	 */
	private void shareApplication() {
	Intent intent = new Intent();
	intent.setAction("android.intent.action.SEND");
	intent.addCategory(Intent.CATEGORY_DEFAULT);
	intent.setType("text/plain");
	intent.putExtra(Intent.EXTRA_TEXT, "�Ƽ���ʹ��һ����������У�"+appInfo.getName());
    startActivity(intent);
	}
	 
//	private void shareApplication1() {
//		Intent intent1 = new Intent();
//        intent1.setAction("android.intent.action.SEND");
//        intent1.addCategory(Intent.CATEGORY_DEFAULT);
//        intent1.setType("text/plain");
//        intent1.putExtra(Intent.EXTRA_TEXT, "�Ƽ���ʹ��һ����������У�"+appInfo.getName());
//        startActivity(intent1);
//	}
	
	/**
	 * ж��һ��Ӧ�ó���
	 */
	
	private void uninstallApplication() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
	    intent.addCategory("android.intent.category.DEFAULT");
	    intent.setData(Uri.parse("package:"+appInfo.getPackname()));
	    startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//ˢ�½���
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}
	
	
	
	/**
	 * ����һ��Ӧ�ó���
	 */
	private void startApplication(){
		//��ѯ���Ӧ�ó�������activity��������������
		
	PackageManager pm = getPackageManager();
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.MAIN");
//	    intent.addCategory("android.intent.category.LAUNCHER");
	//��ѯ���õ��ֻ��ϵľ�������������activities
//	    List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        


		Intent i = pm.getLaunchIntentForPackage(appInfo.getPackname());
        if(i!=null){
        	startActivity(i);
        }else{
        	Toast.makeText(this, "����������ǰӦ��", 0).show();
        }
	}
	

}
