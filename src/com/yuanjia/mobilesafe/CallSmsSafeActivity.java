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
		
		
		//listVIewע��һ�������¼��ļ�����
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {
			
			//��������״̬�����仯��ʱ��
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE://����״̬
					System.out.println("����״̬");
					int lastposition = lv_callsms_safe.getLastVisiblePosition();
				    if(lastposition == (infos.size()-1)){
				    	offset+=maxnumber;
				    	fillData();
						
				    }
					
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://��ָ��������
					System.out.println("��ָ��������");
					break;
				case OnScrollListener.SCROLL_STATE_FLING://���Ի���״̬
					System.out.println("���Ի���״̬");
					break;
			
				}
				
			}
			
			//������ʱ����õķ���
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
				}else{//ԭ�����ع�������
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
				holder.tv_mode.setText("�绰����");
			}else if("2".endsWith(mode)){
				holder.tv_mode.setText("��������");
			}else if("3".endsWith(mode)){
				holder.tv_mode.setText("ȫ������");
			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(CallSmsSafeActivity.this);
					builder.setTitle("���棡");
					builder.setMessage("ȷ��Ҫɾ������������");
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//ɾ������
							dao.delete(infos.get(position).getNumber());
							//���½���
							infos.remove(position);
							//֪ͨlistview��������������
							adapter.notifyDataSetChanged();
							
						}
					});
					builder.setNegativeButton("ȡ��", null);
					builder.show();
				}
			});
			return view;
		}
		

	}
	
	/**
	 * view���������
	 * �൱��һ�����±�
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
					Toast.makeText(getApplicationContext(), "���������벻��Ϊ��", 0).show();
					return ;
				}
				
				String mode ;
				if(cb_phone.isChecked()&&cb_sms.isChecked()){
					//ȫ������
					mode = "3";
				}else if(cb_phone.isChecked()){
					//�绰����
					mode = "1";
				}else if(cb_sms.isChecked()){
					//��������
					mode = "2";
				}else{
					Toast.makeText(getApplicationContext(), "��ѡ������ģʽ", 0).show();
				    return ;
				}
				//���ݱ��ӵ����ݿ�
				dao.add(blacknumber, mode);
				//����listView���������
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
