package com.diary.goal.setting.adapter;

import java.util.Calendar;
import java.util.Date;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.UnitOverviewAdapter.ViewHolder;
import com.diary.goal.setting.tools.Constant;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OverviewStrollAdapter extends BaseAdapter {

	Context context;
	LayoutInflater m_inflater;
	UnitOverviewAdapter mAdapter;
	
	public OverviewStrollAdapter(Context con){
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAdapter=new UnitOverviewAdapter(context);
	}
	
	//@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE/2;
	}

	//@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = m_inflater.inflate(R.layout.essay_overview, null);
			ViewHolder viewHolder=new ViewHolder();
			viewHolder.listview=(ExpandableListView)convertView.findViewById(R.id.overview_list);
			//viewHolder.listview.setDividerHeight(0);
			viewHolder.title=(TextView)convertView.findViewById(R.id.overview_title);
			viewHolder.listview.setAdapter(mAdapter);
			convertView.setTag(viewHolder);
			
//		listview.setDividerHeight(0);
//		mAdapter=new UnitOverviewAdapter(this);
//		listview.setAdapter(mAdapter);
//		TextView title=(TextView)this.findViewById(R.id.overview_title);
//		title.setText(Constant.stringDict.get(Constant.SudoType.getTypeString(DiaryApplication.getInstance().getDateModel().getType())));
//		
		}
		ViewHolder holder=(ViewHolder)convertView.getTag();
		String text=
				context.getResources().getString(Constant.stringDict.get(Constant.SudoType.getTypeString(DiaryApplication.getInstance().getDateModel().getType())));
		text+="("+getMonthString(position)+")";
		holder.title.setText(text);
		mAdapter.notifyDataSetChanged();
		return convertView;
		
	}
	
	class ViewHolder{
		TextView title;
		ExpandableListView listview;
	}
	
	/**
	 * construct string with month and day
	 * @return
	 */
	private String getMonthString(int position){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, position-(this.getCount()-1));
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DAY_OF_MONTH);
//		Log.e("getMonthString month", month+"");
//		Log.e("getMonthString day", day+"");
		return getAbbrMonth(month)+day+context.getResources().getString(R.string.day);
	}
	public void refresh(){
		mAdapter.notifyDataSetChanged();
	}

	private String getAbbrMonth(int month){
		switch (month) {
		case 1:
			return context.getResources().getString(R.string.Jan);
		case 2:
			return context.getResources().getString(R.string.Feb);
		case 3:
			return context.getResources().getString(R.string.Mar);
		case 4:
			return context.getResources().getString(R.string.Apr);
		case 5:
			return context.getResources().getString(R.string.May);
		case 6:
			return context.getResources().getString(R.string.Jun);
		case 7:
			return context.getResources().getString(R.string.Jul);
		case 8:
			return context.getResources().getString(R.string.Aug);
		case 9:
			return context.getResources().getString(R.string.Sep);
		case 10:
			return context.getResources().getString(R.string.Oct);
		case 11:
			return context.getResources().getString(R.string.Nov);
		case 12:
			return context.getResources().getString(R.string.Dec);
	
		default:
			return context.getResources().getString(R.string.Jan);
		}
	}
}
