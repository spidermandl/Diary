package net.blogjava.mobile.calendar;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

/**
 * 绘制边框数据从CalendarParent类获得
 * 
 * */
public class Border extends CalendarParent
{

	public Border(Activity activity, View view)
	{
		super(activity, view);
		//获得边框的颜色
		paint.setColor(activity.getResources().getColor(R.color.border_color));
	}
     /**
      * 绘制日历
      * 
      * */
	@Override
	public void draw(Canvas canvas)
	{
		//获得日历边框的大小
		float left = borderMargin;
		float top = borderMargin;
		float right = view.getMeasuredWidth() - left;
		float bottom = view.getMeasuredHeight() - top;
		//绘制日历边框
		canvas.drawLine(left, top, right, top, paint);
		canvas.drawLine(right, top, right, bottom, paint);
		canvas.drawLine(right, bottom, left, bottom, paint);
		canvas.drawLine(left, bottom, left, top, paint);

	}

}
