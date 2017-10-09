package com.yuanjia.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {

	
	
	    //1.����һ������ʶ����
		private GestureDetector detector;
		protected SharedPreferences sp;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			sp = getSharedPreferences("config", MODE_PRIVATE);
			//2.ʵ�����������ʶ����
			detector = new GestureDetector(this, new OnGestureListener() {
											
				/**
				 * ����ָ����Ļ�ϻ�����ʱ�����
				 */
				
				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
						float velocityY) {
					
					if(Math.abs(velocityX)<200){
						Toast.makeText(getApplicationContext(), "����̫����", 0).show();
						return true;
					}

					if(Math.abs(e2.getRawY()-e1.getRawY())>100){
						
						Toast.makeText(getApplicationContext(), "����������", 0).show();
						return true;
					}
					
					if((e2.getRawX()-e1.getRawX())>200){
						//��ʾ��һ��ҳ�棬��--->��
						System.out.println("��ʾ��һ��ҳ�棬��--->��");
						showPre();
						return true;
					}
					
					if((e1.getRawX()-e2.getRawX())>200){
						//��ʾ��һ��ҳ�棬��--->��
						System.out.println("��ʾ��һ��ҳ�棬��--->��");
						showNext();
						return true;
					}
					return true;
				}

				@Override
				public boolean onDown(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onShowPress(MotionEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2,
						float distanceX, float distanceY) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onLongPress(MotionEvent e) {
					// TODO Auto-generated method stub
					
				}

			
			
			} );
			
		}
		
		
		
		//3.ʹ������ʶ����
		
		public abstract void showNext(); 
		public abstract void showPre(); 
		/**
		 * ��һ���ĵ���¼�
		 */
		public void next(View view){
			showNext();
		}


		/**
		 * ��һ���ĵ���¼�
		 * @param view
		 */
		public void pre(View view){
			showPre();
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//�Ž���������
			detector.onTouchEvent(event);
			
							
			return super.onTouchEvent(event);
		}
		
//		public boolean onDown(MotionEvent e) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//		public void onShowPress(MotionEvent e) {
//			// TODO Auto-generated method stub
//			
//		}
//		public boolean onSingleTapUp(MotionEvent e) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//				float distanceY) {
//			// TODO Auto-generated method stub
//			return false;
//		}
//		public void onLongPress(MotionEvent e) {
//			// TODO Auto-generated method stub
//			
//		}
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//			// TODO Auto-generated method stub
//			return false;
//		}
}
