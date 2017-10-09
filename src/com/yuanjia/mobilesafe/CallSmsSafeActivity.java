package com.yuanjia.mobilesafe;

import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import net.tsz.afinal.utils.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuanjia.mobilesafe.db.dao.BlackNumberDao;
import com.yuanjia.mobilesafe.domain.BlackNumberInfo;

public class CallSmsSafeActivity extends Activity {

	private ListView lv_callsms_safe;
	private BlackNumberDao dao;
	private List<BlackNumberInfo> infos;
	private CallSmsSafeAdapter adapter;
	private LinearLayout ll_loading;
	private int offset = 0;
	private int maxnumber = 20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		fillData();
		
		
		//listVIew注册一个滚动事件的监听器
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {
			
			//当滚动的状态发生变化的时候
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE://空闲状态
					System.out.println("空闲状态");
					int lastposition = lv_callsms_safe.getLastVisiblePosition();
				    if(lastposition == (infos.size()-1)){
				    	offset+=maxnumber;
				    	fillData();
						
				    }
					
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://手指触摸滚动
					System.out.println("手指触摸滚动");
					break;
				case OnScrollListener.SCROLL_STATE_FLING://惯性滑行状态
					System.out.println("惯性滑行状态");
					break;
			
				}
				
			}
			
			//滚动的时候调用的方法
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				
			}
		});
	}

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		dao = new BlackNumberDao(this);
		
		
		new Thread(){
			public void run() {
				if(infos == null){
					infos = dao.findPart(offset, maxnumber);
				}else{//原来加载过数据了
					infos.addAll(dao.findPart(offset, maxnumber));
					
				}
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						if(adapter == null){
							adapter = new CallSmsSafeAdapter();
							lv_callsms_safe.setAdapter(adapter);
						}else{
							adapter.notifyDataSetChanged();
						}
						
						
					}
				});
			};
			
		}.start();
	}
	
	private class CallSmsSafeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view ;
			ViewHolder holder;
			if(convertView == null){
				view = View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_mode = (TextView) view.findViewById(R.id.tv_black_mode);
				holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
				view.setTag(holder);
			} else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			
			
			
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getMode();
		
			if("1".equals(mode)){
				holder.tv_mode.setText("电话拦截");
			}else if("2".endsWith(mode)){
				holder.tv_mode.setText("短信拦截");
			}else if("3".endsWith(mode)){
				holder.tv_mode.setText("全部拦截");
			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
					builder.setTitle("警告！");
					builder.setMessage("确定要删除这条数据吗？");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//删除数据
							dao.delete(infos.get(position).getNumber());
							//更新界面
							infos.remove(position);
							//通知listview数据适配器更新
							adapter.notifyDataSetChanged();
							
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
				}
			});
			return view;
		}
		

	}
	
	/**
	 * view对象的容器
	 * 相当于一个记事本
	 * @author Administrator
	 *
	 */
    static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
    
    
    private EditText et_blacknumber;
    private CheckBox cb_phone;
    private CheckBox cb_sms;
    private Button ok;
    private Button cancel;
    
    public void addBlackNumber(View view){
    	AlertDialog.Builder builder = new Builder(this);
    	final AlertDialog dialog =  builder.create();
    	View view1 = View.inflate(getApplicationContext(), R.layout.dialog_enter_blacknumber, null);
    	dialog.setView(view1, 0, 0, 0, 0);
    	dialog.show();
    	
    	et_blacknumber = (EditText) view1.findViewById(R.id.et_blacknumber);
    	cb_phone = (CheckBox) view1.findViewById(R.id.cb_phone);
    	cb_sms = (CheckBox) view1.findViewById(R.id.cb_sms);
    	ok = (Button) view1.findViewById(R.id.ok);
    	cancel = (Button) view1.findViewById(R.id.cancel);
    	
    	
    	
    	cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
    	
    	ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String blacknumber = et_blacknumber.getText().toString().trim();
				if(TextUtils.isEmpty(blacknumber)){
					Toast.makeText(getApplicationContext(), "黑名单号码不能为空", 0).show();
					return ;
				}
				
				String mode ;
				if(cb_phone.isChecked()&&cb_sms.isChecked()){
					//全部拦截
					mode = "3";
				}else if(cb_phone.isChecked()){
					//电话拦截
					mode = "1";
				}else if(cb_sms.isChecked()){
					//短信拦截
					mode = "2";
				}else{
					Toast.makeText(getApplicationContext(), "请选择拦截模式", 0).show();
				    return ;
				}
				//数据被加到数据库
				dao.add(blacknumber, mode);
				//更新listView里面的内容
				BlackNumberInfo info = new BlackNumberInfo();
				info.setMode(mode);
				info.setNumber(blacknumber);
				
				infos.add(0,info);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
    	
    	
    	
    }
    
	
}
