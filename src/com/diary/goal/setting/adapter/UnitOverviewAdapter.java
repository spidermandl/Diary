package com.diary.goal.setting.adapter;


import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.RichTextEditorActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class UnitOverviewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater m_inflater;
	public UnitOverviewAdapter(Context con) {
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
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
			convertView = m_inflater.inflate(R.layout.essay_overview, null);
			convertView.findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.setClass(context, RichTextEditorActivity.class);
					context.startActivity(intent);
					
				}
			});
		}
		return convertView;
	}

}
