package net.blogjava.mobile.calendar;

import java.io.Serializable;
import java.util.ArrayList;
import net.blogjava.mobile.calendar.interfaces.CalendarElement;
import android.app.Activity;
import android.graphics.Canvas; 
import android.view.View;
/**
 * 绘制日历
 * */
public class Calendar extends CalendarParent 
{
	private ArrayList<CalendarElement> elements = new ArrayList<CalendarElement>();
    public Grid grid;
	public Calendar(Activity activity, View view)
	{	
		super(activity,view);
		//设置日历边框绘制的数据
		elements.add(new Border(activity, view));
		//设置日历内容中周期名（一，二...）的数据
		elements.add(new Week(activity, view));
		grid = new Grid(activity, view);
		//设置日历类容中的日期文字绘制
		elements.add(grid);
	}

	@Override
	public void draw(Canvas canvas)
	{
		  //绘制日历中的所有线条和文字
		for (CalendarElement ce : elements)
			ce.draw(canvas);
	}

}
