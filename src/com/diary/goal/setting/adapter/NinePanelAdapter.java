package com.diary.goal.setting.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.model.PanelDateModel;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.tools.Constant.SudoType;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
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
		return Integer.MAX_VALUE;
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
		Log.e("position", position+"");
		getPanelCache(position);
		if(convertView==null){                                                                                                        
			Log.e("create View", "null "+position);
			convertView = m_inflater.inflate(R.layout.nine_panel_frame, null);
		}
		return convertView;
	}
	
	private void getPanelCache(int position){
		HashMap<Integer, PanelDateModel> panelStatus=DiaryApplication.getInstance().getPanelCache();
		if(panelStatus.containsKey(position)){
			DiaryApplication.getInstance().setPadStatus((PanelDateModel)panelStatus.get(position));
			DateModel model = new DateModel();
			model.setDate(((PanelDateModel)panelStatus.get(position)).getDate());
			DiaryApplication.getInstance().setDateModel(model);
		}
        else {
			int delta = position-Integer.MAX_VALUE / 2;
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DAY_OF_MONTH, delta);
			DateModel model = new DateModel();
			model.setDate(date.getTime());
			DiaryApplication.getInstance().setDateModel(model);
			Cursor c = DiaryApplication.getInstance().getDbHelper().getTodayPad(model.getDate());
			HashMap<Constant.SudoType, Boolean> status = new HashMap<Constant.SudoType, Boolean>();
			status.put(Constant.SudoType.NO_TYPE, false);
			status.put(Constant.SudoType.WORK, false);
			status.put(Constant.SudoType.SOCIAL, false);
			status.put(Constant.SudoType.FAMILY, false);
			status.put(Constant.SudoType.FINANCE, false);
			status.put(Constant.SudoType.DATE, false);
			status.put(Constant.SudoType.SQUARE_6, false);
			status.put(Constant.SudoType.HEALTHY, false);
			status.put(Constant.SudoType.SQUARE_8, false);
			status.put(Constant.SudoType.SQUARE_9, false);
			if (c != null) {
				while (c.moveToNext()) {
					status.put(Constant.SudoType.getSudoType(c.getInt(0)), true);
				}
				c.close();
			}
			PanelDateModel panelModel=new PanelDateModel();
			panelModel.setDate(date.getTime());
			panelModel.setPadStatus(status);
			DiaryApplication.getInstance().setPadStatus(panelModel);
			panelStatus.put(position, panelModel);
		}
	}

}
