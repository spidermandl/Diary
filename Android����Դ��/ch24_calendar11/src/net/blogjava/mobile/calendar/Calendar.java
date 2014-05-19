package net.blogjava.mobile.calendar;

import java.util.ArrayList;
import net.blogjava.mobile.calendar.interfaces.CalendarElement;
import android.app.Activity;
import android.graphics.Canvas; 
import android.view.View;
/**
 * ��������
 * */
public class Calendar extends CalendarParent 
{
	private ArrayList<CalendarElement> elements = new ArrayList<CalendarElement>();
    public Grid grid;
	public Calendar(Activity activity, View view)
	{	
		super(activity,view);
		//���������߿���Ƶ�����
		elements.add(new Border(activity, view));
		//����������������������һ����...��������
		elements.add(new Week(activity, view));
		grid = new Grid(activity, view);
		//�������������е��������ֻ���
		elements.add(grid);
	}

	@Override
	public void draw(Canvas canvas)
	{
		  //���������е���������������
		for (CalendarElement ce : elements)
			ce.draw(canvas);
	}

}
