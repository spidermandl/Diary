package com.diary.goal.setting.adapter;

import com.diary.goal.setting.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NinePanelAdapter extends BaseAdapter {

	Context context;
	private LayoutInflater m_inflater;
	
	public NinePanelAdapter(Context con){
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = m_inflater.inflate(R.layout.nine_panel_frame, null);
		}
		return convertView;
	}

}
