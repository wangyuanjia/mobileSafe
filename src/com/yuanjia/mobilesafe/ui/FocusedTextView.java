package com.yuanjia.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * 自定义一个TextView
 * @author Administrator
 *
 */
public class FocusedTextView extends TextView {

	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 当前没有焦点，只是欺骗了Android系统
	 * (non-Javadoc)
	 * @see android.view.View#isFocused()
	 */
	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
}
