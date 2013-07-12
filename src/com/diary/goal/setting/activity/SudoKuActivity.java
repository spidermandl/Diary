package com.diary.goal.setting.activity;


import java.util.Calendar;
import java.util.HashMap;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.NinePanelAdapter;
import com.diary.goal.setting.adapter.UnitOverviewAdapter;
import com.diary.goal.setting.listener.FlipPathListener;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.model.PanelDateModel;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.view.FlipView;
import com.diary.goal.setting.view.ViewFlow;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Toast;

public class SudoKuActivity extends Activity implements OnTouchListener,OnGestureListener{
	
	private int verticalMinDistance = 20;  
	private int minVelocity         = 0;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DiaryApplication.getInstance().setOrientation(
				this.getResources().getConfiguration().orientation);
		
		init();
		ViewFlow viewFlow=new ViewFlow(this);
		viewFlow.setAdapter(new NinePanelAdapter(this));
		viewFlow.setFlipListener(new FlipPathListener() {
			
			@Override
			public void track(int steps) {
				int position=DiaryApplication.getInstance().getDateCursor();
				position+=steps;
				DiaryApplication.getInstance().setDateCursor(position);
				getPanelCache();
				
			}
		});
		setContentView(viewFlow);
		//setContentView(R.layout.nine_panel_frame);
		//setContentView(R.layout.flip_frame);
		super.onCreate(savedInstanceState);
		
	}
	
	private void init(){
		getPanelCache();
	}
	/**
	 * init date link
	 * get current day status
	 */
	private void getPanelCache(){
		HashMap<Integer, PanelDateModel> panelStatus=DiaryApplication.getInstance().getPanelCache();
		int position=DiaryApplication.getInstance().getDateCursor();
		if(panelStatus.containsKey(position)){
			DiaryApplication.getInstance().setPadStatus((PanelDateModel)panelStatus.get(position));
			DateModel model = new DateModel();
			model.setDate(((PanelDateModel)panelStatus.get(position)).getDate());
			DiaryApplication.getInstance().setDateModel(model);
		}
        else {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DAY_OF_MONTH, position);
			DateModel model = new DateModel();
			model.setDate(date.getTime());
			DiaryApplication.getInstance().setDateModel(model);
			Cursor c = DiaryApplication.getInstance().getDbHelper().getTodayPad(model.getDate());
			HashMap<Constant.SudoType, Boolean> status = new HashMap<Constant.SudoType, Boolean>();
			status.put(Constant.SudoType.SUDO_0, false);
			status.put(Constant.SudoType.SUDO_1, false);
			status.put(Constant.SudoType.SUDO_2, false);
			status.put(Constant.SudoType.SUDO_3, false);
			status.put(Constant.SudoType.SUDO_4, false);
			status.put(Constant.SudoType.SUDO_5, false);
			status.put(Constant.SudoType.SUDO_6, false);
			status.put(Constant.SudoType.SUDO_7, false);
			status.put(Constant.SudoType.SUDO_8, false);
			status.put(Constant.SudoType.SUDO_9, false);
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
	
	@Override
	protected void onDestroy() {
		DiaryApplication.getInstance().quit();
		super.onDestroy();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//if(newConfig.orientation==Configuration)
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {

			// 切换Activity
			// Intent intent = new Intent(ViewSnsActivity.this,
			// UpdateStatusActivity.class);
			// startActivity(intent);
			
			Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
		} else if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity) {

			// 切换Activity
			// Intent intent = new Intent(ViewSnsActivity.this,
			// UpdateStatusActivity.class);
			// startActivity(intent);
			Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
		}

		return false;
	}



}
