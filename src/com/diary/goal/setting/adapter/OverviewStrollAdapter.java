package com.diary.goal.setting.adapter;

import java.util.Calendar;
import java.util.Date;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.UnitOverviewAdapter.ViewHolder;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Function;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

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
//			viewHolder.title=(TextView)convertView.findViewById(R.id.overview_title);
//			viewHolder.back=convertView.findViewById(R.id.back_sudo);
//			viewHolder.back.setBackgroundResource(R.drawable.back_arrow);
//			viewHolder.back.setOnClickListener(listener);
//			viewHolder.forward=convertView.findViewById(R.id.forward_sudo);
//			viewHolder.forward.setBackgroundResource(R.drawable.forward_arrow);
//			viewHolder.forward.setOnClickListener(listener);
			viewHolder.listview.setAdapter(mAdapter);
			mAdapter.setListener(viewHolder.listview);
			int expandCount=mAdapter.getGroupCount();
			for (int i=0;i<expandCount;i++){
				viewHolder.listview.expandGroup(i);
			}
			convertView.setTag(viewHolder);
			
//		listview.setDividerHeight(0);
//		mAdapter=new UnitOverviewAdapter(this);
//		listview.setAdapter(mAdapter);
//		TextView title=(TextView)this.findViewById(R.id.overview_title);
//		title.setText(Constant.stringDict.get(Constant.SudoType.getTypeString(DiaryApplication.getInstance().getDateModel().getType())));		
		}
		ViewHolder holder=(ViewHolder)convertView.getTag();
//		String text=
//				context.getResources().getString(Constant.stringDict.get(Constant.SudoType.getTypeString(DiaryApplication.getInstance().getDateModel().getType())));
//		text+="("+getMonthString(position)+")";
//		holder.title.setText(text);
		mAdapter.notifyDataSetChanged();
		return convertView;
		
	}
	
	class ViewHolder{
		TextView title;
		View back,forward;
		ExpandableListView listview;
	}
//	OnClickListener listener=new OnClickListener() {
//		
//		public void onClick(View v) {
//			if(v.getId()==R.id.back_sudo){
//				moveSudoStep(1);
//				mAdapter.notifyDataSetChanged();
//			}
//			if(v.getId()==R.id.forward_sudo){
//				moveSudoStep(-1);
//				mAdapter.notifyDataSetChanged();
//			}
//			
//		}
//	};
//	
//	private void moveSudoStep(int step){
//		DateModel model=DiaryApplication.getInstance().getDateModel();
//		int sudo_type=model.getType().getType();
//		sudo_type=(sudo_type+step)%9;
//		if(sudo_type==5){
//			if(step>0)
//				sudo_type=6;
//			else
//				sudo_type=4;
//		}
//		sudo_type=sudo_type==0?9:sudo_type;
//		model.setType(SudoType.getSudoType(sudo_type));
//	}
//	/**
//	 * construct string with month and day
//	 * @return
//	 */
//	private String getMonthString(int position){
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(new Date());
//		calendar.add(Calendar.DAY_OF_MONTH, position-(this.getCount()-1));
//		int month=calendar.get(Calendar.MONTH)+1;
//		int day=calendar.get(Calendar.DAY_OF_MONTH);
////		Log.e("getMonthString month", month+"");
////		Log.e("getMonthString day", day+"");
//		return Function.getAbbrMonth(this.context,month)+day+context.getResources().getString(R.string.day);
//	}
	public void refresh(){
		mAdapter.notifyDataSetChanged();
	}


}
