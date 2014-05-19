package net.blogjava.mobile.calendar;


import net.blogjava.mobile.calendar.interfaces.CalendarElement;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
/**
 * 日历内容的基类
 * 
 * */
public class CalendarParent implements CalendarElement
{
	protected Activity activity;
	protected View view;
	protected Paint paint = new Paint();
	protected float borderMargin;		
	protected float weekNameMargin;
	protected float weekNameSize;
	protected int sundaySaturdayColor;
	

    public CalendarParent(Activity activity, View view)
    {    	
    	this.activity = activity;
    	this.view = view;
    	//从资源文件中获取一些公共的数据
		borderMargin = activity.getResources().getDimension(
				R.dimen.calendar_border_margin);
		
		/**
		 * 日历的高度
		 * 
		 * */
        weekNameMargin = activity.getResources().getDimension(R.dimen.weekname_margin);
        
        /**
         * 
         * 
         * 日历周名的大小
         * */
        weekNameSize=activity.getResources().getDimension(R.dimen.weekname_size);
        
        /**
         * 星期的颜色六，日
         * 
         * */
        sundaySaturdayColor = activity.getResources().getColor(R.color.sunday_saturday_color);

    }
	@Override
	public void draw(Canvas canvas)
	{		
		
	}

}
