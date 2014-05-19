package net.blogjava.mobile.calendar;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;
/**
 * 绘制周名称
 * 
 * */
public class Week extends CalendarParent
{
	private String[] weekNames;
	private int weekNameColor;

	public Week(Activity activity, View view)
	{
		super(activity, view);
		//获得周名称的颜色(周一至周五)
		weekNameColor = activity.getResources().getColor(R.color.weekname_color);
		//获得周名称(以数组形式返回)
		weekNames = activity.getResources().getStringArray(R.array.week_name);
		//设置周名称文字的大小
		paint.setTextSize(weekNameSize);
	}

	@Override
	public void draw(Canvas canvas)
	{

		float left = borderMargin;
		float top = borderMargin;
		float everyWeekWidth = (view.getMeasuredWidth() -  borderMargin * 2)/ 7;
		float everyWeekHeight = everyWeekWidth;
		
		paint.setFakeBoldText(true);
		for (int i = 0; i < weekNames.length; i++)
		{
			if(i == 0 || i == weekNames.length - 1)
				//用于周六,日的颜色在其它地方用到
				//sundaySaturdayColor在CalendarParentl类中获得
				paint.setColor(sundaySaturdayColor);
			else
				paint.setColor(weekNameColor);

			left = borderMargin + everyWeekWidth * i
					+ (everyWeekWidth - paint.measureText(weekNames[i])) / 2;
			//开始绘制周期名称
			canvas.drawText(weekNames[i], left, top + paint.getTextSize()+weekNameMargin, paint);
		}

	}

}
