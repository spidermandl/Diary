package net.blogjava.mobile.calendar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;
/**
 * 绘制日期和网格
 * 
 * */
public class Grid extends CalendarParent implements Serializable
{

	private String[] days = new String[42];
	// true表示有记录，false表示没有记录
	private boolean[] recordDays = new boolean[42];
	private String[] monthNames = new String[12];
	private TextView tvMsg1;
	private TextView tvMsg2;
	private TextView tvMsg3;
	private int dayColor;
	private int innerGridColor;
	private int prevNextMonthDayColor;
	private int currentDayColor;
	private int todayColor;
	private int todayBackgroundColor;
	private int sundaySaturdayPrevNextMonthDayColor;
	private float daySize;
	private float dayTopOffset;
	private float currentDaySize;
	private float cellX = -1, cellY = -1;

	// 从0开始
	private int currentRow, currentCol;
	private boolean redrawForKeyDown = false;

	// 当前年和月
	public int currentYear, currentMonth;
	// 上月或下月选中的天
	public int currentDay = -1, currentDay1 = -1, currentDayIndex = -1;
	private java.util.Calendar calendar = java.util.Calendar.getInstance();

	public void setCurrentRow(int currentRow)
	{
		if (currentRow < 0)
		{
			currentMonth--;
			if (currentMonth == 0)
			{
				currentMonth = 11;
				currentYear--;
			}
			currentDay = getMonthDays(currentYear, currentMonth) + currentDay
					- 7;
			currentDay1 = currentDay;
			cellX = -1;
			cellX = -1;
			view.invalidate();
			return;

		}
		else if (currentRow > 5)
		{
			int n = 0;
			for (int i = 35; i < days.length; i++)
			{
				if (!days[i].startsWith("*"))
					n++;
				else
					break;
			}
			currentDay = 7 - n + currentCol + 1;
			currentDay1 = currentDay;
			currentMonth++;
			if (currentMonth == 12)
			{
				currentMonth = 0;
				currentYear++;
			}
			cellX = -1;
			cellX = -1;
			view.invalidate();
			return;
		}
		this.currentRow = currentRow;
		redrawForKeyDown = true;
		view.invalidate();
	}

	private void getRecordDays()
	{
		int beginIndex = 8;
		int endIndex = 7;
		int beginDayIndex = 0;
		if (currentMonth > 9)
		{
			beginIndex = 9;
			endIndex = 8;
		}
		for (int i = 0; i < recordDays.length; i++)
			recordDays[i] = false;
		for (int i = 0; i < days.length; i++)
		{
			if (!days[i].startsWith("*"))
			{
				beginDayIndex = i;
				break;
			}
		}
		if (days[0].startsWith("*"))
		{
			int prevYear = currentYear, prevMonth = currentMonth - 1;
			if (prevMonth == -1)
			{
				prevMonth = 11;
				prevYear--;
			}
			int minDay = Integer.parseInt(days[0].substring(1));
			
		}
		if (days[days.length - 1].startsWith("*"))
		{
			int nextYear = currentYear, nextMonth = currentMonth + 1;
			if (nextMonth == 12)
			{
				
				nextMonth = 0;
				nextYear++;
			}
			int maxDay = Integer.parseInt(days[days.length - 1].substring(1));
		}

	}

	public void setCurrentCol(int currentCol)
	{
		if (currentCol < 0)
		{
			if (currentRow == 0)
			{

				currentMonth--;

				if (currentMonth == -1)
				{
					currentMonth = 11;
					currentYear--;
				}
				currentDay = getMonthDays(currentYear, currentMonth);
				currentDay1 = currentDay;
				cellX = -1;
				cellX = -1;
				view.invalidate();
				return;
			}

			else
			{
				currentCol = 6;
				setCurrentRow(--currentRow);

			}
		}
		else if (currentCol > 6)
		{
			currentCol = 0;
			setCurrentRow(++currentRow);

		}
		this.currentCol = currentCol;
		redrawForKeyDown = true;
		view.invalidate();
	}

	public int getCurrentRow()
	{
		return currentRow;
	}

	public int getCurrentCol()
	{
		return currentCol;
	}

	public void setCellX(float cellX)
	{

		this.cellX = cellX;
	}

	public void setCellY(float cellY)
	{

		this.cellY = cellY;
	}

	private int getMonthDays(int year, int month)
	{
		month++;
		switch (month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
			{
				return 31;
			}
			case 4:
			case 6:
			case 9:
			case 11:
			{
				return 30;
			}
			case 2:
			{
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
					return 29;
				else
					return 28;
			}
		}
		return 0;
	}

	private void calculateDays()
	{
		calendar.set(currentYear, currentMonth, 1);
        //获得当前月的第1天是所在周围的第几天
		int week = calendar.get(calendar.DAY_OF_WEEK);
		int monthDays = 0;
		int prevMonthDays = 0;

		monthDays = getMonthDays(currentYear, currentMonth);
		//当前是1月
		if (currentMonth == 0)
			//计算一月份的上月的天数，也就是前一年最后一个月的天数
			prevMonthDays = getMonthDays(currentYear - 1, 11);
		else
			//计算当前月份的上月的天数
			prevMonthDays = getMonthDays(currentYear, currentMonth - 1);
        
		//生成上月份配到当前的日期文字（ 前面添加星号，在显示时会去掉星号）
		for (int i = week, day = prevMonthDays; i > 1; i--, day--)
		{
			days[i - 2] = "*" + String.valueOf(day);
		}
		//生成普通日期的文字
		for (int day = 1, i = week - 1; day <= monthDays; day++, i++)
		{
			days[i] = String.valueOf(day);
			if (day == currentDay)
			{
				currentDayIndex = i;

			}
		}
		//生成下月份配到当前的日期文字（ 前面添加星号，在显示时会去掉星号）
		for (int i = week + monthDays - 1, day = 1; i < days.length; i++, day++)
		{
			days[i] = "*" + String.valueOf(day);
		}

	}

	public Grid(Activity activity, View view)
	{
		super(activity, view);
		tvMsg1 = (TextView) activity.findViewById(R.id.tvMsg1);
		tvMsg2 = (TextView) activity.findViewById(R.id.tvMsg2);
		//日期文本的颜色(白色)
		dayColor = activity.getResources().getColor(R.color.prev_next_month_day_color);
		//今天的日期文本颜色（白色）
		todayColor = activity.getResources().getColor(R.color.prev_next_month_day_color);
		//今天的日期文本边框颜色(红色)
		todayBackgroundColor = activity.getResources().getColor(
				R.color.prev_next_month_day_color);
		//日历网格线颜色(白色)
		innerGridColor = activity.getResources().getColor(
				R.color.prev_next_month_day_color);
		//上月和下月周期颜色(灰色),单击这日期时，会自动跳到上月和下月
		prevNextMonthDayColor = activity.getResources().getColor(
				R.color.prev_next_month_day_color);
		//当前日期文字的颜色(红色)
		currentDayColor = activity.getResources().getColor(
				R.color.prev_next_month_day_color);
		//周六，周日的颜色(暗红色)
		sundaySaturdayPrevNextMonthDayColor = activity.getResources().getColor(
				R.color.prev_next_month_day_color);
		//日期字体尺寸
		daySize = activity.getResources().getDimension(R.dimen.day_size);
		//日期文字据当前网格顶端的偏移量，用于微调日期文字定位
		dayTopOffset = activity.getResources().getDimension(
				R.dimen.day_top_offset);
		//当前日期文字尺寸
		currentDaySize = activity.getResources().getDimension(
				R.dimen.current_day_size);
		//月份名称(以数组形式返回)
		monthNames = activity.getResources().getStringArray(R.array.month_name);
		paint.setColor(activity.getResources().getColor(R.color.border_color));

		currentYear = calendar.get(calendar.YEAR);
		currentMonth = calendar.get(calendar.MONTH);
		
	}

	private boolean isCurrentDay(int dayIndex, int currentDayIndex,
			Rect cellRect)
	{
		boolean result = false;
		if (redrawForKeyDown == true)
		{
			result = dayIndex == (7 * ((currentRow > 0) ? currentRow : 0) + currentCol);
			if (result)
				redrawForKeyDown = false;

		}
		else if (cellX != -1 && cellY != -1)
		{
			if (cellX >= cellRect.left && cellX <= cellRect.right
					&& cellY >= cellRect.top && cellY <= cellRect.bottom)
			{
				result = true;
			}
			else
			{
				result = false;
			}
		}
		else
		{
			result = (dayIndex == currentDayIndex);

		}
		if (result)
		{
			if (currentRow > 0 && currentRow < 6)
			{
				currentDay1 = currentDay;

			}
			currentDayIndex = -1;
			cellX = -1;
			cellY = -1;

		}
		return result;
	}

	// 更新当前日期的信息
	private void updateMsg(boolean today)
	{
		String monthName = monthNames[currentMonth];
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(currentYear, currentMonth, currentDay);
		dateString = sdf.format(calendar.getTime());
		String lunarStr = "";
		 dateString +=" 星期"+calendar.get(Calendar.WEEK_OF_MONTH)+"周";
		 tvMsg1.setText(dateString);
		if (today)
        dateString +="";
       
		tvMsg2.setText(dateString);
		
	}

	public boolean inBoundary()
	{
		if (cellX < borderMargin
				|| cellX > (view.getMeasuredWidth() - borderMargin)
				|| cellY < top
				|| cellY > (view.getMeasuredHeight() - borderMargin))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	float top, left;

	@Override
	public void draw(Canvas canvas)
	{
		left = borderMargin;
		top = borderMargin + weekNameSize + weekNameMargin * 2 + 4;
		float calendarWidth = view.getMeasuredWidth() - left * 2;
		float calendarHeight = view.getMeasuredHeight() - top - borderMargin;
		float cellWidth = calendarWidth / 7;
		float cellHeight = calendarHeight / 6;
		paint.setColor(innerGridColor);
		//绘制日历网格最顶端的线条
		canvas.drawLine(left, top, left + view.getMeasuredWidth()
				- borderMargin * 2, top, paint);
		// 画横线
		for (int i = 1; i < 6; i++)
		{
			 canvas.drawLine(left, top + (cellHeight) * i, left +
			 calendarWidth,
			 top + (cellHeight) * i, paint);
		}
		// 画竖线
		for (int i = 1; i < 7; i++)
		{
			 canvas.drawLine(left + cellWidth * i, top, left + cellWidth * i,
			 view.getMeasuredHeight() - borderMargin, paint);
		}

		// 生成当前需要显示的月需要显示的日期文本,并将结果保存在days变量中

		calculateDays();

		java.util.Calendar calendar = java.util.Calendar.getInstance();
		//获得当前的日期的天
		int day = calendar.get(calendar.DATE);
		//获得当前日期的年月
		int myYear = calendar.get(calendar.YEAR), myMonth = calendar
				.get(calendar.MONTH);

		calendar.set(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH),
				1);
		int week = calendar.get(calendar.DAY_OF_WEEK);
		int todayIndex = week + day - 2;
		boolean today = false;
		if (currentDayIndex == -1)
		{
			currentDayIndex = todayIndex;

		}
		//获得当前所有包含记录信息日期
		boolean flag = false;
		getRecordDays();
		//开始绘制日期文本
		for (int i = 0; i < days.length; i++)
		{
			today = false;
			int row = i / 7;   //计算当前行
			int col = i % 7;   //计算当前列
			String text = String.valueOf(days[i]);
			//上月和下月的周六，周日，days数组中所有以星号(*)开头的
			if ((i % 7 == 0 || (i - 6) % 7 == 0) && text.startsWith("*"))
			{
				paint.setColor(sundaySaturdayPrevNextMonthDayColor);
			}
			//当前月的周六，周日
			else if (i % 7 == 0 || (i - 6) % 7 == 0)
			{
				paint.setColor(sundaySaturdayColor);
			}
			//当前月的普通日期(周一至周五)
			else if (text.startsWith("*"))
			{
				paint.setColor(prevNextMonthDayColor);
			}
			else
			{
				paint.setColor(dayColor);
			}
			 //将days数组元素中的星号去掉
			text = text.startsWith("*") ? text.substring(1) : text;

			Rect dst = new Rect();
			dst.left = (int) (left + cellWidth * col);
			dst.top = (int) (top + cellHeight * row);
			dst.bottom = (int) (dst.top + cellHeight + 1);
			dst.right = (int) (dst.left + cellWidth + 1);
			String myText = text;
			//如果当前日期包含记录信息，在日期文字前添加星号(*)
			if (recordDays[i])
				myText = "*" + myText;
			paint.setTextSize(daySize);
			float textLeft = left + cellWidth * col
					+ (cellWidth - paint.measureText(myText)) / 2;
			float textTop = top + cellHeight * row
					+ (cellHeight - paint.getTextSize()) / 2 + dayTopOffset;
			//当前日期是今天，在日期文字周围绘制边框
			if (myYear == currentYear && myMonth == currentMonth
					&& i == todayIndex)
			{
				paint.setTextSize(currentDaySize);
				//设置文字边框颜色
				paint.setColor(todayBackgroundColor);
				dst.left += 1;
				dst.top += 1;
				canvas.drawLine(dst.left, dst.top, dst.right, dst.top, paint);
				canvas.drawLine(dst.right, dst.top, dst.right, dst.bottom,
						paint);
				canvas.drawLine(dst.right, dst.bottom, dst.left, dst.bottom,
						paint);
				canvas.drawLine(dst.left, dst.bottom, dst.left, dst.top, paint);
                 //恢复日期文字颜色
				paint.setColor(todayColor);
				today = true;
			}
            //当单击前月中显示的上月或下月日期时，自动显示上月或下月的日历
			if (isCurrentDay(i, currentDayIndex, dst) && flag == false)
			{
				if (days[i].startsWith("*"))
				{
					// 下月
					if (i > 20)
					{
						currentMonth++;
						if (currentMonth == 12)
						{
							currentMonth = 0;
							currentYear++;
						}
                        //刷新当前日历，重新显示下月的日历
						view.invalidate();

					}
					// 上月
					else
					{
						currentMonth--;
						if (currentMonth == -1)
						{
							currentMonth = 11;
							currentYear--;
						}
						//刷新当前日历，重新显示上月的日历
						view.invalidate();

					}
					currentDay = Integer.parseInt(text);
					currentDay1 = currentDay;
					cellX = -1;
					cellY = -1;
					break;

				}
				//如果单击的不是上月或上月或下月的日期，则在当前日期上显示一个背景图，并将日期文字设置成红色
				else
				{
					
					
					paint.setTextSize(currentDaySize);
					flag = true;
					//获得背景颜色图资源
					Bitmap bitmap = BitmapFactory.decodeResource(activity
							.getResources(), R.drawable.day);
					Rect src = new Rect();
					src.left = 0;
					src.top = 0;
					src.right = bitmap.getWidth();
					src.bottom = bitmap.getHeight();
					
					 //绘制背景图
					canvas.drawBitmap(bitmap, src, dst, paint);
					paint.setColor(currentDayColor);
					currentCol = col;
					currentRow = row;
					currentDay = Integer.parseInt(text);
					currentDay1 = currentDay;
				
					//更新日历头的信息
					updateMsg(today);
				}
			}
             //绘制日期文本
			canvas.drawText(myText, textLeft, textTop, paint);
			
		}
		System.out.println(currentYear+"-"+currentMonth+"-"+currentDay+"!!!!!!!!!!!!!!");
	}

}
