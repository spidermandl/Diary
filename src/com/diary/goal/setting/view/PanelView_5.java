package com.diary.goal.setting.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.tools.BitmapCustomize;
import com.diary.goal.setting.tools.Constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
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
		paint.setColor(0xFFFFFFFF);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true); // Ïû³ý¾â³Ý   
		paint.setFlags(Paint.ANTI_ALIAS_FLAG); // Ïû³ý¾â³Ý  
		this.setOnClickListener(null);
	}
	
	@Override
	void selfDraw(Canvas canvas,boolean activated) {
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		calendar = Calendar.getInstance();
		calendar.setTime(DiaryApplication.getInstance().getPadStatus().getDate());
		Calendar today=Calendar.getInstance();
		today.setTime(new Date());
		boolean activated=true;
		if(getQuot(getDate(calendar), getDate(today))>0)
			activated=false;
		Bitmap temp=BitmapCustomize.customizePicture(context, activated?R.drawable.date_activated:R.drawable.date_null,
				this.getWidth(),
				this.getHeight(),false);
		canvas.drawBitmap(temp, zoomBitmap(temp, this.getWidth(), this.getHeight()), mPaint);

		String text=calendar.get(Calendar.YEAR)+"";
		paint.setTextSize(20);
		canvas.drawText(text, (this.getWidth()-paint.measureText(text))/2, this.getHeight()/4, paint);
		int month=calendar.get(Calendar.MONTH)+1;
		text=month>9?"0":""+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.DAY_OF_MONTH);
		paint.setTextSize(28);
		canvas.drawText(text, (this.getWidth()-paint.measureText(text))/2, this.getHeight()*2/3, paint);
		text=this.getResources().getString(Constant.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));
		FontMetrics fontMetrics = paint.getFontMetrics();  
		paint.setTextSize(20);
		canvas.drawText(text, (this.getWidth()-paint.measureText(text))/2, this.getHeight()*23/30+fontMetrics.bottom, paint);
		
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
	
	private String getDate(Calendar cal) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		Date dd = cal.getTime();
		return ft.format(dd);
	}

	private long getQuot(String time1, String time2) {
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		try {
			Date date1 = ft.parse(time1);
			Date date2 = ft.parse(time2);
			quot = date1.getTime() - date2.getTime();
			quot = quot / 1000 / 60 / 60 / 24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}
}
