package com.diary.goal.activity.sudo;


import java.util.Calendar;
import java.util.HashMap;

import com.diary.goal.DiaryApplication;
import com.diary.goal.R;
import com.diary.goal.activity.base.BaseActivity;
import com.diary.goal.activity.base.BaseContentFragment;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
/**
 * diary main UI panel
 * nine square sudo panel style
 * @author DuanLei
 *
 */
public class SudoKuFragment extends BaseContentFragment implements OnTouchListener{
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.nine_panel_frame, null);
		return convertView;
	}
	
	private void clearSudoBitmapCache(){
		DiaryApplication app=DiaryApplication.getInstance();
		app.clearbitmap(R.drawable.family_activated);
		app.clearbitmap(R.drawable.family_null);
		app.clearbitmap(R.drawable.finance_activated);
		app.clearbitmap(R.drawable.finance_null);
		app.clearbitmap(R.drawable.health_activated);
		app.clearbitmap(R.drawable.health_null);
		app.clearbitmap(R.drawable.innovation_activated);
		app.clearbitmap(R.drawable.innovation_null);
		app.clearbitmap(R.drawable.mit_activated);
		app.clearbitmap(R.drawable.mit_null);
		app.clearbitmap(R.drawable.personal_activated);
		app.clearbitmap(R.drawable.personal_null);
		app.clearbitmap(R.drawable.soul_activated);
		app.clearbitmap(R.drawable.soul_null);
		app.clearbitmap(R.drawable.work_activated);
		app.clearbitmap(R.drawable.work_null);
		app.clearbitmap(R.drawable.date_activated);
		app.clearbitmap(R.drawable.date_null);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
