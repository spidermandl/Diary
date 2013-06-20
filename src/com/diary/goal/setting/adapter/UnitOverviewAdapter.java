package com.diary.goal.setting.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.activity.RichTextEditorActivity;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UnitOverviewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater m_inflater;
	private HashMap<Integer, String> categorys,configs;
	private ArrayList<Integer> indexs;
	
	public UnitOverviewAdapter(Context con) {
		this.context=con;
		m_inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		categorys=new HashMap<Integer, String>();
		configs=new HashMap<Integer, String>();
		indexs=new ArrayList<Integer>();
		loadData();
	}
	private void loadData(){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		JSONObject json=DiaryApplication.getInstance().getSudoConfig();
		try {
			JSONArray array=json.getJSONArray(SudoType.getTypeString(model.getType()).toLowerCase());
			for (int i=0;i<array.length();i++){
				JSONObject o=array.optJSONObject(i);
				Iterator<String> it=o.keys();
				String key=it.next();
				int category=o.getInt(key);
				indexs.add(category);
				configs.put(category, key);
				Cursor c=DiaryApplication.getInstance().getDbHelper().getCategory(model.getDate(),model.getType().getType(),category);
				if(c!=null&&c.getCount()!=0){
					c.moveToFirst();
					categorys.put(c.getInt(0),c.getString(1));
				}
				else
					categorys.put(category, null);
				if(c!=null)
					c.close();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		categorys.clear();
		configs.clear();
		indexs.clear();
		loadData();
		
		super.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return indexs.size();
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
		}
		TextView category=(TextView)convertView.findViewById(R.id.title);
		category.setText(configs.get(indexs.get(position)));
		TextView content=(TextView)convertView.findViewById(R.id.content);
		content.setText(categorys.get(indexs.get(position)));
		final int index=position;
		content.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DiaryApplication.getInstance().getDateModel().setCategory(indexs.get(index));
				DiaryApplication.getInstance().getDateModel().setText(categorys.get(indexs.get(index)));
				Intent intent=new Intent();
				intent.setClass(context, RichTextEditorActivity.class);
				((Activity)context).startActivityForResult(intent, 0);
				//context.startActivity(intent);
				
			}
		});
		return convertView;
	}

}
