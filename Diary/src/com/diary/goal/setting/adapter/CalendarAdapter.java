package com.diary.goal.setting.adapter;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map.Entry;


import com.diary.goal.setting.activity.PaperOverviewActivity;
import com.squareup.timessquare.CircleAdapter;
import com.squareup.timessquare.Logr;
import com.squareup.timessquare.MonthCellDescriptor;
import com.squareup.timessquare.MonthCellDescriptor.RangeState;
import com.squareup.timessquare.MonthView;
import com.squareup.timessquare.MonthDescriptor;
import com.squareup.timessquare.R;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarAdapter extends CircleAdapter<MonthDescriptor> {

	public enum SelectionMode {
		/**
		 * Only one date will be selectable. If there is already a selected date
		 * and you select a new one, the old date will be unselected.
		 */
		SINGLE,
		/**
		 * Multiple dates will be selectable. Selecting an already-selected date
		 * will un-select it.
		 */
		MULTIPLE,
		/**
		 * Allows you to select a date range. Previous selections are cleared
		 * when you either:
		 * <ul>
		 * <li>Have a range selected and select another date (even if it's in
		 * the current range).</li>
		 * <li>Have one date selected and then select an earlier date.</li>
		 * </ul>
		 */
		RANGE
	}

	private Context context;
	private DateFormat monthNameFormat;
	private DateFormat weekdayNameFormat;
	private DateFormat fullDateFormat;
	SelectionMode selectionMode;
	//final List<MonthDescriptor> months = new ArrayList<MonthDescriptor>();
	final List<MonthCellDescriptor> selectedCells = new ArrayList<MonthCellDescriptor>();
	final Calendar today = Calendar.getInstance();
	private HashMap<MonthDescriptor,List<List<MonthCellDescriptor>>> cells = new HashMap<MonthDescriptor, List<List<MonthCellDescriptor>>>();
	final List<Calendar> selectedCals = new ArrayList<Calendar>();
	private Calendar midCal = Calendar.getInstance();//当前月
//	private Calendar monthCounter = Calendar.getInstance();
//	private Calendar minCal= Calendar.getInstance();//最小月份
//	private	Calendar maxCal= Calendar.getInstance();//最大月份
	private final MonthView.Listener listener = new CellClickedListener();

	private OnDateSelectedListener dateListener;
	private DateSelectableFilter dateConfiguredListener;
	private OnInvalidDateSelectedListener invalidDateListener = new DefaultOnInvalidDateSelectedListener();
	private final LayoutInflater inflater;

	public CalendarAdapter(Context context) {

//		final int bg = getResources().getColor(R.color.calendar_bg);
//		setBackgroundColor(bg);
//		setCacheColorHint(bg);
		super();
		monthNameFormat = new SimpleDateFormat(
				context.getString(R.string.month_name_format));
		weekdayNameFormat = new SimpleDateFormat(
				context.getString(R.string.day_name_format));
		fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);




		init(new Date())
					.withSelectedDate(new Date());
		
	     this.context=context;
	     inflater = LayoutInflater.from(context);
		
	}

	/**
	 * Both date parameters must be non-null and their {@link Date#getTime()}
	 * must not return 0. Time of day will be ignored. For instance, if you pass
	 * in {@code minDate} as 11/16/2012 5:15pm and {@code maxDate} as 11/16/2013
	 * 4:30am, 11/16/2012 will be the first selectable date and 11/15/2013 will
	 * be the last selectable date ({@code maxDate} is exclusive).
	 * <p>
	 * This will implicitly set the {@link SelectionMode} to
	 * {@link SelectionMode#SINGLE}. If you want a different selection mode, use
	 * {@link FluentInitializer#inMode(SelectionMode)} on the
	 * {@link FluentInitializer} this method returns.
	 * 
	 * @param minDate
	 *            Earliest selectable date, inclusive. Must be earlier than
	 *            {@code maxDate}.
	 * @param maxDate
	 *            Latest selectable date, exclusive. Must be later than
	 *            {@code minDate}.
	 */
	public FluentInitializer init(Date midDay) {
		if (midDay == null ) {
			throw new IllegalArgumentException(
					"minDate and midDay must be non-null.  ");
		}
		if (midDay.getTime() == 0) {
			throw new IllegalArgumentException(
					"minDate and midDay must be non-zero.  ");
		}
		this.selectionMode = SelectionMode.SINGLE;
		// Clear out any previously-selected dates/cells.
		selectedCals.clear();
		selectedCells.clear();

		// Clear previous state.
		cells.clear();
		dataLinks.clear();
		midCal.setTime(midDay);
		setMidnight(midCal);


		// Now iterate between minCal and maxCal and build up our list of months
		// to show.
        Calendar minCal=Calendar.getInstance();
        minCal.setTime(midCal.getTime());
        minCal.add(MONTH, -capacity/2);//设置最小月份
//        maxCal.setTime(minCal.getTime());
//        maxCal.add(MONTH, capacity);//设置最大月份
//		monthCounter.setTime(minCal.getTime());
        initCalCapacity(minCal,Integer.MAX_VALUE/2);

		validateAndUpdate();
		return new FluentInitializer();
	}
	/**
	 * 赋值capacity个位置的日期
	 * @param minCal 插入最小日期
	 * @param position 划屏位置 为区间的中心位置
	 */
	private void initCalCapacity(Calendar minCal,int position){
		for (int i=0;i<capacity;i++) {//总共capacity各月份
			Date date = minCal.getTime();
			MonthDescriptor month = new MonthDescriptor(
					minCal.get(MONTH), minCal.get(YEAR), date,
					monthNameFormat.format(date));
			cells.put(month,getMonthCells(month, minCal));
			Log.e(""+(-capacity/2+position+i), ""+month.getYear()+"/"+month.getMonth());
			dataMap.put(-capacity/2+position+i, month);
			minCal.add(MONTH, 1);
		}
	}

	public class FluentInitializer {
		/**
		 * Override the {@link SelectionMode} from the default (
		 * {@link SelectionMode#SINGLE}).
		 */
		public FluentInitializer inMode(SelectionMode mode) {
			selectionMode = mode;
			validateAndUpdate();
			return this;
		}

		/**
		 * Set an initially-selected date. The calendar will scroll to that date
		 * if it's not already visible.
		 */
		public FluentInitializer withSelectedDate(Date selectedDates) {
			return withSelectedDates(Arrays.asList(selectedDates));
		}

		/**
		 * Set multiple selected dates. This will throw an
		 * {@link IllegalArgumentException} if you pass in multiple dates and
		 * haven't already called {@link #inMode(SelectionMode)}.
		 */
		public FluentInitializer withSelectedDates(
				Collection<Date> selectedDates) {
			if (selectionMode == SelectionMode.SINGLE
					&& selectedDates.size() > 1) {
				throw new IllegalArgumentException(
						"SINGLE mode can't be used with multiple selectedDates");
			}
			if (selectedDates != null) {
				for (Date date : selectedDates) {
					selectDate(date);
				}
			}
			Integer selectedIndex = null;
			Integer todayIndex = null;
			Calendar today = Calendar.getInstance();
			ListIterator<MonthDescriptor> it=dataLinks.listIterator();
			for (int c=0;it.hasNext();c++) {
				MonthDescriptor month = it.next();
				if (selectedIndex == null) {
					for (Calendar selectedCal : selectedCals) {
						if (sameMonth(selectedCal, month)) {
							selectedIndex = c;
							break;
						}
					}
					if (selectedIndex == null && todayIndex == null
							&& sameMonth(today, month)) {
						todayIndex = c;
					}
				}
			}
			if (selectedIndex != null) {
				scrollToSelectedMonth(selectedIndex);
			} else if (todayIndex != null) {
				scrollToSelectedMonth(todayIndex);
			}

			validateAndUpdate();
			return this;
		}

		/**
		 * Override default locale: specify a locale in which the calendar
		 * should be rendered.
		 */
		public FluentInitializer withLocale(Locale locale) {
			monthNameFormat = new SimpleDateFormat(context.getString(
					R.string.month_name_format), locale);
			ListIterator<MonthDescriptor> it=dataLinks.listIterator();
			while (it.hasNext()) {
				MonthDescriptor month=it.next();
				month.setLabel(monthNameFormat.format(month.getDate()));
			}
			weekdayNameFormat = new SimpleDateFormat(context.getString(
					R.string.day_name_format), locale);
			fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM,
					locale);
			validateAndUpdate();
			return this;
		}
	}

	private void validateAndUpdate() {
		this.notifyDataSetChanged();
	}

	private void scrollToSelectedMonth(final int selectedIndex) {
//		post(new Runnable() {
//			@Override
//			public void run() {
//				smoothScrollToPosition(selectedIndex);
//			}
//		});
	}


	public Date getSelectedDate() {
		return (selectedCals.size() > 0 ? selectedCals.get(0).getTime() : null);
	}

	public List<Date> getSelectedDates() {
		List<Date> selectedDates = new ArrayList<Date>();
		for (MonthCellDescriptor cal : selectedCells) {
			selectedDates.add(cal.getDate());
		}
		Collections.sort(selectedDates);
		return selectedDates;
	}

	/** Returns a string summarizing what the client sent us for init() params. */
	private static String dbg(Date minDate, Date maxDate) {
		return "minDate: " + minDate + "\nmaxDate: " + maxDate;
	}

	/** Clears out the hours/minutes/seconds/millis of a Calendar. */
	static void setMidnight(Calendar cal) {
		cal.set(HOUR_OF_DAY, 0);
		cal.set(MINUTE, 0);
		cal.set(SECOND, 0);
		cal.set(MILLISECOND, 0);
	}

	private class CellClickedListener implements MonthView.Listener {
		@Override
		public void handleClick(MonthCellDescriptor cell) {
			Date clickedDate = cell.getDate();
			/**
			 * Desmond
			 */
			Intent intent=new Intent();
			intent.setClass(context, PaperOverviewActivity.class);
			intent.putExtra("review_date", clickedDate);
			context.startActivity(intent);
			/**
			 * Desmond end
			 */

//			if (!betweenDates(clickedDate, minCal, maxCal)
//					|| !isDateSelectable(clickedDate)) {
//				if (invalidDateListener != null) {
//					invalidDateListener.onInvalidDateSelected(clickedDate);
//				}
//			} else {
//				boolean wasSelected = doSelectDate(clickedDate, cell);
//
//				if (wasSelected && dateListener != null) {
//					dateListener.onDateSelected(clickedDate);
//				}
//			}
		}
	}

	/**
	 * Select a new date. Respects the {@link SelectionMode} this
	 * CalendarPickerView is configured with: if you are in
	 * {@link SelectionMode#SINGLE}, the previously selected date will be
	 * un-selected. In {@link SelectionMode#MULTIPLE}, the new date will be
	 * added to the list of selected dates.
	 * <p>
	 * If the selection was made (selectable date, in range), the view will
	 * scroll to the newly selected date if it's not already visible.
	 * 
	 * @return - whether we were able to set the date
	 */
	public boolean selectDate(Date date) {
		if (date == null) {
			throw new IllegalArgumentException(
					"Selected date must be non-null.  " + date);
		}
		if (date.getTime() == 0) {
			throw new IllegalArgumentException(
					"Selected date must be non-zero.  " + date);
		}
//		if (date.before(minCal.getTime()) || date.after(maxCal.getTime())) {
//			throw new IllegalArgumentException(
//					"selectedDate must be between minDate and maxDate.  "
//							+ date);
//		}
		MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
		if (monthCellWithMonthIndex == null || !isDateSelectable(date)) {
			return false;
		}
		boolean wasSelected = doSelectDate(date, monthCellWithMonthIndex.cell);
		if (wasSelected) {
			scrollToSelectedMonth(monthCellWithMonthIndex.monthIndex);
		}
		return wasSelected;
	}

	private boolean doSelectDate(Date date, MonthCellDescriptor cell) {
		Calendar newlySelectedCal = Calendar.getInstance();
		newlySelectedCal.setTime(date);
		// Sanitize input: clear out the hours/minutes/seconds/millis.
		setMidnight(newlySelectedCal);

		// Clear any remaining range state.
		for (MonthCellDescriptor selectedCell : selectedCells) {
			selectedCell.setRangeState(RangeState.NONE);
		}

		switch (selectionMode) {
		case RANGE:
			if (selectedCals.size() > 1) {
				// We've already got a range selected: clear the old one.
				clearOldSelections();
			} else if (selectedCals.size() == 1
					&& newlySelectedCal.before(selectedCals.get(0))) {
				// We're moving the start of the range back in time: clear the
				// old start date.
				clearOldSelections();
			}
			break;

		case MULTIPLE:
			date = applyMultiSelect(date, newlySelectedCal);
			break;

		case SINGLE:
			clearOldSelections();
			break;
		default:
			throw new IllegalStateException("Unknown selectionMode "
					+ selectionMode);
		}

		if (date != null) {
			// Select a new cell.
			if (selectedCells.size() == 0 || !selectedCells.get(0).equals(cell)) {
				selectedCells.add(cell);
				cell.setSelected(true);
			}
			selectedCals.add(newlySelectedCal);

			if (selectionMode == SelectionMode.RANGE
					&& selectedCells.size() > 1) {
				// Select all days in between start and end.
				Date start = selectedCells.get(0).getDate();
				Date end = selectedCells.get(1).getDate();
				selectedCells.get(0).setRangeState(
						MonthCellDescriptor.RangeState.FIRST);
				selectedCells.get(1).setRangeState(
						MonthCellDescriptor.RangeState.LAST);

				
				Iterator<Entry<MonthDescriptor, List<List<MonthCellDescriptor>>>> iter = cells.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<MonthDescriptor, List<List<MonthCellDescriptor>>> entry = iter.next(); 
					List<List<MonthCellDescriptor>> month = entry.getValue();
					for (List<MonthCellDescriptor> week : month) {
						for (MonthCellDescriptor singleCell : week) {
							if (singleCell.getDate().after(start)
									&& singleCell.getDate().before(end)
									&& singleCell.isSelectable()) {
								singleCell.setSelected(true);
								singleCell
										.setRangeState(MonthCellDescriptor.RangeState.MIDDLE);
								selectedCells.add(singleCell);
							}
						}
					}
				}
			}
		}

		// Update the adapter.
		validateAndUpdate();
		return date != null;
	}

	private void clearOldSelections() {
		for (MonthCellDescriptor selectedCell : selectedCells) {
			// De-select the currently-selected cell.
			selectedCell.setSelected(false);
		}
		selectedCells.clear();
		selectedCals.clear();
	}

	private Date applyMultiSelect(Date date, Calendar selectedCal) {
		for (MonthCellDescriptor selectedCell : selectedCells) {
			if (selectedCell.getDate().equals(date)) {
				// De-select the currently-selected cell.
				selectedCell.setSelected(false);
				selectedCells.remove(selectedCell);
				date = null;
				break;
			}
		}
		for (Calendar cal : selectedCals) {
			if (sameDate(cal, selectedCal)) {
				selectedCals.remove(cal);
				break;
			}
		}
		return date;
	}

	/** Hold a cell with a month-index. */
	private static class MonthCellWithMonthIndex {
		public MonthCellDescriptor cell;
		public int monthIndex;

		public MonthCellWithMonthIndex(MonthCellDescriptor cell, int monthIndex) {
			this.cell = cell;
			this.monthIndex = monthIndex;
		}
	}

	/** Return cell and month-index (for scrolling) for a given Date. */
	private MonthCellWithMonthIndex getMonthCellWithIndexByDate(Date date) {
		int index = 0;
		Calendar searchCal = Calendar.getInstance();
		searchCal.setTime(date);
		Calendar actCal = Calendar.getInstance();

		Iterator<Entry<MonthDescriptor, List<List<MonthCellDescriptor>>>> iter = cells.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<MonthDescriptor, List<List<MonthCellDescriptor>>> entry = iter.next(); 
			List<List<MonthCellDescriptor>> monthCells = entry.getValue();
			for (List<MonthCellDescriptor> weekCells : monthCells) {
				for (MonthCellDescriptor actCell : weekCells) {
					actCal.setTime(actCell.getDate());
					if (sameDate(actCal, searchCal) && actCell.isSelectable()) {
						return new MonthCellWithMonthIndex(actCell, index);
					}
				}
			}
			index++;
		}
		return null;
	}

	List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor month,
			Calendar startCal) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startCal.getTime());
		List<List<MonthCellDescriptor>> cells = new ArrayList<List<MonthCellDescriptor>>();
		cal.set(DAY_OF_MONTH, 1);
		int firstDayOfWeek = cal.get(DAY_OF_WEEK);
		int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
		if (offset > 0) {
			offset -= 7;
		}
		cal.add(Calendar.DATE, offset);

		Calendar minSelectedCal = minDate(selectedCals);
		Calendar maxSelectedCal = maxDate(selectedCals);

		while ((cal.get(MONTH) < month.getMonth() + 1 || cal.get(YEAR) < month
				.getYear()) //
				&& cal.get(YEAR) <= month.getYear()) {
			Logr.d("Building week row starting at %s", cal.getTime());
			List<MonthCellDescriptor> weekCells = new ArrayList<MonthCellDescriptor>();
			cells.add(weekCells);
			for (int c = 0; c < 7; c++) {
				Date date = cal.getTime();
				boolean isCurrentMonth = cal.get(MONTH) == month.getMonth();
				boolean isSelected = isCurrentMonth
						&& containsDate(selectedCals, cal);
				boolean isSelectable = isCurrentMonth
						//&& betweenDates(cal, minCal, maxCal)
						&& isDateSelectable(date);
				boolean isToday = sameDate(cal, today);
				int value = cal.get(DAY_OF_MONTH);

				MonthCellDescriptor.RangeState rangeState = MonthCellDescriptor.RangeState.NONE;
				if (selectedCals != null && selectedCals.size() > 1) {
					if (sameDate(minSelectedCal, cal)) {
						rangeState = MonthCellDescriptor.RangeState.FIRST;
					} else if (sameDate(maxDate(selectedCals), cal)) {
						rangeState = MonthCellDescriptor.RangeState.LAST;
					} else if (betweenDates(cal, minSelectedCal, maxSelectedCal)) {
						rangeState = MonthCellDescriptor.RangeState.MIDDLE;
					}
				}

				weekCells.add(new MonthCellDescriptor(date, isCurrentMonth,
						isSelectable, isSelected, isToday, value, rangeState));
				cal.add(DATE, 1);
			}
		}
		return cells;
	}

	private static boolean containsDate(List<Calendar> selectedCals,
			Calendar cal) {
		for (Calendar selectedCal : selectedCals) {
			if (sameDate(cal, selectedCal)) {
				return true;
			}
		}
		return false;
	}

	private static Calendar minDate(List<Calendar> selectedCals) {
		if (selectedCals == null || selectedCals.size() == 0) {
			return null;
		}
		Collections.sort(selectedCals);
		return selectedCals.get(0);
	}

	private static Calendar maxDate(List<Calendar> selectedCals) {
		if (selectedCals == null || selectedCals.size() == 0) {
			return null;
		}
		Collections.sort(selectedCals);
		return selectedCals.get(selectedCals.size() - 1);
	}

	private static boolean sameDate(Calendar cal, Calendar selectedDate) {
		return cal.get(MONTH) == selectedDate.get(MONTH)
				&& cal.get(YEAR) == selectedDate.get(YEAR)
				&& cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
	}

	private static boolean betweenDates(Calendar cal, Calendar minCal,
			Calendar maxCal) {
		final Date date = cal.getTime();
		return betweenDates(date, minCal, maxCal);
	}

	static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
		final Date min = minCal.getTime();
		return (date.equals(min) || date.after(min)) // >= minCal
				&& date.before(maxCal.getTime()); // && < maxCal
	}

	private static boolean sameMonth(Calendar cal, MonthDescriptor month) {
		return (cal.get(MONTH) == month.getMonth() && cal.get(YEAR) == month
				.getYear());
	}

	private boolean isDateSelectable(Date date) {
		if (dateConfiguredListener == null) {
			return true;
		}
		return dateConfiguredListener.isDateSelectable(date);
	}

	public void setOnDateSelectedListener(OnDateSelectedListener listener) {
		dateListener = listener;
	}

	/**
	 * Set a listener to react to user selection of a disabled date.
	 * 
	 * @param listener
	 *            the listener to set, or null for no reaction
	 */
	public void setOnInvalidDateSelectedListener(
			OnInvalidDateSelectedListener listener) {
		invalidDateListener = listener;
	}

	/**
	 * Set a listener used to discriminate between selectable and unselectable
	 * dates. Set this to disable arbitrary dates as they are rendered.
	 * <p>
	 * Important: set this before you call {@link #init(Date, Date)} methods. If
	 * called afterwards, it will not be consistently applied.
	 */
	public void setDateSelectableFilter(DateSelectableFilter listener) {
		dateConfiguredListener = listener;
	}

	/**
	 * Interface to be notified when a new date is selected. This will only be
	 * called when the user initiates the date selection. If you call
	 * {@link #selectDate(Date)} this listener will not be notified.
	 * 
	 * @see #setOnDateSelectedListener(OnDateSelectedListener)
	 */
	public interface OnDateSelectedListener {
		void onDateSelected(Date date);
	}

	/**
	 * Interface to be notified when an invalid date is selected by the user.
	 * This will only be called when the user initiates the date selection. If
	 * you call {@link #selectDate(Date)} this listener will not be notified.
	 * 
	 * @see #setOnInvalidDateSelectedListener(OnInvalidDateSelectedListener)
	 */
	public interface OnInvalidDateSelectedListener {
		void onInvalidDateSelected(Date date);
	}

	/**
	 * Interface used for determining the selectability of a date cell when it
	 * is configured for display on the calendar.
	 * 
	 * @see #setDateSelectableFilter(DateSelectableFilter)
	 */
	public interface DateSelectableFilter {
		boolean isDateSelectable(Date date);
	}

	private class DefaultOnInvalidDateSelectedListener implements
			OnInvalidDateSelectedListener {
		@Override
		public void onInvalidDateSelected(Date date) {
//			String errMessage = getResources().getString(R.string.invalid_date,
//					fullDateFormat.format(minCal.getTime()),
//					fullDateFormat.format(maxCal.getTime()));
//			Toast.makeText(getContext(), errMessage, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		ListIterator<MonthDescriptor> it = dataLinks.listIterator(position%capacity);
		return it.next();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//Log.e("getview", "position "+position);
		MonthView monthView = (MonthView) convertView;
		if (monthView == null) {
			monthView = MonthView.create(parent, inflater,
					weekdayNameFormat, listener, today);
		}
		MonthDescriptor mDes=super.getDataItem(position);
		monthView.init(mDes, cells.get(mDes));
		return monthView;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// Disable selectability: each cell will handle that itself.
		return false;
	}

	@Override
	public void refreshLinks(MonthDescriptor object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCapacityBlock(int position) {
		int gap=position-Integer.MAX_VALUE/2+capacity/2;//距离第一个区间的第一个位置的距离
		int gapBlock= gap>=0?gap/capacity:gap/capacity-1;//计算第几个区间
		Calendar minDay=Calendar.getInstance();
		minDay.setTime(midCal.getTime());
		minDay.add(MONTH, gapBlock*capacity-capacity/2);
		initCalCapacity(minDay,Integer.MAX_VALUE/2+gapBlock*capacity);
		
	}


}
