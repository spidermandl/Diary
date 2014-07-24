package com.diary.goal.setting.invalid;

import com.diary.goal.setting.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CategoryTextView extends TextView{

	private Context context;
	public CategoryTextView(Context context) {
		super(context);
		this.context=context;
		init();
	}
	
	public CategoryTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init();
	}
	
	public CategoryTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		init();
	}
	
	void init(){
		this.setBackgroundResource(R.drawable.me_item_selector);
	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		int action = event.getAction();
//		switch (action) {
//		
//		case MotionEvent.ACTION_DOWN:
//			this.setBackgroundColor(0xFFC0C0C0);
//			return true;
//        case MotionEvent.ACTION_MOVE:
////        	float x=event.getX(),y=event.getY();
////        	Log.e("coordinate", "x:"+x+" y:"+y);
////        	Log.e("border", "x:"+this.getWidth()+" y:"+this.getHeight());
////        	if(x>=this.getWidth()||y>=this.getHeight()){
////        		this.setBackgroundColor(0x0000000);
////        	}
//			return false;
//        case MotionEvent.ACTION_UP:
//        	this.setBackgroundColor(0x00000000);
//	        return false;
//		default:
//			return false;
//		}
//	}


}
