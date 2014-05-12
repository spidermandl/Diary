package com.diary.goal.activity.sudo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.diary.goal.DiaryApplication;
import com.diary.goal.R;
import com.diary.goal.toolkit.wheel.CalendarWheelView;
import com.diary.goal.toolkit.wheel.adapter.CalendarWheelAdapter;
import com.diary.goal.util.BitmapCustomize;
import com.diary.goal.util.Constant;
import com.diary.goal.util.Constant.SudoType;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

public class PanelView_5 extends PanelView {
	
	View overviewTab;
	
	Calendar calendar;
	Paint paint;
	
	int icon_width;
	int icon_height;
    boolean overview_taped=false;
	
	public PanelView_5(Context context) {
		super(context);
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
		sudoType=SudoType.SUDO_5;
		paint=new Paint();
		paint.setColor(0xFFFFFFFF);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true); // �������   
		paint.setFlags(Paint.ANTI_ALIAS_FLAG); // �������  
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
		Bitmap temp=BitmapCustomize.customizePicture(context, sudoType.getResDrawable(activated),
				this.getWidth(),
				this.getHeight(),false);
		canvas.drawBitmap(temp, zoomBitmap(temp, this.getWidth(), this.getHeight()), mPaint);

		String text=calendar.get(Calendar.YEAR)+"";
		paint.setTextSize(20);
		canvas.drawText(text, (this.getWidth()-paint.measureText(text))/2, this.getHeight()/4, paint);
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		text=month>9?"":"0"+month+"."+(day>9?"":"0")+day;
		paint.setTextSize(28);
		canvas.drawText(text, (this.getWidth()-paint.measureText(text))/2, this.getHeight()*2/3, paint);
		text=this.getResources().getString(Constant.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)));
		FontMetrics fontMetrics = paint.getFontMetrics();
		paint.setTextSize(20);
		canvas.drawText(text, (this.getWidth()-paint.measureText(text))/2, this.getHeight()*23/30+fontMetrics.bottom, paint);
		
		if(activated){
			Bitmap cap=BitmapCustomize.customizePicture(context, R.drawable.overview_active,0,0,false);
			//canvas.drawBitmap(cap, 0, 0, mPaint);
			icon_width=cap.getWidth();
			icon_height=cap.getHeight();
			if(overviewTab==null){
				overviewTab=new View(context);
				overviewTab.setLayoutParams(new LayoutParams(icon_width, icon_height));
				this.addView(overviewTab);
		        overviewTab.layout(0, 0, icon_width, icon_height);
				overviewTab.setBackgroundDrawable(new BitmapDrawable(cap));
				Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible  
		        animation.setDuration(500); // duration - half a second  
		        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate  
		        animation.setRepeatCount(10); // Repeat animation with 5 times  
		        animation.setRepeatMode(Animation.REVERSE); //   
		        overviewTab.setAnimation(animation); 
		        overviewTab.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						callPaperOverview();
					}
				});
			}else{
				overviewTab.setVisibility(View.VISIBLE);
			}
		}else{
			if(overviewTab!=null)
				overviewTab.setVisibility(View.GONE);
		}
		
		this.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Calendar nextYear = Calendar.getInstance();
				nextYear.add(Calendar.YEAR, 1);
				CalendarWheelView dialogView = new CalendarWheelView(context);
				CalendarWheelAdapter adapter=new CalendarWheelAdapter(context);
				dialogView.setCyclic(true);
				dialogView.setBackground(0);
				dialogView.setViewAdapter(adapter);
				dialogView.setCurrentItem(1);
				dialogView.setVisibleItems(1);
				new AlertDialog.Builder(context)
						.setTitle("I'm a dialog!")
						.setView(dialogView)
						.setNeutralButton("Dismiss",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialogInterface,
											int i) {
										dialogInterface.dismiss();
									}
								}).create().show();
			}
		});
	
	}
	
	
	private String getDate(Calendar cal) {
		SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
		Date dd = cal.getTime();
		return ft.format(dd);
	}

	private long getQuot(String time1, String time2) {
		long quot = 0L;
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
	
	private void callPaperOverview(){
		
	}
}
