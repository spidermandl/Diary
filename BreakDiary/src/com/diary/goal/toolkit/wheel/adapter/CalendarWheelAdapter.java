package com.diary.goal.toolkit.wheel.adapter;

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
import java.util.List;
import java.util.Locale;

import com.diary.goal.R;
import com.diary.goal.toolkit.calendar.CalendarPickerView.DateSelectableFilter;
import com.diary.goal.toolkit.calendar.CalendarPickerView.OnDateSelectedListener;
import com.diary.goal.toolkit.calendar.CalendarPickerView.OnInvalidDateSelectedListener;
import com.diary.goal.toolkit.calendar.Logr;
import com.diary.goal.toolkit.calendar.MonthCellDescriptor;
import com.diary.goal.toolkit.calendar.MonthCellDescriptor.RangeState;
import com.diary.goal.toolkit.calendar.MonthDescriptor;
import com.diary.goal.toolkit.calendar.MonthView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class CalendarWheelAdapter extends AbstractWheelCalendarAdapter {

	private static int DEFAULT_MIN_MONTH=-1;
	private static int DEFAULT_MAX_MONTH=1;
	private static int MONTH_RANGE=3;
	
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

	private DateFormat monthNameFormat;
	private DateFormat weekdayNameFormat;
	private DateFormat fullDateFormat;
	SelectionMode selectionMode;
	final List<MonthDescriptor> months = new ArrayList<MonthDescriptor>();
	final List<MonthCellDescriptor> selectedCells = new ArrayList<MonthCellDescriptor>();
	final Calendar today = Calendar.getInstance();
	private final List<List<List<MonthCellDescriptor>>> cells = new ArrayList<List<List<MonthCellDescriptor>>>();
	final List<Calendar> selectedCals = new ArrayList<Calendar>();
	private final Calendar minCal = Calendar.getInstance();
	private final Calendar maxCal = Calendar.getInstance();
	private final Calendar monthCounter = Calendar.getInstance();
	private final MonthView.Listener listener = new CellClickedListener();

	private OnDateSelectedListener dateListener;
	private DateSelectableFilter dateConfiguredListener;
	private OnInvalidDateSelectedListener invalidDateListener = new DefaultOnInvalidDateSelectedListener();

	public CalendarWheelAdapter(Context context) {
		super(context);
		dataInit();
	}

	public  CalendarWheelAdapter(Context context, int itemResource) {
		super(context, itemResource);
		dataInit();
	}

	void dataInit() {
		monthNameFormat = new SimpleDateFormat(
				context.getString(R.string.month_name_format));
		weekdayNameFormat = new SimpleDateFormat(
				context.getString(R.string.day_name_format));
		fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);

		Calendar preMonth = Calendar.getInstance(),
				nextMonth = Calendar.getInstance();
		
		preMonth.add(Calendar.MONTH, DEFAULT_MIN_MONTH);
		nextMonth.add(Calendar.MONTH, DEFAULT_MAX_MONTH);

		init(preMonth.getTime(), nextMonth.getTime()).withSelectedDate(new Date());

	}

	public int getItemsCount() {
		// TODO Auto-generated method stub
		return MONTH_RANGE;
	}

	@Override
	protected View getView(int index, View convertView, ViewGroup parent) {
		convertView = MonthView.create(parent, inflater, new SimpleDateFormat(
				context.getString(R.string.day_name_format)),
				new CellClickedListener(), today);
		((MonthView)convertView).init(months.get(index), cells.get(index));
		return convertView;
	}

	@Override
	protected void updateView() {
		// TODO Auto-generated method stub

	}

	private class CellClickedListener implements MonthView.Listener {
		public void handleClick(MonthCellDescriptor cell) {
			Date clickedDate = cell.getDate();

			if (!betweenDates(clickedDate, minCal, maxCal)
					|| !isDateSelectable(clickedDate)) {
				if (invalidDateListener != null) {
					invalidDateListener.onInvalidDateSelected(clickedDate);
				}
			} else {
				boolean wasSelected = doSelectDate(clickedDate, cell);

				if (wasSelected && dateListener != null) {
					dateListener.onDateSelected(clickedDate);
				}
			}
		}

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

				for (List<List<MonthCellDescriptor>> month : cells) {
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
	
	private void validateAndUpdate() {
		// TODO Auto-generated method stub
		
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
	public FluentInitializer init(Date minDate, Date maxDate) {
		if (minDate == null || maxDate == null) {
			throw new IllegalArgumentException(
					"minDate and maxDate must be non-null.  "
							+ dbg(minDate, maxDate));
		}
		if (minDate.after(maxDate)) {
			throw new IllegalArgumentException(
					"minDate must be before maxDate.  " + dbg(minDate, maxDate));
		}
		if (minDate.getTime() == 0 || maxDate.getTime() == 0) {
			throw new IllegalArgumentException(
					"minDate and maxDate must be non-zero.  "
							+ dbg(minDate, maxDate));
		}
		this.selectionMode = SelectionMode.SINGLE;
		// Clear out any previously-selected dates/cells.
		selectedCals.clear();
		selectedCells.clear();

		// Clear previous state.
		cells.clear();
		months.clear();
		minCal.setTime(minDate);
		maxCal.setTime(maxDate);
		setMidnight(minCal);
		setMidnight(maxCal);

		// maxDate is exclusive: bump back to the previous day so if maxDate is
		// the first of a month,
		// we don't accidentally include that month in the view.
		maxCal.add(MINUTE, -1);

		// Now iterate between minCal and maxCal and build up our list of months
		// to show.
		monthCounter.setTime(minCal.getTime());
		final int maxMonth = maxCal.get(MONTH);
		final int maxYear = maxCal.get(YEAR);
		while ((monthCounter.get(MONTH) <= maxMonth // Up to, including the
													// month.
				|| monthCounter.get(YEAR) < maxYear) // Up to the year.
				&& monthCounter.get(YEAR) < maxYear + 1) { // But not > next yr.
			Date date = monthCounter.getTime();
			MonthDescriptor month = new MonthDescriptor(
					monthCounter.get(MONTH), monthCounter.get(YEAR), date,
					monthNameFormat.format(date));
			cells.add(getMonthCells(month, monthCounter));
			Logr.d("Adding month %s", month);
			months.add(month);
			monthCounter.add(MONTH, 1);
		}

		validateAndUpdate();
		return new FluentInitializer();
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
						&& betweenDates(cal, minCal, maxCal)
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
	private void clearOldSelections() {
		for (MonthCellDescriptor selectedCell : selectedCells) {
			// De-select the currently-selected cell.
			selectedCell.setSelected(false);
		}
		selectedCells.clear();
		selectedCals.clear();
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

	private boolean isDateSelectable(Date date) {
		if (dateConfiguredListener == null) {
			return true;
		}
		return dateConfiguredListener.isDateSelectable(date);
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
			for (int c = 0; c < months.size(); c++) {
				MonthDescriptor month = months.get(c);
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
			} else if (todayIndex != null) {
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
			for (MonthDescriptor month : months) {
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
	
	private class DefaultOnInvalidDateSelectedListener implements
			OnInvalidDateSelectedListener {
		public void onInvalidDateSelected(Date date) {
			String errMessage = context.getResources().getString(
					R.string.invalid_date,
					fullDateFormat.format(minCal.getTime()),
					fullDateFormat.format(maxCal.getTime()));
			Toast.makeText(context, errMessage, Toast.LENGTH_SHORT).show();
		}
	}

	/** Clears out the hours/minutes/seconds/millis of a Calendar. */
	static void setMidnight(Calendar cal) {
		cal.set(HOUR_OF_DAY, 0);
		cal.set(MINUTE, 0);
		cal.set(SECOND, 0);
		cal.set(MILLISECOND, 0);
	}
	
	/** Returns a string summarizing what the client sent us for init() params. */
	private static String dbg(Date minDate, Date maxDate) {
		return "minDate: " + minDate + "\nmaxDate: " + maxDate;
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
		if (date.before(minCal.getTime()) || date.after(maxCal.getTime())) {
			throw new IllegalArgumentException(
					"selectedDate must be between minDate and maxDate.  "
							+ date);
		}
		MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
		if (monthCellWithMonthIndex == null || !isDateSelectable(date)) {
			return false;
		}
		boolean wasSelected = doSelectDate(date, monthCellWithMonthIndex.cell);
		if (wasSelected) {

		}
		return wasSelected;
	}
	
	/** Return cell and month-index (for scrolling) for a given Date. */
	private MonthCellWithMonthIndex getMonthCellWithIndexByDate(Date date) {
		int index = 0;
		Calendar searchCal = Calendar.getInstance();
		searchCal.setTime(date);
		Calendar actCal = Calendar.getInstance();

		for (List<List<MonthCellDescriptor>> monthCells : cells) {
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
	
	/** Hold a cell with a month-index. */
	private static class MonthCellWithMonthIndex {
		public MonthCellDescriptor cell;
		public int monthIndex;

		public MonthCellWithMonthIndex(MonthCellDescriptor cell, int monthIndex) {
			this.cell = cell;
			this.monthIndex = monthIndex;
		}
	}

}
