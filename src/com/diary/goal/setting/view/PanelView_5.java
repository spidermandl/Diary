package com.diary.goal.setting.view;

import java.util.Calendar;

import com.diary.goal.setting.DiaryApplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class PanelView_5 extends PanelView {
	Calendar calendar;
	Paint paint;
	
	public PanelView_5(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PanelView_5(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public PanelView_5(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	void init() {
		// TODO Auto-generated method stub
		super.init();
		
		paint=new Paint();
		paint.setColor(0xFFFFFF00);
		this.setOnClickListener(null);
	}
	
	@Override
	void selfDraw(Canvas canvas,boolean activated) {
		Log.e("55555555555", "55555555555");
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {

		calendar = Calendar.getInstance();
		calendar.setTime(DiaryApplication.getInstance().getPadStatus().getDate());
		canvas.drawText(calendar.get(Calendar.YEAR)+"Äê", this.getWidth()/2, this.getHeight()/4, paint);
		canvas.drawText((calendar.get(Calendar.MONTH)+1)+"ÔÂ"+calendar.get(Calendar.DAY_OF_MONTH)+"ÈÕ", this.getWidth()/2, this.getHeight()*3/4,paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action=event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.e("PanelView_5", "ACTION_down");
			return false;
		case MotionEvent.ACTION_MOVE:
			Log.e("PanelView_5", "ACTION_move");
			return false;
		case MotionEvent.ACTION_UP:
			Log.e("PanelView_5", "ACTION_UP");
			return false;
		}
		return false;
	}
}
