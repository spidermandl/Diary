package com.diary.goal.setting.view;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

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
		calendar = Calendar.getInstance();
		paint=new Paint();
		paint.setColor(0xFFFFFF00);
		this.setOnClickListener(null);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawText(calendar.get(Calendar.YEAR)+"Äê", this.getMeasuredWidth()/2, this.getHeight()/4, paint);
		canvas.drawText(calendar.get(Calendar.MONTH)+"ÔÂ"+calendar.get(Calendar.DAY_OF_MONTH)+"ÈÕ", this.getMeasuredWidth()/2, this.getHeight()*3/4,paint);
	}
}
