package com.yuanjia.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.yuanjia.mobilesafe.domain.TaskInfo;
import com.yuanjia.mobilesafe.engine.TaskInfoProvider;
import com.yuanjia.mobilesafe.utils.SystemInfoUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskManagerActivity extends Activity {

	private static final String TAG = "TaskManagerActivity";
	private TextView tv_process_count;
	private TextView tv_men_info;
	private TextView tv_status;
	private ListView lv_task_manager;
	private LinearLayout ll_loading;
	private List<TaskInfo> allTaskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	private TaskManagerAdapter adapter;
	private long availMem;
	private long totalMem;
	private int processCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		tv_men_info = (TextView) findViewById(R.id.tv_men_info);
		lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		
		setTitle();
	    
	    fillData();
	    tv_status = (TextView) findViewById(R.id.tv_status);
	    lv_task_manager.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(userTaskInfos!=null&systemTaskInfos!=null){
					if(firstVisibleItem>userTaskInfos.size()){
						tv_status.setText("ϵͳ����"+systemTaskInfos.size()+"��");
					}else{
						tv_status.setText("�û�����"+userTaskInfos.size()+"��");
					}
				}
			}
		});
	    lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 TaskInfo taskInfo;
					if(position == 0){//�û�����
						return ;
					}else if(position == (userTaskInfos.size()+1)){
						return ;
					}else if(position <= userTaskInfos.size()){
						taskInfo = userTaskInfos.get(position-1);
					}else{
					    taskInfo = systemTaskInfos.get(position-1-userTaskInfos.size()-1);	
					}
					
					if(getPackageName().equals(taskInfo.getPackname())){
						return;
					}
					
					ViewHolder viewHolder = (ViewHolder) view.getTag();
					
					if(taskInfo.isChecked()){
						taskInfo.setChecked(false);
						viewHolder.cb_status.setChecked(false);
					}else{
						taskInfo.setChecked(true);
						viewHolder.cb_status.setChecked(true);
					}
			}
		});
	}

	private void setTitle() {
		processCount = SystemInfoUtils.getRunningProcessCount(this);
	    Log.d(TAG, processCount+"");
		tv_process_count.setText("�����еĽ���"+processCount+"��");
		 
	    availMem = SystemInfoUtils.getAvailMem(this);
		totalMem = SystemInfoUtils.getTotalMem(this);
		tv_men_info.setText("ʣ��/���ڴ棺"+Formatter.formatFileSize(this, availMem)+"/"+Formatter.formatFileSize(this, totalMem));
	}

	private void fillData() {

		ll_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				
			   allTaskInfos = TaskInfoProvider.getTaskInfos(getApplicationContext());
			   userTaskInfos = new ArrayList<TaskInfo>();
			   systemTaskInfos = new ArrayList<TaskInfo>();
			   
			   for(TaskInfo info : allTaskInfos){
				   if(info.isUserTask()){
					   userTaskInfos.add(info);
				   }else{
					   systemTaskInfos.add(info);
				   }
			   }
			   
			   //�������ý���
			   runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
                    ll_loading.setVisibility(View.INVISIBLE);	
                    if(adapter==null){
                        adapter = new TaskManagerAdapter();
                        lv_task_manager.setAdapter(adapter);
                    }else{
                    	adapter.notifyDataSetChanged();
                    }
               
                    
				}
			});
			};
		}.start();
	}
	
	
	private class TaskManagerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			Boolean showSystem = sp.getBoolean("showSystem", false);
			if(showSystem){
				return userTaskInfos.size()+systemTaskInfos.size()+2;
			}else{
				return userTaskInfos.size() + 1;
			}
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		    TaskInfo taskInfo;
			if(position == 0){//�û�����
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("�û����̣�"+userTaskInfos.size()+"��");
				return tv;
			}else if(position == (userTaskInfos.size()+1)){
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("ϵͳ���̣�"+systemTaskInfos.size()+"��");
				return tv;
			}else if(position <= userTaskInfos.size()){
				taskInfo = userTaskInfos.get(position-1);
			}else{
			    taskInfo = systemTaskInfos.get(position-1-userTaskInfos.size()-1);	
			}
			View view;
			ViewHolder viewHolder;
			if(convertView!=null&&convertView instanceof RelativeLayout){
				view = convertView;
				viewHolder = (ViewHolder) view.getTag();
			}else{
				view = View.inflate(getApplicationContext(), R.layout.list_item_taskinfo, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_task_icon = (ImageView) view.findViewById(R.id.iv_task_icon);
		        viewHolder.tv_task_name = (TextView) view.findViewById(R.id.tv_task_name);
		        viewHolder.tv_task_memsize = (TextView) view.findViewById(R.id.tv_task_memsize);
		        viewHolder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
		        view.setTag(viewHolder);
			}
			
			viewHolder.iv_task_icon.setImageDrawable(taskInfo.getIcon());
            viewHolder.tv_task_name.setText(taskInfo.getName());
            viewHolder.tv_task_memsize.setText("�ڴ�ռ�ã�"+Formatter.formatFileSize(getApplicationContext(), taskInfo.getMemsize()));
			viewHolder.cb_status.setChecked(taskInfo.isChecked());
			if(getPackageName().equals(taskInfo.getPackname())){
				viewHolder.cb_status.setVisibility(View.INVISIBLE);
			}else{
				viewHolder.cb_status.setVisibility(View.VISIBLE);
			}
			return view;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}	
		
	}
	
	static class ViewHolder{
		ImageView iv_task_icon;
		TextView tv_task_name;
		TextView tv_task_memsize;
		CheckBox cb_status;
	}
	
	/**
	 * ѡ������
	 * 
	 */
	public void selectAll(View view) {

		for(TaskInfo info:allTaskInfos){
			if(getPackageName().equals(info.getPackname())){
				continue;
			}
			info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * ѡ���෴��
	 */
	
	public void selectOppo(View view) {
		for(TaskInfo info:allTaskInfos){
			info.setChecked(!info.isChecked());
		}
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * һ������
	 */
	
	public void killAll(View view) {

		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    int count = 0;
	    int savedMem = 0;
	    //��¼��Щ��ɱ������Ŀ
	    List<TaskInfo> killedTaskInfos = new ArrayList<TaskInfo>();
		for(TaskInfo info:allTaskInfos){
			if(info.isChecked()){//����ѡ�ģ�ɱ���������
				am.killBackgroundProcesses(info.getPackname());
				if(info.isUserTask()){
					userTaskInfos.remove(info);
				}else{
					systemTaskInfos.remove(info);
				}
				killedTaskInfos.add(info);
				count++;
				savedMem += info.getMemsize();
			}
		}
			allTaskInfos.remove(killedTaskInfos);  
			adapter.notifyDataSetChanged();
			Toast.makeText(this, "ɱ����"+count+"�����̣��ͷ���"+Formatter.formatFileSize(this, savedMem)+"�ڴ�", 0).show();
	        processCount-= count;
	        availMem+=savedMem;
	        tv_process_count.setText("�����еĽ���"+processCount+"��");
	        tv_men_info.setText("ʣ��/���ڴ棺"+Formatter.formatFileSize(this, availMem)+"/"+Formatter.formatFileSize(this, totalMem));
		
	//	fillData();
	}
	/**
	 * ��������
	 * @param view
	 */
	public void enterSetting(View view) {

		Intent intent = new Intent(this,TaskSettingActivity.class);
		startActivityForResult(intent, 0);
	}
	
}
