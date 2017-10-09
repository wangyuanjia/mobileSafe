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

	
	
	    //1.定义一个手势识别器
		private GestureDetector detector;
		protected SharedPreferences sp;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			sp = getSharedPreferences("config", MODE_PRIVATE);
			//2.实例化这个手势识别器
			detector = new GestureDetector(this, new OnGestureListener() {
											
				/**
				 * 当手指在屏幕上滑动的时候调用
				 */
				
				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
						float velocityY) {
					
					if(Math.abs(velocityX)<200){
						Toast.makeText(getApplicationContext(), "划的太慢了", 0).show();
						return true;
					}

					if(Math.abs(e2.getRawY()-e1.getRawY())>100){
						
						Toast.makeText(getApplicationContext(), "不能这样划", 0).show();
						return true;
					}
					
					if((e2.getRawX()-e1.getRawX())>200){
						//显示上一个页面，左--->右
						System.out.println("显示上一个页面，左--->右");
						showPre();
						return true;
					}
					
					if((e1.getRawX()-e2.getRawX())>200){
						//显示下一个页面，右--->左
						System.out.println("显示下一个页面，右--->左");
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
		
		
		
		//3.使用手势识别器
		
		public abstract void showNext(); 
		public abstract void showPre(); 
		/**
		 * 下一步的点击事件
		 */
		public void next(View view){
			showNext();
		}


		/**
		 * 上一步的点击事件
		 * @param view
		 */
		public void pre(View view){
			showPre();
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			//放进触摸方法
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
