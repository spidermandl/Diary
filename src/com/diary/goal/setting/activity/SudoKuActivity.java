package com.diary.goal.setting.activity;


import java.util.Date;
import java.util.HashMap;

import com.diary.goal.setting.DiaryApplication;
import com.diary.goal.setting.R;
import com.diary.goal.setting.adapter.NinePanelAdapter;
import com.diary.goal.setting.adapter.UnitOverviewAdapter;
import com.diary.goal.setting.model.DateModel;
import com.diary.goal.setting.tools.Constant;
import com.diary.goal.setting.view.ViewFlow;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
		setContentView(viewFlow);
		//setContentView(R.layout.nine_panel_frame);
		super.onCreate(savedInstanceState);
	}
	
	private void init(){
		DateModel model=DiaryApplication.getInstance().getDateModel();
		model.setDate(new Date());
		Cursor c=DiaryApplication.getInstance().getDbHelper().getTodayPad(model.getDate());
        HashMap<Constant.SudoType, Boolean> status=DiaryApplication.getInstance().getPadStatus();
		if(c!=null){
			while(c.moveToNext()){
				status.put(Constant.SudoType.getSudoType(c.getInt(0)), true);
			}
			c.close();
		}
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
