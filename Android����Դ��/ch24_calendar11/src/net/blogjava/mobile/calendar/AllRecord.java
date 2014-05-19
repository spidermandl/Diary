package net.blogjava.mobile.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 
 * 添加日志
 * 
 * */
public class AllRecord extends ListActivity
{
	public static int year, month, day;
	
	public static List<String> recordArray;
	public static ArrayAdapter<String> arrayAdapter;
	public static List<Integer> idList = new ArrayList<Integer>();
	public static ListActivity myListActivity;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		
		year = getIntent().getExtras().getInt("year");
		month = getIntent().getExtras().getInt("month");
		day = getIntent().getExtras().getInt("day");

		if (recordArray == null)
			recordArray = new ArrayList<String>();
		if (arrayAdapter == null)
			arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, recordArray);
		else
			arrayAdapter.clear();

		idList.clear();
		

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(year, month, day);
		setTitle(sdf.format(calendar.getTime()));
		setListAdapter(arrayAdapter);
		myListActivity = null;
		myListActivity = this;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

	}

	class MenuItemClickParent
	{
		protected Activity activity;

		public MenuItemClickParent(Activity activity)
		{
			this.activity = activity;
		}
	}

	class OnDeleteRecordMenuItemClick extends MenuItemClickParent implements
			OnMenuItemClickListener
	{

		public OnDeleteRecordMenuItemClick(Activity activity)
		{
			super(activity);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean onMenuItemClick(MenuItem item)
		{
			AllRecord allRecord = (AllRecord) activity;
			int index = allRecord.getSelectedItemPosition();

			if (index < 0)
				return false;
			
			recordArray.remove(index);
			idList.remove(index);
			allRecord.setListAdapter(arrayAdapter);

			return true;
		}

	}

}
